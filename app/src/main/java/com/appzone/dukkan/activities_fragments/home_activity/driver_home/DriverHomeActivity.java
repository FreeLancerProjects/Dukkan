package com.appzone.dukkan.activities_fragments.home_activity.driver_home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.driver_home.fragment.Fragment_Driver_Orders;
import com.appzone.dukkan.activities_fragments.home_activity.driver_home.fragment.Fragment_Driver_Profile;
import com.appzone.dukkan.activities_fragments.home_activity.driver_home.fragment.Fragment_Driver_Notification;
import com.appzone.dukkan.language_helper.LanguageHelper;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.Locale;

import io.paperdb.Paper;

public class DriverHomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private AHBottomNavigation ahBottomNavigation;
    private Fragment_Driver_Notification fragment_driver_notification;
    private Fragment_Driver_Orders fragment_driver_orders;
    private Fragment_Driver_Profile fragment_driver_profile;
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

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.my_order),R.drawable.bottom_nav_cart,R.color.gray_text);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.profile),R.drawable.bottom_nav_user,R.color.gray_text);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.my_notification),R.drawable.nav_bottom_notfication,R.color.gray_text);
      
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
                        DisplayFragmentDriverOrders();
                        break;
                    case 1:
                        DisplayFragmentDriverProfile();
                        break;
                    case 2:
                        DisplayFragmentDriverNotification();
                        break;
                   
                }
                return false;
            }
        });

        DisplayFragmentDriverOrders();

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
    public void DisplayFragmentDriverOrders()
    {
        if (fragment_driver_orders == null)
        {
            fragment_driver_orders = Fragment_Driver_Orders.newInstance();
        }

        if (fragment_driver_orders.isAdded())
        {
            if (!fragment_driver_orders.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_driver_orders).commit();
                UpdateBottomNavPos(0);
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_driver_home_container, fragment_driver_orders,"fragment_driver_orders").addToBackStack("fragment_driver_orders").commit();

            UpdateBottomNavPos(0);
        }


        if (fragment_driver_profile !=null&& fragment_driver_profile.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_driver_profile).commit();
        }

        if (fragment_driver_notification !=null&& fragment_driver_notification.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_driver_notification).commit();
        }

    }
    private void DisplayFragmentDriverProfile()
    {
        if (fragment_driver_profile ==null)
        {
            fragment_driver_profile = Fragment_Driver_Profile.newInstance();
        }

        if (fragment_driver_profile.isAdded())
        {
            if (!fragment_driver_profile.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_driver_profile).commit();
                UpdateBottomNavPos(1);
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_driver_home_container, fragment_driver_profile,"fragment_driver_profile").addToBackStack("fragment_driver_profile").commit();
            UpdateBottomNavPos(1);
        }


        if (fragment_driver_orders !=null&& fragment_driver_orders.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_driver_orders).commit();
        }
        if (fragment_driver_notification !=null&& fragment_driver_notification.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_driver_notification).commit();
        }

    }
    private void DisplayFragmentDriverNotification()
    {
        if (fragment_driver_notification ==null)
        {
            fragment_driver_notification = fragment_driver_notification.newInstance();
        }
        if (fragment_driver_notification.isAdded())
        {
            if (!fragment_driver_notification.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_driver_notification).commit();
                UpdateBottomNavPos(2);
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_driver_home_container, fragment_driver_notification,"fragment_driver_notification").addToBackStack("fragment_driver_notification").commit();
            UpdateBottomNavPos(2);
        }


        if (fragment_driver_orders !=null&& fragment_driver_orders.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_driver_orders).commit();
        }
        if (fragment_driver_profile !=null&& fragment_driver_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_driver_profile).commit();
        }

    }


}
