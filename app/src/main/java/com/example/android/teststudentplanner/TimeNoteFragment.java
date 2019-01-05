package com.example.android.teststudentplanner;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TimePicker;

public class TimeNoteFragment extends TimePickerFragment {

    int x;

    public TimeNoteFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        DayDetailFragment fragment = (DayDetailFragment) getTargetFragment();
        fragment.processTimeNotePickerResult(hourOfDay, minute);

    }

}
