package com.finedust.model;

/**
 * Created by CH on 2017-09-08.
 */

public class GpsData {
    private String tm_x;
    private String tm_y;

    private String wgs_x;
    private String wgs_y;

    public GpsData() {
        this.tm_x = "0";
        this.tm_y = "0";
        this.wgs_x = "0";
        this.wgs_y = "0";
    }

    public String getTm_x() {
        return tm_x;
    }

    public void setTm_x(String tm_x) {
        this.tm_x = tm_x;
    }

    public String getTm_y() {
        return tm_y;
    }

    public void setTm_y(String tm_y) {
        this.tm_y = tm_y;
    }

    public String getWgs_x() {
        return wgs_x;
    }

    public void setWgs_x(String wgs_x) {
        this.wgs_x = wgs_x;
    }

    public String getWgs_y() {
        return wgs_y;
    }

    public void setWgs_y(String wgs_y) {
        this.wgs_y = wgs_y;
    }
}
