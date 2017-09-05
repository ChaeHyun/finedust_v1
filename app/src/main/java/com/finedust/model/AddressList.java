package com.finedust.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddressList {

    @SerializedName("list")
    @Expose
    private ArrayList<Address> list = null;

    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;

    public ArrayList<Address> getList() {
        return list;
    }

    public void setList(ArrayList<Address> list) {
        this.list = list;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

}
