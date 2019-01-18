package com.example.android.teststudentplanner;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TimePicker;

public class TimeNoteFragment extends TimePickerFragment {

    int hour,minute;

    static String NOTE_HOUR_OF_DAY = "NOTE_HOUR_OF_DAY";
    static String NOTE_MINUTE = "NOTE_MINUTE";

    public TimeNoteFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        /*DayDetailFragment fragment = (DayDetailFragment) getTargetFragment();
        fragment.processTimeNotePickerResult(hourOfDay, minute);*/
        hour = hourOfDay;
        this.minute = minute;
        sendResult(2);

    }

    private void sendResult(int REQUEST_CODE){
        Intent intent = new Intent();
        intent.putExtra(NOTE_HOUR_OF_DAY,hour);
        intent.putExtra(NOTE_MINUTE,minute);
        getTargetFragment().onActivityResult(getTargetRequestCode(),
                REQUEST_CODE, intent);
    }

}
