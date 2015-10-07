package com.prototypeskripsi_materialdesign2.UserInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prototypeskripsi_materialdesign2.DataAccessObject.ObjectMapsData;
import com.prototypeskripsi_materialdesign2.DataControl.FragmentTabDeviceData_RetrieveData;
import com.prototypeskripsi_materialdesign2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTabDeviceData extends Fragment {

    public static RecyclerView mRecyclerView;
    public static List<ObjectMapsData> listMaps;
    public static FragmentActivity fragmentActivity;

    public FragmentTabDeviceData() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentActivity = getActivity();
        View view = inflater.inflate(R.layout.fragment_fragment_tab_device_data, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        listMaps = new ArrayList<>();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        new FragmentTabDeviceData_RetrieveData().execute();
    }

    public static FragmentTabDeviceData getInstance() {
        return new FragmentTabDeviceData();
    }
}
