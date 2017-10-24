package com.finedust.utils;


import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;

public class SharedPreferences {
    private final String PREF_NAME = "pref";

    private Context context;

    public static final String CURRENT_MODE = "CurrentMode";
    public static final String GRADE_MODE = "GradeMode";
    public static final String[] RECENT_DATA = {"RecentData_CURRENT", "RecentData_ONE", "RecentData_TWO", "RecentData_THREE"};
    public static final String[] MEMORIZED_LOCATIONS = {"MemorizedAddress_Zero", "MemorizedAddress_One", "MemorizedAddress_Two", "MemorizedAddress_Three"};
    public static final String RECENT_DATA_FORECAST = "RecentData_Forecast";

    public static final String INTERVAL = "Interval_";
    public static final String TRANSPARENT = "Transparent_";
    public static final String WIDGET_SELECTED_LOCATION_INDEX = "SelectedLocation_";
    public static final String WIDGET_MODE = "WidgetMode_";







    public SharedPreferences(Context context) {
        this.context = context;
    }

    public void put(String key, String value)
    {
        android.content.SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public void put(String key, boolean value)
    {
        android.content.SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putObject(String key, Object obj) {
        put(key, new Gson().toJson(obj));
    }

    public Object getObject(String key, String def, Object obj) {
        return new Gson().fromJson(getValue(key, def), obj.getClass());
    }

    public String getValue(String key, String value)
    {
        android.content.SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        try {
            return pref.getString(key, value);
        }
        catch(Exception e)
        {
            return value;
        }
    }

    public boolean getValue(String key, boolean value)
    {
        android.content.SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        try {
            return pref.getBoolean(key, value);
        }
        catch(Exception e)
        {
            return value;
        }
    }

    public void removeValue(String key)
    {
        android.content.SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = pref.edit();

        editor.remove(key);
        editor.commit();
    }


}
