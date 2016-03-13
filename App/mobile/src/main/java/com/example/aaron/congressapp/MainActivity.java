package com.example.aaron.congressapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.json.JSONArray;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "a5vpkHDjvjbaGoLiREMk0D4Tz";
    private static final String TWITTER_SECRET = "S36moxNR9IWJkqQFl4sneGi1AKeV6kHHwiYsO5igZhTfJGveWw";


    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey("DATA")) {
        //if(true) {
            String s = extras.get("DATA").toString();
            //String s = "RANDOM";
            if (s.equals("RANDOM")) {

                try {
                    InputStream initium = getAssets().open("us_postal_codes.csv");
                    BufferedReader rober = new BufferedReader(new InputStreamReader(initium));
                    StringBuilder responseStrBuilder = new StringBuilder();

                    ArrayList<String> z = new ArrayList<>(44444);
                    String inputStr;
                    while ((inputStr = rober.readLine()) != null)
                        z.add(inputStr);
                    Random r = new Random();
                    int results = 0;
                    String a = "";
                    while(results == 0) {
                        a = z.get(r.nextInt(z.size()));
                        String url = "https://congress.api.sunlightfoundation.com/legislators/locate?apikey=3fc2abac58d44114a7fca035026d647f&zip="+a;
                        Integer ueu = new NT().execute(url).get();
                        results = ueu.intValue();
                    }
                    useZipCode(a);
                } catch (Exception e)
                {
                    Log.d("ou",e.toString());
                }


            } else if (s != null) {
                String[] ddd = s.split("#");
                Representative r = new Representative(ddd[0]);
                r.party = ddd[1];
                //r.bills = ddd[2];
                //r.comittees = ddd[3];
                r.imgName = ddd[2];
                r.bioguide = ddd[3];
                RepresentativeDetailFragment f = new RepresentativeDetailFragment();
                f.setRepresentative(r);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, f).addToBackStack("cat").commit();

            }
        } else {
            Fragment f = (Fragment) new EnterZipcodeFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.main_frame, f).commit();
        }

    }

    public void useZipCode(String zipcode) {
        Fragment f = (Fragment) RepresentativeListFragment.newInstance(zipcode);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, f).addToBackStack("lol").commit();
    }

    public void useLocation() {
        if (mLastLocation != null) {
            //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            double lo = mLastLocation.getLongitude();
            double la = mLastLocation.getLatitude();
            Log.d("wtf", Double.toString(lo)+","+Double.toString(la));

            Fragment f = (Fragment) RepresentativeListFragment.newInstance(la, lo);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, f).addToBackStack("lol").commit();
        }
    }

    private class DownloadTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... url) {

            // params comes from the execute() call: params[0] is the url.
            try {
                Log.d("imumum", url[0]);

                return NetworkUtil.downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("ttttt",e.toString());
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);
            Log.d("LOL",result);
        }
    }

    public void detailRepresentative(Representative r) {
        //Fragment f = (Fragment) RepresentativeDetailFragment.newInstance(r);
        RepresentativeDetailFragment f = new RepresentativeDetailFragment();
        f.setRepresentative(r);
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, f).addToBackStack("abc").commit();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("ABC","DEF");
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
            double lo = mLastLocation.getLongitude();
            double la = mLastLocation.getLatitude();
            Log.d("wtf", Double.toString(lo)+","+Double.toString(la));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private class NT extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... url) {
            try {
                Log.d("imumum", url[0]);

                String r1 = NetworkUtil.downloadUrl(url[0]);
                Log.d("eu",r1);
                JSONObject o = new JSONObject(r1);
                JSONArray res = o.getJSONArray("results");
                return new Integer(res.length());
            } catch (Exception e) {
                Log.d("ttttt",e.toString());
                return new Integer(0);
            }
        }

    }
}
