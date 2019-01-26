package com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment.fragment_cart;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.client_home.activity.HomeActivity;
import com.appzone.dukkan.adapters.CartAdapter;
import com.appzone.dukkan.models.OrderItem;
import com.appzone.dukkan.models.TaxModel;
import com.appzone.dukkan.remote.Api;
import com.appzone.dukkan.singletone.OrderItemsSingleTone;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Review_Purchases extends Fragment {
    private static String TAG = "ListData";
    private ImageView image_arrow;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private TextView tv_tax,tv_product_cost,tv_total;
    private CardView card_bill;
    private FrameLayout fl_continue;
    private HomeActivity activity;
    private List<OrderItem> orderItemList;
    private CartAdapter cartAdapter;
    private int tax = 0;
    private OrderItemsSingleTone orderItemsSingleTone;
    private LinearLayout ll_empty_cart;
    private double total_order_cost_after_tax=0.0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_purshases,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Review_Purchases newInstance(List<OrderItem> orderItemList)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, (Serializable) orderItemList);
        Fragment_Review_Purchases fragment_review_purchases = new Fragment_Review_Purchases();
        fragment_review_purchases.setArguments(bundle);
        return fragment_review_purchases;
    }
    private void initView(View view)
    {
        orderItemsSingleTone = OrderItemsSingleTone.newInstance();
        activity = (HomeActivity) getActivity();

        image_arrow = view.findViewById(R.id.image_arrow);
        String current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (current_lang.equals("ar"))
        {
            image_arrow.setImageResource(R.drawable.arrow_right);
        }else
        {
            image_arrow.setImageResource(R.drawable.arrow_left);

        }
        card_bill = view.findViewById(R.id.card_bill);
        ll_empty_cart = view.findViewById(R.id.ll_empty_cart);

        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setNestedScrollingEnabled(false);
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(15);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

        tv_tax = view.findViewById(R.id.tv_tax);
        tv_product_cost = view.findViewById(R.id.tv_product_cost);
        tv_total = view.findViewById(R.id.tv_total);
        fl_continue = view.findViewById(R.id.fl_continue);


        fl_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.SaveListOf_Order_Order_Total_Cost(orderItemList,total_order_cost_after_tax);
                activity.DisplayFragmentDelivery_Address();
            }
        });

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
           orderItemList = (List<OrderItem>) bundle.getSerializable(TAG);

            UpdateUI(orderItemList);
        }

    }
    private void UpdateUI(List<OrderItem> orderItemList)
    {


        if (orderItemList!=null&&orderItemList.size()>0)
        {

            getTax();
            double total = getTotalOrderPrice(orderItemList);
            updateProductCost(total);
            UpdateAdapter(this.orderItemList);
            updateCard_ContinueUI(true);

        }else
            {

                updateCard_ContinueUI(false);


            }


    }

    private void updateProductCost(double total) {
        tv_product_cost.setText(String.valueOf(total)+" "+getString(R.string.rsa));

    }

    private void updateCard_ContinueUI(boolean visible)
    {
        if (visible)
        {
            card_bill.setVisibility(View.VISIBLE);
            fl_continue.setVisibility(View.VISIBLE);
            ll_empty_cart.setVisibility(View.GONE);
            activity.updateUIToolBarFragmentCart(1);

        }else
            {
                ll_empty_cart.setVisibility(View.VISIBLE);
                card_bill.setVisibility(View.GONE);
                fl_continue.setVisibility(View.GONE);
                activity.updateUIToolBarFragmentCart(0);
            }
    }
    private double getTotalOrderPrice(List<OrderItem> orderItemList)
    {
        double total = 0.0;
        for (OrderItem orderItem :orderItemList)
        {
            total+=  orderItem.getProduct_price()*orderItem.getProduct_quantity();
        }

        return total;
    }
    private void UpdateTaxUI(int tax)
    {
        tv_tax.setText(String.valueOf(tax)+" %");

        double total_items_prices = getTotalOrderPrice(orderItemList);
        double price_after_tax = total_items_prices*((double) tax/100.0);

        total_order_cost_after_tax = getTotalOrderPrice(orderItemList) + price_after_tax;

        tv_total.setText(String.valueOf(total_order_cost_after_tax)+" "+getString(R.string.rsa));

    }
    private void getTax()
    {
        final ProgressDialog dialog = createProgressDialog(getString(R.string.wait));
        dialog.show();
        Api.getService()
                .getTax()
                .enqueue(new Callback<TaxModel>() {
                    @Override
                    public void onResponse(Call<TaxModel> call, Response<TaxModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            if (response.body()!=null)
                            {
                                tax = response.body().getProduct_tax();
                                UpdateTaxUI(tax);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TaxModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
                            activity.DisplayFragmentHome();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }
    private void UpdateAdapter(List<OrderItem> orderItemList)
    {

        cartAdapter = new CartAdapter(getActivity(),orderItemList,this);
        recView.setAdapter(cartAdapter);
    }
    public void Increment_Decrement(OrderItem orderItem,int counter)
    {

        double total_price =  orderItem.getProduct_price()*counter;
        orderItem.setProduct_quantity(counter);
        orderItem.setProduct_total_price(total_price);

        orderItemsSingleTone.UpdateProduct(orderItem);
        this.orderItemList = orderItemsSingleTone.getOrderItemList();
        updateProductCost(getTotalOrderPrice(this.orderItemList));

        UpdateTaxUI(tax);
    }
    public void RemoveItem(OrderItem orderItem)
    {
        orderItemsSingleTone.RemoveProduct(orderItem);
        this.orderItemList = orderItemsSingleTone.getOrderItemList();
        cartAdapter.notifyDataSetChanged();
        if (orderItemList.size()>0)
        {
            UpdateTaxUI(tax);

            ll_empty_cart.setVisibility(View.GONE);
            updateCard_ContinueUI(true);
            updateProductCost(getTotalOrderPrice(orderItemList));

        }else
            {
                activity.Clear_Order_Object();
                updateCard_ContinueUI(false);
                activity.UpdateCartNotification(0);
                ll_empty_cart.setVisibility(View.VISIBLE);
            }
    }

    private ProgressDialog createProgressDialog(String msg)
    {
        ProgressDialog  dialog = new ProgressDialog(activity);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        ProgressBar bar = new ProgressBar(activity);
        Drawable drawable = bar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        dialog.setIndeterminateDrawable(drawable);
        return dialog;
    }

}
