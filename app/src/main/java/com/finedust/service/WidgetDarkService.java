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

    static int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private Context mContext;
    private SharedPreferences pref;

    public WidgetDarkService() {
        this.mContext = this;
        this.pref = new SharedPreferences(this);
    }

    public Context getmContext() {
        return mContext;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, " ** onStartCommand()");
        Context context = getmContext();

        try {
            Bundle mExtras = intent.getExtras();

            // Extra Data from WidgetDark.class - updateAppWidget()
            mAppWidgetId = mExtras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            String widgetMode = intent.getStringExtra(Const.WIDGET_MODE);
            String location = intent.getStringExtra(Const.WIDGET_LOCATION);
            String tm_x = intent.getStringExtra(Const.WIDGET_TM_X);
            String tm_y = intent.getStringExtra(Const.WIDGET_TM_Y);

            //Temp Interval time
            String interval = pref.getValue(SharedPreferences.INTERVAL + mAppWidgetId, Const.WIDGET_DEFAULT_INTERVAL);
            int intervalNum = Integer.parseInt(interval) * 1000 * 60 * 60;

            // set Alarm
            long nextTime = System.currentTimeMillis() + intervalNum;

            Log.i(TAG, "** 알람설정\n  mAppWidgetId : " + mAppWidgetId + " , 인터벌 : " + interval + " 시간 \n  (x,y) : " + tm_x + " , " + tm_y + " 주소 : " + location);

            Intent alarmRefresh = new Intent(context , WidgetDarkService.class);
            alarmRefresh.setData(Uri.withAppendedPath(Uri.parse("WidgetDark" + "://widget/id/") , String.valueOf(mAppWidgetId)));
            alarmRefresh.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID , mAppWidgetId);
            alarmRefresh.putExtra(Const.WIDGET_LOCATION, location);
            alarmRefresh.putExtra(Const.WIDGET_MODE, widgetMode);
            alarmRefresh.putExtra(Const.WIDGET_TM_X, tm_x);
            alarmRefresh.putExtra(Const.WIDGET_TM_Y, tm_y);

            if (mAppWidgetId != 0) {
                PendingIntent pendingAlarmRefresh = PendingIntent.getService(context, mAppWidgetId, alarmRefresh, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
                alarmManager.cancel(pendingAlarmRefresh);       // cancel the existing schedule before setting a new one.
                alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pendingAlarmRefresh);
            }

            // GET DATA FROM THE SERVER.
            updateWidgetDataFromServer(location, widgetMode, tm_x, tm_y);

        }
        catch (NullPointerException e) {
            Log.i(TAG, "BUNDLE is NULL !!!!");
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public static void cancelAlarmSchedule(Context context, int mAppWidgetId) {
        Log.i(TAG, "WidgetId [" + mAppWidgetId + "] 's schedule is deleted.");
        Intent alarmRefresh = new Intent(context , WidgetDarkService.class);
        alarmRefresh.setData(Uri.withAppendedPath(Uri.parse("WidgetDark" + "://widget/id/") , String.valueOf(mAppWidgetId)));

        if (mAppWidgetId != 0) {
            PendingIntent pendingAlarmRefresh = PendingIntent.getService(context, mAppWidgetId, alarmRefresh, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
            alarmManager.cancel(pendingAlarmRefresh);
        }
    }

    public void updateWidgetDataFromServer(String location, String widgetMode, String tmX, String tmY) {

        Intent i  = new Intent(Const.WIDGET_DARK_RESPONSE_FROM_SERVER);
        i.putExtra(Const.WIDGET_ID, String.valueOf(mAppWidgetId));
        i.putExtra(Const.WIDGET_LOCATION, location);
        i.putExtra(Const.WIDGET_MODE, widgetMode);
        i.putExtra(Const.WIDGET_TM_X, tmX);
        i.putExtra(Const.WIDGET_TM_Y, tmY);

        // Run Progress Dial
        WidgetDark.setProgressViewVisibility(getmContext(), mAppWidgetId, true);


        mContext.sendBroadcast(i);
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
