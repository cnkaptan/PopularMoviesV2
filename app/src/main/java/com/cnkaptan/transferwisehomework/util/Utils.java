package com.cnkaptan.transferwisehomework.util;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Window;

import com.cnkaptan.transferwisehomework.R;

/**
 * Created by cnkaptan on 10/04/2017.
 */

public final class Utils {
    private Utils(){
        // Empty private Constructor
    }

    public static float convertDpToPixel(float dp, Resources resources) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static Dialog showLoading(Context context){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }
}
