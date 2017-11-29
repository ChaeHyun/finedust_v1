package ch.breatheinandout.view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import ch.breatheinandout.R;

public class FirstVisit extends Dialog {

    public FirstVisit(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_first_visit);

        ImageView image = findViewById(R.id.imageFinish);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }
}
