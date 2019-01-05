package com.appzone.dukkan.activities_fragments.home_activity.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appzone.dukkan.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Home extends Fragment {

    private ImageView image_back;
    private AppBarLayout app_bar;
    private LinearLayout ll_toolbar_container;

    private ImageView image_collapse,image_arrow;
    private FrameLayout fl_departments;
    private ExpandableLayout expand_layout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Home newInstance()
    {
        return new Fragment_Home();
    }
    private void initView(View view) {
        ll_toolbar_container = view.findViewById(R.id.ll_toolbar_container);
        app_bar = view.findViewById(R.id.app_bar);
        image_back = view.findViewById(R.id.image_back);
        Paper.init(getActivity());
        String current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);
        }else
        {
            image_back.setImageResource(R.drawable.arrow_left);

        }
        image_collapse = view.findViewById(R.id.image_collapse);
        image_arrow = view.findViewById(R.id.image_arrow);
        fl_departments = view.findViewById(R.id.fl_departments);
        expand_layout = view.findViewById(R.id.expand_layout);

        fl_departments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!expand_layout.isExpanded())
                {
                    image_arrow.animate().rotation(-180f).setDuration(1500).start();
                }
                expand_layout.setExpanded(true,true);
            }
        });

        image_collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_arrow.animate().rotation(0f).setDuration(1500).start();



                expand_layout.collapse(true);
            }
        });

        expand_layout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                if (state == ExpandableLayout.State.EXPANDED)
                {
                    image_collapse.setVisibility(View.VISIBLE);
                    fl_departments.setBackgroundResource(R.drawable.btn_login_bg);
                }else if (state == ExpandableLayout.State.COLLAPSED)
                {
                    image_collapse.setVisibility(View.GONE);
                    fl_departments.setBackgroundResource(R.drawable.btn_sign_up_bg);

                }
            }
        });

        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            int scroll_range = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (scroll_range==-1)
                {
                    scroll_range = appBarLayout.getTotalScrollRange();
                }


                if (scroll_range+verticalOffset==0)
                {
                    ll_toolbar_container.setVisibility(View.VISIBLE);
                }else
                {
                    ll_toolbar_container.setVisibility(View.GONE);

                }

            }
        });



    }
}
