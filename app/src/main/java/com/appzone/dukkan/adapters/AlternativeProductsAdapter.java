package com.appzone.dukkan.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
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
import com.appzone.dukkan.activities_fragments.product_details.activity.ProductDetailsActivity;
import com.appzone.dukkan.models.MainCategory;
import com.appzone.dukkan.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class AlternativeProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_PROGRESS = 2;
    private Context context;
    private List<MainCategory.Products> productsList;
    private ProductDetailsActivity activity;
    private boolean isLoading = false;
    private int lastVisibleItemPosition, totalItemCount;
    private LoadMoreListener loadMoreListener;


    public AlternativeProductsAdapter(Context context, final RecyclerView recView, List<MainCategory.Products> productsList) {
        this.context = context;
        this.productsList = productsList;
        this.activity = (ProductDetailsActivity) context;

        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx < 0) {
                    LinearLayoutManager manager = (LinearLayoutManager) recView.getLayoutManager();


                    if (manager != null) {
                        lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                        totalItemCount = manager.getItemCount();
                        if ((totalItemCount - lastVisibleItemPosition) <=10 && !isLoading && loadMoreListener != null) {
                            isLoading = true;

                            loadMoreListener.onLoadMore();
                        }
                    }


                }
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            View view = LayoutInflater.from(context).inflate(R.layout.alternative_product_row, parent, false);
            return new MyHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.progress_bar_loadmore_alternative_row, parent, false);
            return new ProgressHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {
            final MyHolder myHolder = (MyHolder) holder;

            MainCategory.Products products = productsList.get(myHolder.getAdapterPosition());
            myHolder.BindData(products);

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainCategory.Products products = productsList.get(myHolder.getAdapterPosition());

                    if (products != null)

                    {
                        activity.setItemAlternativeForDetails(products);

                    }
                }
            });
        } else if (holder instanceof ProgressHolder) {
            ProgressHolder progressHolder = (ProgressHolder) holder;
            progressHolder.progBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        private FrameLayout fl_discount_container;
        private ImageView image;
        private TextView tv_discount, tv_name, tv_before_discount, tv_after_discount;

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
            if (lang.equals("ar")) {
                tv_name.setText(products.getName_ar());
            } else {
                tv_name.setText(products.getName_en());

            }

            if (products.getImage().size() > 0) {
                Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL + products.getImage().get(0))).priority(Picasso.Priority.HIGH).fit().into(image);

            }


            if (products.getFeatures().size() > 0) {
                MainCategory.Features features = products.getFeatures().get(0);
                try {
                    double price_after_discount = Double.parseDouble(features.getDiscount().trim());
                    double price_before_discount = Double.parseDouble(features.getOld_price().getNet_price().trim());

                    double diff = price_before_discount - price_after_discount;

                    double discount = (diff / price_before_discount) * 100;

                    tv_discount.setText(((int) discount) + " %");
                    fl_discount_container.setVisibility(View.VISIBLE);

                    tv_before_discount.setPaintFlags(tv_before_discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tv_before_discount.setText(products.getFeatures().get(0).getOld_price().getNet_price() + " " + context.getString(R.string.rsa));
                    tv_after_discount.setText(products.getFeatures().get(0).getDiscount() + " " + context.getString(R.string.rsa));


                } catch (NumberFormatException e) {
                    Log.e("Error", e.getMessage() + "__");
                }
            } else {
                if (products.getSize_prices().size() > 0) {
                    tv_before_discount.setText("");
                    tv_after_discount.setText(products.getSize_prices().get(0).getNet_price() + " " + context.getString(R.string.rsa));

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

    public void setLoaded() {
        isLoading = false;

    }

    public interface LoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(LoadMoreListener loadMoreListener)
    {
        this.loadMoreListener = loadMoreListener;
    }
    @Override
    public int getItemViewType(int position) {
        MainCategory.Products products = productsList.get(position);
        if (products != null) {
            return ITEM_DATA;
        } else {
            return ITEM_PROGRESS;
        }
    }
}
