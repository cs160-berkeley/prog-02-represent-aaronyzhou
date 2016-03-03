package com.example.aaron.congressapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aaron on 2/29/16.
 */
public class RepresentativeListFragment extends Fragment {
    String s = "";
    RepresentativeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        s = getArguments().getString("zipcode", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rep_list, null);


        TextView t = (TextView) view.findViewById(R.id.coolId);

        if(s.length() == 0) {
            t.setText("Representatives for your location");
            s = "00000";
        } else {
            t.setText("Representatives for " + s);
        }

        ListView l = (ListView) view.findViewById(R.id.repList);

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
}
