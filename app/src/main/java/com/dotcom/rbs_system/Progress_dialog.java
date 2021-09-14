package com.dotcom.rbs_system;

import android.content.Context;
import android.graphics.Color;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class Progress_dialog {
    public ACProgressFlower dialog;

    public void showProgressBar(Context context) {
        dialog = new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading...")
                .fadeColor(Color.DKGRAY).build();

        dialog.setCancelable(false);
        dialog.show();
    }

    public void dismissProgressBar(Context context) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}