package com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment.fragment_my_order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.dukkan.R;
import com.appzone.dukkan.adapters.OrderViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Client_Orders extends Fragment {

    private TabLayout tab;
    private ViewPager pager;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private OrderViewPagerAdapter orderViewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_order,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Client_Orders newInstance()
    {
        return new Fragment_Client_Orders();
    }
    private void initView(View view) {
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        tab = view.findViewById(R.id.tab);
        pager = view.findViewById(R.id.pager);

        tab.setupWithViewPager(pager);

        fragmentList.add(Fragment_Client_Current_Order.newInstance());
        fragmentList.add(Fragment_Client_Previous_Order.newInstance());

        titleList.add(getString(R.string.cur_order));
        titleList.add(getString(R.string.prv_order));

        orderViewPagerAdapter = new OrderViewPagerAdapter(getChildFragmentManager());
        orderViewPagerAdapter.AddFragments(fragmentList);
        orderViewPagerAdapter.AddTitle(titleList);

        pager.setAdapter(orderViewPagerAdapter);


    }
}
