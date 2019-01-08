package com.appzone.dukkan.activities_fragments.product_details.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.product_details.fragment.Fragment_Pager_Images;
import com.appzone.dukkan.adapters.ProductViewPagerAdapter;
import com.appzone.dukkan.adapters.Sizes_Prices_Adapter;
import com.appzone.dukkan.language_helper.LanguageHelper;
import com.appzone.dukkan.models.MainCategory;
import com.appzone.dukkan.models.ProductSize_OfferModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class ProductDetailsActivity extends AppCompatActivity {
    private MainCategory.Products product;
    private ImageView image_back,image_increment,image_decrement;
    private ViewPager pager;
    private TabLayout tab;
    private TextView tv_name,tv_price_before_discount,tv_price_after_discount,tv_counter;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private Sizes_Prices_Adapter sizes_prices_adapter;
    private List<String> productImagesList;
    private Timer timer;
    private TimerTask timerTask;
    private List<Fragment> fragmentList;
    private ProductViewPagerAdapter productViewPagerAdapter;
    private String current_lang;
    private List<ProductSize_OfferModel> productSize_offerModelList;
    private String product_name="";
    private ScaleAnimation scaleAnimation;
    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        super.attachBaseContext(LanguageHelper.onAttach(base,current_lang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        initView();
        getDataFromIntent();
    }


    private void initView() {
        productSize_offerModelList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        productImagesList = new ArrayList<>();
        scaleAnimation = new ScaleAnimation(.7f,1f,.7f,1f,1f,1f);
        scaleAnimation.setDuration(300);
        scaleAnimation.setFillAfter(true);

        image_back = findViewById(R.id.image_back);
        image_increment = findViewById(R.id.image_increment);
        image_decrement = findViewById(R.id.image_decrement);
        pager = findViewById(R.id.pager);
        tab = findViewById(R.id.tab);
        tab.setupWithViewPager(pager);
        tv_name = findViewById(R.id.tv_name);
        tv_price_before_discount = findViewById(R.id.tv_price_before_discount);
        tv_price_after_discount = findViewById(R.id.tv_price_after_discount);

        tv_counter = findViewById(R.id.tv_counter);
        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recView.setLayoutManager(manager);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_price_before_discount.setPaintFlags(tv_price_before_discount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        image_increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_increment.clearAnimation();
                image_decrement.clearAnimation();
                image_increment.startAnimation(scaleAnimation);
            }
        });

        image_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_decrement.clearAnimation();
                image_increment.clearAnimation();
                image_decrement.startAnimation(scaleAnimation);
            }
        });

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            product = (MainCategory.Products) intent.getSerializableExtra("product");
            UpdateUi(product);
        }
    }

    private void UpdateUi(MainCategory.Products product) {

        UpdateAdapter(product);
        if (current_lang.equals("ar"))
        {
            product_name = product.getName_ar();


        }else
        {
            product_name = product.getName_en();

        }
        UpdateProductName(product_name);


        productImagesList.addAll(product.getImage());

        if (productImagesList.size()>0)
        {
            for (String img : productImagesList)
            {
                fragmentList.add(Fragment_Pager_Images.newInstance(img));
            }

            productViewPagerAdapter = new ProductViewPagerAdapter(getSupportFragmentManager());
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

    private void UpdateProductName(String name)
    {
        tv_name.setText(name);
    }
    private void UpdateAdapter(MainCategory.Products product)
    {

        productSize_offerModelList.addAll(updateProductPrices_SizesData(product));

        sizes_prices_adapter = new Sizes_Prices_Adapter(this,productSize_offerModelList);
        recView.setAdapter(sizes_prices_adapter);

    }

    private List<ProductSize_OfferModel> updateProductPrices_SizesData(MainCategory.Products product)
    {
        List<ProductSize_OfferModel> productSize_offerModelList = new ArrayList<>();

        for (MainCategory.Prices_Sizes prices_sizes:product.getSize_prices())
        {
            ProductSize_OfferModel productSize_offerModel = new ProductSize_OfferModel();
            productSize_offerModel.setId(prices_sizes.getId());
            productSize_offerModel.setAr_name(prices_sizes.getSize_ar());
            productSize_offerModel.setEn_name(prices_sizes.getSize_en());

            String price_after_discount = isOffer(product.getFeatures(),prices_sizes.getId());

            if (!TextUtils.isEmpty(price_after_discount))
            {
                productSize_offerModel.setOffer(true);
                productSize_offerModel.setPrice_before_discount(prices_sizes.getNet_price());
                productSize_offerModel.setPrice_after_discount(price_after_discount);
                productSize_offerModel.setDiscount(getDiscount(prices_sizes.getNet_price(),price_after_discount));
                productSize_offerModelList.add(productSize_offerModel);
            }else
                {
                    productSize_offerModel.setOffer(false);
                    productSize_offerModel.setPrice_before_discount(prices_sizes.getNet_price());
                    productSize_offerModel.setPrice_after_discount(prices_sizes.getNet_price());
                    productSize_offerModel.setDiscount("0");
                    productSize_offerModelList.add(productSize_offerModel);

                }




        }
        return productSize_offerModelList;
    }


    private String getDiscount(String price_before_discount,String price_after_discount)
    {
        double diff = Double.parseDouble(price_before_discount) - Double.parseDouble(price_after_discount);
        double dis = (diff/Double.parseDouble(price_before_discount))*100;
        return String.valueOf((int)dis);
    }

    private String isOffer(List<MainCategory.Features> featuresList, String product_id)
    {
        String price_after_discount = "";
        for (MainCategory.Features features :featuresList )
        {
            if (features.getOld_price().getId().equals(product_id))
            {
                price_after_discount = features.getDiscount();

                break;
            }else
                {
                    price_after_discount="";
                }
        }

        return price_after_discount;

    }

    public void setItemForSize(ProductSize_OfferModel productSize_offerModel) {

        if (current_lang.equals("ar"))
        {
            UpdateProductName(product_name+" "+productSize_offerModel.getAr_name());

        }else
            {
                UpdateProductName(product_name+" "+productSize_offerModel.getEn_name());

            }

            if (productSize_offerModel.isOffer())
            {
                tv_price_before_discount.setVisibility(View.VISIBLE);
                tv_price_before_discount.setText(productSize_offerModel.getPrice_before_discount()+" "+getString(R.string.rsa));
                tv_price_after_discount.setText(productSize_offerModel.getPrice_after_discount()+" "+getString(R.string.rsa));
            }else
                {
                    tv_price_before_discount.setVisibility(View.GONE);
                    tv_price_after_discount.setText(productSize_offerModel.getPrice_before_discount()+" "+getString(R.string.rsa));

                }

    }

    private class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
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
