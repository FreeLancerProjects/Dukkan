package com.appzone.dukkan.activities_fragments.activity_order_details.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
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
import com.appzone.dukkan.models.UserModel;
import com.appzone.dukkan.remote.Api;
import com.appzone.dukkan.share.Common;
import com.appzone.dukkan.singletone.UserSingleTone;
import com.appzone.dukkan.tags.Tags;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Delegate_Current_Order_Details extends Fragment{
    private static final String TAG = "ORDER";
    private OrderDetailsActivity activity;
    private ImageView image_back,image_arrow,image_chat,image,image_upload_icon;
    private LinearLayout ll_back,ll_notes;
    private String current_lang;
    private Button btn_show_products,btn_start,btn_finish;
    private TextView tv_current_time,tv_client_name,tv_address,tv_payment,tv_notes,tv_delivery_time_type;
    private FrameLayout fl_map;
    private OrdersModel.Order order;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private int img_req=1;
    private TextView tv_hour,tv_minute,tv_second;
    private long now;
    private Handler handler;
    private Runnable runnable;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private String image_bill_path="";



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
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
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
        btn_start = view.findViewById(R.id.btn_start);
        btn_finish = view.findViewById(R.id.btn_finish);

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
                activity.DisplayFragment_Delegate_Collecting_Order_Products();
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

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateBillAlertDialog();
            }
        });
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinishDelivery();
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


        switch (order.getStatus())
        {
            case Tags.status_delegate_accept_order:

                btn_show_products.setText(getString(R.string.collect_orders));
                btn_show_products.setEnabled(true);

                break;
            case Tags.status_delegate_collect_order:
                btn_show_products.setText(getString(R.string.collect_orders));
                btn_show_products.setEnabled(true);


                break;
            case Tags.status_delegate_already_collect_order:
                btn_show_products.setText(getString(R.string.order_collected));
                btn_show_products.setEnabled(false);
                btn_start.setVisibility(View.VISIBLE);


                break;
            case Tags.status_delegate_delivering_order:
                btn_show_products.setText(getString(R.string.delivering_order));
                btn_show_products.setEnabled(false);
                btn_finish.setVisibility(View.VISIBLE);
                break;


        }

    }
    private String getCurrentTime()
    {
        Calendar calendar = Calendar.getInstance(new Locale(current_lang));
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa",new Locale(current_lang));
        return dateFormat.format(calendar.getTime());
    }
    public void setOrderCollected()
    {
        btn_show_products.setText(getString(R.string.order_collected));
        btn_show_products.setEnabled(false);
        btn_start.setVisibility(View.VISIBLE);
    }
    private void StartDelivery()
    {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService()
                .updateOrderStatus(order.getId(),userModel.getToken(),Tags.status_delegate_delivering_order)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            btn_start.setVisibility(View.GONE);
                            btn_finish.setVisibility(View.VISIBLE);
                            activity.dismissSnackBar();
                            dialog.dismiss();
                            activity.setDeliveryStarted();

                            Log.e("success","true");
                        }else
                        {
                            activity.CreateSnackBar(getString(R.string.failed));
                            Log.e("code",response.code()+"");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            activity.CreateSnackBar(getString(R.string.something));

                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void FinishDelivery()
    {
        final ProgressDialog dialog = Common.createProgressDialog(getActivity(),getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService()
                .updateOrderStatus(order.getId(),userModel.getToken(),Tags.status_delegate_delivered_order)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            btn_start.setVisibility(View.GONE);
                            btn_finish.setVisibility(View.GONE);
                            activity.dismissSnackBar();
                            dialog.dismiss();
                            activity.setDeliveryFinished();

                            Log.e("success","true");
                        }else
                        {
                            activity.CreateSnackBar(getString(R.string.failed));
                            Log.e("code",response.code()+"");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            activity.CreateSnackBar(getString(R.string.something));

                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    public void CreateBillAlertDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_bill_photo,null);

        image =view.findViewById(R.id.image);
        image_upload_icon =view.findViewById(R.id.image_upload_icon);
        Button btn_upload = view.findViewById(R.id.btn_upload);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(img_req);
            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartDelivery();

            }
        });
        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }

    private void Check_ReadPermission(int img_req)
    {
        if (ContextCompat.checkSelfPermission(getActivity(),read_permission)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{read_permission},img_req);
        }else
        {
            select_photo(img_req);
        }
    }


    private void select_photo(int img_req) {
        Intent intent ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }else
        {
            intent = new Intent(Intent.ACTION_GET_CONTENT);

        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(intent,img_req);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == img_req && resultCode == Activity.RESULT_OK && data!=null)
        {
            image_upload_icon.setVisibility(View.GONE);
            Uri uri = data.getData();
            image_bill_path = Common.getImagePath(getActivity(),uri);
            Bitmap bitmap = BitmapFactory.decodeFile(image_bill_path);
            image.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (runnable!=null&& handler!=null)
        {
            handler.removeCallbacks(runnable);

        }
    }



}
