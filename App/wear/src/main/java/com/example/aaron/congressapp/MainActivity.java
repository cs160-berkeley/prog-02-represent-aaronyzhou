package com.example.aaron.congressapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {

    private TextView mTextView;

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity


    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            //Log.d("oeu", Float.toString(mAccelCurrent));
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            //float delta = 0;
            if(delta > 1000) {
                //mAccelLast = mAccelCurrent;

                //Log.d("van","lou");
                //Intent sendIntent = new Intent(getBaseContext(), WatchToPhone.class);
                //sendIntent.putExtra("DATA", "RANDOM");
                //startService(sendIntent);

                Random rr = new Random();
                int zz = rr.nextInt(90000)+10000;

                String[] nn = new String[]{"James Bond","Jackson Taylor","Joey Wheeler"};
                String[] pp = new String[]{"Democrat","Other","Republican"};
                String[] ii = new String[]{"arnold","willis","stallone"};
                String[] bb = new String[]{"Cool Bill","","Millenium Bill\nWheeler Bill"};
                String[] cc = new String[]{"MI6\nCIA","Cool Committee","Duelist Kingdom"};

                List<String> n = Arrays.asList(nn);
                List<String> p = Arrays.asList(pp);
                List<String> i = Arrays.asList(ii);
                List<String> b = Arrays.asList(bb);
                List<String> c = Arrays.asList(cc);

                final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
                //pager.setAdapter(new RepGridPagerAdapter(this, getFragmentManager()));
                RepGridPagerAdapter r = new RepGridPagerAdapter(MainActivity.this);
                r.setData(n,p,i,b,c,Integer.toString(zz));
                pager.setAdapter(r);
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        //mAccel = 0.00f;
        //mAccelCurrent = SensorManager.GRAVITY_EARTH;
        //mAccelLast = SensorManager.GRAVITY_EARTH;



        Intent intent = getIntent();
        Bundle extras = intent.getExtras();



        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        //pager.setAdapter(new RepGridPagerAdapter(this, getFragmentManager()));
        RepGridPagerAdapter r = new RepGridPagerAdapter(this);

        if (extras != null) {
            String data = extras.getString("DATA");
            if(data != null && data.length() > 0) {
                String[] ddd = data.split("[|]");
                String[] n = ddd[0].split("#");
                String[] p = ddd[1].split("#");
                String[] i = ddd[2].split("#");
                String[] b = ddd[3].split("#");
                String[] c = ddd[4].split("#");
                List<String> names = Arrays.asList(n);
                List<String> parties = Arrays.asList(p);
                List<String> imgN = Arrays.asList(i);
                List<String> bills = Arrays.asList(b);
                List<String> committees = Arrays.asList(c);
                r.setData(names, parties, imgN, bills, committees, ddd[5]);
            }

        }
        pager.setAdapter(r);

        //pager.setAdapter(new CoolAdapter(this, getFragmentManager()));
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}
