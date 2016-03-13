package com.example.aaron.congressapp;

import android.content.Intent;
import android.content.pm.PackageManager;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        String countyUrl = "";

        String gkey = "";
        try {
            gkey = getActivity()
                    .getPackageManager()
                    .getApplicationInfo(getActivity().getPackageName(), PackageManager.GET_META_DATA)
                    .metaData.getString("com.google.android.geo.API_KEY");
        } catch (Exception e) {
            Log.d("a","b");
        }

        if(s.length() == 0) {
            t.setText("Representatives for your location");
            url = "https://congress.api.sunlightfoundation.com/legislators/locate?apikey=3fc2abac58d44114a7fca035026d647f&longitude=" + Double.toString(lo) + "&latitude=" + Double.toString(la);
            countyUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng"+Double.toString(la)+","+Double.toString(lo)+"&key=" + gkey;
        } else {
            t.setText("Representatives for " + s);
            url = "https://congress.api.sunlightfoundation.com/legislators/locate?apikey=3fc2abac58d44114a7fca035026d647f&zip="+s;
            countyUrl = "https://maps.googleapis.com/maps/api/geocode/json?address="+s+"&key=" + gkey;
        }



        try {
            new DownloadTask().execute(url, countyUrl);
        } catch(Exception e)
        {
            Log.d("wtf", e.toString());
        }
        //Log.d("eueueu", sss);


        l = (ListView) view.findViewById(R.id.repList);


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

                String r1 = NetworkUtil.downloadUrl(url[0]);
                String r2 = NetworkUtil.downloadUrl(url[1]);
                return "{r1:"+r1+",r2:"+r2+"}";
            } catch (Exception e) {
                Log.d("ttttt",e.toString());
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            reps.clear();
            adapter = new RepresentativeAdapter(getActivity());
            try {

                JSONObject r = new JSONObject(result);


                JSONObject r2 = r.getJSONObject("r2");
                //Log.d("oeuo",r2.toString());

                String sonomo = "";
                JSONArray r2r = r2.getJSONArray("results");

                if(r2r.length() > 0) {
                    JSONObject rosalyn = r2r.getJSONObject(0);
                    JSONArray adc = rosalyn.getJSONArray("address_components");
                    for (int i = 0; i < adc.length(); i++) {
                        JSONObject lobo = adc.getJSONObject(i);
                        JSONArray type = lobo.getJSONArray("types");
                        if (type.getString(0).equals("administrative_area_level_2")) {
                            sonomo = lobo.getString("long_name");
                        }
                    }
                }
                Log.d("sonomo", sonomo);
                InputStream initium = getActivity().getAssets().open("cool_election_results_2012.json");
                BufferedReader rober = new BufferedReader(new InputStreamReader(initium));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = rober.readLine()) != null)
                    responseStrBuilder.append(inputStr);

                JSONObject electionResults = new JSONObject(responseStrBuilder.toString());

                JSONObject momomom = new JSONObject("{obama: 0,romney: 0}");
                if(electionResults.has(sonomo)) {
                    momomom = electionResults.getJSONObject(sonomo);
                }

                Log.d("mon", momomom.toString());




                JSONObject r1 = r.getJSONObject("r1");
                JSONArray res = r1.getJSONArray("results");
                if(res.length() > 0) {
                    for (int i = 0; i < res.length(); i++) {
                        JSONObject o = res.getJSONObject(i);
                        Representative robert = new Representative(o.getString("title") + ". " + o.getString("first_name") + " " + o.getString("last_name"));
                        String party = o.getString("party");
                        if (party.equals("D")) {
                            robert.party = "Democrat";
                        } else if (party.equals("R")) {
                            robert.party = "Republican";
                        } else {
                            robert.party = "Independent";
                        }
                        robert.website = o.getString("website");
                        robert.email = o.getString("oc_email");
                        //
                        robert.bioguide = o.getString("bioguide_id");
                        robert.years = o.getString("term_end");
                        robert.imgName = "https://theunitedstates.io/images/congress/450x550/" + o.getString("bioguide_id") + ".jpg";
                        robert.tweet = o.getString("twitter_id");
                        reps.add(robert);
                    }
                }

                Intent sendIntent = new Intent(getActivity().getBaseContext(), PhoneToWatch.class);

                String allStuff = "{r0:"+result+",r3:"+momomom+",r4:\"" + sonomo+"\"}";

                //Log.d("al",allStuff);
                sendIntent.putExtra("DATA", allStuff);
                //sendIntent.putExtra("COUNTY", sonomo);
                //sendIntent.putExtra("POLL", momomom.toString());
                getActivity().startService(sendIntent);



                adapter.setData(reps);
                l.setAdapter(adapter);
            } catch (Exception e) {
                adapter.setData(reps);
                l.setAdapter(adapter);
                Log.d("abc", e.toString());
            }
        }
    }
}
