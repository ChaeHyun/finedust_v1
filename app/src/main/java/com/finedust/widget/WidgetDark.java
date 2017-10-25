package com.finedust.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.finedust.R;
import com.finedust.model.Addresses;
import com.finedust.model.Const;
import com.finedust.service.WidgetDarkService;
import com.finedust.utils.DeviceInfo;
import com.finedust.utils.SharedPreferences;
import com.finedust.view.MainActivity;
import com.google.android.gms.location.ActivityRecognitionResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class WidgetDark extends AppWidgetProvider implements WidgetViews.WidgetDarkView {
    private final static String TAG = WidgetDark.class.getSimpleName();

    static String intervalValue;
    static String transparentValue;
    static int selectedRadioButton;
    static String widgetMode;
    static String widgetLocation;
    //static Addresses savedLocation;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.i(TAG, "## updateAppWidget() with " + appWidgetId);
        RemoteViews views = getProperRemoteViews(context);
        SharedPreferences pref = new SharedPreferences(context);

            intervalValue = pref.getValue(SharedPreferences.INTERVAL + appWidgetId, Const.WIDGET_DEFAULT_INTERVAL);
            transparentValue = pref.getValue(SharedPreferences.TRANSPARENT + appWidgetId, Const.WIDGET_DEFAULT_TRANSPARENT);
            widgetMode = pref.getValue(SharedPreferences.WIDGET_MODE + appWidgetId , Const.MODE[0]);
            selectedRadioButton = Integer.parseInt(pref.getValue(SharedPreferences.WIDGET_SELECTED_LOCATION_INDEX + appWidgetId, "0"));
            widgetLocation = pref.getValue(SharedPreferences.WIDGET_LOCATION, Const.EMPTY_STRING);
            Log.i(TAG, "selectedRadioButton : " + selectedRadioButton);

            views.setTextViewText(R.id.value_location, widgetLocation);
            views.setInt(R.id.layout_ground, "setBackgroundColor", Color.argb(Integer.parseInt(transparentValue), 0,0,0));

            Log.i(TAG, "주소 : " + widgetLocation + " , id : " + appWidgetId + "\n시간 : " + intervalValue + " , 투명도 : " + transparentValue + " , 모드 : " + widgetMode);


            if  (selectedRadioButton != 0) {
                Intent requestIntent = new Intent(context, WidgetDarkService.class);
                requestIntent.setData(Uri.withAppendedPath(Uri.parse("WidgetDark" + "://widget/id/"), String.valueOf(appWidgetId)));
                requestIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                requestIntent.putExtra(Const.WIDGET_MODE, widgetMode);
                requestIntent.putExtra(Const.WIDGET_THEME, Const.DARK);

                PendingIntent refreshPending = PendingIntent.getService(context, appWidgetId, requestIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.refresh, refreshPending);
                context.startService(requestIntent);
            }


        // Launch MainActivity.
        Intent mainActivity = new Intent(context, MainActivity.class);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainActivity.setData(Uri.withAppendedPath(Uri.parse("WidgetDark" + "://widget/id/"), String.valueOf(appWidgetId)));
        mainActivity.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);    //실제 id첨부
        mainActivity.putExtra(Const.WIDGET_MODE, widgetMode);
        mainActivity.putExtra(Const.WIDGET_THEME, Const.DARK);

        PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(context, appWidgetId, mainActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.layout_ground, mainActivityPendingIntent);

        // Configuration setting launch.
        Intent configIntent = new Intent(context, WidgetDarkConfigureActivity.class);
        configIntent.setData(Uri.withAppendedPath(Uri.parse("WidgetDark" + "://widget/id/"), String.valueOf(appWidgetId)));
        configIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        PendingIntent configPendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.setting, configPendingIntent);

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
            Log.i(TAG, "  # widgetId : " + appWidgetId);
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

        SharedPreferences pref = new SharedPreferences(context);

        String action = intent.getAction();
        RemoteViews remoteViews = getProperRemoteViews(context);
        AppWidgetManager manager  = AppWidgetManager.getInstance(context);

        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            Log.i(TAG, "  #onReceive() - ACTION_APPWIDGET_UPDATE  ");
            //updateWidget 하나만 업데이트 할 방법 찾기. mAppWidgetId를 받아올 방법. Uri 이용.
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

            Log.i(TAG, "Service 응답 확인 >> " + " id  : " + widgetId + " , location : " + widgetLocation);

            if (widgetMode.equals(Const.WIDGET_DELETED)) {
                WidgetDarkService.cancelAlarmSchedule(context, appWidgetId);
            }
            else {
                // PendingIntent - Refresh Button  Clicked
                Intent refreshIntent = new Intent(context, WidgetDarkService.class);
                refreshIntent.setData(Uri.withAppendedPath(Uri.parse("WidgetDark" + "://widget/id/"), widgetId));
                refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                refreshIntent.putExtra(Const.WIDGET_MODE, widgetMode);

                PendingIntent darkRefreshPending = PendingIntent.getService(context, appWidgetId, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.refresh, darkRefreshPending);

                remoteViews.setTextViewText(R.id.value_date, calculateCurrentTime());       // updated time
            }

            /**
             * 위젯의 특정 부분을 클릭하였을 때 발생하는 이벤트 - 재부팅 시 (Galaxy Series)
             * */
            //  PendingIntent - Configuration Button clicked
            Intent configIntent = new Intent(context, WidgetDarkConfigureActivity.class);
            configIntent.setData(Uri.withAppendedPath(Uri.parse("WidgetDark" + "://widget/id/"), widgetId));
            configIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            PendingIntent configPendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.setting, configPendingIntent);

            // Update results to widgetUI.
            remoteViews.setTextViewText(R.id.value_location, widgetLocation);
            remoteViews.setInt(R.id.layout_ground, "setBackgroundColor", Color.argb(Integer.parseInt(transparent), 0,0,0));

            setValuesAndImages(remoteViews, R.id.info_img_one, R.id.value_pm10, gradeList.get(0), valueList.get(0), Const.DRAWABLE_STATES);
            setValuesAndImages(remoteViews, R.id.info_img_two, R.id.value_pm25, gradeList.get(1), valueList.get(1), Const.DRAWABLE_STATES);
            setValuesAndImages(remoteViews, R.id.info_img_three, R.id.value_cai, gradeList.get(2), valueList.get(2), Const.DRAWABLE_STATES_FACE_SMALL);

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

    private void setValuesAndImages(RemoteViews views, int imageViewId, int textViewId, String grade, String value, int[] imageType) {
        if (grade == null ) {
            return;
        }
        if (grade.equals("-") || grade.equals("")) {
            views.setImageViewResource(imageViewId, imageType[0]);
            views.setTextColor(textViewId, Const.COLORS[0]);
        }
        else {
            int GRADE = Integer.parseInt(grade);

            views.setImageViewResource(imageViewId, imageType[GRADE]);
            views.setTextColor(textViewId, Const.COLORS[GRADE]);
        }

        views.setTextViewText(textViewId, value);
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

