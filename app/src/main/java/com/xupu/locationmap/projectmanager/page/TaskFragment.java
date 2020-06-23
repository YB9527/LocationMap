package com.xupu.locationmap.projectmanager.page;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
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

import com.xupu.locationmap.R;
import com.xupu.locationmap.common.page.PhotoSingleActivty;
import com.xupu.locationmap.common.po.Media;
import com.xupu.locationmap.common.po.ViewHolderCallback;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.MediaTool;
import com.xupu.locationmap.common.tools.TableTool;
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
        for (MyJSONObject meidaJson : medias) {
            Media media = meidaJson.getJsonobject().toJavaObject(Media.class);
            if (media.getMilepost() != null && media.getMilepost().equals(task.getId())) {
                this.medias.add(meidaJson);
            }
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
        filedCustoms.add(new PositionField(R.id.index, "").setStartIndex(-1));
        filedCustoms.add(new ImgFieldCusom(R.id.img, "path") {
            @Override
            public void onClick(MyJSONObject myJSONObject) {
                if (myJSONObject.getId().equals("-1")) {
                    //第一个添加按钮
                    MyJSONObject media =  MediaService.newMediaJSONObject(parent,task,0);
                    MediaTool.photo(TaskFragment.this, 101, media);
                    //AndroidTool.showAnsyTost(MediaService.getPath(media),0);

                } else {
                    //后面的是 media
                    Intent intent = new Intent(getActivity(), PhotoSingleActivty.class);
                    intent.putExtra("media", myJSONObject);
                    startActivity(intent);
                }
            }
        });
        filedCustoms.add(new ViewFieldCustom(R.id.iv_delete) {
            @Override
            public void OnClick(View view,MyJSONObject media) {
                myItemRecyclerViewAdapter.remove(media);
                TableTool.delete(media);
                //删除照片
                MediaService.deleteFile(media);
            }
        }.setConfirm(true,"确认要删除吗？"));



        //第一个添加 添加按钮 ，任务名
        task.getJsonobject().put("name", "");
        MyJSONObject myJSONObject = MediaService.getMedia(task, 0, TaskService.getTaskName(task));
        myJSONObject.setId(Media.ADD_BUTTON);

        //MediaService.setPath(myJSONObject, getResourcesUri(R.drawable.good_night_img));
        medias.add(myJSONObject);


        TableDataCustom tableDataCustom = new TableDataCustom(fragmentItem, filedCustoms, medias);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom,recyclerView);

        //recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        recyclerView.setAdapter(myItemRecyclerViewAdapter);

        myItemRecyclerViewAdapter.setLoadViewCallback(new ViewHolderCallback() {
            @Override
            public void call(MyItemRecyclerViewAdapter.ViewHolder holder, int position) {
                if (position == medias.size()-1) {
                    holder.mView.findViewById(R.id.iv_delete).setVisibility(View.GONE);
                    ImageView imageView = holder.mView.findViewById(R.id.img);
                   // imageView.getLayoutParams().height=150;
                    //imageView.getLayoutParams().width=150;
                }
            }
        });

        return view;
    }

    private void initSelfPage(View view) {
       // TextView tv= view.findViewById(R.id.title);
        //tv.setText(TaskService.getTaskName(task));
        Integer rid =view.getId();
        MyJSONObject jsonObject = task;
        List<FieldCustom> filedCustoms = new ArrayList<>();
        filedCustoms.add(new FieldCustom(R.id.title,"taskname"));
        ItemDataCustom itemDataCustom = new ItemDataCustom( rid,  jsonObject,  filedCustoms);
        AndroidTool.setView(view,itemDataCustom,false,0);
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
                    TableTool.insert(media,TableTool.STATE_INSERT);
                    myItemRecyclerViewAdapter.addItem(medias.size()-1, media);
                }
                break;
            default:
                break;
        }
    }
}
