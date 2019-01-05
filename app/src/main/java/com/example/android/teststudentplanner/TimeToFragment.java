package com.example.android.teststudentplanner;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TimePicker;

public class TimeToFragment extends TimePickerFragment {

    int x;

    public TimeToFragment(){

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        DayDetailFragment fragment = (DayDetailFragment) getTargetFragment();
        fragment.processTimeToPickerResult(hourOfDay, minute);

    }
}
