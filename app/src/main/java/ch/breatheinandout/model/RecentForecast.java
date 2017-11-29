package ch.breatheinandout.model;


public class RecentForecast {

    private Forecast PM10;
    private Forecast PM25;
    private Forecast O3;

    private String informOverallToday_PM10;
    private String informOverallToday_PM25;
    private String informOverallToday_O3;

    private String informCause_PM10;
    private String informCause_PM25;
    private String informCause_O3;

    private String imageUrl_PM10;
    private String imageUrl_PM25;
    private String imageUrl_O3;



    private String saveDay;

    public RecentForecast() {
    }

    public String getInformCause_PM10() {
        return informCause_PM10;
    }

    public void setInformCause_PM10(String informCause_PM10) {
        this.informCause_PM10 = informCause_PM10;
    }

    public String getInformCause_PM25() {
        return informCause_PM25;
    }

    public void setInformCause_PM25(String informCause_PM25) {
        this.informCause_PM25 = informCause_PM25;
    }

    public String getInformCause_O3() {
        return informCause_O3;
    }

    public void setInformCause_O3(String informCause_O3) {
        this.informCause_O3 = informCause_O3;
    }

    public String getImageUrl_PM10() {
        return imageUrl_PM10;
    }

    public void setImageUrl_PM10(String imageUrl_PM10) {
        this.imageUrl_PM10 = imageUrl_PM10;
    }

    public String getImageUrl_PM25() {
        return imageUrl_PM25;
    }

    public void setImageUrl_PM25(String imageUrl_PM25) {
        this.imageUrl_PM25 = imageUrl_PM25;
    }

    public String getImageUrl_O3() {
        return imageUrl_O3;
    }

    public void setImageUrl_O3(String imageUrl_O3) {
        this.imageUrl_O3 = imageUrl_O3;
    }

    public String getInformOverallToday_PM10() {
        return informOverallToday_PM10;
    }

    public void setInformOverallToday_PM10(String informOverallToday_PM10) {
        this.informOverallToday_PM10 = informOverallToday_PM10;
    }

    public String getInformOverallToday_PM25() {
        return informOverallToday_PM25;
    }

    public void setInformOverallToday_PM25(String informOverallToday_PM25) {
        this.informOverallToday_PM25 = informOverallToday_PM25;
    }

    public String getInformOverallToday_O3() {
        return informOverallToday_O3;
    }

    public void setInformOverallToday_O3(String informOverallToday_O3) {
        this.informOverallToday_O3 = informOverallToday_O3;
    }

    public String getSaveDay() {
        return saveDay;
    }

    public void setSaveDay(String saveDay) {
        this.saveDay = saveDay;
    }

    public Forecast getPM10() {
        return PM10;
    }

    public void setPM10(Forecast PM10) {
        this.PM10 = PM10;
    }

    public Forecast getPM25() {
        return PM25;
    }

    public void setPM25(Forecast PM25) {
        this.PM25 = PM25;
    }

    public Forecast getO3() {
        return O3;
    }

    public void setO3(Forecast o3) {
        O3 = o3;
    }

}
