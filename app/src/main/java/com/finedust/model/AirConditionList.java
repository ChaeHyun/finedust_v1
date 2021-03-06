package com.finedust.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class AirConditionList {
    @SerializedName("list")
    @Expose
    private ArrayList<AirCondition> list = null;
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;

    public ArrayList<AirCondition> getList() {
        return list;
    }

    public void setList(ArrayList<AirCondition> list) {
        this.list = list;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
