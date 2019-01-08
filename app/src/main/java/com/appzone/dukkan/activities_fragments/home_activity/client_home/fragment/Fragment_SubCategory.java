package com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.client_home.HomeActivity;
import com.appzone.dukkan.activities_fragments.product_details.activity.ProductDetailsActivity;
import com.appzone.dukkan.adapters.DepartmentAdapter;
import com.appzone.dukkan.adapters.ProductsAdapter;
import com.appzone.dukkan.models.MainCategory;
import com.appzone.dukkan.models.ProductPaginationModel;
import com.appzone.dukkan.remote.Api;
import com.appzone.dukkan.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_SubCategory extends Fragment{
    private static final String TAG = "DATA";
    private LinearLayout ll_back;
    private ImageView image_back,image;
    private EditText edt_search_toolbar;
    private TextView tv_no_products,tv_name;
    private RecyclerView recViewDepartment,recView;
    private RecyclerView.LayoutManager manager,managerDepartment;
    private DepartmentAdapter departmentAdapter;
    private MainCategory.MainCategoryItems mainCategoryItems;
    private String current_lang;
    private HomeActivity activity;
    private List<MainCategory.Products> productsList;
    private ProductsAdapter productsAdapter;
    private String sub_category_id;
    private int current_page_index = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_category,container,false);
        initView(view);
        return view;
    }

    public static Fragment_SubCategory newInstance(MainCategory.MainCategoryItems mainCategoryItems)
    {
        Fragment_SubCategory fragment_subCategory = new Fragment_SubCategory();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,mainCategoryItems);
        fragment_subCategory.setArguments(bundle);
        return fragment_subCategory;
    }
    private void initView(View view) {
        productsList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        ll_back = view.findViewById(R.id.ll_back);
        image_back = view.findViewById(R.id.image_back);
        Paper.init(getActivity());
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);
        }else
        {
            image_back.setImageResource(R.drawable.arrow_left);

        }
        image = view.findViewById(R.id.image);
        edt_search_toolbar = view.findViewById(R.id.edt_search_toolbar);
        tv_name = view.findViewById(R.id.tv_name);

        tv_no_products = view.findViewById(R.id.tv_no_products);
        recViewDepartment = view.findViewById(R.id.recViewDepartment);
        recView = view.findViewById(R.id.recView);
        manager = new GridLayoutManager(getActivity(),2);
        recView.setLayoutManager(manager);
        recView.setNestedScrollingEnabled(true);
        managerDepartment = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recViewDepartment.setLayoutManager(managerDepartment);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentHome();
            }
        });

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            mainCategoryItems = (MainCategory.MainCategoryItems) bundle.getSerializable(TAG);
            UpdateUI(mainCategoryItems);
        }





    }


    private void UpdateUI(MainCategory.MainCategoryItems mainCategoryItems) {
        Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_URL+mainCategoryItems.getImage())).into(image);
        if (current_lang.equals("ar"))
        {
            tv_name.setText(mainCategoryItems.getName_ar());
        }else
        {
            tv_name.setText(mainCategoryItems.getName_en());

        }
        departmentAdapter = new DepartmentAdapter(getActivity(),mainCategoryItems.getSub_categories(),this);
        recViewDepartment.setAdapter(departmentAdapter);

        ///////////////////////////////////////////////////
        if (mainCategoryItems.getSub_categories().size()>0)
        {
            if (mainCategoryItems.getSub_categories().get(0).getProducts().size()>0)
            {
                recView.setVisibility(View.VISIBLE);
                tv_no_products.setVisibility(View.GONE);

                sub_category_id = mainCategoryItems.getSub_categories().get(0).getId();
                UpdateSubCategoryAdapter(mainCategoryItems.getSub_categories().get(0).getProducts());

            }else
                {
                    recView.setVisibility(View.GONE);
                    tv_no_products.setVisibility(View.VISIBLE);
                }

        }else
            {
                recView.setVisibility(View.GONE);
                tv_no_products.setVisibility(View.VISIBLE);
            }


    }

    private void UpdateSubCategoryAdapter(final List<MainCategory.Products> products) {
        productsList.addAll(products);
        productsAdapter = new ProductsAdapter(getActivity(),productsList,this,recView);
        recView.setAdapter(productsAdapter);


        productsAdapter.setOnLoadMoreListener(new ProductsAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                LoadMore(current_page_index);
            }
        });


    }

    private void LoadMore(final int current_page_index)
    {
        int next_page = current_page_index+1;

        productsList.add(null);
        productsAdapter.notifyDataSetChanged();

        Api.getService()
                .getProductPaganation(sub_category_id,next_page)
                .enqueue(new Callback<ProductPaginationModel>() {
                    @Override
                    public void onResponse(Call<ProductPaginationModel> call, Response<ProductPaginationModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body()!=null)
                            {
                                Fragment_SubCategory.this.current_page_index = response.body().getCurrent_page();
                                productsList.remove(productsList.size()-1);
                                productsAdapter.notifyItemRemoved(productsList.size()-1);

                                productsList.addAll(response.body().getData());
                                productsAdapter.setLoaded();
                                productsAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductPaginationModel> call, Throwable t) {

                    }
                });
    }

    public void setItemForDepartment(MainCategory.SubCategory itemForDepartment)
    {
        current_page_index = 1;
        sub_category_id = itemForDepartment.getId();
        productsList.clear();
        productsList.addAll(itemForDepartment.getProducts());
        productsAdapter.notifyDataSetChanged();
    }

    public void setItemForDetails(MainCategory.Products product) {
        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
        intent.putExtra("product",product);
        startActivity(intent);
    }
}
