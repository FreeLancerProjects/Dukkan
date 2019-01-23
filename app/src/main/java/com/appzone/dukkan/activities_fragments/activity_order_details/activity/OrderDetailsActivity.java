package com.appzone.dukkan.activities_fragments.activity_order_details.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.activity_order_details.fragments.Fragment_Client_Order_Details;
import com.appzone.dukkan.activities_fragments.activity_order_details.fragments.Fragment_Delegate_New_Order_Details;
import com.appzone.dukkan.activities_fragments.activity_order_details.fragments.Fragment_Delegate_Order_Details;
import com.appzone.dukkan.activities_fragments.activity_order_details.fragments.Fragment_Map_Order_Details;
import com.appzone.dukkan.activities_fragments.activity_order_details.fragments.Fragment_Order_Products;
import com.appzone.dukkan.activities_fragments.chat_activity.ChatActivity;
import com.appzone.dukkan.language_helper.LanguageHelper;
import com.appzone.dukkan.models.OrdersModel;
import com.appzone.dukkan.models.UserChatModel;
import com.appzone.dukkan.models.UserModel;
import com.appzone.dukkan.remote.Api;
import com.appzone.dukkan.share.Common;
import com.appzone.dukkan.singletone.UserSingleTone;
import com.appzone.dukkan.tags.Tags;

import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Fragment_Client_Order_Details fragment_client_order_details;
    private Fragment_Delegate_New_Order_Details fragment_delegate_new_order_details;
    private Fragment_Delegate_Order_Details fragment_delegate_order_details;
    private Fragment_Map_Order_Details fragment_map_order_details;
    private Fragment_Order_Products fragment_order_products;
    private OrdersModel.Order order;
    private String order_type ="",user_type;
    private String current_lang;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private Snackbar snackbar;
    private View root;

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        super.attachBaseContext(LanguageHelper.onAttach(base,current_lang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        initView();
        getDataFromIntent();
    }



    private void initView() {
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        this.user_type = userModel.getUser().getRole();
        Paper.init(this);
        current_lang = Paper.book().read("lang",Locale.getDefault().getLanguage());
        LanguageHelper.setLocality(this,current_lang);
        fragmentManager = getSupportFragmentManager();
        root = findViewById(R.id.root);

    }

    private void getDataFromIntent()
    {
        Intent intent = getIntent();
        if (intent!=null)
        {
            this.order = (OrdersModel.Order) intent.getSerializableExtra("order");
            this.order_type = intent.getStringExtra("order_type");

            UpdateUI(order, order_type);
        }
    }
    private void UpdateUI(OrdersModel.Order order, String order_type)
    {
        if (user_type.equals(Tags.user_client))
        {
            if (order_type.equals(Tags.order_old))
            {

            }else if (order_type.equals(Tags.order_new)||order_type.equals(Tags.order_current))
                {
                    DisplayFragment_Client_Order_Details(order);

                }
        }else if (user_type.equals(Tags.user_delegate))
            {
                if (order_type.equals(Tags.order_new))
                {
                    DisplayFragment_Delegate_New_Order_Details(order);
                }else if (order_type.equals(Tags.order_current))
                {
                    DisplayFragment_Delegate_Order_Details(order);
                }
            }
    }
    public void DisplayFragment_Client_Order_Details(OrdersModel.Order order)
    {
        fragment_client_order_details = Fragment_Client_Order_Details.newInstance(order);
        fragmentManager.beginTransaction().replace(R.id.fragment_order_details_container,fragment_client_order_details).commit();

    }
    public void DisplayFragment_Delegate_New_Order_Details(OrdersModel.Order order)
    {
        if (fragment_map_order_details!=null && fragment_map_order_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_map_order_details).commit();
        }
        if (fragment_order_products!=null && fragment_order_products.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_order_products).commit();
        }

        if (fragment_delegate_new_order_details==null)
        {
            fragment_delegate_new_order_details = Fragment_Delegate_New_Order_Details.newInstance(order);
        }

        if (!fragment_delegate_new_order_details.isAdded())
        {
            fragmentManager.beginTransaction().add(R.id.fragment_order_details_container,fragment_delegate_new_order_details,"fragment_delegate_new_order_details").addToBackStack("fragment_delegate_new_order_details").commit();
        }else
            {
                fragmentManager.beginTransaction().show(fragment_delegate_new_order_details).commit();
            }


    }
    public void DisplayFragment_Delegate_Order_Details(OrdersModel.Order order)
    {
        if (fragment_map_order_details!=null && fragment_map_order_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_map_order_details).commit();
        }
        if (fragment_order_products!=null && fragment_order_products.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_order_products).commit();
        }

        if (fragment_delegate_order_details==null)
        {
            fragment_delegate_order_details = Fragment_Delegate_Order_Details.newInstance(order);
        }


        if (!fragment_delegate_order_details.isAdded())
        {
            fragmentManager.beginTransaction().add(R.id.fragment_order_details_container,fragment_delegate_order_details,"fragment_delegate_order_details").addToBackStack("fragment_delegate_order_details").commit();
        }else
        {
            fragmentManager.beginTransaction().show(fragment_delegate_order_details).commit();
        }


    }
    public void DisplayFragment_Order_Products()
    {
        if (fragment_map_order_details!=null && fragment_map_order_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_map_order_details).commit();
        }
        if (fragment_delegate_new_order_details!=null && fragment_delegate_new_order_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delegate_new_order_details).commit();
        }
        if (fragment_delegate_order_details!=null && fragment_delegate_order_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delegate_order_details).commit();
        }

        if (fragment_order_products == null)
        {
            fragment_order_products = Fragment_Order_Products.newInstance(order);
        }

        if (!fragment_order_products.isAdded())
        {
            fragmentManager.beginTransaction().add(R.id.fragment_order_details_container,fragment_order_products,"fragment_order_products").addToBackStack("fragment_order_products").commit();
        }else
        {
            fragmentManager.beginTransaction().show(fragment_order_products).commit();
        }

    }
    public void DisplayFragment_Map_Order_Details()
    {
        if (fragment_delegate_new_order_details!=null && fragment_delegate_new_order_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delegate_new_order_details).commit();
        }
        if (fragment_delegate_order_details!=null && fragment_delegate_order_details.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delegate_order_details).commit();
        }

        if (fragment_map_order_details==null)
        {
            fragment_map_order_details = Fragment_Map_Order_Details.newInstance(order.getLat(),order.getLng());

        }

        if (fragment_map_order_details.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_map_order_details).commit();

        }else
            {
                fragmentManager.beginTransaction().add(R.id.fragment_order_details_container,fragment_map_order_details,"fragment_map_order_details").addToBackStack("fragment_map_order_details").commit();

            }

    }

    public void AcceptOrder()
    {
        Log.e("order_id",order.getId()+"__");
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService()
                .Accept_Refuse_order(order.getId(),userModel.getToken(),Tags.order_accepted)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            dismissSnackBar();
                            dialog.dismiss();
                            Toast.makeText(OrderDetailsActivity.this, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            intent.putExtra("accepted",true);
                            setResult(RESULT_OK,intent);
                            finish();
                        }else
                            {
                                if (response.code()==404)
                                {
                                    CreateAlertDialogString(getString(R.string.the_order_accepted_by_another_delegate));
                                }else
                                    {
                                        dismissSnackBar();
                                        dialog.dismiss();
                                        Toast.makeText(OrderDetailsActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();

                                    }


                            }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }

    public void RefuseOrder()
    {
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService()
                .Accept_Refuse_order(order.getId(),userModel.getToken(),Tags.order_refused)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            dismissSnackBar();
                            dialog.dismiss();
                            Toast.makeText(OrderDetailsActivity.this, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            intent.putExtra("accepted",false);
                            setResult(RESULT_OK,intent);
                            finish();
                        }else
                        {
                            dismissSnackBar();
                            dialog.dismiss();
                            Toast.makeText(OrderDetailsActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });



    }

    public void NavigateToChatActivity()
    {
        UserChatModel userChatModel = null;
        if (user_type.equals(Tags.user_client))
        {
             userChatModel = new UserChatModel(order.getDelegate().getId(),order.getDelegate().getName(),order.getDelegate().getPhone(),order.getDelegate().getAvatar(),Tags.user_delegate,order.getDelegate().getRate());
        }else if (user_type.equals(Tags.user_delegate))
        {

             userChatModel = new UserChatModel(order.getClient().getId(),order.getClient().getName(),order.getClient().getPhone(),"",Tags.user_client,0);

        }

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("user_chat_data",userChatModel);
        startActivity(intent);
    }

    public void CreateSnackBar(String msg)
    {
        snackbar = Common.CreateSnackBar(this,root,msg);
        snackbar.show();
    }

    public void dismissSnackBar()
    {
        if (snackbar!=null)
        {
            snackbar.dismiss();

        }
    }

    public void CreateAlertDialogString (String msg)
    {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_sign,null);
        Button doneBtn = view.findViewById(R.id.doneBtn);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = getIntent();
                intent.putExtra("accepted",true);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Back();
    }

    public void Back()
    {
        if (fragment_client_order_details!=null && fragment_client_order_details.isVisible())
        {
            finish();
        }else if (fragment_delegate_new_order_details!=null && fragment_delegate_new_order_details.isVisible())
        {
            finish();
        }else if (fragment_delegate_order_details!=null && fragment_delegate_order_details.isVisible())
        {
            finish();
        }
        else
            {
                if (user_type.equals(Tags.user_delegate)&&order_type.equals(Tags.order_new))
                {
                    DisplayFragment_Delegate_New_Order_Details(order);
                }else if (user_type.equals(Tags.user_delegate)&&order_type.equals(Tags.order_current))
                {
                    DisplayFragment_Delegate_Order_Details(order);
                }
            }
    }
}
