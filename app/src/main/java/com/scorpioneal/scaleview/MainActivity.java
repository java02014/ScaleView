package com.scorpioneal.scaleview;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private ScaleView mScaleView;
    private int i = 1;

    private Button mValueBtn, mRangeBtn, mAngelBtn, mColorBtn, mScaleBtn;

    private Random mRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScaleView = (ScaleView)findViewById(R.id.scaleview);
        mScaleView.setShownValue(110);
        mValueBtn = (Button)findViewById(R.id.change_value_btn);
        mRangeBtn = (Button)findViewById(R.id.change_range_btn);
        mAngelBtn = (Button)findViewById(R.id.change_angle_btn);
        mColorBtn = (Button)findViewById(R.id.change_color_btn);
        mScaleBtn = (Button)findViewById(R.id.change_scale_btn);

        mValueBtn.setOnClickListener(this);
        mRangeBtn.setOnClickListener(this);
        mAngelBtn.setOnClickListener(this);
        mColorBtn.setOnClickListener(this);
        mScaleBtn.setOnClickListener(this);


        mRandom = new Random();
    }

    private boolean flag = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_value_btn:
                float f = mScaleView.getMaxValue();
                Message message = mHandler.obtainMessage(123);
                message.arg1 = mRandom.nextInt((int)f);
                mHandler.sendMessage(message);
                break;
            case R.id.change_range_btn:
                flag = !flag;
                if(flag) {
                    mScaleView.setMinMaxValue(100, 400);
                }else {
                    mScaleView.setMinMaxValue(0, 180);
                }

                break;
            case R.id.change_angle_btn:
                flag = !flag;
                if(flag) {
                    mScaleView.setStartEndAngle(180, 360);
                } else {
                    mScaleView.setStartEndAngle(135, 405);
                }
                break;
            case R.id.change_color_btn:
                mScaleView.setScaleBackgroundColor(Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)));
                mScaleView.setScaleSecondaryBackgroundColor(Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)));
                mScaleView.setScaleNumberColor(Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)));
                mScaleView.setScaleTextColor(Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)));
                break;
            case R.id.change_scale_btn:
                mScaleView.setCount(mRandom.nextInt(10));
                break;
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 123){
                int t = msg.arg1;
                Log.d(TAG, "update view " + t);
                mScaleView.setShownValue(t);
                mScaleView.setShowText(t + "");
            }
        }
    };
}
