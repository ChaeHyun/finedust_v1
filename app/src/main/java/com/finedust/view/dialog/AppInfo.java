package com.finedust.view.dialog;


import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.finedust.R;
import com.finedust.view.MainActivity;

public class AppInfo extends Dialog {
    public AppInfo(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_appinfo);

        try {
            ComponentName componentName = new ComponentName(context, MainActivity.class);
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(componentName.getPackageName(), 0);

            String versionName = pInfo.versionName;

            TextView textView_versionName = findViewById(R.id.textView_versionName);
            textView_versionName.setText("버전정보 : " + versionName);

        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ImageView image = findViewById(R.id.imageFinish);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
