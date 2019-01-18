package com.example.android.teststudentplanner;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    int hour,minute;

    static String HOUR_OF_DAY = "HOUR_OF_DAY";
    static String MINUTE = "MINUTE";

    public TimePickerFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this,hour,minute,true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        /*DaysActivity fragmentActivity = (DaysActivity)getActivity();
        fragmentActivity.processMyTimePickerResult(hourOfDay, minute);*/

        hour = hourOfDay;
        this.minute = minute;
        sendResult(0);

    }

    private void sendResult(int REQUEST_CODE){
        Intent intent = new Intent();
        intent.putExtra(HOUR_OF_DAY,hour);
        intent.putExtra(MINUTE,minute);
        getTargetFragment().onActivityResult(getTargetRequestCode(),
                REQUEST_CODE, intent);
    }

}
