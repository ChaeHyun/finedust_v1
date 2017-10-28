package com.finedust.model;

import android.graphics.Color;

import com.finedust.R;


public class Const {
    public static final String SERVICEKEY = "w7R0iLcYWCyiGgCoLaoR4WiGUZMF3tpUwJNIdlsKT%2FSqFnpzhA48By8T3eTkbypNn1P%2BhzMFVeKPHI4rgMvGew%3D%3D";   //Sihoon
    public static final String DAUM_API_KEY = "f9946de16d015bc728f1d55ceafe0c2e&libraries=services";

    public static final String RETURNTYPE_JSON = "json";
    public static final String EMPTY_STRING = "";


    public static final String[] MODE = {"CURRENT" , "LOC_ONE" , "LOC_TWO" , "LOC_THREE"};
    public static final String[] ON_OFF = {"ON" , "OFF"};

    public static  final int NAVI_ICON_LOCATION_SAVED = R.drawable.pin_128;
    public static  final int NAVI_ICON_LOCATION_NOT_SAVED = R.drawable.plus_circle_128;


    public static final String STR_NETWORK_NOT_AVAILABLE = "네트워크가 연결되어 있지 않습니다.";
    public static final String STR_FAIL_GET_DATA_FROM_SERVER = "서버에서 정보를 가져오지 못했습니다.";
    public static final String STR_FAIL_UPDATE_DATA_TO_UI = "업데이트 할 정보를 찾지 못했습니다.";
    public static final String STR_NEED_PERMISSION = "권한 허가가 필요합니다.";

    public static final int[] DRAWABLE_STATES =  {R.drawable.state_fail, R.drawable.state_best, R.drawable.state_good, R.drawable.state_bad, R.drawable.state_verybad};
    public static final int[] DRAWABLE_STATES_SMALL =  {R.drawable.small_face_fail, R.drawable.small_best, R.drawable.small_good, R.drawable.small_bad, R.drawable.small_verybad};
    public static final int[] DRAWABLE_STATES_FACE =  {R.drawable.face_fail, R.drawable.face_best, R.drawable.face_good, R.drawable.face_bad, R.drawable.face_verybad};
    public static final int[] DRAWABLE_STATES_FACE_SMALL =  {R.drawable.small_face_fail, R.drawable.small_face_best, R.drawable.small_face_good, R.drawable.small_face_bad, R.drawable.small_face_verybad};
    public static final int[] COLORS = {Color.rgb(0, 0, 0) , Color.rgb(0,150,250), Color.rgb(120, 190, 80) , Color.rgb(255, 220, 80) , Color.rgb(255, 80, 80)};     //FAIL , BEST, GOOD, BAD, VERYBAD
    public static final int[] TOOLBAR_COLORS_DARK = {Color.rgb(0, 105, 92) , Color.rgb(21, 101,192) , Color.rgb(46, 125, 50) , Color.rgb(255, 143, 0) , Color.rgb(211, 47, 47) };
    public static final int[] TOOLBAR_COLORS = { Color.rgb(0, 150, 136) , Color.rgb(33, 150, 243) , Color.rgb(76, 175, 80) , Color.rgb(255, 193, 7) , Color.rgb(244, 67, 54)};

    public static final  int[] STANDARD_FOR_PM10_AIRKOREA   = {30, 80, 150};
    public static final  int[] STANDARD_FOR_PM25_AIRKOREA   = {30, 50, 70};
    public static final  int[] STANDARD_FOR_PM10_WHO        = {15, 50, 100};
    public static final  int[] STANDARD_FOR_PM25_WHO        = {15, 25, 35};

    public static final String[] SELF_GRADE_PM10 = {"PM10_BEST", "PM10_GOOD", "PM10_BAD"};
    public static final String[] SELF_GRADE_PM25 = {"PM25_BEST", "PM25_GOOD", "PM25_BAD"};

    public static final String GRADE_BEST    = "1";
    public static final String GRADE_GOOD    = "2";
    public static final String GRADE_BAD     = "3";
    public static final String GRADE_VERYBAD = "4";


    public static final String BOOT_COMPLETED = "ch.bootcompleted.control.BootCompletedReceiver";


    public static final String DEVICE_CATEGORY_SAMSUNG = "samsung";

    public static final String WIDGET_DEFAULT_INTERVAL = "3";
    public static final String WIDGET_DEFAULT_TRANSPARENT = "170";


    public static final String WIDGET_DARK_CALL_SERVICE = "ch.widget_action.WIDGET_DARK_GETDATA_FROM_SERVER";
    public static final String WIDGET_WHITE_CALL_SERVICE = "ch.widget_action.WIDGET_WHITE_GETDATA_FROM_SERVER";
    public static final String WIDGET_DARK_RESPONSE_FROM_SERVER = "ch.widget_action.WIDGET_DARK_RESPONSE_FROM_SERVER";
    public static final String WIDGET_WHITE_RESPONSE_FROM_SERVER = "ch.widget_action.WIDGET_WHITE_RESPONSE_FROM_SERVER";


    public static final String WIDGET_MODE = "WidgetMode_";
    public static final String WIDGET_LOCATION = "WidgetLocation_";
    public static final String WIDGET_TM_X = "Widget_TM_X_";
    public static final String WIDGET_TM_Y = "Widget_TM_Y_";
    public static final String WIDGET_THEME = "WidgetTheme_";

    public static final String WIDGET_ID = "WidgetId";
    public static final String WIDGET_TRANS = "TransparentValue";


    public static final String DARKWIDGET = "WidgetDark";
    public static final String WHITEWIDGET = "WidgetWhite";

    public static final String ARRAY_VALUE = "ArrayValue";
    public static final String ARRAY_GRADE = "ArrayGrade";

    public static final String WIDGET_DELETED = "DELETED";



}
