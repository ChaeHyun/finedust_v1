package com.finedust.service;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.finedust.model.Const;
import com.finedust.utils.SharedPreferences;
import com.finedust.widget.WidgetDark;


public class WidgetDarkService extends Service {
    private static final String TAG = WidgetDarkService.class.getSimpleName();

    private SharedPreferences pref;

    public WidgetDarkService() {
        this.pref = new SharedPreferences(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, " ** onStartCommand()");
        try {
            Bundle mExtras = intent.getExtras();
            int mAppWidgetId = mExtras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            String widgetMode = intent.getStringExtra(Const.WIDGET_MODE);
            String widgetTheme = intent.getStringExtra(Const.WIDGET_THEME);

            //Temp Interval time
            String interval = pref.getValue(SharedPreferences.INTERVAL + mAppWidgetId, Const.WIDGET_DEFAULT_INTERVAL);
            int intervalNum = Integer.parseInt(interval) * 1000 * 60 * 60;

            Log.i(TAG, "   [알람설정(WidgetService)\n   widgetId : " + mAppWidgetId + " , THEME : "+ widgetTheme + " , 모드 : " + widgetMode +" , 인터벌 : " + interval + " 시간 ");

            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                long nextTime = System.currentTimeMillis() + intervalNum;
                setNextAlarmSchedule(mAppWidgetId, nextTime, widgetMode, widgetTheme);
            }

            // GET DATA FROM THE SERVER.
            WidgetDark.setProgressViewVisibility(this, mAppWidgetId, true);
            RequestWidgetData requestWidgetData = new RequestWidgetData(this, mAppWidgetId, widgetMode, widgetTheme);
            requestWidgetData.startGetDataFromServer(widgetMode);
        }
        catch (NullPointerException e) {
            Log.v(TAG, "AppWidgetId is not valid. [Wrong Widget Id].");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void setNextAlarmSchedule(int mAppWidgetId, long nextTime, String widgetMode, String widgetTheme) {
        Intent alarmRefresh = new Intent(this, WidgetDarkService.class);
        alarmRefresh.setData(Uri.withAppendedPath(Uri.parse(widgetTheme + "://widget/id") , String.valueOf(mAppWidgetId)));
        alarmRefresh.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        alarmRefresh.putExtra(Const.WIDGET_MODE, widgetMode);
        alarmRefresh.putExtra(Const.WIDGET_THEME, widgetTheme);

        PendingIntent pendingAlarmRefresh = PendingIntent.getService(this, mAppWidgetId, alarmRefresh, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Service.ALARM_SERVICE);
        alarmManager.cancel(pendingAlarmRefresh);           // cancel the existing schedule before setting a new one.
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pendingAlarmRefresh);
    }

    public static void cancelAlarmSchedule(Context context, int mAppWidgetId, String widgetTheme) {
        Log.i(TAG, "WidgetId [" + mAppWidgetId + "]'s schedule is deleted.");
        Intent alarmRefresh = new Intent(context , WidgetDarkService.class);
        alarmRefresh.setData(Uri.withAppendedPath(Uri.parse(widgetTheme + "://widget/id/") , String.valueOf(mAppWidgetId)));

        if (mAppWidgetId != 0) {
            PendingIntent pendingAlarmRefresh = PendingIntent.getService(context, mAppWidgetId, alarmRefresh, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
            alarmManager.cancel(pendingAlarmRefresh);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }
}
