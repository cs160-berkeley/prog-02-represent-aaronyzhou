package com.example.aaron.congressapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by aaron on 3/11/16.
 */
public class NetworkUtil {

    public static void displayImage(String url, ImageView imageView)
    {
        try {
            Log.d("abc", url);
            Bitmap b = new ImageTask().execute(url).get();
            imageView.setImageBitmap(b);
        } catch (Exception e) {
            Log.d("lolo","fail: " + url);
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
                Log.d("lol", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

        }
    }
}
