package com.appzone.dukkan.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.models.WeekOfferModel;
import com.appzone.dukkan.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderWeekPagerAdapter extends PagerAdapter {

    private List<WeekOfferModel.Offer> offerList;
    private Context context;

    public SliderWeekPagerAdapter(List<WeekOfferModel.Offer> offerList, Context context) {
        this.offerList = offerList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return offerList.size();
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_row,container,false);
        ImageView image = view.findViewById(R.id.image);
        WeekOfferModel.Offer offer = offerList.get(position);
        Picasso.with(context).load(Tags.IMAGE_URL+offer.getImage()).fit().into(image);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
