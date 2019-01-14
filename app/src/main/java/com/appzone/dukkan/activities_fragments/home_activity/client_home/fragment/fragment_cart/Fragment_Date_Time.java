package com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment.fragment_cart;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.client_home.activity.HomeActivity;
import com.appzone.dukkan.models.DeliveryCostModel;
import com.appzone.dukkan.remote.Api;
import com.appzone.dukkan.share.Common;
import com.appzone.dukkan.tags.Tags;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Date_Time extends Fragment{

    private ImageView image_back,image_arrow;
    private LinearLayout ll_back;
    private TextView tv_less,tv_more,tv_date_time_details;
    private FrameLayout fl_save;
    private String current_lang;
    private Date_Time_Listener date_time_listener;
    private HomeActivity activity;
    private String time_type="";
    private String delivery_cost="";
    private String current_date="";
    private String cost_less_2="",cost_more_2="";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_time,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Date_Time newInstance()

    {
        return new Fragment_Date_Time();
    }


    private void initView(View view)
    {
        Paper.init(getActivity());

        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        image_back = view.findViewById(R.id.image_back);
        image_arrow = view.findViewById(R.id.image_arrow);

        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);
            image_arrow.setImageResource(R.drawable.arrow_right);
        }else
            {
                image_back.setImageResource(R.drawable.arrow_left);
                image_arrow.setImageResource(R.drawable.arrow_left);
            }

        ll_back = view.findViewById(R.id.ll_back);
        tv_less = view.findViewById(R.id.tv_less);
        tv_more = view.findViewById(R.id.tv_more);

        tv_date_time_details = view.findViewById(R.id.tv_date_time_details);
        fl_save = view.findViewById(R.id.fl_save);

        fl_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(time_type))
                {
                    date_time_listener.onDate_Time_Set(time_type,delivery_cost);

                }else
                    {
                        Toast.makeText(activity, R.string.sel_delv_time, Toast.LENGTH_LONG).show();
                    }
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             activity.Back();
            }
        });

        tv_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                time_type = Tags.less_2;
                tv_less.setBackgroundResource(R.drawable.tv_selected_delivery_cost);
                tv_less.setTextColor(ContextCompat.getColor(activity,R.color.white));
                tv_more.setBackgroundResource(R.drawable.ll_from_to);
                tv_more.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));

                if (!TextUtils.isEmpty(current_date))
                {
                    update_delivery_time_cost_ui(current_date,cost_less_2);
                }else
                    {
                        current_date = getCurrentDate();
                        update_delivery_time_cost_ui(current_date,cost_less_2);

                    }

            }
        });

        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                time_type = Tags.more_2;

                tv_more.setBackgroundResource(R.drawable.tv_selected_delivery_cost);
                tv_more.setTextColor(ContextCompat.getColor(activity,R.color.white));

                tv_less.setBackgroundResource(R.drawable.ll_from_to);
                tv_less.setTextColor(ContextCompat.getColor(activity,R.color.colorPrimary));

                if (!TextUtils.isEmpty(current_date))
                {
                    update_delivery_time_cost_ui(current_date,cost_more_2);
                }else
                {
                    current_date = getCurrentDate();
                    update_delivery_time_cost_ui(current_date,cost_more_2);

                }

            }
        });


        getDeliveryCost();

    }
    private void getDeliveryCost()
    {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService()
                .getDeliveryCost()
                .enqueue(new Callback<DeliveryCostModel>() {
                    @Override
                    public void onResponse(Call<DeliveryCostModel> call, Response<DeliveryCostModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            activity.dismissSnackBar();
                            if (response.body()!=null && response.body().getDelivery()!=null)
                            {
                                cost_less_2 = response.body().getDelivery().getLess_2();
                                cost_more_2 = response.body().getDelivery().getMore_2();

                                UpdateCostUI(cost_less_2,cost_more_2);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<DeliveryCostModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            activity.CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void update_delivery_time_cost_ui(String date , String cost)
    {
        delivery_cost = cost;
        tv_date_time_details.setText(date+" ( "+getString(R.string.delivery_cost)+" "+cost+" "+getString(R.string.rsa)+" )");

    }
    private void UpdateCostUI(String cost_less_2,String cost_more_2)
    {
        tv_less.setText(getString(R.string.less_than_2_hour)+" "+cost_less_2+" "+getString(R.string.rsa));
        tv_more.setText(getString(R.string.within_2_hour)+" "+cost_more_2+" "+getString(R.string.rsa));

    }
    private String getCurrentDate()
    {
        Calendar calendar = Calendar.getInstance(new Locale(current_lang));
        long timeInMillis = calendar.getTimeInMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE - dd/MM/yyyy -",new Locale(current_lang));
        current_date = dateFormat.format(new Date(timeInMillis));
        return current_date;
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
         activity = (HomeActivity) context;
        date_time_listener = activity;
    }
    public interface Date_Time_Listener
    {
        void onDate_Time_Set(String time_type , String delivery_cost);
    }
}
