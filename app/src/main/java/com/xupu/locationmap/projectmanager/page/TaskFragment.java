package com.xupu.locationmap.projectmanager.page;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.page.PhotoSingleActivty;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.po.MyCallback;
import com.xupu.locationmap.common.po.ResultData;
import com.xupu.locationmap.common.po.SFZBack;
import com.xupu.locationmap.common.po.SFZFront;
import com.xupu.locationmap.common.po.ViewHolderCallback;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.MediaTool;
import com.xupu.locationmap.common.tools.SFZPhotoTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.ImgFiledCusom;
import com.xupu.locationmap.projectmanager.po.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.PositionField;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.service.MediaService;
import com.xupu.locationmap.projectmanager.service.SFZService;
import com.xupu.locationmap.projectmanager.service.TableService;
import com.xupu.locationmap.projectmanager.service.TaskService;

import org.json.JSONException;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 单个任务 fragment
 */
public class TaskFragment extends Fragment {
    View view;
    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private MyJSONObject task;
    private MyJSONObject parent;
    private List<MyJSONObject> medias;

    public TaskFragment() {

    }

    public TaskFragment(MyJSONObject task, MyJSONObject parent, List<MyJSONObject> medias) {
        this.medias = medias;
        this.task = task;
        this.parent = parent;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_item, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recy);
        int fragmentItem = R.layout.fragment_task_photo;
        List<FiledCustom> filedCustoms = new ArrayList<>();
        filedCustoms.add(new PositionField(R.id.index, "").setStartIndex(-1));
        filedCustoms.add(new ImgFiledCusom(R.id.img, "path") {
            @Override
            public void onClick(MyJSONObject myJSONObject) {
                if (myJSONObject.getId().equals("0")) {
                    //第一个添加按钮
                    MyJSONObject media = MediaService.getMedia(parent, 0, TaskService.getTaskName(task));
                    MediaTool.photo(TaskFragment.this, 101, media);
                } else {
                    //后面的是 media
                    Intent intent = new Intent(getActivity(), PhotoSingleActivty.class);
                    intent.putExtra("media", myJSONObject);
                    startActivity(intent);
                }
            }
        });
        filedCustoms.add(new BtuFiledCustom<MyJSONObject>(R.id.btn_delete, "删除") {
            @Override
            public void OnClick(MyJSONObject media) {
                myItemRecyclerViewAdapter.remove(media);
                TableTool.delete(media);
            }
        }.setConfirm(true, "确定要删除吗？"));
        List<MyJSONObject> list = new ArrayList<>();


        //第一个添加 添加按钮 ，任务名
        task.getJsonobject().put("name", "");
        MyJSONObject myJSONObject = MediaService.getMedia(task, 0, TaskService.getTaskName(task));
        myJSONObject.setId("0");
        //MediaService.setPath(myJSONObject, getResourcesUri(R.drawable.good_night_img));
        list.add(myJSONObject);
        list.addAll(medias);

        TableDataCustom tableDataCustom = new TableDataCustom(fragmentItem, filedCustoms, list);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(myItemRecyclerViewAdapter);

        myItemRecyclerViewAdapter.setLoadViewCallback(new ViewHolderCallback() {
            @Override
            public void call(MyItemRecyclerViewAdapter.ViewHolder holder, int position) {
                if (position == 0) {
                    holder.mView.findViewById(R.id.ll_data).setVisibility(View.GONE);
                    ImageView imageView = holder.mView.findViewById(R.id.img);
                    android.widget.FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(200, 500);

                    FrameLayout fl=   holder.mView.findViewById(R.id.fl);
                    params.gravity= Gravity.CENTER_VERTICAL;
                    params.topMargin=200;
                    fl.setLayoutParams(params);
                }
            }
        });

        return view;
    }

    private String getResourcesUri(@DrawableRes int id) {
        Resources resources = getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }

    /**
     * 拍照
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        switch (requestCode) {
            case 101:
                if (resultCode == RESULT_OK) {
                    // 将拍摄的照片显示出来
                    MyJSONObject media = (MyJSONObject) getActivity().getIntent().getSerializableExtra("media");
                    TableTool.insert(media);
                    myItemRecyclerViewAdapter.addItem(media);
                }
                break;
            default:
                break;
        }
    }
}
