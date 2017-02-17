package com.example.hari.qpay;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Credentials;

public class signup extends AppCompatActivity {
    OkHttpClient client;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String credentials;
    String confirm;
    String mobileno;
    String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

    }
/*check whether all fields are filled and transfer data to server*/
    public void checklist(final View view) {
        final EditText email = (EditText) findViewById(R.id.username1);
        final EditText password = (EditText) findViewById(R.id.password);
        final EditText aadhar = (EditText) findViewById(R.id.aadhar);
        String error = "notempty";
        String emailid = email.getText().toString();
        String pass = password.getText().toString();
        final String mobile = aadhar.getText().toString();
        mobileno = mobile;
        JSONObject post_dict = new JSONObject();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        try {
            post_dict.put("login_id", 555);
            post_dict.put("user_Name", emailid);
            post_dict.put("password", pass);
            post_dict.put("mob_num", mobile);
            post_dict.put("walletAmount", 100);
            post_dict.put("bankACNum", 0);
            post_dict.put("userStatus", true);

        } catch (JSONException e) {
        }//String response=new String();

        final String jsonString = post_dict.toString();
        client = new OkHttpClient();

        if (email.getText().toString().trim().length() == 0) {

            error = "empty";
        }
        if (!emailid.matches(emailPattern)) {

            error = "invalid email";
        }
        if (password.getText().toString().trim().length() == 0) {

            error = "empty";
        }

        if (aadhar.getText().toString().trim().length() <= 9) {

            error = "mobile";
        }


        if (error != "empty" && error != "invalid email" && error != "mobile") {
            this.credentials = Credentials.basic("admin", "admin");
            if (isNetworkStatusAvialable(getApplicationContext())) {
                new Thread(new Runnable() {

                    @Override

                    public void run() {

                        try {
                            RequestBody body = RequestBody.create(JSON, jsonString);
                            okhttp3.Request request = new okhttp3.Request.Builder()
                                    .url("http://qpayproject-blazemits.rhcloud.com/api/create/")
                                    .header("Authorization", credentials)
                                    .put(body)
                                    .build();

                            Response response = client.newCall(request).execute();
                            String json2 = response.body().string();
                            try {
                                JSONObject job = new JSONObject(json2);
                                confirm = job.getString("details");
                                Snackbar.make(view, confirm, Snackbar.LENGTH_LONG).show();
                            } catch (JSONException je) {
                                Snackbar.make(view, "error", Snackbar.LENGTH_LONG).show();
                            }


                            if (confirm.equals("Your Account Is Available")) {
                                send(view, jsonString);

                            } else
                                Snackbar.make(view, confirm, Snackbar.LENGTH_LONG).show();

                            // Toast.makeText(signup.this, response.code()+"", Toast.LENGTH_LONG).show();

                        } catch (IOException ie) {
                            ie.printStackTrace();
                        }
                    }

                }).start();


            }
            else
            {
                Snackbar.make(view, "Please check your Internet Connection", Snackbar.LENGTH_LONG).show();
            }
        }
            else if (error == "invalid email")

            {
                Snackbar.make(view, "Invalid Emailid", Snackbar.LENGTH_LONG).show();
            } else if (error == "mobile")
                Snackbar.make(view, "Invalid mobile number", Snackbar.LENGTH_LONG).show();

            else

            {
                Snackbar.make(view, "Fill Empty Field", Snackbar.LENGTH_LONG).show();
            }
        }

/*Check the otp and signup is confirmed*/

    public void send(final View view,final String jsonString) {
        this.credentials = Credentials.basic("admin", "admin");
        new Thread(new Runnable() {

            @Override

            public void run() {

                try {
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url("http://qpayproject-blazemits.rhcloud.com/otp/"+mobileno)
                            .header("Authorization", credentials)

                            .build();

                    Response response = client.newCall(request).execute();
                    String json2 = response.body().string();
                    try {
                        JSONObject job = new JSONObject(json2);
                        otp = job.getString("otp");
                        Intent intent = new Intent(view.getContext(), Otp.class);
                        intent.putExtra("Json", jsonString);
                        intent.putExtra("otp", otp);
                        startActivity(intent);

                    } catch (JSONException je) {

                    }

                } catch (IOException ie) {
                }
            }
        }).start();
    }
    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
            {
                return netInfos.isConnected();
            }
        }
        return false;
    }
}



    //public void otp(View view) {
    //   Intent intent = new Intent(this, Otp.class);
    // startActivity(intent);
    //}
//Posting json data



