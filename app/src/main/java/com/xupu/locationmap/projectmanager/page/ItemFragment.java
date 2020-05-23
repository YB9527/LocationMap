package com.xupu.locationmap.projectmanager.page;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.xupu.locationmap.R;

import com.xupu.locationmap.projectmanager.po.TableDataCustom;
import com.xupu.locationmap.projectmanager.po.TableViewCustom;
import com.xupu.locationmap.projectmanager.po.XZDM;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class ItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private TableDataCustom tableDataCustom;
    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {

    }


    public ItemFragment(TableDataCustom tableDataCustom) {
        this.tableDataCustom = tableDataCustom;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            tableDataCustom = new Gson().fromJson(getArguments().getString("TableDataCustom"), TableDataCustom.class);
            mColumnCount = tableDataCustom.getList().size();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            if (mColumnCount <= 1) {
                //recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                //recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            myItemRecyclerViewAdapter =new MyItemRecyclerViewAdapter(tableDataCustom);

            recyclerView.setAdapter(myItemRecyclerViewAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnListFragmentInteractionListener) {
            //mListener = (OnListFragmentInteractionListener) context;
            //TableListPage tableListPage = (TableListPage)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void addItem(JSONObject jsonObject) {

        myItemRecyclerViewAdapter.addItem(jsonObject);

    }

    public void remove(JSONObject jsonObject) {
        myItemRecyclerViewAdapter.remove(jsonObject);
    }

    public void update(JSONObject jsonObject) {
        myItemRecyclerViewAdapter.update(jsonObject);
    }

}
