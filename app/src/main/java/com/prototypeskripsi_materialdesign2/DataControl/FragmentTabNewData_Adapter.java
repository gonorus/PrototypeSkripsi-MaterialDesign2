package com.prototypeskripsi_materialdesign2.DataControl;

import android.app.Dialog;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prototypeskripsi_materialdesign2.DataAccessObject.ObjectFileData;
import com.prototypeskripsi_materialdesign2.R;
import com.prototypeskripsi_materialdesign2.UserInterface.FragmentTabNewData;

import java.io.File;
import java.util.List;

public class FragmentTabNewData_Adapter extends RecyclerView.Adapter<FragmentTabNewData_Adapter.ViewHolder> {
    private List<ObjectFileData> mDataset;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Dialog msgDialog;
        public TextView mTextTitle;
        public TextView mTextPath;
        public ImageView mImageView;
        public LinearLayout mLinearLayout;

        public ViewHolder(ViewGroup v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.image_icon);
            mTextTitle = (TextView) v.findViewById(R.id.text_title);
            mTextPath = (TextView) v.findViewById(R.id.text_path);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.rowContainer);
            mLinearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            File currentDir = new File(mTextPath.getText().toString());
            if (currentDir.isDirectory()) {
                FragmentTabNewData_RetrieveData retrieveData = new FragmentTabNewData_RetrieveData(currentDir);
                mDataset = retrieveData.getDirectories();
                if (currentDir.getAbsolutePath().equalsIgnoreCase(FragmentTabNewData_RetrieveData.root)) {
                    Snackbar.make(FragmentTabNewData.coordinatorLayout, "root", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(FragmentTabNewData.coordinatorLayout, currentDir.getName(), Snackbar.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            } else if (currentDir.isFile()) {
                String ext = MimeTypeMap.getFileExtensionFromUrl(currentDir.getAbsolutePath());
                if (ext.equalsIgnoreCase("csv")) {
                    new FragmentTabNewData_CheckingFile().execute(currentDir.getAbsolutePath());
                } else {
                    msgDialog = new Dialog(FragmentTabNewData.fragmentActivity);
                    msgDialog.setTitle("ERROR");
                    msgDialog.setContentView(R.layout.layout_dialog);
                    TextView textView = (TextView) msgDialog.findViewById(R.id.message);
                    Button button = (Button) msgDialog.findViewById(R.id.dialogButtonOK);
                    textView.setText("Terjadi Kesalahan Pada Inputan, Periksa Data Inputan Kembali !!!");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            msgDialog.dismiss();
                        }
                    });
                    msgDialog.show();
                }
            }
        }
    }

    public FragmentTabNewData_Adapter(List<ObjectFileData> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public FragmentTabNewData_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_custom_file_data, parent, false);
        ViewHolder vh = new ViewHolder((ViewGroup) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        File file = new File(mDataset.get(position).getPath());
        if (mDataset.get(position).getData().equalsIgnoreCase("Folder")) {
            holder.mImageView.setImageResource(R.drawable.ic_folder);
        } else if (mDataset.get(position).getData().equalsIgnoreCase("Root")) {
            holder.mImageView.setImageResource(R.drawable.ic_folder_up);
        } else if (mDataset.get(position).getData().equalsIgnoreCase("File")) {
            holder.mImageView.setImageResource(R.drawable.ic_file);
        } else {
            holder.mImageView.setImageResource(R.drawable.ic_wrong);
        }
        holder.mTextTitle.setText(mDataset.get(position).getName());
        holder.mTextPath.setText(mDataset.get(position).getPath());
    }

    @Override
    public int getItemCount() {
        if (mDataset.isEmpty()) {
            return 0;
        } else {
            return mDataset.size();
        }
    }
}
