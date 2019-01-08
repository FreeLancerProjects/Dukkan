package com.appzone.dukkan.activities_fragments.home_activity.driver_home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.driver_home.fragment.Fragment_MyOrder;
import com.appzone.dukkan.activities_fragments.home_activity.driver_home.fragment.Fragment_Profile;
import com.appzone.dukkan.activities_fragments.home_activity.driver_home.fragment.Fragment_myNotification;
import com.appzone.dukkan.language_helper.LanguageHelper;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.Locale;

import io.paperdb.Paper;

public class DriverHomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private AHBottomNavigation ahBottomNavigation;
    private Fragment_myNotification fragment_myNotification;
    private Fragment_MyOrder fragment_myOrder;
    private Fragment_Profile fragment_profile;
    private String current_lang = "";
    private View root;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        current_lang =Paper.book().read("language",Locale.getDefault().getLanguage());
        super.attachBaseContext(LanguageHelper.onAttach(newBase,current_lang));
    }

    private void initView() {
        root=findViewById(R.id.root);
        fragmentManager=getSupportFragmentManager();
        ahBottomNavigation = findViewById(R.id.ah_bottom_nav);

        ahBottomNavigation.setDefaultBackgroundColor(ContextCompat.getColor(this,R.color.white));
        ahBottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ahBottomNavigation.setTitleTextSizeInSp(13,14);
        ahBottomNavigation.setForceTint(true);
        ahBottomNavigation.setColored(false);
        ahBottomNavigation.setAccentColor(ContextCompat.getColor(this,R.color.colorPrimary));
        ahBottomNavigation.setInactiveColor(ContextCompat.getColor(this,R.color.gray_text));

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.my_order),R.drawable.bottom_nav_home,R.color.gray_text);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.profile),R.drawable.bottom_nav_offer,R.color.gray_text);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.my_notification),R.drawable.bottom_nav_cart,R.color.gray_text);
      
        ahBottomNavigation.addItem(item1);
        ahBottomNavigation.addItem(item2);
        ahBottomNavigation.addItem(item3);
        
        ahBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                UpdateBottomNavPos(position);

                switch (position)
                {
                    case 0:
                        DisplayFragmentMyorder();
                        break;
                    case 1:
                        DisplayFragmentProfile();
                        break;
                    case 2:
                        DisplayFragmentMynotification();
                        break;
                   
                }
                return false;
            }
        });

        DisplayFragmentMyorder();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        initView();


    }
    private void UpdateBottomNavPos(int pos)
    {
        ahBottomNavigation.setCurrentItem(pos,false);
    }
    public void DisplayFragmentMyorder()
    {
        if (fragment_myOrder==null)
        {
            fragment_myOrder = Fragment_MyOrder.newInstance();
        }

        if (fragment_myOrder.isAdded())
        {
            if (!fragment_myOrder.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_myOrder).commit();
                UpdateBottomNavPos(0);
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_driver_home_container,fragment_myOrder,"fragment_myOrder").addToBackStack("fragment_myOrder").commit();

            UpdateBottomNavPos(0);
        }


        if (fragment_profile!=null&&fragment_profile.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }

        if (fragment_myNotification!=null&&fragment_myNotification.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_myNotification).commit();
        }

    }
    private void DisplayFragmentProfile()
    {
        if (fragment_profile==null)
        {
            fragment_profile = Fragment_Profile.newInstance();
        }

        if (fragment_profile.isAdded())
        {
            if (!fragment_profile.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_profile).commit();
                UpdateBottomNavPos(1);
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_driver_home_container,fragment_profile,"fragment_profile").addToBackStack("fragment_profile").commit();
            UpdateBottomNavPos(1);
        }


        if (fragment_myOrder!=null&&fragment_myOrder.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_myOrder).commit();
        }
        if (fragment_myNotification!=null&&fragment_myNotification.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_myNotification).commit();
        }

    }
    private void DisplayFragmentMynotification()
    {
        if (fragment_myNotification==null)
        {
            fragment_myNotification = fragment_myNotification.newInstance();
        }

        if (fragment_myNotification.isAdded())
        {
            if (!fragment_myNotification.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_myNotification).commit();
                UpdateBottomNavPos(2);
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_driver_home_container,fragment_myNotification,"fragment_notfy").addToBackStack("fragment_notfy").commit();
            UpdateBottomNavPos(2);
        }


        if (fragment_myOrder!=null&&fragment_myOrder.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_myOrder).commit();
        }
        if (fragment_profile!=null&&fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }

    }


}
