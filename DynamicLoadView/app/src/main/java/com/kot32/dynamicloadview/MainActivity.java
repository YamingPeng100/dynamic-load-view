package com.kot32.dynamicloadview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.kot32.dynamicloadviewlibrary.core.DynamicViewGroup;
import com.kot32.dynamicloadviewlibrary.core.DynamicViewManager;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 0x11;

    private DynamicViewGroup dynamicViewGroup;

    private View newView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dynamicViewGroup = (DynamicViewGroup) findViewById(R.id.dv);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //Must have permission :WRITE_EXTERNAL_STORAGE
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }


        // you can Handle this view like a normal view.
        newView = dynamicViewGroup.getDynamicView();

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // also you can use resources in your host Activity.
                newView.setBackgroundResource(R.mipmap.ic_launcher);
                Toast.makeText(MainActivity.this, R.string.app_name, Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicViewManager.getInstance().clearUpdateCache();
            }
        });

        // But the dynamicView how to asses the host Activity?
        // I don't advise you to do that. If that's necessary for you,
        // you can use Reflect in Java to asses Context in View

    }

}
