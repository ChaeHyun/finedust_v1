package com.finedust.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import com.finedust.R;
import com.finedust.model.Const;
import com.finedust.utils.DeviceInfo;


public class WidgetDark extends AppWidgetProvider {
    private final static String TAG = WidgetDark.class.getSimpleName();


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        String strManufacturer = DeviceInfo.checkDeviceManufacturer();
        Log.i(TAG, "제조사 확인 : " + strManufacturer);

        //CharSequence widgetText = WidgetDarkConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views;
        if (strManufacturer.equals(Const.DEVICE_CATEGORY_SAMSUNG) ) {
            views = new RemoteViews(context.getPackageName(), R.layout.widget_dark);
        }
        else {
            views = new RemoteViews(context.getPackageName(), R.layout.widget_dark_others);
        }

        //views.setTextViewText(R.id.value_location, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WidgetDarkConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

