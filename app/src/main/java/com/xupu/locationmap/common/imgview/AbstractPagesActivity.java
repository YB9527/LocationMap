package com.xupu.locationmap.common.imgview;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.xupu.locationmap.projectmanager.po.MyJSONObject;

import java.util.List;

public abstract class AbstractPagesActivity extends FragmentActivity {

    private static final String BUNDLE_PAGE = "page";

    private int page;


    private final int layout;
    private  List<MyJSONObject> medias;

    protected AbstractPagesActivity( int layout) {
        this.layout = layout;
       /* Intent intent = getIntent();
        String s = intent.getStringExtra("path");
        s = intent.getStringExtra("path");*/
    }

    public void setPages(int currentIndex, List<MyJSONObject> medias){
        this.medias = medias;
        this.page = currentIndex;
    }

    public List<MyJSONObject> getPages() {
        return medias;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNotes();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_PAGE, page);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }


    private void updateNotes() {
        if (page > medias.size() - 1) {
            return;
        }


        onPageChanged(page);
    }

    protected final int getPage() {
        return page;
    }

    protected void onPageChanged(int page) {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
