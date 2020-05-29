package com.xupu.locationmap.common.tools;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.po.SFZBack;
import com.xupu.locationmap.common.po.SFZFront;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.service.MediaService;


import java.io.File;

public class SFZPhotoTool {
    public static final int REQUEST_CODE_CAMERA = 102;
    private static SFZPhotoTool sfzPhotoTool;
    private static Activity activity;
    public static  boolean INTERNET;//是否联网状态

    public static SFZPhotoTool getSFZPhotoTool(Activity activity) {

        if (sfzPhotoTool == null) {
            synchronized (SFZPhotoTool.class) {
                if (sfzPhotoTool == null) {
                    sfzPhotoTool = new SFZPhotoTool();
                }
            }
        }
        if (SFZPhotoTool.activity != activity) {
            SFZPhotoTool.activity = activity;
            OCR.getInstance(activity).initAccessToken(new OnResultListener<AccessToken>() {
                @Override
                public void onResult(AccessToken accessToken) {
                    String token = accessToken.getAccessToken();
                }

                @Override
                public void onError(OCRError error) {
                    error.printStackTrace();

                }
            }, activity.getApplicationContext());

            OCR.getInstance(activity).initAccessTokenWithAkSk(
                    new OnResultListener<AccessToken>() {
                        @Override
                        public void onResult(AccessToken result) {
                            // 本地自动识别需要初始化
                            sfzPhotoTool.initLicense();
                            Log.d("MainActivity", "onResult: " + result.toString());
                            INTERNET =true;
                        }

                        @Override
                        public void onError(OCRError error) {
                            error.printStackTrace();
                            Log.e("MainActivity", "onError: " + error.getMessage());
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "没有联网所有无法识别身份证", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }, activity.getApplicationContext(),
                    // 需要自己配置 https://console.bce.baidu.com
                    "CwO4IZlL4QG8bqa7ScQFyBjc",
                    // 需要自己配置 https://console.bce.baidu.com
                    "Dx3Z1QIOfRgNoCyvb49lLGQNoITc6lka");
        }
        return sfzPhotoTool;
    }
    private void initLicense() {
        CameraNativeHelper.init(activity, OCR.getInstance(activity).getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        final String msg;
                        switch (errorCode) {
                            case CameraView.NATIVE_SOLOAD_FAIL:
                                msg = "加载so失败，请确保apk中存在ui部分的so";
                                break;
                            case CameraView.NATIVE_AUTH_FAIL:
                                msg = "授权本地质量控制token获取失败";
                                break;
                            case CameraView.NATIVE_INIT_FAIL:
                                msg = "本地质量控制";
                                break;
                            default:
                                msg = String.valueOf(errorCode);
                        }
                    }
                });
    }

    public void front(MyJSONObject media) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                MediaService.getPath(media));
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
        activity.getIntent().putExtra("media", media);
        activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    public void back(MyJSONObject media) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                MediaService.getPath(media));
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
        activity.getIntent().putExtra("media", media);
        activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }


    /**
     * 解析身份证图片
     *
     * @param idCardSide 身份证正反面
     * @param filePath   图片路径
     */
     public void recIDCard(String idCardSide, String filePath, MyCallback myCallback) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(40);
        OCR.getInstance(activity).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    if(idCardSide.equals("back")){
                        SFZBack sfz = new SFZBack();
                        sfz.setBack(result);
                        myCallback.call(new ResultData<SFZBack>(0,sfz));
                    }else{
                        SFZFront sfz = new SFZFront();
                        sfz.setFont(result);
                        myCallback.call(new ResultData<SFZFront>(0,sfz));
                    }
                }
            }
            @Override
            public void onError(OCRError error) {
                String er = error.getMessage();
            }
        });
    }
}
