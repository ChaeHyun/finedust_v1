package com.finedust.model;



import java.util.ArrayList;

public class RecentData {
    private String currentMode;

    private Addresses addr;     //
    private ArrayList<Station> savedStations;   // 0~3, Const.RECENT_STATION_LIST
    private AirCondition airCondition;      //Aircondition Data

    public RecentData() {

    }

    public RecentData(String currentMode, Addresses addr, ArrayList<Station> saveStations, AirCondition airCondition) {
        this.currentMode = currentMode;
        this.addr = addr;
        this.savedStations = saveStations;
        this. airCondition = airCondition;
    }

    public String getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(String currentMode) {
        this.currentMode = currentMode;
    }

    public Addresses getAddr() {
        return addr;
    }

    public void setAddr(Addresses addr) {
        this.addr = addr;
    }

    public ArrayList<Station> getSavedStations() {
        return savedStations;
    }

    public void setSavedStations(ArrayList<Station> savedStations) {
        this.savedStations = savedStations;
    }

    public AirCondition getAirCondition() {
        return airCondition;
    }

    public void setAirCondition(AirCondition airCondition) {
        this.airCondition = airCondition;
    }
}
