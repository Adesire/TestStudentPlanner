package com.example.android.teststudentplanner;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class DaysActivity extends AppCompatActivity {

    Button monday,tuesday,wednesday,thursday,friday;
    private String dayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days);

        monday = (Button) findViewById(R.id.mon);
        tuesday = (Button) findViewById(R.id.tue);
        wednesday = (Button) findViewById(R.id.wed);
        thursday = (Button) findViewById(R.id.thur);
        friday = (Button) findViewById(R.id.fri);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar(). setTitle(getString(R.string.app_name));

    }
    public void monWork(View view) {
        dayName = "Monday";
        replaceDayFragment(DayDetailFragment.newInstance("mon",dayName),
                true);
    }

    public void tueWork(View view) {
        dayName = "Tuesday";
        replaceDayFragment(DayDetailFragment.newInstance("tue",dayName),
                true);
    }

    public void wedWork(View view) {
        dayName = "Wednesday";
        replaceDayFragment(DayDetailFragment.newInstance("wed",dayName),
                true);
    }

    public void thurWork(View view) {
        dayName = "Thursday";
        replaceDayFragment(DayDetailFragment.newInstance("thu",dayName),
                true);
    }

    public void friWork(View view) {
        dayName = "Friday";
        replaceDayFragment(DayDetailFragment.newInstance("fri",dayName),
                true);
    }

    private void replaceDayFragment(Fragment fragment, boolean shouldAddBackStack){
        FragmentTransaction frag = getSupportFragmentManager().beginTransaction();
        frag.replace(R.id.main_content,fragment);
        if(shouldAddBackStack)
            frag.addToBackStack(null);
        frag.commit();
    }


}
