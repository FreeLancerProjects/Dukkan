package com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.dukkan.R;

public class Fragment_Client_Profile extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_profile,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Client_Profile newInstance()
    {
        return new Fragment_Client_Profile();
    }
    private void initView(View view) {

    }
}
