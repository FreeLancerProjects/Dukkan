package com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.client_home.activity.HomeActivity;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Offers extends Fragment {

    private LinearLayout ll_back;
    private ImageView image_back;
    private ViewPager pager;
    private TabLayout tab;
    private Spinner spinner_arrangement;
    private String [] spinner_array;
    private HomeActivity homeActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Offers newInstance()
    {
        return new Fragment_Offers();
    }
    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        ll_back = view.findViewById(R.id.ll_back);
        image_back = view.findViewById(R.id.image_back);
        pager = view.findViewById(R.id.pager);
        tab = view.findViewById(R.id.tab);
        spinner_arrangement = view.findViewById(R.id.spinner_arrangement);

        Paper.init(getActivity());
        String current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);
        }else
        {
            image_back.setImageResource(R.drawable.arrow_left);

        }

        //////////////////////////////////////////////////////////////////

        spinner_array = getResources().getStringArray(R.array.sort_array);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(getActivity(),R.layout.spinner_row,spinner_array);
        spinner_arrangement.setAdapter(spinner_adapter);

        spinner_arrangement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position==0)
                {
                    try {
                        ((TextView)spinner_arrangement.getSelectedView()).setTextColor(ContextCompat.getColor(getActivity(),R.color.gray_text));
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }else
                    {
                        try {
                            ((TextView)spinner_arrangement.getSelectedView()).setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //////////////////////////////////////////////////////////////////
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.Back();
            }
        });

    }
}
