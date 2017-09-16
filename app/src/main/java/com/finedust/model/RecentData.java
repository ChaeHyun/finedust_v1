package com.finedust.model;

import com.finedust.model.pref.MemorizedAddress;

import java.util.ArrayList;

public class RecentData {
    private String Mode;

    private MemorizedAddress addr;
    private ArrayList<Station> savedStations;
    private AirCondition airCondition;

    public RecentData() {

    }

    public RecentData(String mode, MemorizedAddress addr, ArrayList<Station> saveStations, AirCondition airCondition) {
        this.Mode = mode;
        this.addr = addr;
        this.savedStations = saveStations;
        this. airCondition = airCondition;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    public MemorizedAddress getAddr() {
        return addr;
    }

    public void setAddr(MemorizedAddress addr) {
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
