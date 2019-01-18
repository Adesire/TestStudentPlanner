package com.example.android.teststudentplanner;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TimePicker;

public class TimeToFragment extends TimePickerFragment {

    int hour,minute;

    static String TO_HOUR_OF_DAY = "TO_HOUR_OF_DAY";
    static String TO_MINUTE = "TO_MINUTE";

    public TimeToFragment(){

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        /*DayDetailFragment fragment = (DayDetailFragment) getTargetFragment();
        fragment.processTimeToPickerResult(hourOfDay, minute);*/
        hour = hourOfDay;
        this.minute = minute;
        sendResult(1);

    }

    private void sendResult(int REQUEST_CODE){
        Intent intent = new Intent();
        intent.putExtra(TO_HOUR_OF_DAY,hour);
        intent.putExtra(TO_MINUTE,minute);
        getTargetFragment().onActivityResult(getTargetRequestCode(),
                REQUEST_CODE, intent);
    }

}
