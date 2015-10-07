package com.prototypeskripsi_materialdesign2.DataAccessObject;

import java.util.List;

public class ObjectMaps {
    private String namaKota;
    private double latitude;
    private double longitude;
    private List<String> boundaryLatitude;
    private List<String> boundaryLongitude;

    public ObjectMaps() {
    }

    public ObjectMaps(String namaKota, double latitude, double longitude, List<String> boundaryLatitude, List<String> boundaryLongitude) {
        this.namaKota = namaKota;
        this.latitude = latitude;
        this.longitude = longitude;
        this.boundaryLatitude = boundaryLatitude;
        this.boundaryLongitude = boundaryLongitude;
    }

    public String getNamaKota() {
        return namaKota;
    }

    public void setNamaKota(String namaKota) {
        this.namaKota = namaKota;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<String> getBoundaryLatitude() {
        return boundaryLatitude;
    }

    public void setBoundaryLatitude(List<String> boundaryLatitude) {
        this.boundaryLatitude = boundaryLatitude;
    }

    public List<String> getBoundaryLongitude() {
        return boundaryLongitude;
    }

    public void setBoundaryLongitude(List<String> boundaryLongitude) {
        this.boundaryLongitude = boundaryLongitude;
    }
}
