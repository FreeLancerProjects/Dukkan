package com.appzone.dukkan.activities_fragments.activity_home.client_home.fragment.fragment_cart;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.activity_home.client_home.activity.HomeActivity;
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
    private final static  String TAG = "cost";
    private ImageView image_arrow,image_arrow2,image_arrow_back,image_arrow_continue,image_visa,image_mada,image_cash;
    private EditText edt_first_name,edt_last_name,edt_phone,edt_street,edt_feedback;
    private FrameLayout fl_choose_address,fl_continue,fl_back,fl_date;
    private TextView tv_address,tv_time,tv_cash;
    private LinearLayout ll_visa, ll_mada, ll_cash;
    private String payment_method= Tags.payment_cash;
    private HomeActivity activity;
    private int time_type = -1;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private String address ="";
    private CouponModel couponModel,couponModel2= null;
    private String delivery_cost = "";
    private  String current_lang;
    private double total_order = 0.0;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_address,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Delivery_Address newInstance(double total_order_cost)
    {
        Bundle bundle = new Bundle();
        bundle.putDouble(TAG,total_order_cost);
        Fragment_Delivery_Address fragment_delivery_address = new Fragment_Delivery_Address();
        fragment_delivery_address.setArguments(bundle);
        return fragment_delivery_address;
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

        fl_choose_address = view.findViewById(R.id.fl_choose_address);
        fl_continue = view.findViewById(R.id.fl_continue);
        fl_back = view.findViewById(R.id.fl_back);
        tv_address = view.findViewById(R.id.tv_address);
        tv_time = view.findViewById(R.id.tv_time);



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

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            total_order = bundle.getDouble(TAG,0.0);
            updateUI(total_order);


        }
    }


    private void updateUI(double total_order)
    {

        if (userModel!=null)
        {
            edt_phone.setText(userModel.getUser().getPhone());
            String[] split = userModel.getUser().getName().split(" ",2);

            if (split.length>=1)
            {
                try {
                    edt_first_name.setText(split[0]);
                    edt_last_name.setText(split[1]);

                }catch (IndexOutOfBoundsException e){}
            }else
                {
                    try {
                        edt_first_name.setText(split[0]);

                    }catch (IndexOutOfBoundsException e){}
                }


                getCouponData(total_order);

        }

    }
    public void setTotal_order(double total_order_cost)
    {
        this.total_order = total_order_cost;
        updateUI(total_order_cost);

    }
    private void getCouponData(final double total_order)
    {

        Api.getService()
                .isCouponAvailable()
                .enqueue(new Callback<CouponModel>() {
                    @Override
                    public void onResponse(Call<CouponModel> call, Response<CouponModel> response) {
                        if (response.isSuccessful())
                        {

                            if (response.body()!=null)
                            {
                                couponModel = response.body();


                                if (userModel.getUser().getCoupon()== 0)
                                {
                                    couponModel2 = couponModel;

                                    if (couponModel!=null)
                                    {
                                        if (total_order >= couponModel.getMinimum_order_cost()&&userModel.getUser().getPoints()>0)
                                        {
                                            CreateDialogUseCoupon_Point(couponModel);
                                        }else if (total_order >= couponModel.getMinimum_order_cost())
                                        {
                                            CreateCongratulationDialog(couponModel);
                                        }
                                    }

                                }else
                                    {
                                        couponModel2 = new CouponModel(0,couponModel.getMinimum_order_cost(),couponModel.getClient_point_cost());
                                    }

                            }else
                                {
                                    Toast.makeText(activity, R.string.coup_not_active, Toast.LENGTH_LONG).show();
                                }

                        }
                    }

                    @Override
                    public void onFailure(Call<CouponModel> call, Throwable t) {
                        try {
                            activity.CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e) {}
                    }
                });

    }
    private void CreateCongratulationDialog(final CouponModel couponModel)
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
        final EditText edt_coupon_code = view.findViewById(R.id.edt_coupon_code);

        Button btn_get = view.findViewById(R.id.btn_get);
        tv_content.setText(getString(R.string.type_the_word)+" "+couponModel.getCoupon_codes().getAr()+" "+getString(R.string.or)+" "+couponModel.getCoupon_codes().getEn()+" "+getString(R.string.to_get_the_discount));
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
                    String m_coupon_code = edt_coupon_code.getText().toString().trim();
                    if (!TextUtils.isEmpty(m_coupon_code))
                    {
                        if (m_coupon_code.equals(couponModel.getCoupon_codes().getAr())||m_coupon_code.equals(couponModel.getCoupon_codes().getEn()))
                        {
                            edt_coupon_code.setError(null);
                            dialog.dismiss();


                        }else
                            {
                                edt_coupon_code.setError(getString(R.string.words_not_match));

                            }
                    }else
                        {
                            edt_coupon_code.setError(getString(R.string.field_req));
                        }
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
    private void CreateDialogUseCoupon_Point(final CouponModel couponModel)
    {
        Animation cup_animation = AnimationUtils.loadAnimation(getActivity(),R.anim.cup_animation);
        final Animation image_congratulation_animation = AnimationUtils.loadAnimation(getActivity(),R.anim.image_congtaulation);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .create();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_points_coupon,null);
        ImageView image_cup = view.findViewById(R.id.image_cup);
        final ImageView image_congratulation = view.findViewById(R.id.image_congratulation);
        TextView tv_content = view.findViewById(R.id.tv_content);

        Button btn_coupon = view.findViewById(R.id.btn_coupon);
        Button btn_point = view.findViewById(R.id.btn_point);

        tv_content.setText(getString(R.string.you_have_received_a_discount_of)+" "+couponModel.getCoupon_value()+" %"+" "+getString(R.string.on_the_price_of_the_products));
        if (current_lang.equals("ar"))
        {
            image_congratulation.setImageResource(R.drawable.ar_cong);
        }else
        {
            image_congratulation.setImageResource(R.drawable.en_cong);

        }
        btn_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CreateCongratulationDialog(couponModel);
            }
        });
        btn_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Delivery_Address.this.couponModel = null;
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
            Common.CloseKeyBoard(getActivity(),edt_phone);

            edt_first_name.setError(null);
            edt_last_name.setError(null);
            edt_phone.setError(null);
            edt_street.setError(null);

            String d_name = m_first_name+" "+m_last_name;
            DisplayFragmentPayment_Confirmation(d_name,m_phone,m_street_name,m_feedback, couponModel2,payment_method);

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

    private void DisplayFragmentPayment_Confirmation(String d_name, String p_phone, String m_street_name, String m_feedback, CouponModel couponModel, String payment_method)
    {
        activity.Save_Order_Data(d_name,p_phone,m_street_name,m_feedback,couponModel,payment_method);
        activity.DisplayFragmentPayment_Confirmation(payment_method,couponModel);

    }
}