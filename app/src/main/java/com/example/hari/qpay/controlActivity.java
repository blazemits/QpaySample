package com.example.hari.qpay;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class controlActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
String value;
    String amount;
    String id;
    OkHttpClient client;
    String details;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String credentials;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Donor");
        tabSpec.setContent(R.id.Donor);
        tabSpec.setIndicator("I'm Donor");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("Recipient");
        tabSpec.setContent(R.id.Recipient);
        tabSpec.setIndicator("I'm Recipient");
        tabHost.addTab(tabSpec);
       // TextView id= (TextView)findViewById(R.id.userid);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("userid");
         //   id.setText("userid:"+value);

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();
        }
    }

   // @Override
   /* public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.control, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_first_layout) {
            // Handle the camera action
           Intent intent = new Intent(this,ProfileActivity.class);
            intent.putExtra("uid",value);
            startActivity(intent);
        } else if (id == R.id.nav_second_layout) {
            Intent intent = new Intent(this,PasswordActivity.class);
            intent.putExtra("uid",value);
            startActivity(intent);
        } else if (id == R.id.nav_third_layout) {
            Intent intent = new Intent(this,login.class);
            startActivity(intent);
        }
        else if(id==R.id.nav_Contact_Us){

            Intent intent = new Intent(this,Contactus.class);
            startActivity(intent);
        }
else if(id==R.id.nav_About_US)
        {

            Intent intent = new Intent(this,Aboutus.class);
startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //For changing to recipient mode
    public void request(View view) {
        Intent intent = new Intent(this, Request.class);
        intent.putExtra("login_id",value);
        startActivity(intent);
    }
    //Initiate Qrcode scanning
    public void scan(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanResult != null && resultCode==RESULT_OK) {
            String re = scanResult.getContents();
             amount=re.substring(0,re.indexOf("I"));
             id=re.substring(re.indexOf(":") +1 , re.length());

   }}
/*For making the transanction if amount entered is correct*/
public void Transfer(final View view)
{

JSONObject transfer=new JSONObject();
    this.credentials = Credentials.basic("admin", "admin");
    client =new OkHttpClient();
    Snackbar.make(view, "The amount to be transfered is "+amount, Snackbar.LENGTH_LONG).show();
final String url="http://qpayproject-blazemits.rhcloud.com/trans/";
    int did=0;
    int rid=0;
    int amt=0;
    try
    {
        did=Integer.parseInt(value);
        rid=Integer.parseInt(id);
        amt=Integer.parseInt(amount);
    }
    catch(NumberFormatException nfe)
    {
        Snackbar.make(view,"Amount not in correct format",Snackbar.LENGTH_LONG).show();
    }
    try{
        transfer.put("donorID",did);
        transfer.put("recipientID",rid);
        transfer.put("amount",amt);

    }
    catch(JSONException je){}
    final String transferString=transfer.toString();

    new Thread(new Runnable() {

        @Override

        public void run() {

            try {



                 RequestBody body = RequestBody.create(JSON, transferString);
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(url)
                        .header("Authorization", credentials)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                String json=response.body().string();
               //Snackbar.make(view,json, Snackbar.LENGTH_LONG).show();
                // Toast.makeText(signup.this, response.code()+"", Toast.LENGTH_LONG).show();
                try{
                    JSONObject jsonobject1=new JSONObject(json);

                        details= jsonobject1.getString("details");

                        Snackbar.make(view,details, Snackbar.LENGTH_LONG).show();

                }
                catch (JSONException je){}

            }
            catch (IOException ie) {
                ie.printStackTrace();
            }

        }
    }).start();
}
}


