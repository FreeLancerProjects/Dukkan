package com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment.fragment_home.sub_fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.client_home.activity.HomeActivity;
import com.appzone.dukkan.adapters.MainCategoryAdapter;
import com.appzone.dukkan.models.MainCategory;
import com.appzone.dukkan.remote.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Food_Department extends Fragment {

    private ProgressBar progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private MainCategoryAdapter adapter;
    private List<MainCategory.MainCategoryItems> mainCategoryItemsList;
    private HomeActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_department,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Food_Department newInstance()
    {
        return new Fragment_Food_Department();
    }
    private void initView(View view) {
        activity = (HomeActivity) getActivity();
        mainCategoryItemsList = new ArrayList<>();
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new GridLayoutManager(getActivity(),3);
        recView.setLayoutManager(manager);
        adapter = new MainCategoryAdapter(getActivity(),mainCategoryItemsList,this);
        recView.setAdapter(adapter);
        getMainCategoryData();
    }

    private void getMainCategoryData()
    {
        Api.getService()
                .getMainCategory()
                .enqueue(new Callback<MainCategory>() {
                    @Override
                    public void onResponse(Call<MainCategory> call, Response<MainCategory> response) {
                        if (response.isSuccessful())
                        {


                            progBar.setVisibility(View.GONE);
                            if (response.body().getData().size()>0)
                            {
                                mainCategoryItemsList.clear();
                                mainCategoryItemsList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                activity.setMainCategory(response.body());
                                activity.dismissSnackBar();
                            }else
                                {
                                    activity.CreateSnackBar(getString(R.string.There_are_no_departments_to_display));


                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<MainCategory> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            activity.CreateSnackBar(getString(R.string.something));

                            // CreateSnackBar(getString( R.string.something));
                        }catch (Exception e){}
                    }
                });
    }
    public void setItem(MainCategory.MainCategoryItems mainCategoryItems)
    {
        activity.DisplayFragmentSubCategory(mainCategoryItems);
    }
}
