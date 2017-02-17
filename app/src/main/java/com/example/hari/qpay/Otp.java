package com.example.hari.qpay;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class Otp extends AppCompatActivity {

    String jsonString;
    String otpno;
    static EditText OtpNumber;
    OkHttpClient client;
    public static  final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private String credentials;
    String message1;
    String status;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        OtpNumber= (EditText) findViewById(R.id.otp);

    }

public void onBackPressed(){}
    public void recivedSms(String message) {
        try {
            message1=message;
            OtpNumber.setText(message);

        }

catch (Exception e)
        {
        }
    }
//Verifing OTP
    public void verify(final View view)

    {        client = new OkHttpClient();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonString = extras.getString("Json");
            otpno=extras.getString("otp");
            status=OtpNumber.getText().toString();

           if(status.equals(otpno))
            {
                Snackbar.make(view,"Successfully SignedUp" , Snackbar.LENGTH_LONG).show();
                this.credentials = Credentials.basic("admin", "admin");
                new Thread(new Runnable() {

                    @Override

                    public void run() {

                        try {

                            RequestBody body = RequestBody.create(JSON, jsonString);
                            okhttp3.Request request = new okhttp3.Request.Builder()
                                    .url("http://qpayproject-blazemits.rhcloud.com/api/create/")
                                   
                                    .header("Authorization", credentials)
                                    .post(body)
                                    .build();

                            Response response = client.newCall(request).execute();
                            String detail = response.body().string();




                                    Intent intent=new Intent(view.getContext(),login.class);
                                    startActivity(intent);







                        } catch (IOException ie) {
                            ie.printStackTrace();
                        }
                                          }
                }).start();

            }
            else
              Snackbar.make(view, "Please provide correct number", Snackbar.LENGTH_LONG).show();

        }

    }

}
