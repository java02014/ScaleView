package com.scorpioneal.scaleview;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Fragment mScaleFragment;
    private FragmentTransaction mTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mScaleFragment = new ScaleFragment();

        mTransaction = getFragmentManager().beginTransaction();
        mTransaction.add(R.id.container, mScaleFragment);
        mTransaction.commit();
    }

}
