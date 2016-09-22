package com.kot32.testdynamicviewproject;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kot32 on 16/9/14.
 */
public class MyButton extends TextView {
    int i = 0;

    public MyButton(Context context) {
        super(context);
        init();
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackgroundResource(R.drawable.test);
        setText(getResources().getText(R.string.testttt));
        setTextColor(Color.WHITE);
        setGravity(Gravity.CENTER);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "HAHAHAHAHA", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
