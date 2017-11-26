package com.finedust.service;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.finedust.model.Const;
import com.finedust.widget.WidgetDark;

public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = BootCompletedReceiver.class.getSimpleName();
    public BootCompletedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d(TAG , "Boot Completed. Start updating widgets.");
            /*
            Intent pushIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            pushIntent.putExtra("BOOT_COMPLETED", "YES");
            context.sendBroadcast(pushIntent);
            */
            // onUpdate() 실행해서 위젯 전체 업데이트 해줄 수 있는 플래그 설정.
            Intent i = new Intent(Const.BOOT_COMPLETED);
            context.sendBroadcast(i);
        }
    }

}
