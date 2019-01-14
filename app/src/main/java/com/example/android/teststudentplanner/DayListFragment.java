package com.example.android.teststudentplanner;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class DayListFragment extends Fragment {

    Button monday, tuesday, wednesday, thursday, friday;
    private String dayName;

    public static DayListFragment NewInstance() {
        return new DayListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_days_list, container, false);

        monday = (Button) v.findViewById(R.id.mon);
        tuesday = (Button) v.findViewById(R.id.tue);
        wednesday = (Button) v.findViewById(R.id.wed);
        thursday = (Button) v.findViewById(R.id.thur);
        friday = (Button) v.findViewById(R.id.fri);
        GradientDrawable tueDrawable = (GradientDrawable) tuesday.getBackground();
        tueDrawable.setColor(Color.parseColor("#FF5722"));
        GradientDrawable wedDrawabe = (GradientDrawable) wednesday.getBackground();
        wedDrawabe.setColor(Color.parseColor("#AFB42B"));
        GradientDrawable thurDrawable = (GradientDrawable) thursday.getBackground();
        thurDrawable.setColor(Color.parseColor("#FFC107"));
        GradientDrawable friDrawable = (GradientDrawable) friday.getBackground();
        friDrawable.setColor(Color.parseColor("#5D4037"));
        return v;
    }
}
