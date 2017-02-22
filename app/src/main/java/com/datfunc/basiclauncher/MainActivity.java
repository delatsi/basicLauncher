package com.datfunc.basiclauncher;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TypefaceUtil.overrideFont(getApplicationContext(), "MONOSPACE", "fonts/RobotoMono-Regular.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String currentDateString = DateFormat.getDateInstance().format(new Date());

// textView is the TextView view that should display it
        TextView textView = (TextView) findViewById(R.id.DATE);
        textView.setText(currentDateString);

    }

    public void showApps(View v){
        Intent i = new Intent(this, AppsListActivity.class);
        startActivity(i);

    }

    public void openCamera(View v){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(intent);
    }
}
