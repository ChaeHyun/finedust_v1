package ch.breatheinandout.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Forecast {
    @SerializedName("_returnType")
    @Expose
    private String returnType;
    @SerializedName("actionKnack")
    @Expose
    private String actionKnack;
    @SerializedName("dataTime")
    @Expose
    private String dataTime;
    @SerializedName("f_data_time")
    @Expose
    private String fDataTime;
    @SerializedName("f_data_time1")
    @Expose
    private String fDataTime1;
    @SerializedName("f_data_time2")
    @Expose
    private String fDataTime2;
    @SerializedName("f_data_time3")
    @Expose
    private String fDataTime3;
    @SerializedName("f_inform_data")
    @Expose
    private String fInformData;
    @SerializedName("imageUrl1")
    @Expose
    private String imageUrl1;
    @SerializedName("imageUrl2")
    @Expose
    private String imageUrl2;
    @SerializedName("imageUrl3")
    @Expose
    private String imageUrl3;
    @SerializedName("imageUrl4")
    @Expose
    private String imageUrl4;
    @SerializedName("imageUrl5")
    @Expose
    private String imageUrl5;
    @SerializedName("imageUrl6")
    @Expose
    private String imageUrl6;
    @SerializedName("imageUrl7")
    @Expose
    private String imageUrl7;
    @SerializedName("imageUrl8")
    @Expose
    private String imageUrl8;
    @SerializedName("imageUrl9")
    @Expose
    private String imageUrl9;
    @SerializedName("informCause")
    @Expose
    private String informCause;
    @SerializedName("informCode")
    @Expose
    private String informCode;
    @SerializedName("informData")
    @Expose
    private String informData;
    @SerializedName("informGrade")
    @Expose
    private String informGrade;
    @SerializedName("informOverall")
    @Expose
    private String informOverall;
    @SerializedName("numOfRows")
    @Expose
    private String numOfRows;
    @SerializedName("pageNo")
    @Expose
    private String pageNo;
    @SerializedName("resultCode")
    @Expose
    private String resultCode;
    @SerializedName("resultMsg")
    @Expose
    private String resultMsg;
    @SerializedName("searchDate")
    @Expose
    private String searchDate;
    @SerializedName("serviceKey")
    @Expose
    private String serviceKey;
    @SerializedName("totalCount")
    @Expose
    private String totalCount;
    @SerializedName("ver")
    @Expose
    private String ver;

    private String day;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getActionKnack() {
        return actionKnack;
    }

    public void setActionKnack(String actionKnack) {
        this.actionKnack = actionKnack;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getFDataTime() {
        return fDataTime;
    }

    public void setFDataTime(String fDataTime) {
        this.fDataTime = fDataTime;
    }

    public String getFDataTime1() {
        return fDataTime1;
    }

    public void setFDataTime1(String fDataTime1) {
        this.fDataTime1 = fDataTime1;
    }

    public String getFDataTime2() {
        return fDataTime2;
    }

    public void setFDataTime2(String fDataTime2) {
        this.fDataTime2 = fDataTime2;
    }

    public String getFDataTime3() {
        return fDataTime3;
    }

    public void setFDataTime3(String fDataTime3) {
        this.fDataTime3 = fDataTime3;
    }

    public String getFInformData() {
        return fInformData;
    }

    public void setFInformData(String fInformData) {
        this.fInformData = fInformData;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public String getImageUrl3() {
        return imageUrl3;
    }

    public void setImageUrl3(String imageUrl3) {
        this.imageUrl3 = imageUrl3;
    }

    public String getImageUrl4() {
        return imageUrl4;
    }

    public void setImageUrl4(String imageUrl4) {
        this.imageUrl4 = imageUrl4;
    }

    public String getImageUrl5() {
        return imageUrl5;
    }

    public void setImageUrl5(String imageUrl5) {
        this.imageUrl5 = imageUrl5;
    }

    public String getImageUrl6() {
        return imageUrl6;
    }

    public void setImageUrl6(String imageUrl6) {
        this.imageUrl6 = imageUrl6;
    }

    public String getImageUrl7() {
        return imageUrl7;
    }

    public void setImageUrl7(String imageUrl7) {
        this.imageUrl7 = imageUrl7;
    }

    public String getImageUrl8() {
        return imageUrl8;
    }

    public void setImageUrl8(String imageUrl8) {
        this.imageUrl8 = imageUrl8;
    }

    public String getImageUrl9() {
        return imageUrl9;
    }

    public void setImageUrl9(String imageUrl9) {
        this.imageUrl9 = imageUrl9;
    }

    public String getInformCause() {
        return informCause;
    }

    public void setInformCause(String informCause) {
        this.informCause = informCause;
    }

    public String getInformCode() {
        return informCode;
    }

    public void setInformCode(String informCode) {
        this.informCode = informCode;
    }

    public String getInformData() {
        return informData;
    }

    public void setInformData(String informData) {
        this.informData = informData;
    }

    public String getInformGrade() {
        return informGrade;
    }

    public void setInformGrade(String informGrade) {
        this.informGrade = informGrade;
    }

    public String getInformOverall() {
        return informOverall;
    }

    public void setInformOverall(String informOverall) {
        this.informOverall = informOverall;
    }

    public String getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(String numOfRows) {
        this.numOfRows = numOfRows;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }
}
