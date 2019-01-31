package com.appzone.dukkan.activities_fragments.activity_splash;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.activity_home.client_home.activity.HomeActivity;
import com.appzone.dukkan.activities_fragments.activity_home.delegate_home.DelegateHomeActivity;
import com.appzone.dukkan.activities_fragments.activity_sign_in.SignInActivity;
import com.appzone.dukkan.models.UserModel;
import com.appzone.dukkan.preferences.Preferences;
import com.appzone.dukkan.singletone.UserSingleTone;
import com.appzone.dukkan.tags.Tags;

public class SplashActivity extends AppCompatActivity {

    private ImageView image;
    private ProgressBar prgBar;
    private Preferences preferences;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferences = Preferences.getInstance();
        userSingleTone = UserSingleTone.getInstance();

        image = findViewById(R.id.image);
        prgBar = findViewById(R.id.progBar);
        prgBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade);
        image.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String session = preferences.getSession(SplashActivity.this);
                Log.e("Session",session);
                if (session.equals(Tags.session_login))
                {
                    userModel = preferences.getUserData(SplashActivity.this);
                    userSingleTone.setUserModel(userModel);

                    if (userModel.getUser().getRole().equals(Tags.user_client))
                    {
                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else if (userModel.getUser().getRole().equals(Tags.user_delegate))
                    {
                        Intent intent = new Intent(SplashActivity.this, DelegateHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }else
                    {


                        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
