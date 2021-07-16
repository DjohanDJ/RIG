package com.example.rig.animations;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.rig.R;

public class LoadingAnimation {

    private static AlertDialog dialog;

    @SuppressLint("InflateParams")
    public static void startLoading(Activity myActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);

        LayoutInflater inflater = myActivity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.animation_loading, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    public static AlertDialog getDialog() {
        return  dialog;
    }

}
