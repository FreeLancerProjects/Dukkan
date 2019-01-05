package com.appzone.dukkan.activities_fragments.home_activity.fragment.fragment_cart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.activity.HomeActivity;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Review_Purchases extends Fragment {

    private ImageView image_arrow;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private TextView tv_tax,tv_product_cost,tv_total;
    private FrameLayout fl_continue;
    private HomeActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_purshases,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Review_Purchases newInstance()
    {
        return new Fragment_Review_Purchases();
    }
    private void initView(View view) {
        activity = (HomeActivity) getActivity();

        image_arrow = view.findViewById(R.id.image_arrow);
        String current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (current_lang.equals("ar"))
        {
            image_arrow.setImageResource(R.drawable.arrow_right);
        }else
        {
            image_arrow.setImageResource(R.drawable.arrow_left);

        }
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        tv_tax = view.findViewById(R.id.tv_tax);
        tv_product_cost = view.findViewById(R.id.tv_product_cost);
        tv_total = view.findViewById(R.id.tv_total);
        fl_continue = view.findViewById(R.id.fl_continue);


        fl_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentDelivery_Address();
            }
        });


    }
}
