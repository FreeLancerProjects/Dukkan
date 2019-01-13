package com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.client_home.activity.HomeActivity;
import com.appzone.dukkan.adapters.OfferedProductsAdapter;
import com.appzone.dukkan.models.MainCategory;
import com.appzone.dukkan.models.ProductPaginationModel;
import com.appzone.dukkan.remote.Api;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Offers extends Fragment {

    private LinearLayout ll_back;
    private ImageView image_back;
    private ViewPager pager;
    private TabLayout tab;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private LinearLayout ll_no_offers;
    private OfferedProductsAdapter offeredProductsAdapter;
    private List<MainCategory.Products> productsList;
    private ProgressBar progBar;
    private int current_page = 1;
    private HomeActivity homeActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Offers newInstance()
    {
        return new Fragment_Offers();
    }
    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        productsList = new ArrayList<>();
        ll_back = view.findViewById(R.id.ll_back);
        image_back = view.findViewById(R.id.image_back);
        pager = view.findViewById(R.id.pager);
        tab = view.findViewById(R.id.tab);

        Paper.init(getActivity());
        String current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);
        }else
        {
            image_back.setImageResource(R.drawable.arrow_left);

        }
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new GridLayoutManager(getActivity(),2);
        recView.setLayoutManager(manager);
        offeredProductsAdapter = new OfferedProductsAdapter(getActivity(),productsList,this,recView);
        recView.setAdapter(offeredProductsAdapter);




        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.Back();
            }
        });

        getOfferedProducts(current_page,false);

        offeredProductsAdapter.setOnLoadMoreListener(new OfferedProductsAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                int next_page = current_page+1;
                getOfferedProducts(next_page,true);
            }
        });
    }

    private void getOfferedProducts(int page_index, final boolean loadMore)
    {
        Api.getService()
                .getOfferedProductPagination(page_index)
                .enqueue(new Callback<ProductPaginationModel>() {
                    @Override
                    public void onResponse(Call<ProductPaginationModel> call, Response<ProductPaginationModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);
                            homeActivity.dismissSnackBar();

                            if (response.body()!=null)
                            {
                                if (response.body().getData().size()>0)
                                {
                                    current_page = response.body().getCurrent_page();
                                    if (loadMore)
                                    {
                                        Fragment_Offers.this.productsList.remove(Fragment_Offers.this.productsList.size()-1);
                                        offeredProductsAdapter.notifyItemRemoved(Fragment_Offers.this.productsList.size()-1);
                                        Fragment_Offers.this.productsList.addAll(response.body().getData());
                                        offeredProductsAdapter.setLoaded();
                                        offeredProductsAdapter.notifyDataSetChanged();

                                    }else
                                        {
                                            Fragment_Offers.this.productsList.addAll(response.body().getData());
                                            offeredProductsAdapter.notifyDataSetChanged();
                                        }

                                }else
                                    {
                                        if (loadMore)
                                        {
                                            Fragment_Offers.this.productsList.remove(Fragment_Offers.this.productsList.size()-1);
                                            offeredProductsAdapter.notifyItemRemoved(Fragment_Offers.this.productsList.size()-1);
                                            offeredProductsAdapter.setLoaded();
                                            offeredProductsAdapter.notifyDataSetChanged();

                                        }else
                                            {
                                                ll_no_offers.setVisibility(View.VISIBLE);
                                            }
                                    }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductPaginationModel> call, Throwable t) {
                        try {
                            homeActivity.CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    public void setItemForDetails(MainCategory.Products products) {

        homeActivity.NavigateToProductDetailsActivity(products,homeActivity.getSimilarProducts(products.getMain_category_id(),products.getSub_category_id(),products.getId()));
    }
}
