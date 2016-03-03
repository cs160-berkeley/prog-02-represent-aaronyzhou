package com.example.aaron.congressapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class EnterZipcodeFragment extends Fragment {

    public EnterZipcodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_enter_zipcode, container, false);

        final EditText zipcodeEditText = (EditText) v.findViewById(R.id.zipEditText);
        Button zipcodeButton = (Button) v.findViewById(R.id.zipButton);
        Button locationButton = (Button) v.findViewById(R.id.locButton);

        zipcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = zipcodeEditText.getText().toString();

                //Fragment f = (Fragment) new RepresentativeListFragment();

                //getFragmentManager().beginTransaction().add(R.id.main_frame, f).commit();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(zipcodeEditText.getWindowToken(), 0);

                ((MainActivity) getActivity()).useZipCode(s);
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(zipcodeEditText.getWindowToken(), 0);

                ((MainActivity) getActivity()).useZipCode("");
            }
        });



        return v;
    }


}
