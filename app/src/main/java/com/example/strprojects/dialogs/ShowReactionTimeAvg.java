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
import com.example.strprojects.reactiontime.ButtonTimes;
import com.example.strprojects.reactiontime.ReactionTimeCount;

import java.util.List;

public class ShowReactionTimeAvg extends DialogFragment {

    private Context context;
    private static final int LAYOUT_ID = R.layout.layout_reaction_time_result_dialog;
    private TextView tvReactionTimeAvg, tvScore;
    private List<ButtonTimes> buttonTimesList;
    private int[] numberOfViews;
    private int[] numberOfClicks;

    public ShowReactionTimeAvg(Context context, List<ButtonTimes> buttonTimesList, int[] numberOfViews, int[] numberOfClicks) {
        this.context = context;
        this.buttonTimesList = buttonTimesList;
        this.numberOfViews = numberOfViews;
        this.numberOfClicks = numberOfClicks;
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
        tvReactionTimeAvg = view.findViewById(R.id.tv_reaction_time_avg);
        tvScore = view.findViewById(R.id.tv_score);
        String reactionTimeAvg = tvReactionTimeAvg.getText().toString() + " " + calculateAvgTime() + "ms";
        tvReactionTimeAvg.setText(reactionTimeAvg);
        String score = tvScore.getText().toString() + " " + calculateScore();
        tvScore.setText(score);
    }

    private int calculateScore() {
        int success = 0;
        int fail = 0;

        if(numberOfClicks[ReactionTimeCount.NUMBER_VIEW_GREEN_BUTTON_INDEX] - numberOfViews[ReactionTimeCount.NUMBER_VIEW_GREEN_BUTTON_INDEX] == 0)
            success = success + numberOfClicks[ReactionTimeCount.NUMBER_VIEW_GREEN_BUTTON_INDEX];
        else
            fail = fail + (-1* (numberOfClicks[ReactionTimeCount.NUMBER_VIEW_GREEN_BUTTON_INDEX] - numberOfViews[ReactionTimeCount.NUMBER_VIEW_GREEN_BUTTON_INDEX]));

        if(numberOfClicks[ReactionTimeCount.NUMBER_VIEW_YELLOW_BUTTON_INDEX] - numberOfViews[ReactionTimeCount.NUMBER_VIEW_YELLOW_BUTTON_INDEX] == 0)
            success = success + numberOfClicks[ReactionTimeCount.NUMBER_VIEW_YELLOW_BUTTON_INDEX];
        else
            fail = fail + (-1* (numberOfClicks[ReactionTimeCount.NUMBER_VIEW_YELLOW_BUTTON_INDEX] - numberOfViews[ReactionTimeCount.NUMBER_VIEW_YELLOW_BUTTON_INDEX]));

        if(numberOfClicks[ReactionTimeCount.NUMBER_VIEW_RED_BUTTON_INDEX] - numberOfViews[ReactionTimeCount.NUMBER_VIEW_RED_BUTTON_INDEX] == 0)
            success = success + numberOfClicks[ReactionTimeCount.NUMBER_VIEW_RED_BUTTON_INDEX];
        else
            fail = fail + (-1* (numberOfClicks[ReactionTimeCount.NUMBER_VIEW_RED_BUTTON_INDEX] - numberOfViews[ReactionTimeCount.NUMBER_VIEW_RED_BUTTON_INDEX]));

        if(numberOfClicks[ReactionTimeCount.NUMBER_VIEW_BLUE_BUTTON_INDEX] - numberOfViews[ReactionTimeCount.NUMBER_VIEW_BLUE_BUTTON_INDEX] == 0)
            success = success + numberOfClicks[ReactionTimeCount.NUMBER_VIEW_BLUE_BUTTON_INDEX];
        else
            fail = fail + (-1* (numberOfClicks[ReactionTimeCount.NUMBER_VIEW_BLUE_BUTTON_INDEX] - numberOfViews[ReactionTimeCount.NUMBER_VIEW_BLUE_BUTTON_INDEX]));

        return success - fail;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private long calculateAvgTime(){
        long totalTime = 0L;
        int dateListSize = 1;
        if(buttonTimesList != null && !buttonTimesList.isEmpty()){
            dateListSize = buttonTimesList.size();
            totalTime = buttonTimesList.stream().mapToLong(date ->{
                if(date.getClickDate() != null)
                    return date.getClickDate().getTime() - date.getShowDate().getTime();
                else
                    return 0;
            }).sum();
        }
        return totalTime/dateListSize;
    }
}
