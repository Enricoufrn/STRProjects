package com.example.strprojects.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.strprojects.R;

import java.util.Date;
import java.util.List;

public class ShowReactionTimeAvg extends DialogFragment {

    private Context context;
    private List<Date> dateList;
    private static final int LAYOUT_ID = R.layout.layout_reaction_time_result_dialog;
    private TextView tvResult;

    public ShowReactionTimeAvg(Context context, List<Date> dateList) {
        this.context = context;
        this.dateList = dateList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(LAYOUT_ID, null);
        configTextViews(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.dialog_reaction_time_result_title));
        builder.setView(view);
        return builder.create();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void configTextViews(View view) {
        tvResult = view.findViewById(R.id.tv_result);
        String result = tvResult.getText().toString() + " " + calculateAvgTime();
        tvResult.setText(result);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private long calculateAvgTime(){
        long totalTime = 0L;
        int dateListSize = 1;
        if(dateList != null && !dateList.isEmpty()){
            dateListSize = dateList.size();
            totalTime = dateList.stream().mapToLong(date ->{
                return date.getTime();
            }).sum();
        }
        return totalTime/dateListSize;
    }
}
