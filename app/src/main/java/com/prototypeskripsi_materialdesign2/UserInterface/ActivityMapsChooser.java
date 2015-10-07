package com.prototypeskripsi_materialdesign2.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.prototypeskripsi_materialdesign2.CustomLayout.SlidingTabLayout;
import com.prototypeskripsi_materialdesign2.R;

public class ActivityMapsChooser extends AppCompatActivity {
    public static ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_chooser);

        mViewPager = (ViewPager) findViewById(R.id.container_page);
        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.container_tab);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mViewPager.setKeepScreenOn(true);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ActivityMapsChooser.this, ActivityMain.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.translate_back_in, R.anim.translate_back_out);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private String[] tabTeks = {"Server Data", "Device Data", "New Data"};

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    if (ActivityMain.mapsListServer.isEmpty() || !ActivityMain.connectionResult) {
                        fragment = FragmentLostConnection.getInstance();
                    } else {
                        fragment = FragmentTabDownloadData.getInstance();
                    }
                    break;
                case 1:
                    fragment = FragmentTabDeviceData.getInstance();
                    break;
                case 2:
                    fragment = FragmentTabNewData.getInstance();
                    break;
                default:
                    fragment = FragmentLostConnection.getInstance();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTeks[position];
        }
    }
}
