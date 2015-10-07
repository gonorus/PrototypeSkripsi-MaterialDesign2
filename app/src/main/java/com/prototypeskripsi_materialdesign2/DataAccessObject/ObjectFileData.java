package com.prototypeskripsi_materialdesign2.DataAccessObject;

public class ObjectFileData implements Comparable<ObjectFileData> {
    private String name;
    private String data;
    private String path;

    public ObjectFileData(String name, String data, String path) {
        this.name = name;
        this.data = data;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int compareTo(ObjectFileData another) {
        if(this.name != null)
            return this.name.toLowerCase().compareTo(another.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }
}
