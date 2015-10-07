package com.prototypeskripsi_materialdesign2.UserInterface;


import android.app.Application;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prototypeskripsi_materialdesign2.DataAccessObject.ObjectFileData;
import com.prototypeskripsi_materialdesign2.DataControl.FragmentTabNewData_Adapter;
import com.prototypeskripsi_materialdesign2.DataControl.FragmentTabNewData_RetrieveData;
import com.prototypeskripsi_materialdesign2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTabNewData extends Fragment {

    public static FragmentActivity fragmentActivity;
    public static CoordinatorLayout coordinatorLayout;
    public static List<ObjectFileData> listFileData;
    private RecyclerView mRecyclerView;

    public FragmentTabNewData() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentActivity = getActivity();
        View view = inflater.inflate(R.layout.fragment_fragment_tab_new_data, container, false);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator_layout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        listFileData = new ArrayList<>();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        FragmentTabNewData_RetrieveData retrieveData = new FragmentTabNewData_RetrieveData();
        listFileData = retrieveData.getDirectories();
        RecyclerView.Adapter mAdapter = new FragmentTabNewData_Adapter(retrieveData.getDirectories());
        mRecyclerView.setAdapter(mAdapter);
    }

    public static FragmentTabNewData getInstance() {
        return new FragmentTabNewData();
    }
}
