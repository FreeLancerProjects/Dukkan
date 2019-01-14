package com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment.fragment_cart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.appzone.dukkan.models.UserModel;
import com.appzone.dukkan.singletone.UserSingleTone;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Delivery_Address extends Fragment {

    private ImageView image_arrow,image_arrow2,image_arrow_back,image_arrow_continue,image_visa,image_mada,image_cash;
    private EditText edt_first_name,edt_last_name,edt_phone,edt_street,edt_feedback,edt_coupon;
    private FrameLayout fl_choose_address,fl_continue,fl_back,fl_date;
    private TextView tv_address,tv_time,tv_cash;
    private LinearLayout ll_visa, ll_mada, ll_cash;
    private String payment_method="";
    private HomeActivity activity;
    private String time_type="",delivery_cost="";
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private String address ="";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_address,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Delivery_Address newInstance()
    {
        return new Fragment_Delivery_Address();
    }
    private void initView(View view) {

        activity = (HomeActivity) getActivity();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();

        image_arrow = view.findViewById(R.id.image_arrow);
        image_arrow2 = view.findViewById(R.id.image_arrow2);
        image_arrow_back = view.findViewById(R.id.image_arrow_back);
        image_arrow_continue = view.findViewById(R.id.image_arrow_continue);

        String current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (current_lang.equals("ar"))
        {
            image_arrow.setImageResource(R.drawable.arrow_right);
            image_arrow2.setImageResource(R.drawable.arrow_right);
            image_arrow_back.setImageResource(R.drawable.arrow_right);
            image_arrow_continue.setImageResource(R.drawable.arrow_left);

        }else
        {
            image_arrow.setImageResource(R.drawable.arrow_left);
            image_arrow2.setImageResource(R.drawable.arrow_left);
            image_arrow_back.setImageResource(R.drawable.arrow_left);
            image_arrow_continue.setImageResource(R.drawable.arrow_right);

        }

        image_visa = view.findViewById(R.id.image_visa);
        image_mada = view.findViewById(R.id.image_mada);
        image_cash = view.findViewById(R.id.image_cash);
        ll_visa = view.findViewById(R.id.ll_visa);
        ll_mada = view.findViewById(R.id.ll_mada);
        ll_cash = view.findViewById(R.id.ll_cash);
        fl_date = view.findViewById(R.id.fl_date);
        tv_cash = view.findViewById(R.id.tv_cash);


        edt_first_name = view.findViewById(R.id.edt_first_name);
        edt_last_name = view.findViewById(R.id.edt_last_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_street = view.findViewById(R.id.edt_street);
        edt_feedback = view.findViewById(R.id.edt_feedback);
        edt_coupon = view.findViewById(R.id.edt_coupon);

        fl_choose_address = view.findViewById(R.id.fl_choose_address);
        fl_continue = view.findViewById(R.id.fl_continue);
        fl_back = view.findViewById(R.id.fl_back);
        tv_address = view.findViewById(R.id.tv_address);
        tv_time = view.findViewById(R.id.tv_time);



       /* ll_visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_visa.setImageResource(R.drawable.visa_whit);
                ll_visa.setBackgroundResource(R.drawable.selected_payment);

                image_mada.setImageResource(R.drawable.mada_gay);
                ll_mada.setBackgroundResource(R.drawable.un_selected_payment);

                ll_cash.setBackgroundResource(R.drawable.un_selected_payment);

                image_cash.setImageResource(R.drawable.payment_gray);
                tv_cash.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray3));

            }
        });

        ll_mada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_visa.setImageResource(R.drawable.visa_gray2);
                ll_visa.setBackgroundResource(R.drawable.un_selected_payment);

                image_mada.setImageResource(R.drawable.mada_white);
                ll_mada.setBackgroundResource(R.drawable.selected_payment);

                ll_cash.setBackgroundResource(R.drawable.un_selected_payment);

                image_cash.setImageResource(R.drawable.payment_gray);
                tv_cash.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray3));

            }
        });

        ll_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_visa.setImageResource(R.drawable.visa_gray2);
                ll_visa.setBackgroundResource(R.drawable.un_selected_payment);

                image_mada.setImageResource(R.drawable.mada_gay);
                ll_mada.setBackgroundResource(R.drawable.un_selected_payment);

                ll_cash.setBackgroundResource(R.drawable.selected_payment);

                image_cash.setImageResource(R.drawable.payment_white);
                tv_cash.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));

            }
        });*/


        fl_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckData();
            }
        });

        fl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentReview_Purchases();
            }
        });

        fl_choose_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentMap();
            }
        });

        fl_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentDateTime();
            }
        });

        updateUI();
    }

    private void updateUI() {
        if (userModel!=null)
        {
            //edt_phone.setText(userModel);
        }
    }

    public void UpdateDate_Time(String time_type,String delivery_cost)
    {
        this.time_type = time_type;
        this.delivery_cost = delivery_cost;
        tv_time.setText(getString(R.string.delv_cost)+delivery_cost+" "+getString(R.string.rsa));

    }
    public void UpdateAddress(String address)
    {
        this.address = address;
        tv_address.setText(address);
    }

    private void CheckData() {

        String m_first_name = edt_first_name.getText().toString().trim();
        String m_last_name = edt_last_name.getText().toString().trim();
        String m_phone = edt_phone.getText().toString().trim();
        String m_street_name = edt_street.getText().toString().trim();
        String m_feedback = edt_feedback.getText().toString().trim();
        String m_coupn = edt_coupon.getText().toString().trim();
        activity.DisplayFragmentPayment_Confirmation();
    }
}
