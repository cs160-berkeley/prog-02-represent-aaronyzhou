package com.example.aaron.congressapp;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by aaron on 3/7/16.
 */
public class NetworkUtil {
    final static String DEBUG_TAG = "NETWORK";

    public static String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        //Reader reader = null;
        //reader = new InputStreamReader(stream, "UTF-8"); //hopefully utf-8 is enough for this class
        //char[] buffer = new char[len];
        //reader.read(buffer);
        //return new String(buffer);
        java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    //private static AsyncHttpClient client = new AsyncHttpClient();
    public static String downloadUrl(String myurl) {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 999;

        try {

            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            return readIt(is, len);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (Exception e) {
            Log.d(DEBUG_TAG, e.toString());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    Log.d("lol","hahahah");
                }
            }
        }
        return "";
    }

    public static void displayTweet(final String username, final TextView textView) {
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> appSessionResult) {
                AppSession session = appSessionResult.data;
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(session);
                twitterApiClient.getStatusesService().userTimeline(null, username, 1, null, null, false, false, false, true, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> listResult) {
                        for (Tweet tweet : listResult.data) {
                            Log.d("fabricstuff", "result: " + tweet.text + "  " + tweet.createdAt);
                            textView.setText(tweet.text);
                        }
                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });
    }

    public static void displayImage(String url, ImageView imageView)
    {
        /*
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else
        {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }*/
        try {
            Bitmap b = new ImageTask().execute(url).get();
            imageView.setImageBitmap(b);
        } catch (Exception e) {
            Log.d("lolo","oeou");
        }
    }

    public static void getBills(String bioguide, TextView textView) {

        try {
            String url = "https://congress.api.sunlightfoundation.com/bills?sponsor_id=" + bioguide + "&apikey=3fc2abac58d44114a7fca035026d647f";
            String s = new SunlightTask().execute(url).get();
            JSONObject r = new JSONObject(s);
            JSONArray results = r.getJSONArray("results");
            String b = "";
            for(int i=0;i<Math.max(5, results.length());i++) {
                JSONObject jr = results.getJSONObject(i);
                if(jr.has("short_title") && !JSONObject.NULL.equals(jr.get("short_title")))
                    b = b + jr.getString("short_title")+"\n";
            }
            textView.setText(b);
        } catch (Exception e) {
            Log.d("lolo", e.toString());
        }
    }

    public static void getCommittees(String bioguide, TextView textView) {
        try {
            String url = "https://congress.api.sunlightfoundation.com/committees?member_ids=" + bioguide + "&apikey=3fc2abac58d44114a7fca035026d647f";
            String s = new SunlightTask().execute(url).get();
            JSONObject r = new JSONObject(s);
            Log.d("abc", s);
            JSONArray results = r.getJSONArray("results");
            String b = "";
            for(int i=0;i<Math.max(5, results.length());i++) {
                JSONObject jr = results.getJSONObject(i);
                b = b + jr.getString("name")+"\n";
            }
            textView.setText(b);

        } catch (Exception e) {
            Log.d(DEBUG_TAG, e.toString());
        }
    }

    private static class SunlightTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            return downloadUrl(params[0]);
        }
    }

    private static class ImageTask extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(params[0]).getContent());
                //holder.i.setImageBitmap(bitmap);
                return bitmap;
            } catch (Exception e) {
                Log.d("lol",e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

        }
    }
}
