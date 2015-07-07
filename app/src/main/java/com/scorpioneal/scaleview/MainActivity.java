package com.scorpioneal.scaleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ScaleView mScaleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScaleView = (ScaleView)findViewById(R.id.scaleview);
        mScaleView.setShownValue(110f);
        mScaleView.setShowText("110公斤");
    }
}
