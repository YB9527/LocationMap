package com.xupu.locationmap.projectmanager.page;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.imgview.ViewPagerActivity;
import com.xupu.locationmap.common.page.PhotoSingleActivty;
import com.xupu.locationmap.common.po.Callback;
import com.xupu.locationmap.common.po.Media;
import com.xupu.locationmap.common.po.ViewHolderCallback;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.FileTool;
import com.xupu.locationmap.common.tools.JSONTool;
import com.xupu.locationmap.common.tools.MediaTool;
import com.xupu.locationmap.common.tools.OkHttpClientUtils;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.view.ImgFieldCusom;
import com.xupu.locationmap.projectmanager.view.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.view.PositionField;
import com.xupu.locationmap.projectmanager.view.TableDataCustom;
import com.xupu.locationmap.projectmanager.view.ViewFieldCustom;
import com.xupu.locationmap.projectmanager.service.MediaService;
import com.xupu.locationmap.projectmanager.service.TaskService;


import java.io.File;
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
        this.medias = new ArrayList<>();
        if(medias != null){
            this.medias.addAll(medias);
        }

        this.task = task;
        this.parent = parent;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_item, container, false);
        initSelfPage(view);
        RecyclerView recyclerView = view.findViewById(R.id.recy);
        int fragmentItem = R.layout.fragment_task_photo;
        List<FieldCustom> filedCustoms = new ArrayList<>();
        //filedCustoms.add(new PositionField(R.id.index, "").setStartIndex(-1));
        filedCustoms.add(new ImgFieldCusom(R.id.img, "path") {
            @Override
            public void onClick(MyJSONObject myJSONObject) {
                if (myJSONObject.getId().equals("-1")) {
                    //第一个添加按钮
                    MyJSONObject media = MediaService.newMediaJSONObject(parent, task, 0);
                    MediaTool.photo(TaskFragment.this, 101, media);
                    //AndroidTool.showAnsyTost(MediaService.getPath(media),0);

                } else {
                    //后面的是 media
                    Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
                    List<MyJSONObject> myJSONObjects1 = new ArrayList<>();
                    myJSONObjects1.addAll(myItemRecyclerViewAdapter.getmValues());
                    myJSONObjects1.remove(myItemRecyclerViewAdapter.getmValues().size()-1);
                    intent.putExtra("medias", new Gson().toJson(myJSONObjects1));
                    intent.putExtra("index", myItemRecyclerViewAdapter.getmValues().indexOf(myJSONObject));
                    startActivity(intent);


                }
            }
        });
        filedCustoms.add(new ViewFieldCustom(R.id.iv_delete) {
            @Override
            public void OnClick(View view, MyJSONObject media) {
                myItemRecyclerViewAdapter.remove(media);
                TableTool.delete(media);
                //删除照片
                MediaService.deleteFile(media);
            }
        }.setConfirm(true, "确认要删除吗？"));


        //第一个添加 添加按钮 ，任务名
        task.getJsonobject().put("name", "");
        MyJSONObject myJSONObject = MediaService.getMedia(task, 0, TaskService.getTaskName(task));
        myJSONObject.setId(Media.ADD_BUTTON);

        //MediaService.setPath(myJSONObject, getResourcesUri(R.drawable.good_night_img));
        medias.add(myJSONObject);


        TableDataCustom tableDataCustom = new TableDataCustom(fragmentItem, filedCustoms, medias);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom, recyclerView);

        //recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView.setAdapter(myItemRecyclerViewAdapter);

        myItemRecyclerViewAdapter.setLoadViewCallback(new ViewHolderCallback() {
            @Override
            public void call(MyItemRecyclerViewAdapter.ViewHolder holder, int position) {
                //默认显示下载图片icon
                ImageView imageView = holder.mView.findViewById(R.id.img);
               // holder.mView.findViewById(R.id.iv_delete).setVisibility(View.GONE);
                if (position == medias.size() - 1) {
                    holder.mView.findViewById(R.id.iv_delete).setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.data_icon_add);
                } else {

                    MyJSONObject item = myItemRecyclerViewAdapter.getItem(position);
                    if(item.getState() != TableTool.STATE_NONE){
                        //如果不是网络图片就不用再网上找
                        return;
                    }
                    //先放置下载的图片
                    setLoadImage(imageView);

                    //肯定是网络图片
                    String srcpath =MediaService.getPath(item).replace("\\","/").replace("#","%23");
                    String nativePath = null;
                    if(!srcpath.startsWith(AndroidTool.getRootDir())){
                        //如果不包含本地前缀，就增加，查看本地是否已经下载了
                        nativePath = AndroidTool.getRootDir() + srcpath;
                    }else{
                        nativePath = srcpath;
                    }
                    File file = new File(nativePath);
                    MediaService.setPath(item,nativePath);

                    if(file.exists()){
                        try {
                            notifyImage(imageView,nativePath,position);
                        }catch (Exception e){
                            AndroidTool.showAnsyTost("图片有问题："+nativePath,1);
                        }

                    }else {

                        String url = Tool.getPhotoHostAddress() + srcpath;
                        String finalNativePath = nativePath;
                        new OkHttpClientUtils.GetBuild(url).photoBuild(nativePath, new Callback<Boolean>() {
                            @Override
                            public void call(Boolean hasePhoto) {
                                if (hasePhoto) {

                                    notifyImage(imageView, finalNativePath,position);
                                } else {
                                    setLoadLossImg(imageView);


                                }
                            }
                        });

                    }

                }
            }
        });

        return view;
    }

    /**
     * 下载失败的图片
     * @param imageView
     */
    private void setLoadLossImg(ImageView imageView) {
        //照片失联
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(R.mipmap.data_icon_picture_loss);
            }
        });
    }
    /**
     * 下载中的图片
     * @param imageView
     */
    private void setLoadImage(ImageView imageView) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //下载好了，就更新item
                imageView.setImageResource(R.mipmap.data_icon_download);
                //myItemRecyclerViewAdapter.notifyItemChanged(position);
                //保存图片到本地
            }
        });
    }

    private void notifyImage(ImageView imageView, String finalNativePath, int position) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //下载好了，就更新item
                AndroidTool.setImgViewPath(imageView, finalNativePath);
                //myItemRecyclerViewAdapter.notifyItemChanged(position);
                //保存图片到本地
            }
        });
    }

    private void initSelfPage(View view) {
        // TextView tv= view.findViewById(R.id.title);
        //tv.setText(TaskService.getTaskName(task));
        Integer rid = view.getId();
        MyJSONObject jsonObject = task;
        List<FieldCustom> filedCustoms = new ArrayList<>();
        filedCustoms.add(new FieldCustom(R.id.title, "taskname"));
        ItemDataCustom itemDataCustom = new ItemDataCustom(rid, jsonObject, filedCustoms);
        AndroidTool.setView(view, itemDataCustom, false, 0);
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
                    TableTool.insert(media, TableTool.STATE_INSERT);
                    myItemRecyclerViewAdapter.addItem(medias.size() - 1, media);
                }
                break;
            default:
                break;
        }
    }
}
