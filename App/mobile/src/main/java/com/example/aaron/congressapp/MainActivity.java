package com.example.aaron.congressapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null && extras.containsKey("DATA")) {

            String s = extras.get("DATA").toString();
            //Log.d("oeu", extras.get("DATA").toString());
            if(s.equals("RANDOM")) {
                Random r = new Random();
                int z = r.nextInt(89999) + 10000;
                useZipCode(Integer.toString(z));
            }
            else if(s!=null) {
                String[] ddd = s.split("#");
                Representative r = new Representative(ddd[0]);
                r.party = ddd[1];
                r.bills = ddd[2];
                r.comittees = ddd[3];
                r.imgName = ddd[4];
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

    public void detailRepresentative(Representative r) {
        //Fragment f = (Fragment) RepresentativeDetailFragment.newInstance(r);
        RepresentativeDetailFragment f = new RepresentativeDetailFragment();
        f.setRepresentative(r);
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, f).addToBackStack("abc").commit();
    }
}
