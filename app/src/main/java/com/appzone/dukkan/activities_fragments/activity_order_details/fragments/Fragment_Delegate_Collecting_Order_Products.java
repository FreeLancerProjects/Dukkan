package com.appzone.dukkan.activities_fragments.activity_order_details.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.activity_order_details.activity.OrderDetailsActivity;
import com.appzone.dukkan.adapters.DelegateCollectOrderAdapter;
import com.appzone.dukkan.adapters.RecyclerItemTouchHelper;
import com.appzone.dukkan.models.OrdersModel;
import com.appzone.dukkan.models.UserModel;
import com.appzone.dukkan.remote.Api;
import com.appzone.dukkan.singletone.UserSingleTone;
import com.appzone.dukkan.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Delegate_Collecting_Order_Products extends Fragment implements RecyclerItemTouchHelper.SwipeListener{
    private static final String TAG = "ORDER";
    private OrderDetailsActivity activity;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private DelegateCollectOrderAdapter adapter;
    private ImageView image_back;
    private LinearLayout ll_back;
    private String current_lang;
    private OrdersModel.Order order;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private List<OrdersModel.Products> orderProductList,productsList;
    private List<OrdersModel.Products> choosedProductList;
    private TextView tv_order_price;
    private Button btn_collected;
    private double order_total_cost=0.0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delegate_collecting_order_products,container,false);
        initView(view);
        return view;
    }
    public static Fragment_Delegate_Collecting_Order_Products newInstance(OrdersModel.Order order)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,order);

        Fragment_Delegate_Collecting_Order_Products fragment_DelegateCollecting_order_products = new Fragment_Delegate_Collecting_Order_Products();
        fragment_DelegateCollecting_order_products.setArguments(bundle);
        return fragment_DelegateCollecting_order_products;
    }
    private void initView(View view)
    {
        choosedProductList = new ArrayList<>();
        orderProductList = new ArrayList<>();
        productsList = new ArrayList<>();
        activity = (OrderDetailsActivity) getActivity();
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();

        image_back = view.findViewById(R.id.image_back);
        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);
        }else
        {
            image_back.setImageResource(R.drawable.arrow_left);

        }
        ll_back = view.findViewById(R.id.ll_back);
        tv_order_price = view.findViewById(R.id.tv_order_price);
        btn_collected = view.findViewById(R.id.btn_collected);

        recView = view.findViewById(R.id.recView);

        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setItemViewCacheSize(25);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);


        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            order = (OrdersModel.Order) bundle.getSerializable(TAG);
            productsList.addAll(order.getProducts());
            UpdateUI(order);
        }

    }

    private void UpdateUI(OrdersModel.Order order)
    {
        orderProductList.clear();
        orderProductList.addAll(order.getProducts());

        adapter = new DelegateCollectOrderAdapter(getActivity(),orderProductList,this);
        recView.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback recyclerItemTouchHelper = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT,this);
        new ItemTouchHelper(recyclerItemTouchHelper).attachToRecyclerView(recView);

        if (order.getStatus()== Tags.status_delegate_accept_order)
        {
            Api.getService()
                    .updateOrderStatus(order.getId(),userModel.getToken(),Tags.status_delegate_collect_order)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful())
                            {
                                Log.e("success","true");
                            }else
                                {
                                    Log.e("code",response.code()+"");
                                }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                Log.e("error",t.getMessage());
                            }catch (Exception e){}
                        }
                    });
        }

    }

    public void updateOrderCost(OrdersModel.Products product)
    {
        double product_cost = getProductCost(product);
        order_total_cost += product_cost;

        tv_order_price.setText(order_total_cost+" "+getString(R.string.rsa));
        tv_order_price.setVisibility(View.VISIBLE);
    }
    private double getProductCost(OrdersModel.Products product)
    {
        double cost;
        if (product.getFeature()!=null)
        {
            cost = product.getQuantity()*product.getFeature().getDiscount();
        }else
            {
                cost = product.getQuantity()*product.getProduct_price().getNet_price();

            }
            return cost;
    }
    public void setItemToShowAlternativeProducts(OrdersModel.Products alternative) {
        activity.UpdateBottomSheetUI(alternative);
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position)
    {

        OrdersModel.Products product = productsList.get(position);

        if (direction == ItemTouchHelper.LEFT)
        {
            updateOrderCost(product);

            choosedProductList.add(product);
            this.orderProductList.remove(position);
            this.adapter.notifyItemRemoved(position);
            if (orderProductList.size()==0)
            {
                btn_collected.setVisibility(View.VISIBLE);
            }


        }else if (direction == ItemTouchHelper.RIGHT)
        {
            this.orderProductList.remove(position);
            this.adapter.notifyItemRemoved(position);
            if (orderProductList.size()==0)
            {
                btn_collected.setVisibility(View.VISIBLE);
            }
        }
    }
}
