package com.prototypeskripsi_materialdesign2.DataControl;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prototypeskripsi_materialdesign2.DataAccessObject.ObjectMapsData;
import com.prototypeskripsi_materialdesign2.R;
import com.prototypeskripsi_materialdesign2.UserInterface.ActivityMaps;
import com.prototypeskripsi_materialdesign2.UserInterface.FragmentTabDeviceData;

import java.util.List;

public class FragmentTabDeviceData_Adapter extends RecyclerView.Adapter<FragmentTabDeviceData_Adapter.ViewHolder> {
    private List<ObjectMapsData> mDataset;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextTitle;
        public TextView mTextDate;
        public ImageView mImageView1;
        public ImageView mImageView2;
        public LinearLayout mLinearLayout;

        public ViewHolder(ViewGroup v) {
            super(v);
            mImageView1 = (ImageView) v.findViewById(R.id.image_preview1);
            mImageView2 = (ImageView) v.findViewById(R.id.image_preview2);
            mTextTitle = (TextView) v.findViewById(R.id.text_title);
            mTextDate = (TextView) v.findViewById(R.id.text_date);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.rowContainer);
            mLinearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(FragmentTabDeviceData.fragmentActivity, ActivityMaps.class);
            intent.putExtra("fileDir", FragmentTabDeviceData.listMaps.get(getPosition()).getName());
            FragmentTabDeviceData.fragmentActivity.finish();
            FragmentTabDeviceData.fragmentActivity.startActivity(intent);
            FragmentTabDeviceData.fragmentActivity.overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
        }
    }

    public FragmentTabDeviceData_Adapter(List<ObjectMapsData> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public FragmentTabDeviceData_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_custom_device_maps_data, parent, false);
        ViewHolder vh = new ViewHolder((ViewGroup) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Bitmap bitmap1 = BitmapFactory.decodeFile(mDataset.get(position).getPath() + "/screenshot1.png");
        Bitmap bitmap2 = BitmapFactory.decodeFile(mDataset.get(position).getPath() + "/screenshot2.png");
        holder.mImageView1.setImageBitmap(bitmap1);
        holder.mImageView2.setImageBitmap(bitmap2);

        String title = mDataset.get(position).getName();
        holder.mTextTitle.setText(title);
        holder.mTextDate.setText("tanggal dibuat : " + mDataset.get(position).getData());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
