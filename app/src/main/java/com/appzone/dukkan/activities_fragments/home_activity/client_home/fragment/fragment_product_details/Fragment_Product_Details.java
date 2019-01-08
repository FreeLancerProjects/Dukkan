package com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment.fragment_product_details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.adapters.ProductViewPagerAdapter;
import com.appzone.dukkan.models.MainCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Fragment_Product_Details extends Fragment{
    private static final String TAG = "product";
    private MainCategory.Products product;
    private ImageView image_back,image_increment,image_decrement;
    private ViewPager pager;
    private TabLayout tab;
    private TextView tv_name,tv_price,tv_counter;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private List<String> productImagesList;
    private Timer timer;
    private TimerTask timerTask;
    private List<Fragment> fragmentList;
    private ProductViewPagerAdapter productViewPagerAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Product_Details newInstance(MainCategory.Products product)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,product);
        Fragment_Product_Details fragment_product_details = new Fragment_Product_Details();
        fragment_product_details.setArguments(bundle);
        return fragment_product_details;
    }

    private void initView(View view) {
        fragmentList = new ArrayList<>();
        productImagesList = new ArrayList<>();
        image_back = view.findViewById(R.id.image_back);
        image_increment = view.findViewById(R.id.image_increment);
        image_decrement = view.findViewById(R.id.image_decrement);
        pager = view.findViewById(R.id.pager);
        tab = view.findViewById(R.id.tab);
        tab.setupWithViewPager(pager);
        tv_name = view.findViewById(R.id.tv_name);
        tv_price = view.findViewById(R.id.tv_price);
        tv_counter = view.findViewById(R.id.tv_counter);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);


        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            product = (MainCategory.Products) bundle.getSerializable(TAG);
            UpdateUi(product);
        }
    }

    private void UpdateUi(MainCategory.Products product) {
        productImagesList.addAll(product.getImage());
        if (productImagesList.size()>0)
        {
            for (String img : productImagesList)
            {
                fragmentList.add(Fragment_Pager_Images.newInstance(img));
            }

            productViewPagerAdapter = new ProductViewPagerAdapter(getChildFragmentManager());
            productViewPagerAdapter.AddFragments(fragmentList);
            pager.setAdapter(productViewPagerAdapter);
            if (fragmentList.size()>1)
            {
                timer = new Timer();
                timerTask = new MyTimerTask();
                timer.scheduleAtFixedRate(timerTask,5000,6000);

            }

        }

    }

    private class MyTimerTask extends TimerTask{
        @Override
        public void run() {
           getActivity().runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   if (pager.getCurrentItem()<productImagesList.size())
                   {
                       pager.setCurrentItem(pager.getCurrentItem()+1);
                   }else
                       {
                           pager.setCurrentItem(0);
                       }
               }
           });
        }
    }

    @Override
    public void onDestroy() {
        try {
            timer.purge();
            timer.cancel();
            timerTask.cancel();
        }catch (Exception e){}

        super.onDestroy();

    }
}
