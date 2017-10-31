package com.example.zac.myapplication.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.zac.myapplication.R;

import java.text.DateFormat;
import java.util.Calendar;

public class DateActivity extends AppCompatActivity {

    private Button pickStartDate = null;
    private Button pickEndDate = null;
    private int curYear, curMonth, curDay, startYear, startMonth, startDay, endYear, endMonth, endDay;
    private Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private TextView txt;
    Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        this.curMonth = Integer.parseInt(intent.getStringExtra("MONTH"));
        this.curDay = Integer.parseInt(intent.getStringExtra("DAY"));
        this.curYear = Integer.parseInt(intent.getStringExtra("YEAR"));

        //pickStartDate = (Button) findViewById(R.id.startDate);
        //pickEndDate = (Button) findViewById(R.id.endDate);

        startMonth = curMonth;
        startDay = curDay;
        startYear = curYear;

        endMonth = curMonth;
        endDay = curDay;
        endYear = curYear;

    }


    void pickStartDate(View view) {
        showCalenderStart(startYear, startMonth, startDay);

    }


    void pickEndDate(View view) {
        showCalenderEnd(endYear, endMonth, endDay);

    }


    void showCalenderStart(int year, int day, int month) {

        // show today
        DatePickerDialog dp = new DatePickerDialog(DateActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                startMonth = month;
                startDay = day;
                startYear = year;
            }
        }, year, month, day);

        dp.setTitle("Select date");
        dp.show();
        Log.d("PICK_START_DATE", "   START:  " + startMonth + "-" + startDay + "-" + startYear);
    }



    void showCalenderEnd(int year, int day, int month) {

        // show today
        DatePickerDialog dp = new DatePickerDialog(DateActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                endMonth = month;
                endDay = day;
                endYear = year;
            }
        }, year, month, day);

        dp.setTitle("Select date");
        dp.show();
        Log.d("PICK_END_DATE", "   START:  " + endMonth + "-" + endDay + "-" + endYear);
    }


    private void showDate(int year, int month, int day) {
        Log.d("PICK_DATE", "   START:  " + month + "-" + day + "-" + year);

    }





}
