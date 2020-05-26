package com.xupu.locationmap.projectmanager.page;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.page.PhotoSingleActivty;
import com.xupu.locationmap.common.po.SFZBack;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.common.tools.FileTool;
import com.xupu.locationmap.common.tools.TableTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.projectmanager.po.BtuFiledCustom;
import com.xupu.locationmap.projectmanager.po.Customizing;
import com.xupu.locationmap.projectmanager.po.EditFiledCusom;
import com.xupu.locationmap.projectmanager.po.FiledCustom;
import com.xupu.locationmap.projectmanager.po.ImgFiledCusom;
import com.xupu.locationmap.projectmanager.po.ItemDataChildCustom;
import com.xupu.locationmap.projectmanager.po.ItemDataCustom;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.po.TableDataCustom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查看详细信息
 */
public class LookInfoFragment extends Fragment {

    private View view;
    private ItemDataCustom itemDataCustom;
    private String photoTag = "photos";
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;


    /**
     * 检查数据是否满足要求
     *
     * @param jsonObject
     * @param filedCustoms
     * @return
     */
    private boolean checkData(JSONObject jsonObject, Collection<FiledCustom> filedCustoms) {
        for (FiledCustom filedCustom : filedCustoms) {
            if (filedCustom instanceof EditFiledCusom) {
                EditFiledCusom editFiledCusom = (EditFiledCusom) filedCustom;
                if (editFiledCusom.isMust()) {
                    String result = jsonObject.getString(filedCustom.getAttribute());
                    if (Tool.isEmpty(result)) {
                        AndroidTool.showAnsyTost("数据没有填写完整", 1);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public LookInfoFragment(ItemDataCustom itemDataCustom) {
        this.itemDataCustom = itemDataCustom;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(itemDataCustom.getRid(), container, false);
        init();
        return view;
    }

    public List<MyJSONObject> medias;
    public List<MyJSONObject> sfzFronts;
    public List<MyJSONObject> sfzBacks;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Map<Integer, FiledCustom> map = new HashMap<>();
        map.put(R.id.img, new ImgFiledCusom("path") {
            @Override
            public void onClick(MyJSONObject mdeia) {

                Intent intent = new Intent(getActivity(), PhotoSingleActivty.class);
                intent.putExtra("media", mdeia);
                startActivity(intent);
            }
        });
        List<ItemDataChildCustom> itemDataChildCustomList = new ArrayList<>();
        //身份证正面
        Map<Integer, FiledCustom> frontMap = new HashMap<>();
        frontMap.put(R.id.name, new EditFiledCusom("name", false));
        frontMap.put(R.id.address, new EditFiledCusom("address", false));
        frontMap.put(R.id.idNumber, new EditFiledCusom("idNumber", false));
        frontMap.put(R.id.sex, new EditFiledCusom("sex", false));
        frontMap.put(R.id.nation, new EditFiledCusom("nation", false));
        frontMap.put(R.id.birthday, new EditFiledCusom("birthday", false));
        ItemDataChildCustom sfzFrontItemDataChildCustom = new ItemDataChildCustom(null, frontMap);

        //身份证背面
        Map<Integer, FiledCustom> backMap = new HashMap<>();
        backMap.put(R.id.signDate, new EditFiledCusom("signDate", false));
        backMap.put(R.id.expiryDate, new EditFiledCusom("expiryDate", false));
        backMap.put(R.id.issueAuthority, new EditFiledCusom("issueAuthority", false));

        List<Map<Integer, FiledCustom>> childridMap = new ArrayList<>();
        childridMap.add(frontMap);
        childridMap.add(backMap);


        map.put(R.id.bz, new EditFiledCusom("bz", false));
        map.put(R.id.task, new EditFiledCusom(Customizing.MEDIA_task, false));
        map.put(R.id.btu_delete, new BtuFiledCustom<MyJSONObject>("删除") {
            @Override
            public void OnClick(MyJSONObject media) {
                TableTool.delete(media);
                myItemRecyclerViewAdapter.remove(media);
                FileTool.deleteFile(media.getJsonobject().getString(Customizing.MEDIA_path));
            }
        }.setConfirm(true, "确认要删除文件吗"));
        //显示身份证信息


       /* TableDataCustom tableDataCustom = new TableDataCustom(R.layout.fragment_photo, map,
                TableTool.findByTableNameAndParentId(Customizing.MEDIA, itemDataCustom.getMyJSONObject().getId()));*/
        TableDataCustom tableDataCustom = new TableDataCustom(R.layout.fragment_photo, map,
                new ArrayList<MyJSONObject>()).setChildRidMap(childridMap);


        tableDataCustom.setEdit(true);
        View recy = view.findViewById(R.id.fl);
        RecyclerView recyclerView = (RecyclerView) recy;
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(tableDataCustom);
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        //myItemRecyclerViewAdapter.notifyDataSetChanged();
        setJSONbject(itemDataCustom.getMyJSONObject());

    }

    private void init() {
        AndroidTool.setView(view, itemDataCustom, false);
        //查看所有的照片

            /*Context context = recy.getContext();
            RecyclerView recyclerView = (RecyclerView) recy;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            MyItemRecyclerViewAdapter myItemRecyclerViewAdapter =new MyItemRecyclerViewAdapter(tableDataCustom);
            recyclerView.setAdapter(myItemRecyclerViewAdapter);
*/


    }

    /**
     * 初始化里面的内容
     *
     * @param myJSONObject
     */
    public void setJSONbject(MyJSONObject myJSONObject) {

        itemDataCustom.setMyJSONObject(myJSONObject);

        if (view != null) {
            AndroidTool.setView(view, itemDataCustom, false);
            medias = TableTool.findByTableNameAndParentId(Customizing.MEDIA, myJSONObject.getId());
            myItemRecyclerViewAdapter.setDatas(medias);

             sfzFronts = new ArrayList<>();
             sfzBacks = new ArrayList<>();
            for (MyJSONObject my : medias){
                sfzFronts.addAll(TableTool.findByTableNameAndParentId(Customizing.SFZ_Front,my.getId()));
                sfzBacks.addAll(TableTool.findByTableNameAndParentId(Customizing.SFZ_back,my.getId()));
            }
            List<List<MyJSONObject>> childs = new ArrayList<>();
            childs.add(sfzFronts);
            childs.add(sfzBacks);
            myItemRecyclerViewAdapter.setChilds(childs);

        }

    }


}
