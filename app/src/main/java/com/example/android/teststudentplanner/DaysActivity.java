package com.example.android.teststudentplanner;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class DaysActivity extends AppCompatActivity {

    Button monday, tuesday, wednesday, thursday, friday;
    private String dayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days);

        if (savedInstanceState == null)
            replaceDayFragment(DayListFragment.NewInstance(), false);

    }

    public void monWork(View view) {
        dayName = "Monday";
        replaceDayFragment(DayDetailFragment.newInstance("mon", dayName, Calendar.MONDAY),
                true);
    }

    public void tueWork(View view) {
        dayName = "Tuesday";
        replaceDayFragment(DayDetailFragment.newInstance("tue", dayName,Calendar.TUESDAY),
                true);
    }

    public void wedWork(View view) {
        dayName = "Wednesday";
        replaceDayFragment(DayDetailFragment.newInstance("wed", dayName,Calendar.WEDNESDAY),
                true);
    }

    public void thurWork(View view) {
        dayName = "Thursday";
        replaceDayFragment(DayDetailFragment.newInstance("thu", dayName,Calendar.THURSDAY),
                true);
    }

    public void friWork(View view) {
        dayName = "Friday";
        replaceDayFragment(DayDetailFragment.newInstance("fri", dayName,Calendar.FRIDAY),
                true);
    }

    private void replaceDayFragment(Fragment fragment, boolean shouldAddBackStack) {
        FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
        frag.replace(R.id.main_content, fragment);
        if (shouldAddBackStack)
            frag.addToBackStack(null);
        frag.commit();
    }


    public void close(View view) {
        finish();
    }
}
