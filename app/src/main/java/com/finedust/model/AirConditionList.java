package com.finedust.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by CH on 2017-09-02.
 */

public class AirConditionList {
    @SerializedName("list")
    @Expose
    private ArrayList<AirCondition> list = null;
    //    @SerializedName("parm")
//    @Expose
//    private Parm parm;
    //@SerializedName("ArpltnInforInqireSvcVo")
    //@Expose
    //private ArpltnInforInqireSvcVo arpltnInforInqireSvcVo;
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;

    public ArrayList<AirCondition> getList() {
        return list;
    }

    public void setList(ArrayList<AirCondition> list) {
        this.list = list;
    }

//    public Parm getParm() {
//        return parm;
//    }
//
//    public void setParm(Parm parm) {
//        this.parm = parm;
//    }

//    public ArpltnInforInqireSvcVo getArpltnInforInqireSvcVo() {
//        return arpltnInforInqireSvcVo;
//    }
//
//    public void setArpltnInforInqireSvcVo(ArpltnInforInqireSvcVo arpltnInforInqireSvcVo) {
//        this.arpltnInforInqireSvcVo = arpltnInforInqireSvcVo;
//    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
