package com.xupu.locationmap.common.imgview;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.projectmanager.po.MyJSONObject;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerActivity extends AbstractPagesActivity {


    public ViewPagerActivity() {
        super(R.layout.view_pager);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.finish();
        return  true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidTool.setFullWindow(this);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String meidsStr = intent.getStringExtra("medias");
        int currentIndex = intent.getIntExtra("index",-1);
        List<MyJSONObject> medias =  new Gson().fromJson(meidsStr,new TypeToken<List<MyJSONObject>>(){}.getType());
        setPages(0,medias);
        ViewPager horizontalPager = findViewById(R.id.horizontal_pager);
        ScreenSlidePagerAdapter screenSlidePagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        horizontalPager.setAdapter(screenSlidePagerAdapter);
        horizontalPager.setCurrentItem(currentIndex);
        initTitle();

    }
    private void initTitle() {
        AndroidTool.addTitleFragment(this, "附件");
    }
    @Override
    public void onBackPressed() {
        ViewPager viewPager = findViewById(getPage() == 0 ? R.id.horizontal_pager : R.id.vertical_pager);
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @Override
    protected void onPageChanged(int page) {
        if (getPage() == 0) {
            findViewById(R.id.horizontal_pager).setVisibility(View.VISIBLE);
            findViewById(R.id.vertical_pager).setVisibility(View.GONE);
        } else {
            findViewById(R.id.horizontal_pager).setVisibility(View.GONE);
            findViewById(R.id.vertical_pager).setVisibility(View.VISIBLE);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ViewPagerFragment fragment = new ViewPagerFragment(position+1+"/"+getPages().size(), getPages().get(position));
            return fragment;
        }

        @Override
        public int getCount() {
            return getPages().size();
        }



    }

}
