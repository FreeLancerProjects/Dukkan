package com.appzone.dukkan.activities_fragments.home_activity.driver_home.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.driver_home.DriverHomeActivity;

public class Fragment_Driver_Orders extends Fragment {

    private DriverHomeActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_driver_orders,container,false);
        initView(view);
        return view;

    }
    public static Fragment_Driver_Orders newInstance()
    {
        return new Fragment_Driver_Orders();
    }

    private void initView(View view) {
        activity = (DriverHomeActivity) getActivity();

    }


}
