package com.appzone.dukkan.activities_fragments.activity_order_details.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.activity_order_details.activity.OrderDetailsActivity;
import com.appzone.dukkan.models.OrdersModel;
import com.appzone.dukkan.tags.Tags;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Delegate_Current_Order_Details extends Fragment{
    private static final String TAG = "ORDER";
    private OrderDetailsActivity activity;
    private ImageView image_back,image_arrow,image_chat;
    private LinearLayout ll_back,ll_notes;
    private String current_lang;
    private Button btn_show_products;
    private TextView tv_current_time,tv_client_name,tv_address,tv_payment,tv_notes,tv_delivery_time_type;
    private FrameLayout fl_map;
    private OrdersModel.Order order;
    private TextView tv_hour,tv_minute,tv_second;
    private long now;
    private Handler handler;
    private Runnable runnable;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delegate_current_order_details,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Delegate_Current_Order_Details newInstance(OrdersModel.Order order)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,order);
        Fragment_Delegate_Current_Order_Details fragment_delegate_Current_order_details = new Fragment_Delegate_Current_Order_Details();
        fragment_delegate_Current_order_details.setArguments(bundle);
        return fragment_delegate_Current_order_details;
    }
    private void initView(View view) {
        activity = (OrderDetailsActivity) getActivity();
        Paper.init(activity);
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
        image_chat = view.findViewById(R.id.image_chat);

        ll_back = view.findViewById(R.id.ll_back);
        ll_notes = view.findViewById(R.id.ll_notes);
        btn_show_products = view.findViewById(R.id.btn_show_products);
        tv_delivery_time_type = view.findViewById(R.id.tv_delivery_time_type);
        tv_hour = view.findViewById(R.id.tv_hour);
        tv_minute = view.findViewById(R.id.tv_minute);
        tv_second = view.findViewById(R.id.tv_second);

        tv_current_time = view.findViewById(R.id.tv_current_time);
        tv_client_name = view.findViewById(R.id.tv_client_name);
        tv_address = view.findViewById(R.id.tv_address);
        tv_payment = view.findViewById(R.id.tv_payment);
        tv_notes = view.findViewById(R.id.tv_notes);
        fl_map = view.findViewById(R.id.fl_map);

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            this.order = (OrdersModel.Order) bundle.getSerializable(TAG);
            UpdateUI(this.order);
        }

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        btn_show_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragment_Order_Products();
            }
        });

        fl_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragment_Map_Order_Details();
            }
        });

        image_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.NavigateToChatActivity();
            }
        });

        handler = new Handler();

        StartTimer();


    }

    private void StartTimer() {

        runnable = new Runnable() {
            @Override
            public void run() {
                updateTimerUI();
                StartTimer();
            }
        };

        handler.postDelayed(runnable,1000);
        updateTimerUI();
    }

    private void updateTimerUI()
    {
        now = Calendar.getInstance().getTimeInMillis()-(order.getAccepted_time()*1000);

        int AllSeconds = (int) ((now+1000)/1000);
        int seconds= AllSeconds%60;
        int minutes = (AllSeconds/60)%60;
        int hours = AllSeconds/3600;

        tv_second.setText(seconds+"");
        tv_minute.setText(minutes+"");
        tv_hour.setText(hours+"");


    }
    private void UpdateUI(OrdersModel.Order order)
    {
        tv_current_time.setText(getCurrentTime());
        tv_address.setText(order.getAddress()+" "+order.getStreet());
        if (order.getClient()!=null)
        {
            tv_client_name.setText(order.getClient().getName());

        }
        if (order.getPayment_method()== 1)
        {
            tv_payment.setText("Cash نقدي");
        }else if (order.getPayment_method()== 2)
        {
            tv_payment.setText(R.string.mada);

        }else if (order.getPayment_method()== 3)
        {
            tv_payment.setText(R.string.visa);

        }
        if (order.getNote()!=null&&!TextUtils.isEmpty(order.getNote()))
        {
            ll_notes.setVisibility(View.VISIBLE);
            tv_notes.setText(order.getNote());

        }else
        {
            ll_notes.setVisibility(View.GONE);
        }

        if (order.getTime_type()== Tags.less_2)
        {
            tv_delivery_time_type.setText(R.string.deliver_order_less_2_hour);
        }else if (order.getTime_type()== Tags.more_2)
        {
            tv_delivery_time_type.setText(R.string.deliver_order_more_2_hour);

        }

    }
    private String getCurrentTime()
    {
        Calendar calendar = Calendar.getInstance(new Locale(current_lang));
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa",new Locale(current_lang));
        return dateFormat.format(calendar.getTime());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (runnable!=null&& handler!=null)
        {
            handler.removeCallbacks(runnable);

        }
    }
}
