package com.appzone.dukkan.activities_fragments.home_activity.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.appzone.dukkan.R;

public class HomeActivity extends AppCompatActivity {

    ImageView image_back;
    AppBarLayout app_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        image_back = findViewById(R.id.image_back);
        app_bar = findViewById(R.id.app_bar);

        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            int scroll_range = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (scroll_range==-1)
                {
                    scroll_range = appBarLayout.getTotalScrollRange();
                }

                Log.e("scroll_range",scroll_range+" ");
                Log.e("verticalOffset",verticalOffset+" ");

                if (scroll_range+verticalOffset==0)
                {
                    image_back.setVisibility(View.VISIBLE);
                }else
                    {
                        image_back.setVisibility(View.GONE);
                    }

            }
        });


    }
}
