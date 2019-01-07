package com.appzone.dukkan.activities_fragments.home_activity.fragment;

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
import com.appzone.dukkan.activities_fragments.home_activity.activity.HomeActivity;
import com.appzone.dukkan.adapters.DepartmentAdapter;
import com.appzone.dukkan.models.MainCategory;
import com.appzone.dukkan.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import io.paperdb.Paper;

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

    public void SetSubCategoryItem(MainCategory.MainCategoryItems mainCategoryItems)
    {
        this.mainCategoryItems = mainCategoryItems;
        UpdateUI(mainCategoryItems);
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
    }
}
