package com.finedust.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.finedust.R;
import com.finedust.model.Addresses;
import com.finedust.model.Const;
import com.finedust.service.WidgetDarkService;
import com.finedust.utils.DeviceInfo;
import com.finedust.utils.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class WidgetDark extends AppWidgetProvider implements WidgetViews.WidgetDarkView {
    private final static String TAG = WidgetDark.class.getSimpleName();

    SharedPreferences pref;

    static String intervalValue;
    static String transparentValue;
    static int selectedRadioButton;
    static String widgetMode;
    static Addresses savedLocation;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.i(TAG, "## updateAppWidget() with " + appWidgetId);
        RemoteViews views = getProperRemoteViews(context);
        SharedPreferences pref = new SharedPreferences(context);

        try {
            intervalValue = pref.getValue(SharedPreferences.INTERVAL + appWidgetId, Const.WIDGET_DEFAULT_INTERVAL);
            transparentValue = pref.getValue(SharedPreferences.TRANSPARENT + appWidgetId, Const.WIDGET_DEFAULT_TRANSPARENT);
            widgetMode = pref.getValue(SharedPreferences.WIDGET_MODE + appWidgetId , Const.MODE[0]);
            selectedRadioButton = Integer.parseInt(pref.getValue(SharedPreferences.WIDGET_SELECTED_LOCATION_INDEX + appWidgetId, "0"));
            Log.i(TAG, "selectedRadioButton : " + selectedRadioButton);

            savedLocation = (Addresses) pref.getObject(SharedPreferences.MEMORIZED_LOCATIONS[selectedRadioButton], Const.EMPTY_STRING ,new Addresses());
            Log.i(TAG, "주소 : " + savedLocation.getAddr() + "\n좌표 : " + savedLocation.getTmX() + " , " + savedLocation.getTmY()
                    + "\n시간 : " + intervalValue + " , 투명도 : " + transparentValue + " , 모드 : " + widgetMode);

            if  (selectedRadioButton != 0) {
                views.setTextViewText(R.id.value_location, savedLocation.getAddr());
                views.setInt(R.id.layout_ground, "setBackgroundColor", Color.argb(Integer.parseInt(transparentValue), 0,0,0));

                Intent requestIntent = new Intent(context, WidgetDarkService.class);
                requestIntent.setData(Uri.withAppendedPath(Uri.parse("WidgetDark" + "://widget/id/"), String.valueOf(appWidgetId)));
                requestIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                requestIntent.putExtra(Const.WIDGET_LOCATION, savedLocation.getAddr());
                requestIntent.putExtra(Const.WIDGET_MODE, widgetMode);
                requestIntent.putExtra(Const.WIDGET_TM_X, savedLocation.getTmX());
                requestIntent.putExtra(Const.WIDGET_TM_Y, savedLocation.getTmY());

                PendingIntent refreshPending = PendingIntent.getService(context, appWidgetId, requestIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.refresh, refreshPending);
                context.startService(requestIntent);

            }

        }
        catch (NullPointerException e) {
            Log.i(TAG, "예외발생");
            //e.printStackTrace();
        }


        /*
        Intent configIntent = new Intent(context, WidgetDarkConfigureActivity.class);
        configIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        configIntent.setData(Uri.withAppendedPath(Uri.parse("WidgetDark" + "://widget/id/"), String.valueOf(appWidgetId)));
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        PendingIntent configPendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.setting, configPendingIntent);
*/

        setProgressViewVisibility(context, appWidgetId, false);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.i(TAG, "## onAppWidgetOptionsChanged() with " + appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        updateAppWidget(context, appWidgetManager, appWidgetId);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Log.i(TAG, "## onUpdate() with  " + appWidgetId + " , size : " + appWidgetIds.length);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.i(TAG, "## onDeleted() ");
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            Log.i(TAG, "  # 위젯아이디 : " + appWidgetId);
            WidgetDarkConfigureActivity.deletePrefForWidgets(context, appWidgetId);
            WidgetDarkService.cancelAlarmSchedule(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.i(TAG, "## onEnabled() ");
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        Log.i(TAG, "## onDisabled() ");
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "#onReceive() ");

        pref = new SharedPreferences(context);

        String action = intent.getAction();
        RemoteViews remoteViews = getProperRemoteViews(context);
        AppWidgetManager manager  = AppWidgetManager.getInstance(context);

        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            Log.i(TAG, "  #onReceive() - ACTION_APPWIDGET_UPDATE + ");
            //updateWidget 하나만 업데이트 할 방법 찾기. mAppWidgetId를 받아올 방법. Uri 이용.
            int id = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            String widgetId = intent.getStringExtra("widgetId");
            String trans = intent.getStringExtra("transparent");
            String location = intent.getStringExtra("location");

            if (id != 0) {
                Log.i(TAG, "Receive >>  ## id : " + id + " , widgetId(str) : " + widgetId + " , trans : " + trans + " , location : " + location);

                updateAppWidget(context, manager, id);
            }

        }

        else if (Const.WIDGET_DARK_RESPONSE_FROM_SERVER.equals(action)) {
            Log.i(TAG, "  #onReceive() - RESPONSE FROM SERVER(SERVICE).");
            String widgetId = intent.getStringExtra("WidgetId");
            String location = intent.getStringExtra("Location");
            String widgetMode = intent.getStringExtra("WidgetMode");
            String tmX = intent.getStringExtra("tmX");
            String tmY = intent.getStringExtra("tmY");
            String transparent = pref.getValue(SharedPreferences.TRANSPARENT + widgetId, Const.WIDGET_DEFAULT_TRANSPARENT);

            Log.i(TAG, "Service 응답 확인\n" +"  location : " + location + " at " + tmX + " , " + tmY +"\n위젯ID : " + widgetId);

            /**
             * 위젯의 특정 부분을 클릭하였을 때 발생하는 이벤트
             * */
            //  PendingIntent - Configuration Button clicked
            Intent configIntent = new Intent(context, WidgetDarkConfigureActivity.class);
            configIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            configIntent.setData(Uri.withAppendedPath(Uri.parse("WidgetDark" + "://widget/id/"), String.valueOf(widgetId)));
            configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, Integer.parseInt(widgetId));

            PendingIntent configPendingIntent = PendingIntent.getActivity(context, Integer.parseInt(widgetId), configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.setting, configPendingIntent);


            // PendingIntent - Refresh Button  Clicked
            Intent refreshIntent = new Intent(context, WidgetDarkService.class);
            refreshIntent.setData(Uri.withAppendedPath(Uri.parse("WidgetDark" + "://widget/id/"), widgetId));
            refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, Integer.parseInt(widgetId));
            refreshIntent.putExtra(Const.WIDGET_LOCATION, location);
            refreshIntent.putExtra(Const.WIDGET_MODE, widgetMode);
            refreshIntent.putExtra(Const.WIDGET_TM_X, tmX);
            refreshIntent.putExtra(Const.WIDGET_TM_Y, tmY);

            PendingIntent darkRefreshPending = PendingIntent.getService(context, Integer.parseInt(widgetId), refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.refresh, darkRefreshPending);


            // calculate updated time
            String refreshedTime = calculateCurrentTime();
            remoteViews.setTextViewText(R.id.value_date, refreshedTime);
            remoteViews.setTextViewText(R.id.value_location, location);     // UI needs to be updated here in case reboot is completed.
            remoteViews.setInt(R.id.layout_ground, "setBackgroundColor", Color.argb(Integer.parseInt(transparent), 0,0,0));

            manager.updateAppWidget(Integer.parseInt(widgetId) , remoteViews);
        }

        else if (Const.WIDGET_DARK_REFRESH.equals(action)) {
            Log.i(TAG, "  #onReceive() - REFRESH BUTTON.");
        }

        else if (Const.BOOT_COMPLETED.equals(action)) {
            Log.i(TAG, "  #onReceive() - BOOT COMPLETED.");
            //onUpdate with all widgetIds[]..
            onUpdate(context, manager, manager.getAppWidgetIds(new ComponentName(context, WidgetDark.class)));
        }

        super.onReceive(context, intent);
    }

    private String calculateCurrentTime() {
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd a h:mm", Locale.getDefault());
        return dateFormat.format(today);
    }

    public static RemoteViews getProperRemoteViews(Context context) {
        String strManufacturer = DeviceInfo.checkDeviceManufacturer();

        if (strManufacturer.equals(Const.DEVICE_CATEGORY_SAMSUNG) ) {
            return new RemoteViews(context.getPackageName(), R.layout.widget_dark);
        }
        else {
            return new RemoteViews(context.getPackageName(), R.layout.widget_dark_others);
        }
    }


    public static void setProgressViewVisibility(Context context, int mAppWidgetId, boolean on) {
        RemoteViews views = getProperRemoteViews(context);

        if (on) {
            views.setViewVisibility(R.id.layout_progress, View.VISIBLE);
            views.setViewVisibility(R.id.progressView, View.VISIBLE);
            views.setViewVisibility(R.id.refresh, View.GONE);
        }
        else {
            views.setViewVisibility(R.id.layout_progress, View.GONE);
            views.setViewVisibility(R.id.progressView, View.GONE);
            views.setViewVisibility(R.id.refresh, View.VISIBLE);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);
    }

}

