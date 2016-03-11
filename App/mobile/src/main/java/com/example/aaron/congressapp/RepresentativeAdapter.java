package com.example.aaron.congressapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by aaron on 2/29/16.
 */

class RepHolder {
    TextView v;
    TextView ev;
    TextView wv;
    TextView tv;
    TextView pv;
    ImageView i;
}


public class RepresentativeAdapter extends BaseAdapter {
    List<Representative> data;
    private LayoutInflater mInflater;
    private Context context;

    public RepresentativeAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    public void setData(List<Representative> d) {
        data = d;
        Log.d("oeu","soapdata");
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RepHolder holder;

        if (convertView == null || convertView.getTag() == null) {
            convertView = mInflater.inflate(R.layout.representative_cell, null);
            holder = new RepHolder();
            holder.v  = (TextView) convertView.findViewById(R.id.rep_cell_name);
            holder.ev = (TextView) convertView.findViewById(R.id.rep_email);
            holder.wv = (TextView) convertView.findViewById(R.id.rep_website);
            holder.tv = (TextView) convertView.findViewById(R.id.rep_tweet);
            holder.pv = (TextView) convertView.findViewById(R.id.rep_party);
            holder.i = (ImageView) convertView.findViewById(R.id.rep_cell_image);

            convertView.setTag(holder);
        } else {
            holder = (RepHolder) convertView.getTag();
        }
        Representative r = data.get(position);
        holder.v.setText(r.name);
        holder.ev.setText(r.email);

        holder.wv.setText(r.website);
        holder.pv.setText(r.party);

        if(r.party.equals("Democrat")) {
            holder.pv.setBackgroundColor(ContextCompat.getColor(context,R.color.democrat));
        } else if(r.party.equals("Republican")) {
            holder.pv.setBackgroundColor(ContextCompat.getColor(context,R.color.republican));
        } else {
            holder.pv.setBackgroundColor(ContextCompat.getColor(context,R.color.other));
        }

        //int resID = context.getResources().getIdentifier(data.get(position).imgName, "drawable", context.getPackageName());
        //holder.i.setImageResource(resID);


        if(r.tweet != null) {
            NetworkUtil.displayTweet(r.tweet, holder.tv);
        }
        //holder.tv.setText(r.tweet);


        NetworkUtil.displayImage(data.get(position).imgName, holder.i);
        return convertView;
    }



}


