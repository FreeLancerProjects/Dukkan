package com.appzone.dukkan.activities_fragments.activity_order_details.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.activity_order_details.activity.OrderDetailsActivity;
import com.appzone.dukkan.models.OrdersModel;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Order_Products extends Fragment {
    private static final String TAG = "ORDER";
    private OrderDetailsActivity activity;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private ImageView image_back;
    private LinearLayout ll_back;
    private String current_lang;
    private OrdersModel.Order order;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_products,container,false);
        initView(view);
        return view;
    }
    public static Fragment_Order_Products newInstance(OrdersModel.Order order)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,order);
        Fragment_Order_Products fragment_order_products = new Fragment_Order_Products();
        fragment_order_products.setArguments(bundle);
        return fragment_order_products;
    }
    private void initView(View view)
    {
        activity = (OrderDetailsActivity) getActivity();
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


        image_back = view.findViewById(R.id.image_back);
        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);
        }else
        {
            image_back.setImageResource(R.drawable.arrow_left);

        }
        ll_back = view.findViewById(R.id.ll_back);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
