package ch.breatheinandout.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StationList {

    @SerializedName("list")
    @Expose
    private ArrayList<Station> list = null;

    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;

    public ArrayList<Station> getList() {
        return list;
    }

    public void setList(ArrayList<Station> list) {
        this.list = list;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

}
