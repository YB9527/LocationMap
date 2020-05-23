package com.xupu.locationmap.projectmanager.service;

import com.tianditu.maps.Map.Project;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.exceptionmanager.MapException;
import com.xupu.locationmap.projectmanager.po.Media;
import com.xupu.locationmap.projectmanager.po.MediaType;
import com.xupu.locationmap.projectmanager.po.NF;
import com.xupu.locationmap.projectmanager.po.SugProject;
import com.xupu.locationmap.projectmanager.po.XZDM;

import java.util.UUID;

public class MediaService {

    /**
     *
     * @param nf
     * @param mediaType
     * @param fileType 不要带格式，会自动根据类型添加
     * @return
     */
    public static Media getMedia(NF nf, MediaType mediaType,  String fileType) {
        SugProject project =ProjectService.getCurrentSugProject();
        if(project == null){
            AndroidTool.showAnsyTost("请先选择项目",1);
            return  null;
        }
        XZDM xzdm = XZQYService.getCurrentXZDM();
        if(xzdm == null){
            AndroidTool.showAnsyTost("请先选择项目",1);
            return  null;
        }
        String uuid = UUID.randomUUID().toString();
        String path = AndroidTool.getMainActivity().getFilesDir().getAbsolutePath()+"/" + project.getName()+"/"+xzdm.getCode()+"_"+xzdm.getCaption()+"/"
                +nf.getName()+"_"+fileType+"_"+nf.getId()+"/"+uuid;

        switch (mediaType){
            case Photo:
                path = path +".jpg";
                break;
            case Video:
                path = path +".mp4";
                break;
        }
        Media media = new Media(mediaType,path);
        return  media;
    }
}
