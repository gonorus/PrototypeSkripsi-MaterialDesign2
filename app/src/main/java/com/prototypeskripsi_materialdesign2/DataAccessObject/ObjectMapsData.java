package com.prototypeskripsi_materialdesign2.DataAccessObject;

public class ObjectMapsData {
    private String name;
    private String data;
    private String path;

    public ObjectMapsData(String n, String d, String p) {
        name = n;
        data = d;
        path = p;
    }

    public ObjectMapsData(String n, String d) {
        name = n;
        data = d;
        path = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public String getPath() {
        return path;
    }
}
