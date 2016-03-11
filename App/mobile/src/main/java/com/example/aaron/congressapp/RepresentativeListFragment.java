package com.example.aaron.congressapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aaron on 2/29/16.
 */
public class RepresentativeListFragment extends Fragment {
    String s = "";
    Double lo = 0d;
    Double la = 0d;
    RepresentativeAdapter adapter;
    ListView l;

    final ArrayList<Representative> reps = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        s = getArguments().getString("zipcode", "");
        lo = getArguments().getDouble("lng", 0d);
        la = getArguments().getDouble("lat", 0d);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rep_list, null);


        TextView t = (TextView) view.findViewById(R.id.coolId);

        String url = "";
        if(s.length() == 0) {
            t.setText("Representatives for your location");
            //s = "00000";
            url = "https://congress.api.sunlightfoundation.com/legislators/locate?apikey=3fc2abac58d44114a7fca035026d647f&longitude=" + Double.toString(lo) + "&latitude=" + Double.toString(la);
        } else {
            t.setText("Representatives for " + s);
            url = "https://congress.api.sunlightfoundation.com/legislators/locate?apikey=3fc2abac58d44114a7fca035026d647f&zip="+s;
        }

        String sss = "";
        try {
            sss = new DownloadTask().execute(url).get();
        } catch(Exception e)
        {
            Log.d("wtf", e.toString());
        }
        Log.d("eueueu",sss);
        //Intent sendIntent = new Intent(getActivity().getBaseContext(), PhoneToWatch.class);
        //sendIntent.putExtra("DATA", sss);
        //getActivity().startService(sendIntent);

        l = (ListView) view.findViewById(R.id.repList);

        /*
        ArrayList<Representative> list = new ArrayList<Representative>();
        Representative r = new Representative("Sen. John Tremain");
        Representative rr = new Representative("Sen. Joe Camel");
        Representative rrr = new Representative("Rep. Jax Truesight");

        r.imgName = "arnold";
        r.email = "John@john.com";
        r.website = "john.com";
        r.tweet = "John is now on twitter";
        r.years = "2012-2016";
        r.party = "Democrat";
        r.bills = "Bill For Better Food\nCool Movie Bill";
        r.comittees = "John's Committee\nAndroid Design Committee\nCommittee of Doom";

        rr.imgName = "stallone";
        rr.email = "Joe@joe.com";
        rr.website = "joe.com";
        rr.tweet = "Joe is now on twitter";
        rr.years = "2012-2016";
        rr.party = "Republican";
        rr.bills = "Joe's Coffee Bill";
        rr.comittees = "Tech Committee\nHeart Surgery Committee";

        rrr.imgName = "willis";
        rrr.email = "Jax@truesight.com";
        rrr.website = "jaxforpresident.com";
        rrr.tweet = "Vote me for president cuz I'm the coolest";
        rrr.years = "1990-2012";
        rrr.party = "Democrat";
        rrr.bills = "Phone Bill\nBill Gates";
        rrr.comittees = "None";


        list.add(r);
        list.add(rr);
        list.add(rrr);


        //ArrayList<String> names = new ArrayList<>();
        //ArrayList<String> parties = new ArrayList<>();
        //ArrayList<String> imgN = new ArrayList<>();

        //names.add(r.name);
        //names.add(rr.name);
        //names.add(rrr.name);

        //parties.add(r.party);
        //parties.add(rr.party);
        //parties.add(rrr.party);

        //imgN.add(r.imgName);
        //imgN.add(rr.imgName);
        //imgN.add(rrr.imgName);
        String du = r.name + "#" + rr.name + "#" + rrr.name + "|" + r.party + "#" + rr.party + "#" + rrr.party + "|" + r.imgName + "#" + rr.imgName + "#" + rrr.imgName + "|";
        du = du + r.bills + "#" + rr.bills + "#" + rrr.bills + "|";
        du = du + r.comittees + "#" + rr.comittees + "#" + rrr.comittees;
        du = du + "|" + s;

        Intent sendIntent = new Intent(getActivity().getBaseContext(), PhoneToWatch.class);
        //sendIntent.putExtra("NAMES", names);
        //sendIntent.putExtra("PARTIES", parties);
        //sendIntent.putExtra("IMGN", imgN);
        //sendIntent.putExtra("llo", "LOL");
        sendIntent.putExtra("DATA", du);
        getActivity().startService(sendIntent);





        adapter = new RepresentativeAdapter(getActivity());
        adapter.setData(list);
        l.setAdapter(adapter);

        */
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Representative r = (Representative) adapter.getItem(position);
                ((MainActivity) getActivity()).detailRepresentative(r);
            }
        });


        return view;
    }

    public static RepresentativeListFragment newInstance(String zipcode) {
        RepresentativeListFragment myFragment = new RepresentativeListFragment();

        Bundle args = new Bundle();
        args.putString("zipcode", zipcode);
        myFragment.setArguments(args);

        return myFragment;
    }

    public static RepresentativeListFragment newInstance(Double la, Double lo) {
        RepresentativeListFragment myFragment = new RepresentativeListFragment();

        Bundle args = new Bundle();
        args.putDouble("lng", lo);
        args.putDouble("lat", la);
        myFragment.setArguments(args);

        return myFragment;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
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
            reps.clear();
            adapter = new RepresentativeAdapter(getActivity());
            try {
                JSONObject r = new JSONObject(result);
                JSONArray res = r.getJSONArray("results");
                for(int i=0;i<res.length();i++) {
                    JSONObject o = res.getJSONObject(i);
                    Representative robert = new Representative(o.getString("title")+". " + o.getString("first_name")+" "+o.getString("last_name"));
                    String party = o.getString("party");
                    if(party.equals("D")) {
                        robert.party = "Democrat";
                    } else if(party.equals("R")) {
                        robert.party = "Republican";
                    } else {
                        robert.party = "Independent";
                    }
                    robert.website = o.getString("website");
                    robert.email = o.getString("oc_email");
                    //
                    robert.bioguide = o.getString("bioguide_id");
                    robert.years = o.getString("term_end");
                    robert.imgName = "https://theunitedstates.io/images/congress/450x550/"+o.getString("bioguide_id")+".jpg";
                    robert.tweet = o.getString("twitter_id");
                    reps.add(robert);
                }
                adapter.setData(reps);
                l.setAdapter(adapter);
            } catch (Exception e) {
                Log.d("abc", e.toString());
                Log.d("abc","counter");
            }
        }
    }
}
