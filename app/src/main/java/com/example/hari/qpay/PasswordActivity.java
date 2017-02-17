package com.example.hari.qpay;

import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
//Updating Password
public class PasswordActivity extends AppCompatActivity {
    String userid="";
    OkHttpClient client;
    public static  final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private String credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

    }
//Method called for updating password
    public void changepass(final View view) {
        final EditText newp = (EditText) findViewById(R.id.newpass);
        final EditText oldp = (EditText) findViewById(R.id.oldpass);
        final String newpass = newp.getText().toString();
        final String oldpass = oldp.getText().toString();
        client = new OkHttpClient();
        this.credentials = Credentials.basic("admin", "admin");

        JSONObject pass = new JSONObject();
        try {
            pass.put("newPWD", newpass);
        } catch (JSONException je) {
        }
        final String jsonString = pass.toString();
        Snackbar.make(view, "Change password", Snackbar.LENGTH_LONG).show();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("uid");

        }
       final String url="http://qpayproject-blazemits.rhcloud.com/api/changepwd/"+userid;
        //Snackbar.make(view, url, Snackbar.LENGTH_LONG).show();
        if (newpass != "") {


            new Thread(new Runnable() {

                @Override

                public void run() {

                    try {



                        RequestBody body = RequestBody.create(JSON, jsonString);
                        okhttp3.Request request = new okhttp3.Request.Builder()
                                .url(url)
                                .header("Authorization", credentials)
                                .post(body)
                                .build();

                        Response response = client.newCall(request).execute();
                        String presponse=response.body().string();
                        Snackbar.make(view,presponse, Snackbar.LENGTH_LONG).show();


                    } catch (IOException ie) {
                        ie.printStackTrace();
                    }
                }
            }).start();



        }
    }
}

