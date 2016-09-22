package com.kot32.testdynamicviewproject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by kot32 on 16/9/22.
 */
public class MyButton1 extends RelativeLayout {


    public MyButton1(Context context) {
        super(context);
        init(context);
    }

    public MyButton1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        SmartisanTime smartisanTime = new SmartisanTime(context);
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(300, 300);
        layoutParams.addRule(CENTER_IN_PARENT, TRUE);
        addView(smartisanTime, layoutParams);

        Button button = new Button(context);
        button.setText("Dynamic Success");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Dynamic-load-view", Toast.LENGTH_SHORT).show();
            }
        });

        addView(button);

        setBackgroundResource(R.drawable.test2);
    }
}
