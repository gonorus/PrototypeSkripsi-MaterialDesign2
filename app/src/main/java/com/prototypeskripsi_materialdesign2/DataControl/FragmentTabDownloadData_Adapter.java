package com.prototypeskripsi_materialdesign2.DataControl;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prototypeskripsi_materialdesign2.DataAccessObject.ObjectMapsData;
import com.prototypeskripsi_materialdesign2.R;

import java.util.List;

public class FragmentTabDownloadData_Adapter extends RecyclerView.Adapter<FragmentTabDownloadData_Adapter.ViewHolder> {
    private List<ObjectMapsData> mDataset;

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextTitle;
        public TextView mTextDate;
        public ImageView mImageView;
        public LinearLayout mLinearLayout;

        public ViewHolder(ViewGroup v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.image_icon);
            mTextTitle = (TextView) v.findViewById(R.id.text_title);
            mTextDate = (TextView) v.findViewById(R.id.text_date);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.rowContainer);
        }
    }

    public FragmentTabDownloadData_Adapter(List<ObjectMapsData> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public FragmentTabDownloadData_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_custom_maps_data, parent, false);
        ViewHolder vh = new ViewHolder((ViewGroup) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mImageView.setImageResource(R.drawable.ic_maps);

        String title = mDataset.get(position).getName();
        if (title.length() >= 8) {
            holder.mTextTitle.setText(title.substring(0, 5) + "...");
        } else {
            holder.mTextTitle.setText(title);
        }
        holder.mTextDate.setText("tanggal : " + mDataset.get(position).getData());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
