package com.prototypeskripsi_materialdesign2.UserInterface;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prototypeskripsi_materialdesign2.CustomActionEvent.RecyclerViewItemClick;
import com.prototypeskripsi_materialdesign2.DataAccessObject.ObjectMapsData;
import com.prototypeskripsi_materialdesign2.DataControl.FragmentTabDownloadData_Adapter;
import com.prototypeskripsi_materialdesign2.DataControl.FragmentTabDownloadData_RetrieveData;
import com.prototypeskripsi_materialdesign2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTabDownloadData extends Fragment {
    private RecyclerView mRecyclerView;

    public FragmentTabDownloadData() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_tab_download_data, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.addOnItemTouchListener(new RecyclerViewItemClick(getActivity(), new RecyclerViewItemClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ObjectMapsData mapsData = ActivityMain.mapsListServer.get(position);
                new FragmentTabDownloadData_RetrieveData(
                        mapsData.getName(),
                        new ProgressDialog(getActivity()), getActivity()
                ).execute(mapsData);
            }
        }));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new FragmentTabDownloadData_Adapter(ActivityMain.mapsListServer);
        mRecyclerView.setAdapter(mAdapter);
    }

    public static FragmentTabDownloadData getInstance() {
        return new FragmentTabDownloadData();
    }
}
