package com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment.fragment_cart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.client_home.activity.HomeActivity;
import com.appzone.dukkan.tags.Tags;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Payment_Confirmation extends Fragment {

    private ImageView image_arrow_back,image_arrow_continue;
    private TextView tv_total,tv_product_cost,tv_delivery_cost,tv_coupon_cost;
    private CardView card_visa,card_cash;
    private EditText edt_card_number,edt_expire,edt_password;
    private LinearLayout ll_coupon;
    private FrameLayout fl_continue,fl_back;
    private HomeActivity activity;
    private double total_order_cost=0.0,coupon_value=0.0,delivery_cost=0.0;
    private String payment_method="";
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
    private void initView(View view)
    {

        activity = (HomeActivity) getActivity();
        this.total_order_cost = activity.total_order_cost;
        this.payment_method = activity.payment_method;
        try {
            if (!TextUtils.isEmpty(activity.coupon_value))
            {
                this.coupon_value = Double.parseDouble(activity.coupon_value);

            }
            if (!TextUtils.isEmpty(activity.delivery_cost))
            {
                this.delivery_cost = Double.parseDouble(activity.delivery_cost);

            }
        }catch (NumberFormatException e)
        {
            Log.e("error",e.getMessage()+"_");
        }
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
        card_cash = view.findViewById(R.id.card_cash);
        edt_card_number = view.findViewById(R.id.edt_card_number);
        edt_expire = view.findViewById(R.id.edt_expire);
        edt_password = view.findViewById(R.id.edt_password);
        tv_delivery_cost = view.findViewById(R.id.tv_delivery_cost);
        tv_product_cost = view.findViewById(R.id.tv_product_cost);
        tv_total = view.findViewById(R.id.tv_total);
        tv_coupon_cost = view.findViewById(R.id.tv_coupon_cost);
        ll_coupon = view.findViewById(R.id.ll_coupon);
        fl_continue = view.findViewById(R.id.fl_continue);
        fl_back = view.findViewById(R.id.fl_back);


        fl_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.UploadOrder();
            }
        });

        fl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentDelivery_Address();
            }
        });

        updateUI();


    }

    private void updateUI() {

        if (payment_method.equals(Tags.payment_cash))
        {
            card_cash.setVisibility(View.VISIBLE);
            card_visa.setVisibility(View.GONE);
        }else if (payment_method.equals(Tags.payment_visa))
        {
            card_visa.setVisibility(View.VISIBLE);
            card_cash.setVisibility(View.GONE);
        }

        if (coupon_value!=0.0)
        {
            ll_coupon.setVisibility(View.VISIBLE);
            tv_coupon_cost.setText(String.valueOf(coupon_value)+" "+getString(R.string.rsa));


        }else
            {
                ll_coupon.setVisibility(View.GONE);
            }

        tv_delivery_cost.setText(String.valueOf(delivery_cost)+" "+getString(R.string.rsa));

        tv_product_cost.setText(String.valueOf(total_order_cost)+" "+getString(R.string.rsa));
        double discount = total_order_cost-coupon_value;
        double total_order_price = discount+delivery_cost;
        tv_total.setText(String.valueOf(total_order_price)+" "+getString(R.string.rsa));

    }


}
