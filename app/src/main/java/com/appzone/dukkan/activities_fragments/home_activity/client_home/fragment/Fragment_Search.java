package com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.client_home.activity.HomeActivity;
import com.appzone.dukkan.adapters.RecentSearchQueryAdapter;
import com.appzone.dukkan.preferences.Preferences;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Search extends Fragment {
    private TextView tv_no_searched_products;
    private LinearLayout ll_back;
    private ImageView image_back,image_logo,image_cancel;
    private EditText edt_search;
    private ExpandableLayout expand_layout;
    private RecyclerView recViewRecentSearch,recView;
    private RecyclerView.LayoutManager manager,managerRecentSearch;
    private RecentSearchQueryAdapter recentSearchQueryAdapter;
    private ProgressBar progBar;
    private Button btn_search;
    private String currentLang;
    private List<String> queryList;
    private Preferences preferences;
    private HomeActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        initView(view);
        return view;
    }

    public static  Fragment_Search newInstance()
    {
        return new Fragment_Search();
    }
    private void initView(View view) {
        activity = (HomeActivity) getActivity();
        queryList = new ArrayList<>();
        preferences = Preferences.getInstance();
        Paper.init(getActivity());
        currentLang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        image_back = view.findViewById(R.id.image_back);

        if (currentLang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);
        }else
            {
                image_back.setImageResource(R.drawable.arrow_left);

            }

        ll_back = view.findViewById(R.id.ll_back);
        image_cancel = view.findViewById(R.id.image_cancel);
        image_logo = view.findViewById(R.id.image_logo);
        btn_search = view.findViewById(R.id.btn_search);
        edt_search = view.findViewById(R.id.edt_search);
        expand_layout = view.findViewById(R.id.expand_layout);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        tv_no_searched_products = view.findViewById(R.id.tv_no_searched_products);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recViewRecentSearch = view.findViewById(R.id.recViewRecentSearch);
        managerRecentSearch = new LinearLayoutManager(getActivity());
        recViewRecentSearch.setLayoutManager(managerRecentSearch);

        recentSearchQueryAdapter = new RecentSearchQueryAdapter(getActivity(),queryList,this);
        recViewRecentSearch.setAdapter(recentSearchQueryAdapter);

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = edt_search.getText().toString().trim();
                if (query.length()>0)
                {
                    image_logo.setVisibility(View.GONE);
                    image_cancel.setVisibility(View.VISIBLE);
                    btn_search.setVisibility(View.VISIBLE);
                    expand_layout.setExpanded(true,true);
                }else
                    {
                        image_logo.setVisibility(View.VISIBLE);
                        image_cancel.setVisibility(View.GONE);
                        btn_search.setVisibility(View.GONE);
                        expand_layout.collapse(true);

                    }

            }
        });
        image_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_search.setText("");
                image_cancel.setVisibility(View.GONE);
                btn_search.setVisibility(View.GONE);
                image_logo.setVisibility(View.VISIBLE);
                expand_layout.collapse(true);
                DisplayNormalList();
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = edt_search.getText().toString().trim();
                Search(query);
            }
        });
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentHome();
            }
        });
        getSavedQueries();
        getSavedVisitedProductsIds();

    }

    private void Search(String query) {

    }


    private void getSavedQueries() {
        queryList.addAll(preferences.getAllQueries(getActivity()));
        recentSearchQueryAdapter.notifyDataSetChanged();

    }
    private void getSavedVisitedProductsIds() {
        List<String> productsIdsList = preferences.getAllVisitedIds(getActivity());
        if (productsIdsList.size()>0)
        {
            progBar.setVisibility(View.VISIBLE);
            getVisitedProducts(productsIdsList);
        }
    }

    private void getVisitedProducts(List<String> productsIdsList) {
        if (productsIdsList.size()>0)
        {

            tv_no_searched_products.setVisibility(View.GONE);



        }else
            {
                tv_no_searched_products.setVisibility(View.VISIBLE);

            }
    }

    private void DisplayNormalList() {

    }
}
