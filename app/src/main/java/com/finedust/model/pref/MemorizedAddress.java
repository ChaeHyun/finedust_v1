package com.finedust.model.pref;


public class MemorizedAddress {
    private String memorizedAddress;
    private String umdName;
    private String tmX;
    private String tmY;

    public MemorizedAddress() {

    }

    public MemorizedAddress(String memorizedAddress, String umdName, String tmX, String tmY) {
        this.memorizedAddress = memorizedAddress;
        this.umdName = umdName;
        this.tmX = tmX;
        this.tmY = tmY;
    }

    public String getMemorizedAddress() {
        return memorizedAddress;
    }

    public void setMemorizedAddress(String memorizedAddress) {
        this.memorizedAddress = memorizedAddress;
    }

    public String getUmdName() {
        return umdName;
    }

    public void setUmdName(String umdName) {
        this.umdName = umdName;
    }

    public String getTmX() {
        return tmX;
    }

    public void setTmX(String tmX) {
        this.tmX = tmX;
    }

    public String getTmY() {
        return tmY;
    }

    public void setTmY(String tmY) {
        this.tmY = tmY;
    }
}
