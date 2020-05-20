package com.xupu.locationmap.projectmanager.page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.xupu.locationmap.R;
import com.xupu.locationmap.common.tools.AndroidTool;
import com.xupu.locationmap.projectmanager.page.dummy.DummyContent;

/**
 * 普通表格数据装载
 */
public class TableListPage extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidTool.setFullWindow(this);
        setTitle("数据");
        setContentView(R.layout.activity_table_list);
        init();
    }

    private void init() {

        //1、表格名称，2、fragment，3、要显示的字段，4、信息按钮
        //viewPagerTab.addView(viewPager);
        //viewPagerTab.addView(viewPager);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("aa", ItemFragment.class)
                .add("bb", ItemFragment.class)
                .add("cc", ItemFragment.class)
                .add("dd", ItemFragment.class)
                .add("ee", ItemFragment.class)
                .add("ff", ItemFragment.class)
                .add("gg", ItemFragment.class)
                .add("ll", ItemFragment.class)
                .add("mm", ItemFragment.class)
                .create());

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        //viewPager.setCurrentItem(5);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("yb","你点击了：");
            }
        });
        SmartTabLayout viewPagerTab = findViewById(R.id.viewpagertab);
        viewPagerTab.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
            @Override
            public void onTabClicked(int position) {
                Log.v("yb","你点击了："+position);
            }
        });
        viewPagerTab.setViewPager(viewPager);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
