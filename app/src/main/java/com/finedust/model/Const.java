package com.finedust.model;

import android.graphics.Color;

import com.finedust.R;


public class Const {
    public static final String SERVICEKEY = "w7R0iLcYWCyiGgCoLaoR4WiGUZMF3tpUwJNIdlsKT%2FSqFnpzhA48By8T3eTkbypNn1P%2BhzMFVeKPHI4rgMvGew%3D%3D";   //Sihoon
    public static final String DAUM_API_KEY = "f9946de16d015bc728f1d55ceafe0c2e&libraries=services";

    public static final String RETURNTYPE_JSON = "json";
    public static final String EMPTY_STRING = "";


    public static final String[] MODE = {"CURRENT" , "LOC_ONE" , "LOC_TWO" , "LOC_THREE"};


    public static  final int NAVI_ICON_LOCATION_SAVED = R.drawable.pin_128;
    public static  final int NAVI_ICON_LOCATION_NOT_SAVED = R.drawable.plus_circle_128;


    public static final String STR_NETWORK_NOT_AVAILABLE = "네트워크가 연결되어 있지 않습니다.";
    public static final String STR_FAIL_GET_DATA_FROM_SERVER = "서버에서 정보를 가져오지 못했습니다.";
    public static final String STR_FAIL_UPDATE_DATA_TO_UI = "업데이트 할 정보를 찾지 못했습니다.";
    public static final String STR_NEED_PERMISSION = "권한 허가가 필요합니다..";

    public static final int[] DRAWABLE_STATES =  {R.drawable.state_fail, R.drawable.state_best, R.drawable.state_good, R.drawable.state_bad, R.drawable.state_verybad};
    public static final int[] DRAWABLE_STATES_FACE =  {R.drawable.face_fail, R.drawable.face_best, R.drawable.face_good, R.drawable.face_bad, R.drawable.face_verybad};
    public static final int[] COLORS = {Color.rgb(0, 0, 0) , Color.rgb(0,150,250), Color.rgb(120, 190, 80) , Color.rgb(255, 220, 80) , Color.rgb(255, 80, 80)};     //FAIL , BEST, GOOD, BAD, VERYBAD
    public static final int[] TOOLBAR_COLORS_DARK = {Color.rgb(0,105,92) , Color.rgb(21,101, 192) , Color.rgb(46,125,50) , Color.rgb(255, 143, 0) , Color.rgb(211,47,47) };
    public static final int[] TOOLBAR_COLORS = { Color.rgb(0,121,107) , Color.rgb(25,118,210) , Color.rgb(56,142,60) , Color.rgb(255, 171, 0) , Color.rgb(229,57,53)};


}
