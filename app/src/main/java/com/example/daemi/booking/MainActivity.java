package com.example.daemi.booking;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{
    EditText editText1,editText2;

    RadioButton AddPassYes,AddPassNo;

    CheckBox expandableButton2;

    ExpandableRelativeLayout  expandableLayoutAddPass,expandableLayoutAddPassNo,expandableLayout3,expandableLayout2;


    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    SchedDataAdapter schedDataAdapter;
    ListView listView;
    Context context;    String position;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        position = getIntent().getExtras().getString("Position");

        String method = "BookingSearch";

        Background background = new Background(MainActivity.this);
        background.execute(method,position);
        finish();


        SchedDataAdapter schedDataAdapter = new SchedDataAdapter(this, R.layout.row_layout);


        try
        {
            jsonObject = new JSONObject(json_string);
            jsonArray = jsonObject.getJSONArray("Response");

            int count=0;
            String pickup,dropoff,date,time;

            while(count<jsonArray.length())
            {
                JSONObject JO = jsonArray.getJSONObject(count);
                pickup = JO.getString("PickUp");
                dropoff = JO.getString("DropOff");
                date = JO.getString("Date");
                time = JO.getString("Time");

                SchedData schedData = new SchedData(pickup,dropoff,date,time);
                schedDataAdapter.add(schedData);
                count++;
            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }

        expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);
        expandableLayout3 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout3);
        expandableLayoutAddPass = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutAddPass);
        expandableLayoutAddPassNo = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutAddPassNo);

        AddPassYes = (RadioButton) findViewById(R.id.AddPassYes);
        AddPassNo = (RadioButton) findViewById(R.id.AddPassNo);
        expandableButton2 = (CheckBox) findViewById(R.id.expandableButton2);

        expandableButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expandableButton2.isChecked())
                {
                    expandableLayout2.expand(); // toggle expand and collapse

                }
                else
                {
                    expandableLayoutAddPass.collapse();
                    expandableLayout2.collapse();
                    expandableLayoutAddPassNo.collapse();

                }
            }
        });

        AddPassYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AddPassYes.isChecked())
                {
                    expandableLayoutAddPassNo.collapse();
                    expandableLayoutAddPass.toggle();
                }
            }
        });
}

    public void ShowDialog(View view)
    {
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
        alertdialogbuilder.setMessage("Do you want to reserve this schedule?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String method = "Register";

                        Background background = new Background(MainActivity.this);
                        background.execute(method);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alertdialogbuilder.show();
    }

    public void Home(View view)
    {
        Intent intent = new Intent("booking");
        startActivity(intent);

    }

    public void expandableButton2(View view) {
        ExpandableRelativeLayout expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);

        AddPassYes = (RadioButton) findViewById(R.id.AddPassYes);
        AddPassYes.setChecked(false);

        AddPassNo = (RadioButton) findViewById(R.id.AddPassNo);
        AddPassNo.setChecked(false);

        expandableLayout2.toggle(); // toggle expand and collapse

        ExpandableRelativeLayout expandableLayoutAddPass = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutAddPass);
        expandableLayoutAddPass.collapse(); // toggle expand and collapse
        expandableLayoutAddPassNo.collapse();

    }

    public void AddPassNo(View view)
    {
        if (AddPassNo.isChecked()) {
            AddPassYes.setChecked(false);
            expandableLayoutAddPass.collapse();
            expandableLayoutAddPassNo.toggle(); // toggle expand and collapse
        }
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
