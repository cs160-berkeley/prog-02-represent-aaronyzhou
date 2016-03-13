package com.example.aaron.congressapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

/**
 * Created by aaron on 3/1/16.
 */
public class RepGridPagerAdapter extends GridPagerAdapter {
    private final Context mContext;
    private List<String> names;
    private List<String> parties;
    private List<String> imgN;
    private List<String> bioguides;
    private List<String> bills;
    private List<String> committees;
    private String zip = "";
    private String county= "";
    private double obama = 0.0;
    private double romney = 0.0;


    public RepGridPagerAdapter(Context ctx) {
        //super(ctx);
        mContext = ctx;
    }

    public void setZip(String z) {
        zip = z;
    }

    public void setData(List<String> n, List<String> p, List<String> i, List<String> b) {//, List<String> c, String z) {
        names = n;
        parties = p;
        imgN = i;
        bioguides = b;
        //committees = c;
        //zip = z;
        notifyDataSetChanged();
    }

    public void setData(List<String> n, List<String> p, List<String> i, List<String> b, String county, double obama, double romney) {
        names = n;
        parties = p;
        imgN = i;
        bioguides = b;
        this.county = county;
        this.obama = obama;
        this.romney = romney;
        notifyDataSetChanged();
    }


    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int i) {
        if(names == null) {return 1; }
        if(county.length() == 0) {return names.size();}
        return names.size()+1;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int i, final int i1) {
        if(names == null || i1 == names.size()) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.vote_results, viewGroup, false);
            TextView v = (TextView) view.findViewById(R.id.district);

            if(county.length() == 0) {
                return view; //die without drawing
            }

            v.setText(county);

            TextView dv = (TextView) view.findViewById(R.id.democrat);
            TextView rv = (TextView) view.findViewById(R.id.republican);
            TextView iv = (TextView) view.findViewById(R.id.other);

            TextView dva = (TextView) view.findViewById(R.id.democrat2);
            TextView rva = (TextView) view.findViewById(R.id.republican2);
            TextView iva = (TextView) view.findViewById(R.id.other2);


            dva.setText(String.format("%.1f", obama));
            rva.setText(String.format("%.1f", romney));
            iva.setText(String.format("%.1f",(100.0-obama-romney)));

            Random ra = new Random();
            int dd = (int) (150.0*obama/100.0);
            int rr = (int) (150.0*romney/100.0);
            int oo = (int) (150.0*(100.0-obama-romney)/100.0);

            dv.setLayoutParams(new LinearLayout.LayoutParams(dd, 30));
            rv.setLayoutParams(new LinearLayout.LayoutParams(rr, 30));
            iv.setLayoutParams(new LinearLayout.LayoutParams(oo, 30));

            viewGroup.addView(view);
            return view;
        } else {


            final View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_rep_card, viewGroup, false);

            ImageView im = (ImageView) view.findViewById(R.id.rep_image);
            TextView rn = (TextView) view.findViewById(R.id.rep_name);
            TextView rp = (TextView) view.findViewById(R.id.rep_party);

            rn.setText(names.get(i1));
            rp.setText(parties.get(i1));

            NetworkUtil.displayImage(imgN.get(i1), im);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("clock", "lololo");

                    Intent sendIntent = new Intent(mContext, WatchToPhone.class);
                    //sendIntent.putExtra("DATA", names.get(i1) + "#" + parties.get(i1) + "#" + bills.get(i1) + "#" + committees.get(i1) + "#" + imgN.get(i1));
                    sendIntent.putExtra("DATA", names.get(i1)+"#"+parties.get(i1)+"#"+imgN.get(i1)+"#"+bioguides.get(i1));
                    mContext.startService(sendIntent);

                }
            });


            viewGroup.addView(view);
            return view;
        }
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int i, int i1, Object o) {
        viewGroup.removeView((View)o);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }
}
