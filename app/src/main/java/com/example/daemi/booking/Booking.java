package com.example.daemi.booking;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class Booking extends AppCompatActivity {

    String json_string;
    private Calendar cal;
    private int day, month, year,hour, minute;
    private EditText DateDepart, DateReturn, TimeDepart, TimeReturn;
    private RadioButton rb1, rb2;
    private String DateCondition, TimeCondition;
    String[] Places = {"Taguig","Makati","Quezon","Manila","Cavite"};
    AutoCompleteTextView TxtViewFrom,TxtViewTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        //Auto Complete Text View
        TxtViewFrom = (AutoCompleteTextView)findViewById(R.id.SpnrFrom);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,Places);
        TxtViewFrom.setThreshold(2);
        TxtViewFrom.setAdapter(adapter);

        TxtViewTo = (AutoCompleteTextView)findViewById(R.id.SpnrTo);
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,Places);
        TxtViewTo.setThreshold(2);
        TxtViewTo.setAdapter(adapter);

        //Date & Time Picker
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        DateDepart = (EditText) findViewById(R.id.EditText);
        DateReturn = (EditText) findViewById(R.id.EditText1);
        TimeDepart = (EditText) findViewById(R.id.EditText2);
        TimeReturn = (EditText) findViewById(R.id.EditText3);

        DateReturn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DateCondition = "DateReturn";
                showDialog(0);
            }
        });

        DateDepart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DateCondition = "DateDepart";
                showDialog(0);
            }
        });

        TimeDepart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TimeCondition = "TimeDepart";
                showDialog(1);
            }
        });

        TimeReturn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TimeCondition = "TimeReturn";
                showDialog(1);
            }
        });
        //END OF DATE PICKER

        rb1 = (RadioButton) findViewById(R.id.RadioReturn);
        rb2 = (RadioButton) findViewById(R.id.RadioOneWay);

        rb1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rb2.setChecked(false);
                TimeReturn.setVisibility(View.VISIBLE);
                DateReturn.setVisibility(View.VISIBLE);
            }
        });

        rb2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rb1.setChecked(false);
                TimeReturn.setVisibility(View.INVISIBLE);
                DateReturn.setVisibility(View.INVISIBLE);
            }
        });
    }

    //Set Date Picked to Text View
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
            case 1:
                return new TimePickerDialog(this, timePickerListener, hour, minute, false);
            case 0:
                return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;

    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay)
        {
            if (DateCondition == "DateReturn")
            {
                DateReturn.setText(selectedDay + " / " + (selectedMonth + 1) + " / " + selectedYear);
            }
            else if (DateCondition == "DateDepart")
            {
                DateDepart.setText(selectedDay + " / " + (selectedMonth + 1) + " / " + selectedYear);
            }
        }
    };


    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes)
        {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            if(TimeCondition == "TimeReturn")
            {
                TimeReturn.setText(hour + ":" + minute);
            }
            else if (TimeCondition == "TimeDepart")
            {
                TimeDepart.setText(hour + ":" + minute);
            }
        }
    };

    public void ShowSched(View view)
    {
        new BackgroundTask(this).execute();
    }

    class BackgroundTask extends AsyncTask<Void,Void,String>
    {
        private Context context;
        String json_url;

        public BackgroundTask(Context context)
        {
            this.context=context;
        }

        public BackgroundTask() {

        }

        @Override
        protected void onPreExecute()
        {
            json_url ="http://angkas.net23.net/JsonRetrieve.php";
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream =  httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                while((json_string = bufferedReader.readLine())!= null)
                {
                    stringBuilder.append(json_string+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(context, Booking_Schedules.class);
            intent.putExtra("json_data",result);
            context.startActivity(intent);
            ((Activity)context).finish();
        }
    }
}