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
import com.finedust.model.Const;
import com.finedust.service.RequestWidgetData;
import com.finedust.service.WidgetDarkService;
import com.finedust.utils.DeviceInfo;
import com.finedust.utils.SharedPreferences;
import com.finedust.view.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class WidgetDark extends AppWidgetProvider {
    private final static String TAG = WidgetDark.class.getSimpleName();

    static String transparentValue;
    static String widgetMode;
    static String widgetLocation;
    static String widgetTheme = Const.DARKWIDGET;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.i(TAG, "## updateAppWidget() with " + appWidgetId);
        RemoteViews views = getProperRemoteViews(context);
        SharedPreferences pref = new SharedPreferences(context);


        transparentValue = pref.getValue(SharedPreferences.TRANSPARENT + appWidgetId , Const.WIDGET_DEFAULT_TRANSPARENT);
        widgetLocation = pref.getValue(SharedPreferences.WIDGET_LOCATION + appWidgetId , Const.EMPTY_STRING);
        widgetMode = pref.getValue(SharedPreferences.WIDGET_MODE + appWidgetId , Const.MODE[0]);


        views.setTextViewText(R.id.value_location, widgetLocation);
        views.setInt(R.id.layout_ground, "setBackgroundColor", Color.argb(Integer.parseInt(transparentValue), 0,0,0));

        if  (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID && !widgetMode.equals(Const.MODE[0])) {
            Log.i(TAG, "주소 : " + widgetLocation + " , id : " + appWidgetId + "\n투명도 : " + transparentValue + " , 모드 : " + widgetMode);
            Intent refresh = BaseWidget.setPendingIntentForRefresh(context, views, appWidgetId, widgetMode, widgetTheme, new WidgetDarkService());
            context.startService(refresh);
        }

        // set listeners for buttons in widgetViews.
        BaseWidget.setPendingIntentForMainActivity(context, views, appWidgetId, widgetMode, widgetTheme);
        BaseWidget.setPendingIntentForConfiguration(context, views, appWidgetId, widgetTheme, new WidgetDarkConfigureActivity());

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.i(TAG, "## onAppWidgetOptionsChanged() with " + appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        //updateAppWidget(context, appWidgetManager, appWidgetId);
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
            Log.i(TAG, "  # widgetId : " + appWidgetId);
            WidgetDarkConfigureActivity.deletePrefForWidgets(context, appWidgetId);
            RequestWidgetData.cancelAlarmSchedule(context, appWidgetId, widgetTheme);
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
        SharedPreferences pref = new SharedPreferences(context);
        String action = intent.getAction();
        RemoteViews remoteViews = getProperRemoteViews(context);
        AppWidgetManager manager  = AppWidgetManager.getInstance(context);

        Log.i(TAG, "#onReceive() >> " + action);
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            Log.i(TAG, "  #onReceive() - ACTION_APPWIDGET_UPDATE  ");
            int id = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            if (id != AppWidgetManager.INVALID_APPWIDGET_ID) {
                Log.i(TAG, "    Receive >>  ## id : " + id);
                updateAppWidget(context, manager, id);
            }
        }

        else if (Const.WIDGET_DARK_RESPONSE_FROM_SERVER.equals(action)) {
            Log.i(TAG, "  #onReceive() - RESPONSE FROM SERVER(SERVICE).");
            String widgetId = intent.getStringExtra(Const.WIDGET_ID);
            int appWidgetId = Integer.parseInt(widgetId);
            String widgetMode = intent.getStringExtra(Const.WIDGET_MODE);
            String widgetLocation = intent.getStringExtra(Const.WIDGET_LOCATION);
            ArrayList<String> gradeList = intent.getStringArrayListExtra(Const.ARRAY_GRADE);
            ArrayList<String> valueList = intent.getStringArrayListExtra(Const.ARRAY_VALUE);
            String transparent = pref.getValue(SharedPreferences.TRANSPARENT + widgetId, Const.WIDGET_DEFAULT_TRANSPARENT);

            if (widgetMode.equals(Const.WIDGET_DELETED)) {
                WidgetDarkService.cancelAlarmSchedule(context, appWidgetId, widgetTheme);
            }
            else {
                // PendingIntent - Refresh Button  Clicked
                BaseWidget.setPendingIntentForRefresh(context, remoteViews, appWidgetId, widgetMode, widgetTheme, new WidgetDarkService());
                BaseWidget.setPendingIntentForMainActivity(context, remoteViews, appWidgetId, widgetMode, widgetTheme);
                remoteViews.setTextViewText(R.id.value_date, BaseWidget.calculateCurrentTime());       // updated time
            }

            //  PendingIntent - Configuration Button clicked
            BaseWidget.setPendingIntentForConfiguration(context, remoteViews, appWidgetId, widgetTheme, new WidgetDarkConfigureActivity());
            remoteViews.setTextViewText(R.id.value_location, widgetLocation);
            remoteViews.setInt(R.id.layout_ground, "setBackgroundColor", Color.argb(Integer.parseInt(transparent), 0,0,0));

            BaseWidget.setValuesAndImages(remoteViews, R.id.info_img_one, R.id.value_pm10, gradeList.get(0), valueList.get(0), Const.DRAWABLE_STATES);
            BaseWidget.setValuesAndImages(remoteViews, R.id.info_img_two, R.id.value_pm25, gradeList.get(1), valueList.get(1), Const.DRAWABLE_STATES);
            BaseWidget.setValuesAndImages(remoteViews, R.id.info_img_three, R.id.value_cai, gradeList.get(2), valueList.get(2), Const.DRAWABLE_STATES_FACE_SMALL);

            setProgressViewVisibility(context, appWidgetId, false);         // stop progress dial.

            manager.updateAppWidget(appWidgetId , remoteViews);
        }

        else if (Const.BOOT_COMPLETED.equals(action)) {
            Log.i(TAG, "  #onReceive() - BOOT COMPLETED.");
            //onUpdate with all widgetIds[]..
            onUpdate(context, manager, manager.getAppWidgetIds(new ComponentName(context, WidgetDark.class)));
        }
        super.onReceive(context, intent);
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

