package com.appzone.dukkan.activities_fragments.product_details.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.tags.Tags;
import com.squareup.picasso.Picasso;

public class Fragment_Pager_Images extends Fragment {

    private final  String TAG = "image";
    private ImageView image;
    public static String image_end_point="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager_images,container,false);
        initView(view);
        return view;
    }


    private void initView(View view) {
        image = view.findViewById(R.id.image);


        UpdateUI(image_end_point);
    }

    private void UpdateUI(String image_endPoint) {

        Log.e("image_endPoint",image_endPoint);
        Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_URL+image_endPoint)).into(image);

    }


}
