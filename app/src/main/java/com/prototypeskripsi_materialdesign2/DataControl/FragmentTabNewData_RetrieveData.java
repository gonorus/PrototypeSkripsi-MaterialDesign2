package com.prototypeskripsi_materialdesign2.DataControl;

import android.os.Environment;
import android.webkit.MimeTypeMap;

import com.prototypeskripsi_materialdesign2.DataAccessObject.ObjectFileData;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentTabNewData_RetrieveData {
    public static String root;
    private File currentDir;
    private List<ObjectFileData> directories = new ArrayList<>();
    private List<ObjectFileData> files = new ArrayList<>();

    public FragmentTabNewData_RetrieveData(File currentDir) {
        this.currentDir = currentDir;
        root = new File(Environment.getExternalStorageDirectory().getPath()).getAbsolutePath();
        fill(currentDir);
    }

    public FragmentTabNewData_RetrieveData() {
        currentDir = new File(Environment.getExternalStorageDirectory().getPath());
        root = new File(Environment.getExternalStorageDirectory().getPath()).getAbsolutePath();
        fill(currentDir);
    }

    private void fill(File f) {
        File[] dirs = f.listFiles();

        try {
            for (File file : dirs) {
                if (file.isDirectory()) {
                    directories.add(new ObjectFileData(file.getName(), "Folder", file.getAbsolutePath()));
                } else {
                    String ext = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
                    if (ext.equalsIgnoreCase("csv")) {
                        files.add(new ObjectFileData(file.getName(), "File", file.getAbsolutePath()));
                    } else {
                        files.add(new ObjectFileData(file.getName(), "Not File", file.getAbsolutePath()));
                    }
                }
            }
        } catch (Exception e) {
        }

        Collections.sort(directories);
        Collections.sort(files);
        directories.addAll(files);

        if (!f.getAbsolutePath().equalsIgnoreCase(root)) {
            directories.add(0, new ObjectFileData("..", "Root", f.getParent()));
        }
    }

    public List<ObjectFileData> getDirectories() {
        return directories;
    }
}
