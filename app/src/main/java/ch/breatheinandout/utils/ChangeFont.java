package ch.breatheinandout.utils;


import android.content.Context;
import android.graphics.Typeface;

import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

public class ChangeFont {

    public static void Typekit(Context context, String normalFont, String boldFont) {
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(context , normalFont))
                .addCustom1(Typekit.createFromAsset(context, normalFont))
                .addBold(Typekit.createFromAsset(context , boldFont));

    }

    public static void Typekit(Context context, String normalFont) {
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(context , normalFont))
                .addCustom1(Typekit.createFromAsset(context, normalFont))
                .addBold(Typekit.createFromAsset(context , "BMDOHYEON.ttf"));

    }

    public static Context TypekitContextWrapper(Context context) {
        return TypekitContextWrapper.wrap(context);
    }

    public static Typeface getTypefaceFromAsset(Context context, String normalFont) {
        return Typeface.createFromAsset(context.getAssets(), normalFont);
    }

}
