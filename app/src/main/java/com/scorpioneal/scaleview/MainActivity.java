package com.scorpioneal.scaleview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ScaleView mScaleView;
    private int i = 1;

    private Random mRandom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScaleView = (ScaleView)findViewById(R.id.scaleview);
        mScaleView.setShownValue(110);

        mRandom = new Random();
        mHandler.sendEmptyMessage(123);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 123){
                int t = mRandom.nextInt(180);
                Log.d(TAG, "update view " + t);
                mScaleView.setShownValue(t);
                mScaleView.setShowText(t+"");

                mHandler.sendEmptyMessageDelayed(123, 1000);
            }
        }
    };
}
