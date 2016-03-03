package com.example.aaron.congressapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class RepresentativeDetailFragment extends Fragment {
    public Representative r;
    TextView nameText;
    TextView partyText;

    TextView committeeList;
    TextView billList;

    ImageView repImage;


    public RepresentativeDetailFragment() {
        // Required empty public constructor
    }


    /*
    public static RepresentativeDetailFragment newInstance(Representative r) {
        RepresentativeDetailFragment fragment = new RepresentativeDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_representative_detail, container, false);

        nameText = (TextView) v.findViewById(R.id.rep_name);
        partyText = (TextView) v.findViewById(R.id.rep_party);
        billList = (TextView) v.findViewById(R.id.bill_list);
        committeeList = (TextView) v.findViewById(R.id.committee_list);

        repImage = (ImageView) v.findViewById(R.id.rep_image);

        if(r != null) {
            nameText.setText(r.name);
            partyText.setText(r.party);
            billList.setText(r.bills);
            committeeList.setText(r.comittees);


            int resID = getResources().getIdentifier(r.imgName, "drawable", getActivity().getPackageName());
            repImage.setImageResource(resID);
        }

        return v;
    }

    public void setRepresentative(Representative r) {
        this.r = r;
    }


}
