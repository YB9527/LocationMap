package com.xupu.locationmap.common.imgview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.xupu.locationmap.R;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;
import com.xupu.locationmap.projectmanager.service.MediaService;


public class ViewPagerFragment extends Fragment {

    private static final String BUNDLE_ASSET = "asset";

    private MyJSONObject media;
    private String tag;

    public ViewPagerFragment(String tag, MyJSONObject media) {

        this.media = media;
        this.tag =tag;
    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_pager_page, container, false);



        SubsamplingScaleImageView imageView = rootView.findViewById(R.id.imageView);
        imageView.setImage(ImageSource.uri(MediaService.getPath(media)));
        TextView tv = rootView.findViewById(R.id.tag);
        tv.setText(tag);


        return rootView;
    }



}
