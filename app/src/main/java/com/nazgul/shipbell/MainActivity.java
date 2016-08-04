package com.nazgul.shipbell;

import android.app.ActivityManager;
import android.app.Dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;



public class MainActivity extends AppCompatActivity  {
    int DIALOG_TIME = 1;

    int myHour = 14;
    int myMinute = 35;

    TextView textView2;
    CheckBox checkbox;
    RadioButton rBIteration;
    RadioButton rBInTime;
    EditText editText;
    EditText editText2;

    public static int iteration = 0;
    public static long interval = 0;
    public static long inTime = 0;
    static long deltaTime = 0;
    static long deltaTime1 = 0;



    static boolean isRunning;
    static String mod;




    public void onClickStartTimer(View view){

        if (editText != null) {                 //  в минутах интервал между гудками
            interval = Long.parseLong(String.valueOf(editText.getText()))*60000;
        }

        if(!checkbox.isChecked()){           // не стоит галка, значит вызываем метод интервал
           mod  = "interval";
        }else {
            if(rBIteration.isChecked()&&!rBInTime.isChecked()){
                if (editText2 != null) {
                    iteration = Integer.parseInt(String.valueOf(editText2.getText()));
                }
                mod = "iteration";
            }
            if(!rBIteration.isChecked()&&rBInTime.isChecked()){
                mod  = "inTime";
                Date date = new Date();   // given date
                Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                calendar.setTime(date);

                if(myHour>calendar.get(Calendar.HOUR_OF_DAY)){     // gets hour in 24h format
                    Date dateInFuture = new Date();   // given date
                    dateInFuture.setHours(myHour);
                    dateInFuture.setMinutes(myMinute);
                    Calendar calendarInFuture = GregorianCalendar.getInstance(); // creates a new calendar instance
                    calendarInFuture.setTime(dateInFuture);
                    calendarInFuture.add(Calendar.DATE, 1);
                    deltaTime = calendarInFuture.getTimeInMillis() - calendar.getTimeInMillis();
                }else {
                    Date dateInFuture = new Date();   // given date
                    dateInFuture.setHours(myHour);
                    dateInFuture.setMinutes(myMinute);
                    Calendar calendarInFuture = GregorianCalendar.getInstance(); // creates a new calendar instance
                    calendarInFuture.setTime(dateInFuture);
                    deltaTime = calendarInFuture.getTimeInMillis() - calendar.getTimeInMillis();
                }
            }
        }
        Intent intent = new Intent(this, Bell.class);
        intent.putExtra(Bell.EXTRA_MOD, mod);

    }


    public void onClickStopTimer(View view){
        isRunning = false;
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();

        Iterator<ActivityManager.RunningAppProcessInfo> iter = runningAppProcesses.iterator();

        while(iter.hasNext()){
            ActivityManager.RunningAppProcessInfo next = iter.next();

            String pricessName = getPackageName() + ":service";

            if(next.processName.equals(pricessName)){
                android.os.Process.killProcess(next.pid);
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView2 = (TextView) findViewById(R.id.textView2);
        checkbox = (CheckBox)findViewById(R.id.checkBox);
        rBIteration = (RadioButton)findViewById(R.id.radioButton);
        rBInTime = (RadioButton)findViewById(R.id.radioButton2);
        setContentView(R.layout.activity_main);
        editText = (EditText)findViewById(R.id.editText);
        editText2 = (EditText)findViewById(R.id.editText2);

    }

    public void onclick2(View view) {
        showDialog(DIALOG_TIME);
    }


    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_TIME) {
            TimePickerDialog tpd = new TimePickerDialog(this, myCallBack, myHour, myMinute, true);
            return tpd;
        }


        return super.onCreateDialog(id);
    }

    OnTimeSetListener myCallBack = new OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myHour = hourOfDay;
            myMinute = minute;
            textView2.setText("Time is " + myHour + " hours " + myMinute + " minutes");
        }
    };


}


