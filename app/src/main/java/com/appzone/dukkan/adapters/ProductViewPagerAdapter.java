package com.appzone.dukkan.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    public ProductViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
    }

    public void AddFragments(List<Fragment> fragmentList)
    {
        this.fragmentList.addAll(fragmentList);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


}
