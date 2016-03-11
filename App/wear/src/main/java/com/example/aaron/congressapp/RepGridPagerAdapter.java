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

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int i) {
        if(names == null) {return 1; }
        return names.size()+1;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int i, final int i1) {
        if(names == null || i1 == names.size()) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.vote_results, viewGroup, false);
            TextView v = (TextView) view.findViewById(R.id.district);

            if(zip.length() == 0) {
                return view; //die without drawing
            }

            v.setText(zip);

            View dv = view.findViewById(R.id.democrat);
            View rv = view.findViewById(R.id.republican);
            View iv = view.findViewById(R.id.other);

            Random ra = new Random();
            int dd = ra.nextInt(80);
            int rr = Math.min(ra.nextInt(80), 120 - dd);
            int oo = 120 - dd - rr;

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

            int resID = mContext.getResources().getIdentifier(imgN.get(i1), "drawable", mContext.getPackageName());

            im.setImageResource(resID);

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
