package com.example.hari.qpay;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


    }
    //For changing the Profile details
    public void change(View view)
    {
        Snackbar.make(view, "Change profile", Snackbar.LENGTH_LONG).show();
    }

}
