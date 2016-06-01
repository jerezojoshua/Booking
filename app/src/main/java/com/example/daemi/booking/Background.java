package com.example.daemi.booking;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;

/**
 * Created by Joshua on 5/19/2016.
 */
public class Background extends AsyncTask<String,Void,String>
{
    Context ctx;
    Background(Context ctx)
    {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String ... params)
    {
        String URL_REG = "http://angkas.net23.net/BookingInsert.php";
        String URL_Search = "http://angkas.net23.net/BookingSearch.php";

        String method = params[0];
        if(method.equals("Register"))
        {
           // String pickup = params[1];
           // String drop = params[2];
            String email_login = "Pass0002";

            try {
                URL url = new URL(URL_REG);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("passengerid","UTF-8")+"="+URLEncoder.encode(email_login,"UTF-8");
                        //+"&"+
                //URLEncoder.encode("drop","UTF-8")+"="+URLEncoder.encode(drop,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                inputStream.close();

                return "Trip Booked";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("BookingSearch"))
        {
            String pickup = params[1];

            try {
                URL url = new URL(URL_Search);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data = URLEncoder.encode("pickup","UTF-8")+"="+URLEncoder.encode(pickup,"UTF-8");
                //+"&"+
                //URLEncoder.encode("drop","UTF-8")+"="+URLEncoder.encode(drop,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                inputStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Search complete";
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
    }
}
