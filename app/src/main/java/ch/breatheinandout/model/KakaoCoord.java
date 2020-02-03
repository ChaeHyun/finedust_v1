package ch.breatheinandout.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KakaoCoord {

    @SerializedName("documents")
    @Expose
    private List<GpsData> documents;

    public List<GpsData> getDocuments() {
        return documents;
    }

    public void setDocuments(List<GpsData> documents) {
        this.documents = documents;
    }
}
