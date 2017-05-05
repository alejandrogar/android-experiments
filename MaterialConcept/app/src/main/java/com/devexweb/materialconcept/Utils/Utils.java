package com.devexweb.materialconcept.Utils;

/**
 * Created by manuel on 16/11/16.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.devexweb.materialconcept.R;

public class Utils {

    private static final String PREFERENCES_FILE = "materialsample_settings";
    public static final String FAVORITES = "Recipe_Favorite";


    public static int getToolbarHeight(Context context) {
        int height = (int) context.getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
        return height;
    }

    public static Drawable tintMyDrawable(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static boolean saveArray(String[] array, String arrayName, Context ctx) {
        SharedPreferences sharedPref = ctx.getSharedPreferences("preferencename", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(arrayName + "_size", array.length);
        for (int i = 0; i < array.length; i++)
            editor.putString(arrayName + i, array[i]);
        return editor.commit();
    }

    public static String[] loadArray(String arrayName, Context ctx) {
        SharedPreferences sharedPref = ctx.getSharedPreferences("preferencename", 0);
        int size = sharedPref.getInt(arrayName + "_size", 0);
        String array[] = new String[size];
        for (int i = 0; i < size; i++)
            array[i] = sharedPref.getString(arrayName + i, null);
        return array;
    }
}

