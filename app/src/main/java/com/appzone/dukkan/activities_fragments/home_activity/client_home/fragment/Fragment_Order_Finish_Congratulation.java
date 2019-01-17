package com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.client_home.activity.HomeActivity;

public class Fragment_Order_Finish_Congratulation extends Fragment {
    public static final String TAG = "order_id";
    private TextView tv_order_id;
    private FrameLayout fl_follow_order,fl_back;
    private HomeActivity activity;
    private String order_id = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_finished_congratulation,container,false);
        initView(view);
        return view;
    }


    public static Fragment_Order_Finish_Congratulation newInstance(String order_id)
    {
        Bundle bundle = new Bundle();
        bundle.putString(TAG,order_id);
        Fragment_Order_Finish_Congratulation fragment_order_finish_congratulation = new Fragment_Order_Finish_Congratulation();
        fragment_order_finish_congratulation.setArguments(bundle);
        return fragment_order_finish_congratulation;
    }

    private void initView(View view) {
        activity= (HomeActivity) getActivity();

        tv_order_id = view.findViewById(R.id.tv_order_id);
        fl_follow_order = view.findViewById(R.id.fl_follow_order);
        fl_back = view.findViewById(R.id.fl_back);

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            order_id = bundle.getString(TAG);
            UpdateUI(order_id);
        }

        fl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        fl_follow_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void UpdateUI(String order_id) {
        tv_order_id.setText(getString(R.string.your_order_successfully_confirmed_your_order_number_is)+" #"+order_id);

    }
}
