package com.appzone.dukkan.activities_fragments.home_activity.driver_home.fragment.fragment_driver_orders;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.driver_home.DriverHomeActivity;
import com.appzone.dukkan.adapters.OrderViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Driver_Orders extends Fragment {
    private DriverHomeActivity activity;
    private TabLayout tab;
    private ViewPager pager;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private OrderViewPagerAdapter orderViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_driver_orders,container,false);
        initView(view);
        return view;

    }
    public static Fragment_Driver_Orders newInstance()
    {
        return new Fragment_Driver_Orders();
    }

    private void initView(View view) {
        activity = (DriverHomeActivity) getActivity();
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        tab = view.findViewById(R.id.tab);
        pager = view.findViewById(R.id.pager);

        tab.setupWithViewPager(pager);

        fragmentList.add(Fragment_Driver_New_Order.newInstance());
        fragmentList.add(Fragment_Driver_Current_Order.newInstance());
        fragmentList.add(Fragment_Driver_Previous_Order.newInstance());

        titleList.add(getString(R.string.new_orders));
        titleList.add(getString(R.string.cur_order));
        titleList.add(getString(R.string.prv_order));

        orderViewPagerAdapter = new OrderViewPagerAdapter(getChildFragmentManager());
        orderViewPagerAdapter.AddFragments(fragmentList);
        orderViewPagerAdapter.AddTitle(titleList);

        pager.setAdapter(orderViewPagerAdapter);

    }


}
