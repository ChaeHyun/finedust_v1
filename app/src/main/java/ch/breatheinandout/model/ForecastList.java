package ch.breatheinandout.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastList {
    @SerializedName("list")
    @Expose
    private List<Forecast> list = null;

    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;

    public List<Forecast> getList() {
        return list;
    }

    public void setList(List<Forecast> list) {
        this.list = list;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
