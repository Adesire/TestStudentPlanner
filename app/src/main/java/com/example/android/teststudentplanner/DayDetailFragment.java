package com.example.android.teststudentplanner;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;


public class DayDetailFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private View mView;
    public static final String NOTIFY_MSG = "NOTIFY_MSG";
    private static final String CHECK_BOX = "check_box";
    Spinner spin;
    ArrayAdapter<String> spinAdapter, table;
    ListView timeTable;

    ArrayList<String> list = new ArrayList<>();
    ArrayList<Long> list2 = new ArrayList<>();
    ArrayList<Long> list3 = new ArrayList<>();
    ArrayList<String> userOrder = new ArrayList<>();

    String spinItem, courseFromTime, courseToTime, totalCourseInfo, noteTime;
    Button saveToTableBtn, fromBtn, toBtn, noteBtn;
    TextView from, to;
    NotificationManager notifyMe;
    static int youMin, youHr, NOTE_HR, NOTE_MIN;
    boolean isChecked = true;

    EditText editNote;
    TextView mTextView;
    Button saveNote, clearNote, timeNote;

    private static final String ACTION_NOTIFY = "com.example.android.teststudentplanner.ACTION_NOTIFY";

    AlarmManager alarmManager;
    Toolbar fragmentAppBar;
    private String day, appBarTitle;

    public DayDetailFragment() {
        // Required empty public constructor
    }

    public static DayDetailFragment newInstance(String daySavedSharePreference, String appBartitle) {
        DayDetailFragment fragment = new DayDetailFragment();
        Bundle b = new Bundle();
        b.putString("day", daySavedSharePreference);
        b.putString("toolbar_title", appBartitle);
        fragment.setArguments(b);
        return fragment;
    }

    public DaysActivity getActivityCast() {
        return (DaysActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        day = getArguments().getString("day");
        appBarTitle = getArguments().getString("toolbar_title");
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fromBtn = mView.findViewById(R.id.fromBtn);
        toBtn = mView.findViewById(R.id.toBtn);
        noteBtn = mView.findViewById(R.id.saveNotes);
        saveToTableBtn = mView.findViewById(R.id.saveTableBtn);
        fragmentAppBar = mView.findViewById(R.id.toolbar2);

        getActivityCast().setSupportActionBar(fragmentAppBar);
        //getActivityCast().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveToTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToTable();
            }
        });

        fragmentAppBar.setTitle(appBarTitle.toUpperCase());

        fromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFromTimePickerDialog();
            }
        });
        toBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToTimePickerDialog();
            }
        });
        /*showFromTimePickerDialog(fromBtn);
        showToTimePickerDialog(toBtn);*/

        alarmManager = (AlarmManager) getActivityCast().getSystemService(Context.ALARM_SERVICE);

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                AlertDialog.Builder buildIt = new AlertDialog.Builder(getActivityCast());
                View mNoteView = getLayoutInflater().inflate(R.layout.notes_dialog, null);

                editNote = (EditText) mNoteView.findViewById(R.id.notes);
                mTextView = (TextView) mNoteView.findViewById(R.id.textView);
                saveNote = (Button) mNoteView.findViewById(R.id.saveNotes);
                clearNote = (Button) mNoteView.findViewById(R.id.clearNotes);
                timeNote = (Button) mNoteView.findViewById(R.id.timeNotes);
                buildIt.setView(mNoteView);
                buildIt.setPositiveButton(android.R.string.ok, null)
                        .create().show();

                loadNotes(timeNote);

                saveNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        savingNotes();
                        alarmNoteStuff(getNotification("You have a Note to remember."), 1);
                    }
                });
                clearNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editNote.setText("");
                        timeNote.setText(R.string.pick_a_time);
                        alarmNoteStuff(getNotification("You have a Note to remember."), 0);
                        savingNotes();
                    }
                });
                timeNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showNoteTimePickerDialog();
                    }
                });

            }
        });

        loadingTimeTableHere(getActivityCast().getApplicationContext());


        notifyMe = (NotificationManager) getActivityCast().getSystemService(NOTIFICATION_SERVICE);


        table = new ArrayAdapter<>(getActivityCast().getApplicationContext(), android.R.layout.simple_list_item_1, list);

        from = (TextView) mView.findViewById(R.id.fromTxt);
        to = (TextView) mView.findViewById(R.id.toTxt);

        timeTable = (ListView) mView.findViewById(R.id.timeTableList);
        timeTable.setAdapter(table);


        spin = (Spinner) mView.findViewById(R.id.course_spinner);

        spinAdapter = new ArrayAdapter<>(getActivityCast().getApplicationContext(), android.R.layout.simple_list_item_1, new MainActivity().loadArrayList(getActivityCast().getApplicationContext()));

        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setOnItemSelectedListener(this);

        if (spin != null) {

            spin.setAdapter(spinAdapter);
        }
        //REMOVE ITEM FROM TABLE
        tableItemRemover();

        //CHECKBOX VALUE
        isChecked = loadCheckboxValue();

        for (long x : list2) {
            Log.e("TEST", String.valueOf(x));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_day_detail, container, false);
        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem checkable = menu.findItem(R.id.turnOff);
        checkable.setChecked(isChecked);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.editCourses:
                //CourseList.next = Monday.class;
                Intent mine = new Intent(getActivityCast(), CourseList.class);
                startActivity(mine);
                if (!(new MainActivity().loadArrayList(getActivityCast().getApplicationContext()).equals(new CourseList().loadArrayList(getActivityCast().getApplicationContext())))) {

                    spinAdapter = new ArrayAdapter<>(getActivityCast().getApplicationContext(), android.R.layout.simple_list_item_1, new CourseList().loadArrayList(getActivityCast().getApplicationContext()));
                    spin.setAdapter(spinAdapter);
                }
                return true;
            case R.id.reset:
                if (list.isEmpty()) {
                    list2.clear();
                    list3.clear();

                    Toast.makeText(getActivityCast().getApplicationContext(), "EMPTY TABLE", Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    AlertDialog.Builder surity = new AlertDialog.Builder(getActivityCast());
                    surity.setTitle("Alert")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage("Are you sure you want to reset the table?")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    list.clear();

                                    savingTimeTable();

                                    scheduleReminder(getNotification("You have a class in 15 minutes"), 0);
                                    scheduleReminder2(getNotification("You have a class in 10 minutes"), 0);

                                    table.notifyDataSetChanged();
                                    list2.clear();
                                    list3.clear();
                                    userOrder.clear();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                    return true;
                }
            case R.id.turnOff:
                isChecked = !item.isChecked();
                userCheckPreference(isChecked);
                item.setChecked(isChecked);

                if (item.isChecked()) {
                    if (list.isEmpty()) {
                        list2.clear();
                        list3.clear();
                    } else {
                        scheduleReminder(getNotification("You have a class in 15 minutes"), 1);
                        scheduleReminder2(getNotification("You have a class in 10 minutes"), 1);
                    }
                    Toast.makeText(getActivityCast(), "Alarm On", Toast.LENGTH_SHORT).show();
                    item.setTitle(R.string.turn_off_alarm);
                } else {
                    scheduleReminder(getNotification("You have a class in 15 minutes"), 0);
                    scheduleReminder2(getNotification("You have a class in 10 minutes"), 0);

                    Toast.makeText(getActivityCast(), "Alarm Off", Toast.LENGTH_SHORT).show();
                    item.setTitle(R.string.turn_on_alarm);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showFromTimePickerDialog() {
        TimePickerFragment time = new TimePickerFragment();
        time.setTargetFragment(DayDetailFragment.this,0);
        time.show(getFragmentManager(), getString(R.string.time_picker));
    }

    public void showToTimePickerDialog() {
        TimeToFragment time2 = new TimeToFragment();
        time2.setTargetFragment(DayDetailFragment.this,1);
        time2.show(getFragmentManager(), getString(R.string.time_picker));
    }

    public void showNoteTimePickerDialog() {
        TimeNoteFragment time3 = new TimeNoteFragment();
        time3.setTargetFragment(DayDetailFragment.this,2);
        time3.show(getFragmentManager(), getString(R.string.time_picker));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            int hour = data.getIntExtra(TimePickerFragment.HOUR_OF_DAY,0);
            int minute = data.getIntExtra(TimePickerFragment.MINUTE,0);
            processTimePickerResult(hour,minute);
        }
        if(requestCode == 1){
            int hour_to = data.getIntExtra(TimeToFragment.TO_HOUR_OF_DAY,0);
            int minute_to = data.getIntExtra(TimeToFragment.TO_HOUR_OF_DAY,0);
            processTimeToPickerResult(hour_to,minute_to);
        }
        if(requestCode == 2){
            int hour_note = data.getIntExtra(TimeNoteFragment.NOTE_HOUR_OF_DAY,0);
            int minute_note = data.getIntExtra(TimeNoteFragment.NOTE_MINUTE,0);
            processTimeNotePickerResult(hour_note,minute_note);
        }
    }

    public void processTimePickerResult(int hourOfDay, int minute) {

        String hour = Integer.toString(hourOfDay);
        String min = Integer.toString(minute);
        youHr = hourOfDay;
        youMin = minute;
        if (minute < 10) {
            //hour = "0"+hour;
            min = "0" + min;
            String time = hour + ":" + min;
            Toast.makeText(getActivityCast(), time, Toast.LENGTH_SHORT).show();

            from.setText(time);
            courseFromTime = time;
        } else {
            String time = hour + ":" + min;
            Toast.makeText(getActivityCast(), time, Toast.LENGTH_SHORT).show();

            from.setText(time);
            courseFromTime = time;
        }
    }

    public void processTimeToPickerResult(int hourOfDay, int minute) {

        String hour = Integer.toString(hourOfDay);
        String min = Integer.toString(minute);
        if (minute < 10) {
            //hour = "0"+hour;
            min = "0" + min;
            String time = hour + ":" + min;
            Toast.makeText(getActivityCast(), time, Toast.LENGTH_SHORT).show();
            to.setText(time);
            courseToTime = time;
        } else {
            String time = hour + ":" + min;
            Toast.makeText(getActivityCast(), time, Toast.LENGTH_SHORT).show();
            to.setText(time);
            courseToTime = time;
        }
    }

    public void processTimeNotePickerResult(int hourOfDay, int minute) {
        String hour = Integer.toString(hourOfDay);
        String min = Integer.toString(minute);
        NOTE_HR = hourOfDay;
        NOTE_MIN = minute;
        if (minute < 10) {
            //hour = "0"+hour;
            min = "0" + min;
            String time = hour + ":" + min;
            Toast.makeText(getActivityCast(), time, Toast.LENGTH_SHORT).show();

            noteTime = time;
        } else {
            String time = hour + ":" + min;
            Toast.makeText(getActivityCast(), time, Toast.LENGTH_SHORT).show();

            noteTime = time;
        }
        timeNote.setText(noteTime);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        spinItem = parent.getItemAtPosition(position).toString();
        //Toast.makeText(this,spinItem+"Chai",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void saveToTable() {
        if (courseFromTime == null || courseToTime == null) {
            AlertDialog.Builder timeChange = new AlertDialog.Builder(getActivityCast());
            timeChange.setTitle("ERROR")
                    .setMessage("PLEASE INPUT TIME")
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            String total = courseFromTime + " - " + courseToTime;
            totalCourseInfo = spinItem + "\t\t\t\t" + total;

            list.add(totalCourseInfo);
            userOrder.add(spinItem);

            savingTimeTable();
            table.notifyDataSetChanged();

            scheduleReminder(getNotification("You have a class in 15 minutes"), 1);
            scheduleReminder2(getNotification("You have a class in 10 minutes"), 1);

            from.setText(R.string.from_time);
            to.setText(R.string.to_time);
        }

    }


    public void tableItemRemover() {

        timeTable.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Object tableItem = parent.getItemAtPosition(position);
                //Toast.makeText(Monday.this,"SUCCESSFUL",Toast.LENGTH_LONG).show();
                AlertDialog.Builder build = new AlertDialog.Builder(getActivityCast());
                build.setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Remove from List?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(tableItem);
                                savingTimeTable();
                                scheduleReminder(getNotification("You have a class in 15 minutes"), 0);
                                scheduleReminder2(getNotification("You have a class in 10 minutes"), 0);
                                table.notifyDataSetChanged();
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    public void savingTimeTable() {
        //USING SHARED PREFERENCES
        SharedPreferences savedContentTimeTable = PreferenceManager.getDefaultSharedPreferences(getActivityCast().getApplicationContext());
        SharedPreferences.Editor editor = savedContentTimeTable.edit();
        editor.putInt("course_time_table_" + day, list.size());

        for (int i = 0; i < list.size(); i++) {
            editor.remove("course_time_table_" + day + i);
            editor.putString("course_time_table_" + day + i, list.get(i));
        }
        editor.apply();
    }

    public void loadingTimeTableHere(Context context) {
        SharedPreferences savedContentLoadTimeTable = PreferenceManager.getDefaultSharedPreferences(context);
        list.clear();
        int size = savedContentLoadTimeTable.getInt("course_time_table_" + day, 0);

        for (int i = 0; i < size; i++) {
            list.add(savedContentLoadTimeTable.getString("course_time_table_" + day + i, null));
            Log.d("Sharing", savedContentLoadTimeTable.getString("course_time_table_" + day + i, null));
        }
    }

    public void savingNotes() {
        //USING SHARED PREFERENCES
        String note = editNote.getText().toString();
        SharedPreferences savedNote = PreferenceManager.getDefaultSharedPreferences(getActivityCast().getApplicationContext());
        SharedPreferences.Editor editor = savedNote.edit();

        editor.putString("save_note_" + day, note);
        editor.putString("note_time_" + day, noteTime);
        editor.apply();
        Toast.makeText(getActivityCast().getApplicationContext(), "SUCCESSFUL", Toast.LENGTH_SHORT).show();
    }

    public void loadNotes(Button button) {
        SharedPreferences savedNote = PreferenceManager.getDefaultSharedPreferences(getActivityCast().getApplicationContext());
        String note = savedNote.getString("save_note_" + day, null);
        editNote.setText(note);
        if (editNote.getText().toString().isEmpty()) {
            button.setText(R.string.pick_a_time);
        } else {
            button.setText(savedNote.getString("note_time_" + day, null));
        }
    }


    public void scheduleReminder(Notification notification, int a) {
        Intent notifyIntent = new Intent(ACTION_NOTIFY);
        notifyIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, 1);
        notifyIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);

        int yourHour = youHr;
        int yourMins = youMin - 15;

        Calendar cal = Calendar.getInstance();
        //cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, yourHour);
        cal.set(Calendar.MINUTE, yourMins);
        cal.set(Calendar.SECOND, 00);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        long triggerTime = cal.getTimeInMillis();
        list2.add(triggerTime);

        for (int i = 0; i < list2.size(); i++) {
            if (Calendar.MONDAY == dayOfWeek) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivityCast().getApplicationContext(), i, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (a == 1) {
                    Log.e("TAG", list2.get(i).toString());
                    alarmManager.setRepeating(AlarmManager.RTC, list2.get(i), TimeTable.repeatInterval, pendingIntent);
                } else if (a == 0) {
                    assert alarmManager != null;
                    alarmManager.cancel(pendingIntent);
                }
            }

        }
    }

    public void scheduleReminder2(Notification notification, int a) {
        Intent notifyIntent = new Intent(ACTION_NOTIFY);
        notifyIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, 2);
        notifyIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);

        int yourHour = youHr;
        int yourMins = youMin - 10;

        Calendar cal = Calendar.getInstance();
        //cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, yourHour);
        cal.set(Calendar.MINUTE, yourMins);
        cal.set(Calendar.SECOND, 00);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        long triggerTime = cal.getTimeInMillis();
        list3.add(triggerTime);

        for (int i = 0; i < list3.size(); i++) {
            if (Calendar.MONDAY == dayOfWeek) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivityCast().getApplicationContext(), 100 + i, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (a == 1) {
                    Log.e("TAG2", list3.get(i).toString());
                    alarmManager.setRepeating(AlarmManager.RTC, list3.get(i), TimeTable.repeatInterval, pendingIntent);
                } else if (a == 0) {
                    assert alarmManager != null;
                    alarmManager.cancel(pendingIntent);
                }
            }

        }
    }

    public void alarmNoteStuff(Notification notification, int a) {
        Intent notifyIntent = new Intent(ACTION_NOTIFY);
        notifyIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, 3);
        notifyIntent.putExtra(AlarmReceiver.NOTIFICATION, notification);
        PendingIntent notifyPendingIntent = PendingIntent.getBroadcast(getActivityCast().getApplicationContext(), 3, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int yourHour = NOTE_HR;
        int yourMins = NOTE_MIN;

        Calendar calN = Calendar.getInstance();
        calN.setTimeInMillis(System.currentTimeMillis());
        calN.set(Calendar.HOUR_OF_DAY, yourHour);
        calN.set(Calendar.MINUTE, yourMins);

        int dayOfWeek = calN.get(Calendar.DAY_OF_WEEK);

        long triggerTime = calN.getTimeInMillis();

        if (dayOfWeek == Calendar.MONDAY) {
            if (a == 1) {
                Toast.makeText(getActivityCast(), "Alarm SET", Toast.LENGTH_SHORT).show();
                alarmManager.set(AlarmManager.RTC, triggerTime, notifyPendingIntent);
            } else if (a == 0) {
                alarmManager.cancel(notifyPendingIntent);
            }
        }
    }

    public Notification getNotification(String content) {
        PendingIntent contentPendingIntent = PendingIntent.getActivity(getActivityCast().getApplicationContext(), 0, new Intent(getActivityCast().getApplicationContext(), getClass()), PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder builder = new Notification.Builder(getActivityCast().getApplicationContext());
        builder.setContentTitle("The Student Planner");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_notification_2);
        builder.setSound(alarmSound);
        builder.setContentIntent(contentPendingIntent);
        return builder.build();
    }

    public void userCheckPreference(boolean v) {
        SharedPreferences checkbox = PreferenceManager.getDefaultSharedPreferences(getActivityCast().getApplicationContext());
        SharedPreferences.Editor editor = checkbox.edit();
        editor.putBoolean(CHECK_BOX, v);
        editor.apply();
    }

    public boolean loadCheckboxValue() {
        SharedPreferences checkbox = PreferenceManager.getDefaultSharedPreferences(getActivityCast().getApplicationContext());
        return checkbox.getBoolean(CHECK_BOX, true);
    }
}
