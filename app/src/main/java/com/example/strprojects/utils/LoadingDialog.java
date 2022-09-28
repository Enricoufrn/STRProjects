package com.example.strprojects.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.strprojects.R;

public class LoadingDialog extends Dialog {

    Context mContext;

    public LoadingDialog(Context context){
        super(context);
        this.mContext = context;
    }

    public Dialog getLoadingDialog(){
        View loadingDialog = LayoutInflater.from(mContext).inflate(R.layout.layout_loading_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(loadingDialog);
        builder.setCancelable(false);
        return builder.create();
    }
}

