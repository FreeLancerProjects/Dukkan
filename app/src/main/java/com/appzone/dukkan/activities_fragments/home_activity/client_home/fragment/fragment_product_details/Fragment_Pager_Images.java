package com.appzone.dukkan.activities_fragments.home_activity.client_home.fragment.fragment_product_details;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.tags.Tags;
import com.squareup.picasso.Picasso;

public class Fragment_Pager_Images extends Fragment {

    private final static String TAG = "image";
    private ImageView image;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager_images,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Pager_Images newInstance(String image)
    {
        Bundle bundle = new Bundle();
        bundle.putString(TAG,image);
        Fragment_Pager_Images fragment_pager_images = new Fragment_Pager_Images();
        fragment_pager_images.setArguments(bundle);
        return fragment_pager_images;
    }
    private void initView(View view) {
        image = view.findViewById(R.id.image);

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            String image_endPoint = bundle.getString(TAG);
            UpdateUI(image_endPoint);

        }
    }

    private void UpdateUI(String image_endPoint) {

        Picasso.with(getActivity()).load(Uri.parse(Tags.IMAGE_URL+image_endPoint)).into(image);

    }
}
