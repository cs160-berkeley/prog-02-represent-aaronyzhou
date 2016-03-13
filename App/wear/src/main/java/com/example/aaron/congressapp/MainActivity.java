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

import org.json.JSONArray;
import org.json.JSONObject;

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
            //Log.d("dleta", Float.toString(delta));
            if(delta < -1000) {
                //mAccelLast = mAccelCurrent;

                //Log.d("van","lou");
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhone.class);
                sendIntent.putExtra("DATA", "RANDOM");
                startService(sendIntent);



                //final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
                //pager.setAdapter(new RepGridPagerAdapter(this, getFragmentManager()));
                //RepGridPagerAdapter r = new RepGridPagerAdapter(MainActivity.this);
                //r.setData(n,p,i,b,c,Integer.toString(zz));
                //pager.setAdapter(r);
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
            Log.d("eou",data);
            for(String l: data.split("\n")) {
                Log.d("e", l);
            }

            List<String> ii = new ArrayList<String>();
            List<String> n = new ArrayList<String>();
            List<String> b = new ArrayList<String>();
            List<String> p = new ArrayList<String>();
            Double obama = 0.0;
            Double romney = 0.0;
            String county = "";


            try {
                JSONObject robot = new JSONObject(data);
                JSONObject r0 = robot.getJSONObject("r0");
                JSONObject r1 = r0.getJSONObject("r1");

                county = robot.getString("r4");
                JSONObject r3 = robot.getJSONObject("r3");
                obama = r3.getDouble("obama");
                romney = r3.getDouble("romney");

                JSONArray res = r1.getJSONArray("results");
                for (int i = 0; i < res.length(); i++) {
                    JSONObject o = res.getJSONObject(i);
                    n.add(o.getString("title")+". " + o.getString("first_name")+" "+o.getString("last_name"));
                    String party = o.getString("party");
                    if (party.equals("D")) {
                        p.add("Democrat");
                    } else if (party.equals("R")) {
                        p.add("Republican");
                    } else {
                        p.add("Independent");
                    }
                    b.add(o.getString("bioguide_id"));

                    ii.add("https://theunitedstates.io/images/congress/450x550/" + o.getString("bioguide_id") + ".jpg");

                }
            } catch (Exception e) {
                Log.d("oeu",e.toString());
            }
            r.setData(n, p, ii, b, county, obama, romney);

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
