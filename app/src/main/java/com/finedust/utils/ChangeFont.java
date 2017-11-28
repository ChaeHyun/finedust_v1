package com.finedust.utils;


import android.content.Context;

import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

public class ChangeFont {

    public static void Typekit(Context context, String normalFont, String boldFont) {
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(context , normalFont))
                .addBold(Typekit.createFromAsset(context , boldFont));

    }

    public static void Typekit(Context context, String normalFont) {
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(context , normalFont))
                .addBold(Typekit.createFromAsset(context , "BMDOHYEON.ttf"));

    }

    public static Context TypekitContextWrapper(Context context) {
        return TypekitContextWrapper.wrap(context);
    }

}
