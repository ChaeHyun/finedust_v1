package com.finedust.service;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.finedust.model.Const;
import com.finedust.utils.SharedPreferences;
import com.finedust.widget.WidgetWhite;

public class WidgetWhiteService extends Service {
    private static final String TAG = WidgetWhiteService.class.getSimpleName();

    SharedPreferences pref;

    public WidgetWhiteService() {
        pref = new SharedPreferences(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, " ** onStartCommand()");

        try {
            Bundle mExtras = intent.getExtras();
            int mAppWidgetId = mExtras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,  AppWidgetManager.INVALID_APPWIDGET_ID);
            String widgetMode = intent.getStringExtra(Const.WIDGET_MODE);
            String widgetTheme = intent.getStringExtra(Const.WIDGET_THEME);

            String interval = pref.getValue(SharedPreferences.INTERVAL + mAppWidgetId, Const.WIDGET_DEFAULT_INTERVAL);
            int intervalNum = Integer.parseInt(interval) * 1000 * 60 * 60 / 10;

            Log.i(TAG, "   [알람설정(WidgetWhiteService)]\n   widgetId : " + mAppWidgetId
            + " , Theme : " + widgetTheme + " , mode : " + widgetMode + " , interval : " + interval + "시간");


            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                long nextTime = System.currentTimeMillis() + intervalNum;
                setNextAlarmSchedule(mAppWidgetId, nextTime, widgetMode, widgetTheme);
            }

            WidgetWhite.setProgressViewVisibility(this, mAppWidgetId, true);
            RequestWidgetData requestWidgetData = new RequestWidgetData(this, mAppWidgetId, widgetMode, widgetTheme);
            requestWidgetData.startGetDataFromServer(widgetMode);
        }
        catch (NullPointerException e) {
            Log.v(TAG, "AppWidgetId is not valid..");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void setNextAlarmSchedule(int mAppWidgetId, long nextTime, String widgetMode, String widgetTheme) {
        Intent alarmRefresh = new Intent(this, WidgetWhiteService.class);
        alarmRefresh.setData(Uri.withAppendedPath(Uri.parse(widgetTheme + "://widget/id") , String.valueOf(mAppWidgetId)));
        alarmRefresh.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        alarmRefresh.putExtra(Const.WIDGET_MODE, widgetMode);
        alarmRefresh.putExtra(Const.WIDGET_THEME, widgetTheme);

        PendingIntent pendingAlarmRefresh = PendingIntent.getService(this, mAppWidgetId, alarmRefresh, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Service.ALARM_SERVICE);
        alarmManager.cancel(pendingAlarmRefresh);           // cancel the existing schedule before setting a new one.
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pendingAlarmRefresh);
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
