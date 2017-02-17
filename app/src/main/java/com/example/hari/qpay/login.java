package com.example.hari.qpay;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.*;

public class login extends AppCompatActivity {
    OkHttpClient client;
   // public static  final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private String credentials;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    @Override
    public void onBackPressed() {
    }
    /*Check wheather the mailid is already used for registration*/
    public void checkEmail(final View view) {
        client=new OkHttpClient();
        this.credentials = Credentials.basic("admin", "admin");
        final EditText emailValidate = (EditText) findViewById(R.id.username1);
        final EditText password =(EditText)findViewById(R.id.password);
        String email = emailValidate.getText().toString().trim();
        String emailid=emailValidate.getText().toString();
        String spass= password.getText().toString();
        final String url ="http://qpayproject-blazemits.rhcloud.com/api/login/"+emailid+"/"+spass;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(emailPattern)&& password.getText().toString().trim().length()!=0 ) {
            new Thread(new Runnable() {

                @Override

                public void run() {
                    try {
                        String login="";
                        okhttp3.Request request = new okhttp3.Request.Builder()
                                .url(url)
                                .header("Authorization", credentials)
                                .build();
                        Response response = client.newCall(request).execute();
                        String json=response.body().string();
                        try{
                            JSONArray jsonarray = new JSONArray(json);

                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                login = jsonobject.getString("login_id");
                            }
                            Snackbar.make(view,login, Snackbar.LENGTH_LONG).show();
                        }
                        catch (JSONException je){}
                        if(login!="")
                        {
                            Intent intent = new Intent(view.getContext(), controlActivity.class);
                            intent.putExtra("userid",login);
                            startActivity(intent);
                        }
                        else
                        {
                            Snackbar.make(view,"Invalid inputs", Snackbar.LENGTH_LONG).show();
                        }

                    }
                    catch (IOException ie) {
                        ie.printStackTrace();
                    }

                }
            }).start();
        }
        else {
           Snackbar.make(view, "invalid email or enter the password", Snackbar.LENGTH_LONG).show();

        }

    }
    public void signUp(View view)
    {
        Intent intent1=new Intent(this,signup.class);
        startActivity(intent1);
    }

}

