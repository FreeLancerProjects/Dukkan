package com.appzone.dukkan.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.dukkan.R;
import com.appzone.dukkan.tags.Tags;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {

    private List<String> img_end_pointList;
    private Context context;

    public SliderPagerAdapter(List<String> img_end_pointList, Context context) {
        this.img_end_pointList = img_end_pointList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return img_end_pointList.size();
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_row,container,false);
        PhotoView image = view.findViewById(R.id.image);
        String endPoint = img_end_pointList.get(position);
        Picasso.with(context).load(Tags.IMAGE_URL+endPoint).fit().into(image);
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
