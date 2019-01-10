package com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment.fragment_cart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.client_home.activity.HomeActivity;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Payment_Confirmation extends Fragment {

    private ImageView image_arrow_back,image_arrow_continue;
    private TextView tv_total,tv_product_cost,tv_delivery_cost;
    private CardView card_visa;
    private EditText edt_card_number,edt_expire,edt_password;
    private FrameLayout fl_continue,fl_back;
    private String payment_method ="";
    private HomeActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_confirmation,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Payment_Confirmation newInstance()
    {
        return new Fragment_Payment_Confirmation();
    }
    private void initView(View view) {

        activity = (HomeActivity) getActivity();

        image_arrow_back = view.findViewById(R.id.image_arrow_back);
        image_arrow_continue = view.findViewById(R.id.image_arrow_continue);

        String current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (current_lang.equals("ar"))
        {
            image_arrow_back.setImageResource(R.drawable.arrow_right);
            image_arrow_continue.setImageResource(R.drawable.arrow_left);
        }else
        {
            image_arrow_back.setImageResource(R.drawable.arrow_left);
            image_arrow_continue.setImageResource(R.drawable.arrow_right);
        }
        card_visa = view.findViewById(R.id.card_visa);
        edt_card_number = view.findViewById(R.id.edt_card_number);
        edt_expire = view.findViewById(R.id.edt_expire);
        edt_password = view.findViewById(R.id.edt_password);
        tv_delivery_cost = view.findViewById(R.id.tv_delivery_cost);
        tv_product_cost = view.findViewById(R.id.tv_product_cost);
        tv_total = view.findViewById(R.id.tv_total);
        fl_continue = view.findViewById(R.id.fl_continue);
        fl_back = view.findViewById(R.id.fl_back);


        fl_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        fl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentDelivery_Address();
            }
        });


    }
}
