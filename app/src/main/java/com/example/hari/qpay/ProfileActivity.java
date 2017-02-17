package com.example.hari.qpay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    OkHttpClient client;
    String userid;
    String login;
    String username;
    String mobnum;
    String wallet;
    private String credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


    }


public void edit(View view)
{


    Intent intent=new Intent(this,EditActivity.class);
    startActivity(intent);

}
    public void loading(final View view)
    {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid= extras.getString("uid");


        }

        final String url="http://qpayproject-blazemits.rhcloud.com/api/accountinfo/"+userid;
        this.credentials = Credentials.basic("admin", "admin");
        client=new OkHttpClient();

        final TextView  user= (TextView) findViewById(R.id.UserName);
        final TextView mobile = (TextView) findViewById(R.id.mobnum);
        final   TextView walletamt = (TextView) findViewById(R.id.wallet);

        new Thread(new Runnable() {

            @Override

            public void run() {
                try {

                    //   client = new OkHttpClient();
                    // RequestBody body = RequestBody.create(JSON, jsonString);
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(url)
                            .header("Authorization", credentials)

                            .build();

                    Response response = client.newCall(request).execute();

                    String json=response.body().string();





                    try{

                        JSONObject jsonobject=new JSONObject(json);

                        login= jsonobject.getString("login_id");
                        username = jsonobject.getString("user_Name");
                        mobnum = jsonobject.getString("mob_num");
                        wallet = jsonobject.getString("walletAmount");

                    }
                    catch (JSONException je){

                    }

                } catch (IOException ie) {
                    ie.printStackTrace();
                }

            }
        }).start();
             if(login!="") {
//                 loginid.setText("Userid:" + login);
                 user.setText("Usrname:" + username);
                 mobile.setText("Mobnum:" + mobnum);
                 walletamt.setText("Balance: Rs" + wallet);
             }
        else
        Snackbar.make(view, "Wait", Snackbar.LENGTH_LONG).show();
    }
}
