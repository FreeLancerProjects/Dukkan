package com.appzone.dukkan.activities_fragments.home_activity.driver_home.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.dukkan.R;


public class Fragment_Profile extends Fragment {


     public static Fragment_Profile newInstance() {

        return new Fragment_Profile();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


}
