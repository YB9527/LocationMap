package com.xupu.locationmap.projectmanager.page;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
import com.xupu.locationmap.common.tools.MediaTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.projectmanager.po.Customizing;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.service.MediaService;
import com.xupu.locationmap.projectmanager.service.ProjectService;
import com.xupu.locationmap.projectmanager.service.TaskService;
import com.xupu.locationmap.projectmanager.view.FieldCustom;
import com.xupu.locationmap.projectmanager.view.ImgFieldCusom;
import com.xupu.locationmap.projectmanager.view.PositionField;
import com.xupu.locationmap.projectmanager.view.TableDataCustom;
import com.xupu.locationmap.projectmanager.view.ViewFieldCustom;


import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import static com.xupu.locationmap.projectmanager.page.MapFragment.NVER_TIME;

/**
 * 随手拍页面
 */
public class NverTimeActivty extends AppCompatActivity {
    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private List<MyJSONObject> medias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_nver_time);
        initData();

        initTitle();
        initView();
    }

    private void initData() {
        medias = new ArrayList<>();
        File[] files = new File(MediaService.getNverTimePhotoDir()).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                if(s.endsWith(".jpg")){
                   
                    return true;
                }
                return false;
            }
        });
        if(files != null){
            for (File file : files){
                //boolean delete = file.delete();
                MyJSONObject media = getMedia(file.getAbsolutePath());
                medias.add(media);
            }
        }

    }

    private void initTitle() {
        AndroidTool.addTitleFragment(this, Customizing.NEVER_TIME);
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.recy);

        int fragmentItem = R.layout.fragment_nvertime_photo;
        List<FieldCustom> filedCustoms = new ArrayList<>();
        filedCustoms.add(new ImgFieldCusom(R.id.img, "path") {
            @Override
            public void onClick(MyJSONObject myJSONObject) {
                if (myJSONObject.getId().equals("-1")) {
                    //第一个添加按钮
                    String path = MediaService.getNverTimePhoto();
                    MediaTool.photo(NverTimeActivty.this, NVER_TIME, path);
                } else {
                    //后面的是 media 显示全屏图片
                    Intent intent = new Intent(NverTimeActivty.this, ViewPagerActivity.class);
                    List<MyJSONObject> myJSONObjects1 = new ArrayList<>();
                    myJSONObjects1.addAll(medias);
                    myJSONObjects1.remove(medias.size()-1);
                    
                    intent.putExtra("medias", new Gson().toJson(myJSONObjects1));
                    intent.putExtra("index", medias.indexOf(myJSONObject));
                    startActivity(intent);
                }
            }
        });
        filedCustoms.add(new ViewFieldCustom(R.id.iv_delete) {
            @Override
            public void OnClick(View view, MyJSONObject media) {
                myItemRecyclerViewAdapter.remove(media);
                String path = MediaService.getPath(media);
                //删除文件
                new File(path).delete();
            }
        }.setConfirm(true, "确认要删除吗？"));


        //第一个添加 添加按钮 ，任务名

        MyJSONObject myJSONObject = MediaService.getMedia(ProjectService.getCurrentSugProject(), 0, "");
        myJSONObject.setId("-1");
        //MediaService.setPath(myJSONObject, getResourcesUri(R.drawable.good_night_img));
        medias.add(myJSONObject);
        MediaService.setPath(myJSONObject,null);

        TableDataCustom tableDataCustom = new TableDataCustom(fragmentItem, filedCustoms, medias);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom, recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(myItemRecyclerViewAdapter);

        myItemRecyclerViewAdapter.setLoadViewCallback(new ViewHolderCallback() {
            @Override
            public void call(MyItemRecyclerViewAdapter.ViewHolder holder, int position) {
                if (position == medias.size() - 1) {
                    holder.mView.findViewById(R.id.iv_delete).setVisibility(View.GONE);
                    ImageView imageView = holder.mView.findViewById(R.id.img);
                    // imageView.getLayoutParams().height=150;
                    //imageView.getLayoutParams().width=150;
                }
            }
        });

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

        if (requestCode == NVER_TIME && resultCode == RESULT_OK) {
            //随手拍的返回值
            String path = getIntent().getStringExtra("data");
            MyJSONObject media = getMedia(path);
            myItemRecyclerViewAdapter.addItem(medias.size() - 1, media);
        }

    }

    private MyJSONObject getMedia(String path) {
        MyJSONObject media = new MyJSONObject();
        media.setId(path);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", FileTool.getFileNameWithoutExtension(path));
        jsonObject.put("path", path);
        media.setJsonobject(jsonObject);
        return media;
    }

}
