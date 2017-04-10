package com.cnkaptan.transferwisehomework.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by cnkaptan on 10/04/2017.
 */

public final class Utils {
    private Utils(){

    }

    public static float convertDpToPixel(float dp, Resources resources) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
