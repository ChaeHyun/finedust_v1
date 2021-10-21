package ch.breatheinandout.widget;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import ch.breatheinandout.R;
import ch.breatheinandout.model.Const;
import ch.breatheinandout.view.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class BaseWidget {

    /**
     * Methods : set PendingIntent
     * */

    public static Intent setPendingIntentForRefresh(Context context, RemoteViews views, int appWidgetId, String widgetMode, String widgetTheme, Object widgetServiceClass) {
        Intent refreshIntent = new Intent(context, widgetServiceClass.getClass());
        refreshIntent.setData(Uri.withAppendedPath(Uri.parse(widgetTheme + "://widget/id/") , String.valueOf(appWidgetId)));
        refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        refreshIntent.putExtra(Const.WIDGET_MODE, widgetMode);
        refreshIntent.putExtra(Const.WIDGET_THEME, widgetTheme);

        PendingIntent refreshPending = PendingIntent.getService(context, appWidgetId, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.refresh, refreshPending);

        return refreshIntent;
    }

    public static void setPendingIntentForConfiguration(Context context, RemoteViews views, int appWidgetId, String widgetTheme, Object widgetConfigureClass) {
        Intent configIntent = new Intent(context, widgetConfigureClass.getClass());
        configIntent.setData(Uri.withAppendedPath(Uri.parse(widgetTheme + "://widget/id/"), String.valueOf(appWidgetId)));
        configIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        PendingIntent configPendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.setting, configPendingIntent);
    }

    public static void setPendingIntentForMainActivity(Context context, RemoteViews views, int appWidgetId, String widgetMode, String widgetTheme) {
        Intent mainActivity = new  Intent(context,  MainActivity.class);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainActivity.setData(Uri.withAppendedPath(Uri.parse(widgetTheme + "://widget/id/") , String.valueOf(appWidgetId)));
        mainActivity.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        mainActivity.putExtra(Const.WIDGET_MODE, widgetMode);
        mainActivity.putExtra(Const.WIDGET_THEME, widgetTheme);

        PendingIntent mainActivityPendingIntent =  PendingIntent.getActivity(context, appWidgetId, mainActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.layout_ground, mainActivityPendingIntent);
    }

    /**
     * Methods : about update views.
     * */

    public static void setValuesAndImages(RemoteViews views, int imageViewId, int textViewId, String grade, String value, int[] imageType) {
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

    public static String calculateCurrentTime() {
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd a h:mm", Locale.getDefault());
        return dateFormat.format(today);
    }



}
