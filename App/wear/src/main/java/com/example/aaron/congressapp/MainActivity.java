package com.example.aaron.congressapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    //private static final String TWITTER_KEY = "coeuhknIKuleBtQEq0EL5Xxyx";
    //private static final String TWITTER_SECRET = "4tgveLccI6mt30FabjTbGz2Aasjk2MHGxW6stNEOFuKzEmbg95";



    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private GoogleApiClient mGoogleApiClient;




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

                Log.d("van","lou");
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
        //TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        //Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

        /*
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                //.addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    */

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        //mAccel = 0.00f;
        //mAccelCurrent = SensorManager.GRAVITY_EARTH;
        //mAccelLast = SensorManager.GRAVITY_EARTH;



        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        Log.d("flot","oc");

        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        //pager.setAdapter(new RepGridPagerAdapter(this, getFragmentManager()));
        RepGridPagerAdapter r = new RepGridPagerAdapter(this);

        if (extras != null) {
            String data = extras.getString("DATA");
            Log.d("oeu","khkhkhk");
            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> parties = new ArrayList<>();
            ArrayList<String> imgN = new ArrayList<>();
            ArrayList<String> bioguides = new ArrayList<>();
            try {
                JSONObject robot = new JSONObject(data);
                JSONArray res = robot.getJSONArray("results");
                for (int i = 0; i < res.length(); i++) {
                    JSONObject o = res.getJSONObject(i);
                    names.add(o.getString("title") + ". " + o.getString("first_name") + " " + o.getString("last_name"));
                    String party = o.getString("party");
                    if (party.equals("D")) {
                        parties.add("Democrat");
                    } else if (party.equals("R")) {
                        parties.add("Republican");
                    } else {
                        parties.add("Independent");
                    }
                    imgN.add("https://theunitedstates.io/images/congress/450x550/" + o.getString("bioguide_id") + ".jpg");
                    bioguides.add(o.getString("bioguide_id"));

                }
            } catch (Exception e) {
                Log.d("foo",e.toString());
            }
            r.setData(names, parties, imgN, bioguides);

        }
        pager.setAdapter(r);

        //pager.setAdapter(new CoolAdapter(this, getFragmentManager()));
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        //mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        //mGoogleApiClient.disconnect();
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
