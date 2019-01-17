package com.appzone.dukkan.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment.Fragment_SubCategory;
import com.appzone.dukkan.models.MainCategory;
import com.appzone.dukkan.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ProductsAdapter extends RecyclerView.Adapter{
    private final int ITEM_DATA = 1;
    private final int ITEM_PROGRESS = 2;
    private Context context;
    private List<MainCategory.Products> productsList;
    private Fragment_SubCategory fragment_subCategory;
    private boolean canLoadMore = true;
    private int threshold = 5;
    private int lastVisibleItem , totalItemCount;
    private ProductsAdapter.LoadMoreListener loadMoreListener;
    private MainCategory.Products products;


    public ProductsAdapter(Context context, List<MainCategory.Products> productsList, Fragment_SubCategory fragment_subCategory, final RecyclerView recView) {
        this.context = context;
        this.productsList = productsList;
        this.fragment_subCategory = fragment_subCategory;
        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (dy>0)
                {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) recView.getLayoutManager();
                    lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                    totalItemCount = gridLayoutManager.getChildCount();

                    if (totalItemCount <= (lastVisibleItem+threshold) && canLoadMore)
                    {
                        if (loadMoreListener!=null)
                        {
                            loadMoreListener.onLoadMore();
                            canLoadMore = false;
                        }
                    }
                }


            }
        });


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sub_category_row,parent,false);
            return new MyHolder(view);
        }else
            {
                View view = LayoutInflater.from(context).inflate(R.layout.progress_bar_loadmore_row,parent,false);
                return new ProgressHolder(view);
            }


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyHolder)
        {
            MyHolder myHolder = (MyHolder) holder;
            products = productsList.get(position);


            myHolder.BindData(products);

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    products = productsList.get(position);




                    if (products!=null)
                    {
                        fragment_subCategory.setItemForDetails(products);

                    }
                }
            });

        }else if (holder instanceof ProgressHolder)
        {
            ProgressHolder progressHolder = (ProgressHolder) holder;
            progressHolder.progBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public void UpdateListData(List<MainCategory.Products> productsList)
    {
        this.productsList.addAll(productsList);

    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private FrameLayout fl_discount_container;
        private ImageView image;
        private TextView tv_discount,tv_name,tv_before_discount,tv_after_discount;
        public MyHolder(View itemView) {
            super(itemView);
            fl_discount_container = itemView.findViewById(R.id.fl_discount_container);
            image = itemView.findViewById(R.id.image);
            tv_discount = itemView.findViewById(R.id.tv_discount);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_before_discount = itemView.findViewById(R.id.tv_before_discount);
            tv_after_discount = itemView.findViewById(R.id.tv_after_discount);
        }

        public void BindData(MainCategory.Products products)
        {
            Paper.init(context);
            String lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
            if (lang.equals("ar"))
            {
                tv_name.setText(products.getName_ar());
            }else
            {
                tv_name.setText(products.getName_en());

            }

            if (products.getImage().size()>0)
            {

                Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+products.getImage().get(0))).fit().into(image);

            }


            if (products.getFeatures().size()>0)
            {
                MainCategory.Features features = products.getFeatures().get(0);
                try {
                    double price_after_discount = Double.parseDouble(features.getDiscount().trim());
                    double price_before_discount = Double.parseDouble(features.getOld_price().getNet_price().trim());

                    double diff = price_before_discount - price_after_discount;

                    double discount = (diff/price_before_discount)*100;

                    tv_discount.setText(((int) discount)+" %");
                    fl_discount_container.setVisibility(View.VISIBLE);

                    tv_before_discount.setPaintFlags(tv_before_discount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    tv_before_discount.setText(products.getFeatures().get(0).getOld_price().getNet_price()+" "+context.getString(R.string.rsa));
                    tv_after_discount.setText(products.getFeatures().get(0).getDiscount()+" "+context.getString(R.string.rsa));



                }catch (NumberFormatException e)
                {
                    Log.e("Error",e.getMessage()+"__");
                }
            }else
                {
                    if (products.getSize_prices().size()>0)
                    {
                        tv_before_discount.setText("");
                        tv_after_discount.setText(products.getSize_prices().get(0).getNet_price()+" "+context.getString(R.string.rsa));

                    }
                    fl_discount_container.setVisibility(View.INVISIBLE);

                }
        }
    }

    public class ProgressHolder extends RecyclerView.ViewHolder {
        private ProgressBar progBar;
        public ProgressHolder(View itemView) {
            super(itemView);
            progBar = itemView.findViewById(R.id.progBar);

        }
    }

    @Override
    public int getItemViewType(int position) {

        if (productsList.get(position) != null)
        {
            return ITEM_DATA;
        }else
            {
               return ITEM_PROGRESS;
            }

    }

    public interface LoadMoreListener
    {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(LoadMoreListener loadMoreListener)
    {
        this.loadMoreListener = loadMoreListener;
    }

    public void setLoaded()
    {
        this.canLoadMore = true;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
