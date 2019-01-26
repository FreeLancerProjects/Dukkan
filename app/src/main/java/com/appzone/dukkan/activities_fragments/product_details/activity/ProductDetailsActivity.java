package com.appzone.dukkan.activities_fragments.product_details.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.dukkan.R;
import com.appzone.dukkan.adapters.AlternativeProductsAdapter;
import com.appzone.dukkan.adapters.AlternativeSizes_Prices_Adapter;
import com.appzone.dukkan.adapters.SimilarProductsAdapter;
import com.appzone.dukkan.adapters.Sizes_Prices_Adapter;
import com.appzone.dukkan.adapters.SliderPagerAdapter;
import com.appzone.dukkan.language_helper.LanguageHelper;
import com.appzone.dukkan.models.AlternativeProductItem;
import com.appzone.dukkan.models.MainCategory;
import com.appzone.dukkan.models.OrderItem;
import com.appzone.dukkan.models.ProductSize_OfferModel;
import com.appzone.dukkan.models.SimilarProductModel;
import com.appzone.dukkan.remote.Api;
import com.appzone.dukkan.singletone.OrderItemsSingleTone;
import com.appzone.dukkan.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity {
    private MainCategory.Products product,alternativeProduct;
    private List<MainCategory.Products> similarProductsList,alternativeProductList;
    private Button btn_add_to_cart;
    private ImageView image_back,image_increment,image_decrement;
    private ViewPager pager;
    private TabLayout tab;
    private TextView tv_name,tv_price_before_discount,tv_price_after_discount,tv_counter;
    private RecyclerView recView,recViewSimilarProducts;
    private RecyclerView.LayoutManager manager,similarManager;
    private SimilarProductsAdapter similarProductsAdapter;
    private AlternativeProductsAdapter alternativeProductsAdapter;
    private Sizes_Prices_Adapter sizes_prices_adapter;
    private AlternativeSizes_Prices_Adapter alternativeSizes_prices_adapter;
    private List<String> productImagesList;
    private Timer timer;
    private TimerTask timerTask;
    private List<String> imgsEndPointList;
    private SliderPagerAdapter sliderPagerAdapter;
    private String current_lang;
    private List<ProductSize_OfferModel> productSize_offerModelList, alternativeProductSize_offerModelList;
    private String product_name="";
    private ScaleAnimation scaleAnimation;
    private CardView card_similar;
    private int counter = 1;
    private int lastSelectedPosition = -1;
    private ProductSize_OfferModel productSize_offerModel,alternative_productSize_offerModel = null;
    private int feature_id =-1,alternative_feature_id=-1;
    private OrderItem orderItem;

    private OrderItemsSingleTone orderItemsSingleTone;
    /////////////////////////////////////////////////////////
    private BottomSheetBehavior behavior;
    private View root;
    private ImageView image_product,image_close;
    private TextView tv_product_name,tv_price_before_discount_product,tv_price_after_discount_product,tv_amount,tv_total_price,tv_no_alternative_product;
    private RecyclerView recViewSize,recViewProduct;
    private RecyclerView.LayoutManager managerSize,managerProduct;
    private Button btn_add_alter_product,btn_cancel;
    private AlternativeProductItem alternativeProductItem=null;
    private int alternativeProductTotalPrice =0;
    List<MainCategory.Products> productsList;
    private String alternativeProductName="";
    private int selectedProductId;
    private int current_page =1;


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

    private void initView()
    {
        orderItemsSingleTone= OrderItemsSingleTone.newInstance();
        productSize_offerModelList = new ArrayList<>();
        alternativeProductSize_offerModelList = new ArrayList<>();
        imgsEndPointList = new ArrayList<>();
        productsList = new ArrayList<>();

        ////////////////////////////////////////////////////////////
        root = findViewById(R.id.root);
        behavior = BottomSheetBehavior.from(root);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                {
                    OpenSheet();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        image_close = findViewById(R.id.image_close);

        image_product = findViewById(R.id.image_product);
        tv_product_name = findViewById(R.id.tv_product_name);
        tv_price_before_discount_product = findViewById(R.id.tv_price_before_discount_product);
        tv_price_after_discount_product = findViewById(R.id.tv_price_after_discount_product);
        tv_amount = findViewById(R.id.tv_amount);
        tv_total_price = findViewById(R.id.tv_total_price);
        tv_no_alternative_product = findViewById(R.id.tv_no_alternative_product);

        recViewSize = findViewById(R.id.recViewSize);
        managerSize = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recViewSize.setLayoutManager(managerSize);
        recViewProduct = findViewById(R.id.recViewProduct);
        managerProduct = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recViewProduct.setLayoutManager(managerProduct);
        recViewProduct.setHasFixedSize(true);
        recViewProduct.setDrawingCacheEnabled(true);
        recViewProduct.setItemViewCacheSize(25);
        recViewProduct.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        btn_add_alter_product = findViewById(R.id.btn_add_alter_product);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseSheet();
                NotNeedToAddAlternativeProduct();
            }
        });
        btn_add_alter_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrepareAlternativeToAddToCart();

            }
        });
        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseSheet();
            }
        });
        ////////////////////////////////////////////////////////////

        productImagesList = new ArrayList<>();
        scaleAnimation = new ScaleAnimation(.7f,1f,.7f,1f,1f,1f);
        scaleAnimation.setDuration(300);
        scaleAnimation.setFillAfter(true);

        image_back = findViewById(R.id.image_back);
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart);

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
        card_similar = findViewById(R.id.card_similar);

        recViewSimilarProducts = findViewById(R.id.recViewSimilarProducts);
        similarManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recViewSimilarProducts.setLayoutManager(similarManager);
        recView.setHasFixedSize(true);
        recViewSimilarProducts.setItemViewCacheSize(15);
        recViewSimilarProducts.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recViewSimilarProducts.setDrawingCacheEnabled(true);

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
                increaseCounter();
            }
        });

        image_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_decrement.clearAnimation();
                image_increment.clearAnimation();
                image_decrement.startAnimation(scaleAnimation);
                decreaseCounter();
            }
        });

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdateBottomSheetUI(alternativeProduct);
                PrepareItemToAddToCart();

            }
        });
    }
    private void getDataFromIntent()
    {
        Intent intent = getIntent();
        if (intent!=null)
        {
            product = (MainCategory.Products) intent.getSerializableExtra("product");
            selectedProductId = product.getId();

            similarProductsList = (List<MainCategory.Products>) intent.getSerializableExtra("similar_products");
            alternativeProductList = (List<MainCategory.Products>) intent.getSerializableExtra("similar_products");;
            alternativeProduct = alternativeProductList.get(0);
            UpdateUi(product);
            UpdateBottomSheetUI(alternativeProduct);
            UpdateSimilarAdapterUI(similarProductsList);
            UpdateAlternativeAdapterUI(alternativeProductList);
        }
    }
    private void UpdateBottomSheetUI(MainCategory.Products alternativeProduct)
    {
        tv_amount.setText(String.valueOf(counter));

        if (alternativeProduct!=null)
        {
            if (alternativeProduct.getImage().size()>0)
            {
                Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+alternativeProduct.getImage().get(0))).fit().into(image_product);

            }

            if (current_lang.equals("ar"))
            {
                alternativeProductName = alternativeProduct.getName_ar();


            }else
            {
                alternativeProductName = alternativeProduct.getName_en();

            }

            if (alternativeSizes_prices_adapter!=null)
            {
                alternativeSizes_prices_adapter.UpdateSelectedItem(-1);
            }
            tv_total_price.setText("");
            tv_price_after_discount_product.setText("");
            tv_price_before_discount_product.setText("");
            UpdateAlternativeAdapter(alternativeProduct);
            UpdateAlternativeProductName(alternativeProductName);

        }
    }
    private void PrepareItemToAddToCart()
    {
        if (productSize_offerModelList.size()>0)
        {
            if (productSize_offerModel!=null)
            {
                int total_price = Integer.parseInt(productSize_offerModel.getPrice_after_discount())*counter;

                if (product.getImage().size()>0)
                {
                    orderItem = new OrderItem(product.getImage().get(0),product.getId(),productSize_offerModel.getFeature_id(),productSize_offerModel.getId(),product.getName_ar(),product.getName_en(),productSize_offerModel.getAr_name(),productSize_offerModel.getEn_name(),counter,Integer.parseInt(productSize_offerModel.getPrice_after_discount()),total_price);
                }else
                {
                    orderItem = new OrderItem("",product.getId(),productSize_offerModel.getFeature_id(),productSize_offerModel.getId(),product.getName_ar(),product.getName_en(),productSize_offerModel.getAr_name(),productSize_offerModel.getEn_name(),counter,Integer.parseInt(productSize_offerModel.getPrice_after_discount()),total_price);

                }
                OpenSheet();
            }else
            {
                Toast.makeText(ProductDetailsActivity.this, R.string.ch_size, Toast.LENGTH_SHORT).show();
            }
        }else
        {
            int total_price = Integer.parseInt(productSize_offerModel.getPrice_after_discount())*counter;

            if (product.getImage().size()>0)
            {
                orderItem = new OrderItem(product.getImage().get(0),product.getId(),productSize_offerModel.getFeature_id(),productSize_offerModel.getId(),product.getName_ar(),product.getName_en(),productSize_offerModel.getAr_name(),productSize_offerModel.getEn_name(),counter,Integer.parseInt(productSize_offerModel.getPrice_after_discount()),total_price);

            }else
            {
                orderItem = new OrderItem("",product.getId(),productSize_offerModel.getFeature_id(),productSize_offerModel.getId(),product.getName_ar(),product.getName_en(),productSize_offerModel.getAr_name(),productSize_offerModel.getEn_name(),counter,Integer.parseInt(productSize_offerModel.getPrice_after_discount()),total_price);

            }

            OpenSheet();

        }

    }
    private void PrepareAlternativeToAddToCart()
    {
        if (alternativeProductSize_offerModelList.size()>0)
        {
            if (alternative_productSize_offerModel!=null)
            {
                int total_price = Integer.parseInt(alternative_productSize_offerModel.getPrice_after_discount())*counter;

                if (alternativeProduct.getImage().size()>0)
                {
                    alternativeProductItem = new AlternativeProductItem(alternativeProduct.getImage().get(0),alternativeProduct.getId(),alternative_productSize_offerModel.getFeature_id(),alternative_productSize_offerModel.getId(),alternativeProduct.getName_ar(),alternativeProduct.getName_en(),alternative_productSize_offerModel.getAr_name(),alternative_productSize_offerModel.getEn_name(),counter,Integer.parseInt(alternative_productSize_offerModel.getPrice_after_discount()),total_price);


                }else
                {
                    alternativeProductItem = new AlternativeProductItem("",alternativeProduct.getId(),alternative_productSize_offerModel.getFeature_id(),alternative_productSize_offerModel.getId(),alternativeProduct.getName_ar(),alternativeProduct.getName_en(),alternative_productSize_offerModel.getAr_name(),alternative_productSize_offerModel.getEn_name(),counter,Integer.parseInt(alternative_productSize_offerModel.getPrice_after_discount()),total_price);


                }
                orderItem.setAlternativeProductItem(alternativeProductItem);
                addProductToCard(orderItem);

            }else
            {
                Toast.makeText(ProductDetailsActivity.this, R.string.ch_size, Toast.LENGTH_SHORT).show();
            }
        }else
        {
            int total_price = Integer.parseInt(alternative_productSize_offerModel.getPrice_after_discount())*counter;

            if (alternativeProduct.getImage().size()>0)
            {
                alternativeProductItem = new AlternativeProductItem(alternativeProduct.getImage().get(0),alternativeProduct.getId(),alternative_productSize_offerModel.getFeature_id(),alternative_productSize_offerModel.getId(),alternativeProduct.getName_ar(),alternativeProduct.getName_en(),alternative_productSize_offerModel.getAr_name(),alternative_productSize_offerModel.getEn_name(),counter,Integer.parseInt(alternative_productSize_offerModel.getPrice_after_discount()),total_price);

            }else
            {
                alternativeProductItem = new AlternativeProductItem("",alternativeProduct.getId(),alternative_productSize_offerModel.getFeature_id(),alternative_productSize_offerModel.getId(),alternativeProduct.getName_ar(),alternativeProduct.getName_en(),alternative_productSize_offerModel.getAr_name(),alternative_productSize_offerModel.getEn_name(),counter,Integer.parseInt(alternative_productSize_offerModel.getPrice_after_discount()),total_price);


            }
            orderItem.setAlternativeProductItem(alternativeProductItem);

            addProductToCard(orderItem);


        }
    }
    private void NotNeedToAddAlternativeProduct()
    {
        orderItem.setAlternativeProductItem(null);
        addProductToCard(orderItem);

    }
    private void addProductToCard(OrderItem orderItem)
    {
        CloseSheet();

        orderItemsSingleTone.AddProduct(orderItem);
        Intent intent = getIntent();
        setResult(RESULT_OK,intent);
        finish();
    }
    private void OpenSheet()

    {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    private void CloseSheet()
    {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }
    private void UpdateSimilarAdapterUI(List<MainCategory.Products> similarProductsList)
    {

        if (similarProductsList.size()>0)
        {
            card_similar.setVisibility(View.VISIBLE);
            similarProductsAdapter = new SimilarProductsAdapter(this,similarProductsList);
            recViewSimilarProducts.setAdapter(similarProductsAdapter);




        }else
            {
                card_similar.setVisibility(View.GONE);
            }

    }
    private void UpdateAlternativeAdapterUI(List<MainCategory.Products> alternativeProductList)
    {
        current_page = 1;
        productsList.clear();

        if (alternativeProductList.size()>0)
        {

            for (MainCategory.Products products :alternativeProductList)
            {

                if (products.getId()!=selectedProductId)
                {
                    productsList.add(products);

                }
            }



            if (alternativeProductsAdapter==null)
            {
                alternativeProductsAdapter = new AlternativeProductsAdapter(this,recViewProduct,productsList);
                recViewProduct.setAdapter(alternativeProductsAdapter);

                alternativeProductsAdapter.setOnLoadMoreListener(new AlternativeProductsAdapter.LoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        int nextPage = current_page+1;
                        LoadMore(nextPage);
                    }
                });


            }else
                {
                    alternativeProductsAdapter.notifyDataSetChanged();
                }

            tv_no_alternative_product.setVisibility(View.GONE);
            alternativeProduct = productsList.get(0);
        }else
        {
            recViewProduct.setVisibility(View.GONE);
            btn_add_alter_product.setVisibility(View.GONE);
            tv_no_alternative_product.setVisibility(View.GONE);

        }
    }

    private void LoadMore(int nextPage)
    {
        productsList.add(null);
        alternativeProductsAdapter.notifyDataSetChanged();
        Api.getService()
                .getSimilarProducts(nextPage,selectedProductId,product.getMain_category_id(),product.getSub_category_id())
                .enqueue(new Callback<SimilarProductModel>() {
                    @Override
                    public void onResponse(Call<SimilarProductModel> call, Response<SimilarProductModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body()!=null && response.body().getData().size()>0)
                            {
                                current_page = response.body().getCurrent_page();
                                productsList.remove(productsList.size()-1);
                                alternativeProductsAdapter.setLoaded();
                                productsList.addAll(response.body().getData());
                                alternativeProductsAdapter.notifyDataSetChanged();

                            }else
                                {
                                    productsList.remove(productsList.size()-1);
                                    alternativeProductsAdapter.setLoaded();
                                    alternativeProductsAdapter.notifyDataSetChanged();
                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<SimilarProductModel> call, Throwable t) {
                        try
                        {
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void UpdateUi(MainCategory.Products product)
    {

        UpdateAdapter(product);
        if (current_lang.equals("ar"))
        {
            product_name = product.getName_ar();


        }else
        {
            product_name = product.getName_en();

        }

        UpdateProductName(product_name);
        UpdateViewPagerImages(product);



    }

    private void UpdateViewPagerImages(MainCategory.Products product)
    {
        productImagesList.clear();
        imgsEndPointList.clear();
        productImagesList.addAll(product.getImage());

        if (productImagesList.size()>0)
        {
            for (String img : productImagesList)
            {
                Log.e("img",img);

                imgsEndPointList.add(img);

            }
            sliderPagerAdapter = new SliderPagerAdapter(imgsEndPointList,this);
            pager.setAdapter(sliderPagerAdapter);

            if (imgsEndPointList.size()>1)
            {
                timer = new Timer();
                timerTask = new MyTimerTask();
                timer.scheduleAtFixedRate(timerTask,5000,6000);

            }else
                {
                    if (timer!=null)
                    {
                        timer.purge();
                        timer.cancel();

                    }

                    if (timerTask!=null)
                    {
                        timerTask.cancel();
                    }
                }


        }
    }

    private void increaseCounter()
    {
        counter+=1;
        updateCounter(counter);
    }
    private void decreaseCounter()
    {
        counter-=1;
        if (counter <1)
        {
            counter = 1;
            updateCounter(counter);
        }else
            {
                updateCounter(counter);

            }
    }
    private void updateCounter(int counter)
    {
        tv_counter.setText(String.valueOf(counter));
        tv_amount.setText(String.valueOf(counter));
    }
    private void UpdateProductName(String name)
    {
        tv_name.setText(name);
    }
    private void UpdateAlternativeProductName(String name)
    {

        tv_product_name.setText(name);

    }
    private void UpdateAdapter(MainCategory.Products product)
    {
        productSize_offerModelList.clear();
        productSize_offerModelList.addAll(updateProductPrices_SizesData(product));

        if (sizes_prices_adapter == null)
        {
            sizes_prices_adapter = new Sizes_Prices_Adapter(this,productSize_offerModelList);
            recView.setAdapter(sizes_prices_adapter);
        }else
            {
                sizes_prices_adapter.notifyDataSetChanged();
            }


    }
    private void UpdateAlternativeAdapter(MainCategory.Products alternativeProduct)
    {

        alternativeProductSize_offerModelList.clear();
        alternativeProductSize_offerModelList.addAll(updateAlternativeProductPrices_SizesData(alternativeProduct));

        if (alternativeSizes_prices_adapter == null)
        {
            alternativeSizes_prices_adapter = new AlternativeSizes_Prices_Adapter(this, alternativeProductSize_offerModelList);
            recViewSize.setAdapter(alternativeSizes_prices_adapter);
        }else
        {
            alternativeSizes_prices_adapter.notifyDataSetChanged();
        }


    }
    private List<ProductSize_OfferModel> updateProductPrices_SizesData(MainCategory.Products product)
    {
        List<ProductSize_OfferModel> productSize_offerModelList = new ArrayList<>();

        for (MainCategory.Prices_Sizes prices_sizes : product.getSize_prices())
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
                productSize_offerModel.setFeature_id(feature_id);
                productSize_offerModelList.add(productSize_offerModel);
            }else
                {

                    productSize_offerModel.setOffer(false);
                    productSize_offerModel.setPrice_before_discount(prices_sizes.getNet_price());
                    productSize_offerModel.setPrice_after_discount(prices_sizes.getNet_price());
                    productSize_offerModel.setDiscount("0");
                    productSize_offerModel.setFeature_id(-1);
                    productSize_offerModelList.add(productSize_offerModel);

                }




        }
        return productSize_offerModelList;
    }

    private List<ProductSize_OfferModel> updateAlternativeProductPrices_SizesData(MainCategory.Products product)
    {
        List<ProductSize_OfferModel> productSize_offerModelList = new ArrayList<>();

        for (MainCategory.Prices_Sizes prices_sizes : product.getSize_prices())
        {
            ProductSize_OfferModel productSize_offerModel = new ProductSize_OfferModel();
            productSize_offerModel.setId(prices_sizes.getId());
            productSize_offerModel.setAr_name(prices_sizes.getSize_ar());
            productSize_offerModel.setEn_name(prices_sizes.getSize_en());

            String price_after_discount = isOfferAlternative(product.getFeatures(),prices_sizes.getId());


            if (!TextUtils.isEmpty(price_after_discount))
            {
                productSize_offerModel.setOffer(true);

                productSize_offerModel.setPrice_before_discount(prices_sizes.getNet_price());
                productSize_offerModel.setPrice_after_discount(price_after_discount);
                productSize_offerModel.setDiscount(getDiscount(prices_sizes.getNet_price(),price_after_discount));
                productSize_offerModel.setFeature_id(alternative_feature_id);
                productSize_offerModelList.add(productSize_offerModel);
            }else
            {

                productSize_offerModel.setOffer(false);
                productSize_offerModel.setPrice_before_discount(prices_sizes.getNet_price());
                productSize_offerModel.setPrice_after_discount(prices_sizes.getNet_price());
                productSize_offerModel.setDiscount("0");
                productSize_offerModel.setFeature_id(-1);
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
    private String isOffer(List<MainCategory.Features> featuresList, int product_id)
    {
        String price_after_discount = "";
        for (MainCategory.Features features :featuresList )
        {
            if (features.getOld_price().getId()==product_id)
            {
                price_after_discount = features.getDiscount();
                feature_id = features.getFeature_id();
                break;
            }else
                {
                    price_after_discount="";
                }
        }

        return price_after_discount;

    }
    private String isOfferAlternative(List<MainCategory.Features> featuresList, int product_id)
    {
        String price_after_discount = "";
        for (MainCategory.Features features :featuresList )
        {
            if (features.getOld_price().getId()==product_id)
            {
                price_after_discount = features.getDiscount();
                alternative_feature_id = features.getFeature_id();
                break;
            }else
            {
                price_after_discount="";
            }
        }

        return price_after_discount;

    }
    public void setItemForSize(ProductSize_OfferModel productSize_offerModel, int lastSelectedItem)
    {
        this.lastSelectedPosition = lastSelectedItem;

        this.productSize_offerModel = productSize_offerModel;

        updateProductPrices_SizesUI(productSize_offerModel);

    }
    public void setAlternativeItemForSize(ProductSize_OfferModel alternative_productSize_offerModel, int lastSelectedItem)
    {
        this.alternative_productSize_offerModel = alternative_productSize_offerModel;
        updateAlternativeProductPrices_SizesUI(alternative_productSize_offerModel);
    }
    private void updateProductPrices_SizesUI(ProductSize_OfferModel productSize_offerModel)
    {
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
    private void updateAlternativeProductPrices_SizesUI(ProductSize_OfferModel productSize_offerModel)
    {

        if (current_lang.equals("ar"))
        {
            UpdateAlternativeProductName(alternativeProductName+" "+productSize_offerModel.getAr_name());

        }else
        {
            UpdateAlternativeProductName(alternativeProductName+" "+productSize_offerModel.getEn_name());

        }

        if (productSize_offerModel.isOffer())
        {
            alternativeProductTotalPrice = this.counter*Integer.parseInt(productSize_offerModel.getPrice_after_discount());
            tv_total_price.setText(alternativeProductTotalPrice +" "+getString(R.string.rsa));

            tv_price_before_discount_product.setVisibility(View.VISIBLE);
            tv_price_before_discount_product.setText(productSize_offerModel.getPrice_before_discount()+" "+getString(R.string.rsa));
            tv_price_before_discount_product.setPaintFlags(tv_price_before_discount_product.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
            tv_price_after_discount_product.setText(productSize_offerModel.getPrice_after_discount()+" "+getString(R.string.rsa));
        }else
        {
            tv_price_before_discount_product.setVisibility(View.GONE);
            tv_price_after_discount_product.setText(productSize_offerModel.getPrice_before_discount()+" "+getString(R.string.rsa));

            alternativeProductTotalPrice = this.counter*Integer.parseInt(productSize_offerModel.getPrice_before_discount());
            tv_total_price.setText(alternativeProductTotalPrice +" "+getString(R.string.rsa));

        }
    }
    public void setItemForDetails(MainCategory.Products products)
    {
        selectedProductId = products.getId();

        lastSelectedPosition =-1;
        sizes_prices_adapter.UpdateSelectedItem(lastSelectedPosition);

        if (this.product.getId()!=products.getId())
        {
            this.counter = 1;
            updateCounter(counter);
        }

        this.product = products;
        tv_price_after_discount.setText("");
        tv_price_before_discount.setText("");
        UpdateUi(products);
        UpdateAlternativeAdapterUI(alternativeProductList);

    }
    public void setItemAlternativeForDetails(MainCategory.Products alternativeProduct)
    {

        if (alternativeSizes_prices_adapter!=null)
        {
            alternativeSizes_prices_adapter.UpdateSelectedItem(-1);
        }

        this.alternativeProduct = alternativeProduct;
        tv_price_after_discount_product.setText("");
        tv_price_before_discount_product.setText("");
        tv_total_price.setText("");
        UpdateBottomSheetUI(alternativeProduct);

    }

    private class MyTimerTask extends TimerTask
    {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pager.getCurrentItem()<productImagesList.size()-1)
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("price_size",productSize_offerModel);
        outState.putSerializable("product",product);

        outState.putInt("counter",counter);
        outState.putInt("lastSelectedPosition", lastSelectedPosition);

        Parcelable parcelable = manager.onSaveInstanceState();
        outState.putParcelable("state",parcelable);

        Parcelable parcelable2 = similarManager.onSaveInstanceState();
        outState.putParcelable("state2",parcelable2);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState!=null)
        {
            productSize_offerModel = (ProductSize_OfferModel) savedInstanceState.getSerializable("price_size");
            if (productSize_offerModel!=null)
            {
                updateProductPrices_SizesUI(productSize_offerModel);

            }

            product = (MainCategory.Products) savedInstanceState.getSerializable("product");

            if (product!=null)
            {
                UpdateUi(product);

            }
            counter = savedInstanceState.getInt("counter",1);
            updateCounter(counter);

            lastSelectedPosition = savedInstanceState.getInt("lastSelectedPosition",-1);
            sizes_prices_adapter.UpdateSelectedItem(lastSelectedPosition);
            Parcelable parcelable = savedInstanceState.getParcelable("state");
            if (parcelable!=null)
            {
                manager.onRestoreInstanceState(parcelable);

            }
            Parcelable parcelable2 = savedInstanceState.getParcelable("state2");
            if (parcelable2!=null)
            {
                similarManager.onRestoreInstanceState(parcelable2);

            }


        }

    }

    @Override
    public void onDestroy()
    {
        try {
            timer.purge();
            timer.cancel();
            timerTask.cancel();
        }catch (Exception e){}

        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        if (behavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
        {
            CloseSheet();
        }else
            {
                super.onBackPressed();
            }
    }
}
