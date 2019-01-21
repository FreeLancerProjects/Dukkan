package com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment.fragment_cart;

import android.app.AlertDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.client_home.activity.HomeActivity;
import com.appzone.dukkan.models.CouponModel;
import com.appzone.dukkan.models.UserModel;
import com.appzone.dukkan.remote.Api;
import com.appzone.dukkan.share.Common;
import com.appzone.dukkan.singletone.UserSingleTone;
import com.appzone.dukkan.tags.Tags;

import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Delivery_Address extends Fragment {

    private ImageView image_arrow,image_arrow2,image_arrow_back,image_arrow_continue,image_visa,image_mada,image_cash;
    private EditText edt_first_name,edt_last_name,edt_phone,edt_street,edt_feedback,edt_coupon;
    private FrameLayout fl_choose_address,fl_continue,fl_back,fl_date;
    private TextView tv_address,tv_time,tv_cash;
    private LinearLayout ll_visa, ll_mada, ll_cash;
    private Button btn_active;
    private ProgressBar progBar;
    private ImageView image_correct,image_in_correct;
    private String payment_method= Tags.payment_cash;
    private HomeActivity activity;
    private int time_type=-1;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private String address ="";
    private CouponModel couponModel = null;
    private String coupon_code ="-1";
    private String coupon_value="",delivery_cost="";
    private  String current_lang;



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

        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
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

        btn_active = view.findViewById(R.id.btn_active);
        image_correct = view.findViewById(R.id.image_correct);
        image_in_correct = view.findViewById(R.id.image_in_correct);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        edt_coupon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_coupon.getText().toString().length()>0)
                {
                    btn_active.setVisibility(View.VISIBLE);
                    image_in_correct.setVisibility(View.GONE);

                }else
                    {
                        btn_active.setVisibility(View.INVISIBLE);
                        image_in_correct.setVisibility(View.GONE);
                    }
            }
        });

        btn_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coupon_id = edt_coupon.getText().toString().trim();
                CheckIsCouponAvailable(coupon_id);
            }
        });

      /*  ll_visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_method = Tags.payment_visa;
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
                payment_method = Tags.payment_cash;

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
                Common.CloseKeyBoard(getActivity(),edt_phone);
                activity.DisplayFragmentMap();
            }
        });

        fl_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.CloseKeyBoard(getActivity(),edt_phone);

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

    private void CheckIsCouponAvailable(String coupon_id)
    {
        image_in_correct.setVisibility(View.GONE);
        image_correct.setVisibility(View.GONE);
        progBar.setVisibility(View.VISIBLE);
        Api.getService()
                .isCouponAvailable(coupon_id)
                .enqueue(new Callback<CouponModel>() {
                    @Override
                    public void onResponse(Call<CouponModel> call, Response<CouponModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body()!=null)
                            {
                                if (response.body().getCode()==200)
                                {
                                    couponModel = response.body();

                                    if (couponModel.getCoupon()!=null)
                                    {
                                        image_correct.setVisibility(View.VISIBLE);
                                        CreateCongratulationDialog(couponModel.getCoupon().getValue());

                                    }
                                }else
                                    {
                                        image_correct.setVisibility(View.GONE);
                                        image_in_correct.setVisibility(View.VISIBLE);
                                        Toast.makeText(activity, R.string.coup_not_active, Toast.LENGTH_LONG).show();

                                    }

                            }else
                                {
                                    image_correct.setVisibility(View.GONE);
                                    image_in_correct.setVisibility(View.VISIBLE);
                                    Toast.makeText(activity, R.string.coup_not_active, Toast.LENGTH_LONG).show();
                                }

                        }
                    }

                    @Override
                    public void onFailure(Call<CouponModel> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            activity.CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e) {}
                    }
                });

    }
    private void CreateCongratulationDialog(String discount)
    {
        Animation cup_animation = AnimationUtils.loadAnimation(getActivity(),R.anim.cup_animation);
        final Animation image_congratulation_animation = AnimationUtils.loadAnimation(getActivity(),R.anim.image_congtaulation);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .create();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_congratulation,null);
        ImageView image_cup = view.findViewById(R.id.image_cup);
        final ImageView image_congratulation = view.findViewById(R.id.image_congratulation);
        TextView tv_content = view.findViewById(R.id.tv_content);
        Button btn_get = view.findViewById(R.id.btn_get);
        tv_content.setText(getString(R.string.you_have_received_a_discount_of)+" "+discount+" "+getString(R.string.rsa)+" "+getString(R.string.on_the_price_of_the_products));
        if (current_lang.equals("ar"))
        {
            image_congratulation.setImageResource(R.drawable.ar_cong);
        }else
            {
                image_congratulation.setImageResource(R.drawable.en_cong);

            }
            btn_get.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        image_congratulation.clearAnimation();
        image_cup.clearAnimation();
        image_cup.startAnimation(cup_animation);
        cup_animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                image_congratulation.setVisibility(View.VISIBLE);
                image_congratulation.startAnimation(image_congratulation_animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }
    public void UpdateDate_Time(int time_type,String delivery_cost,String current_date)
    {
        this.time_type = time_type;
        this.delivery_cost = delivery_cost;
        tv_time.setText(current_date+" ( "+getString(R.string.delv_cost)+delivery_cost+" "+getString(R.string.rsa)+" )");

    }
    public void UpdateAddress(String address)
    {
        this.address = address;
        tv_address.setText(address);
    }
    private void CheckData()
    {

        String m_first_name = edt_first_name.getText().toString().trim();
        String m_last_name = edt_last_name.getText().toString().trim();
        String m_phone = edt_phone.getText().toString().trim();
        String m_street_name = edt_street.getText().toString().trim();
        String m_feedback = edt_feedback.getText().toString().trim();
        String m_coupon = edt_coupon.getText().toString().trim();

        if (!TextUtils.isEmpty(m_first_name)&&
                !TextUtils.isEmpty(m_last_name)&&
                !TextUtils.isEmpty(m_phone)&&
                m_phone.length()==9&&
                !TextUtils.isEmpty(m_street_name)&&
                !TextUtils.isEmpty(address)&&
                time_type!=-1&&
                !TextUtils.isEmpty(payment_method)
                )
        {
            if (!TextUtils.isEmpty(m_coupon))
            {
                if (couponModel!=null)
                {
                    Common.CloseKeyBoard(getActivity(),edt_phone);

                    edt_first_name.setError(null);
                    edt_last_name.setError(null);
                    edt_phone.setError(null);
                    edt_street.setError(null);

                    coupon_code = couponModel.getCoupon().getCode();
                    coupon_value = couponModel.getCoupon().getValue();
                    String d_name = m_first_name+" "+m_last_name;
                    DisplayFragmentPayment_Confirmation(d_name,m_phone,m_street_name,m_feedback, coupon_code,payment_method);
                }else
                    {
                        Toast.makeText(activity, R.string.coup_not_active, Toast.LENGTH_LONG).show();

                    }


            }else
                {
                    Common.CloseKeyBoard(getActivity(),edt_phone);

                    edt_first_name.setError(null);
                    edt_last_name.setError(null);
                    edt_phone.setError(null);
                    edt_street.setError(null);
                    tv_time.setError(null);
                    tv_address.setError(null);
                    if (couponModel!=null)
                    {
                        coupon_code = couponModel.getCoupon().getCode();
                        coupon_value = couponModel.getCoupon().getValue();
                    }

                    String d_name = m_first_name+" "+m_last_name;
                    DisplayFragmentPayment_Confirmation(d_name,m_phone,m_street_name,m_feedback, coupon_code,payment_method);

                }
        }else
            {
                if (TextUtils.isEmpty(m_first_name))
                {
                    edt_first_name.setError(getString(R.string.field_req));
                }else
                    {
                        edt_first_name.setError(null);
                    }
                if (TextUtils.isEmpty(m_last_name))
                {
                    edt_last_name.setError(getString(R.string.field_req));
                }else
                {
                    edt_last_name.setError(null);
                }
                if (TextUtils.isEmpty(m_phone))
                {
                    edt_phone.setError(getString(R.string.field_req));
                }else if (m_phone.length()!=9)
                {
                    edt_phone.setError(getString(R.string.inv_phone));

                }else
                {
                    edt_phone.setError(null);
                }
                if (TextUtils.isEmpty(m_street_name))
                {
                    edt_street.setError(getString(R.string.field_req));
                }else
                {
                    edt_street.setError(null);
                }

                if (time_type==-1)
                {
                    tv_time.setError(getString(R.string.field_req));
                }else
                {
                    tv_time.setError(null);
                }

                if (TextUtils.isEmpty(address))
                {
                    tv_address.setError(getString(R.string.field_req));

                }else
                {
                    tv_address.setError(null);
                }


            }

    }

    private void DisplayFragmentPayment_Confirmation(String d_name, String p_phone, String m_street_name, String m_feedback, String coupon_code, String payment_method)
    {
        activity.Save_Order_Data(d_name,p_phone,m_street_name,m_feedback,coupon_code,coupon_value,payment_method);
        activity.DisplayFragmentPayment_Confirmation(payment_method,coupon_value);

    }
}
