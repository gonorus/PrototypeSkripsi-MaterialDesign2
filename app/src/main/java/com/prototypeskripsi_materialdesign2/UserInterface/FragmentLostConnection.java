package com.prototypeskripsi_materialdesign2.UserInterface;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prototypeskripsi_materialdesign2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLostConnection extends Fragment {


    public FragmentLostConnection() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_lost_connection, container, false);
    }

    public static FragmentLostConnection getInstance() {
        return new FragmentLostConnection();
    }
}
