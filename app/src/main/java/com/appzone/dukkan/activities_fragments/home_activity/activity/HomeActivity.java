package com.appzone.dukkan.activities_fragments.home_activity.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.home_activity.fragment.Fragment_SubCategory;
import com.appzone.dukkan.activities_fragments.home_activity.fragment.fragment_home.Fragment_Home;
import com.appzone.dukkan.activities_fragments.home_activity.fragment.Fragment_Offers;
import com.appzone.dukkan.activities_fragments.home_activity.fragment.fragment_cart.Fragment_Delivery_Address;
import com.appzone.dukkan.activities_fragments.home_activity.fragment.fragment_cart.Fragment_Map;
import com.appzone.dukkan.activities_fragments.home_activity.fragment.fragment_cart.Fragment_MyCart;
import com.appzone.dukkan.activities_fragments.home_activity.fragment.fragment_cart.Fragment_Payment_Confirmation;
import com.appzone.dukkan.activities_fragments.home_activity.fragment.fragment_cart.Fragment_Review_Purchases;
import com.appzone.dukkan.activities_fragments.home_activity.fragment.fragment_home.sub_fragments.Fragment_Charging_Cards;
import com.appzone.dukkan.activities_fragments.home_activity.fragment.fragment_home.sub_fragments.Fragment_Food_Department;
import com.appzone.dukkan.language_helper.LanguageHelper;
import com.appzone.dukkan.models.MainCategory;
import com.appzone.dukkan.services.ServiceUpdateLocation;
import com.appzone.dukkan.share.Common;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private String current_lang = "";
    private AHBottomNavigation ah_bottom_nav;
    private Fragment_Home fragment_home;
    private Fragment_Offers fragment_offers;
    private Fragment_SubCategory fragment_subCategory;
    ////////////////////////////////////////
    private Fragment_MyCart fragment_myCart;
    private Fragment_Review_Purchases fragment_review_purchases;
    private Fragment_Delivery_Address fragment_delivery_address;
    private Fragment_Payment_Confirmation fragment_payment_confirmation;

    ///////////////////////////////////////
    private Fragment_Food_Department fragment_food_department;
    private Fragment_Charging_Cards fragment_charging_cards;
    ///////////////////////////////////////
    private Fragment_Map fragment_map;
    private final String fineLoc = Manifest.permission.ACCESS_FINE_LOCATION;

    private final int loc_req = 11;
    private final int gps_req = 12;


    ////////////////////////////////////////
    private Intent intentService;
    private AlertDialog gpsDialog;
    private LocationManager locationManager;
    private View root;
    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        super.attachBaseContext(LanguageHelper.onAttach(base,current_lang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();

    }

    private void initView()
    {
        root = findViewById(R.id.root);
        fragmentManager = getSupportFragmentManager();
        ah_bottom_nav = findViewById(R.id.ah_bottom_nav);

        ah_bottom_nav.setDefaultBackgroundColor(ContextCompat.getColor(this,R.color.white));
        ah_bottom_nav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ah_bottom_nav.setTitleTextSizeInSp(13,14);
        ah_bottom_nav.setForceTint(true);
        ah_bottom_nav.setColored(false);
        ah_bottom_nav.setAccentColor(ContextCompat.getColor(this,R.color.colorPrimary));
        ah_bottom_nav.setInactiveColor(ContextCompat.getColor(this,R.color.gray_text));

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home),R.drawable.bottom_nav_home,R.color.gray_text);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.offers),R.drawable.bottom_nav_offer,R.color.gray_text);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.cart),R.drawable.bottom_nav_cart,R.color.gray_text);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.my_order),R.drawable.bootom_nav_list,R.color.gray_text);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.me),R.drawable.bottom_nav_user,R.color.gray_text);

        ah_bottom_nav.addItem(item1);
        ah_bottom_nav.addItem(item2);
        ah_bottom_nav.addItem(item3);
        ah_bottom_nav.addItem(item4);
        ah_bottom_nav.addItem(item5);

        ah_bottom_nav.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                UpdateBottomNavPos(position);

                switch (position)
                {
                    case 0:
                        DisplayFragmentHome();
                        break;
                    case 1:
                        DisplayFragmentOffer();
                        break;
                    case 2:
                        DisplayFragmentMyCart();
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }
                return false;
            }
        });

        DisplayFragmentHome();



    }

    private void UpdateBottomNavPos(int pos)
    {
        ah_bottom_nav.setCurrentItem(pos,false);
    }

    public void DisplayFragmentHome()
    {
        if (fragment_home==null)
        {
            fragment_home = Fragment_Home.newInstance();
        }

        if (fragment_home.isAdded())
        {
            if (!fragment_home.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_home).commit();
                DisplayFragmentFood_Department();
                UpdateBottomNavPos(0);
            }
        }else
            {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_home,"fragment_home").addToBackStack("fragment_home").commit();
                DisplayFragmentFood_Department();

                UpdateBottomNavPos(0);
            }


            if (fragment_offers!=null&&fragment_offers.isAdded())
            {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }

        if (fragment_myCart!=null&&fragment_myCart.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_myCart).commit();
        }

        if (fragment_subCategory!=null&&fragment_subCategory.isAdded())
        {
            fragmentManager.popBackStack("fragment_subCategory",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }

    private void DisplayFragmentOffer()
    {
        if (fragment_offers==null)
        {
            fragment_offers = Fragment_Offers.newInstance();
        }

        if (fragment_offers.isAdded())
        {
            if (!fragment_offers.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_offers).commit();
                UpdateBottomNavPos(1);
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_offers,"fragment_offers").addToBackStack("fragment_offers").commit();
            UpdateBottomNavPos(1);
        }


        if (fragment_home!=null&&fragment_home.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }
        if (fragment_myCart!=null&&fragment_myCart.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_myCart).commit();
        }
        if (fragment_subCategory!=null&&fragment_subCategory.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_subCategory).commit();
        }

    }

    ////////////////////////////////////
    private void DisplayFragmentMyCart()
    {
        if (fragment_myCart==null)
        {
            fragment_myCart = Fragment_MyCart.newInstance();
        }

        if (fragment_myCart.isAdded())
        {
            if (!fragment_myCart.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_myCart).commit();
                UpdateBottomNavPos(2);
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_myCart,"fragment_myCart").addToBackStack("fragment_myCart").commit();
            UpdateBottomNavPos(2);
        }


        if (fragment_home!=null&&fragment_home.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }

        if (fragment_offers!=null&&fragment_offers.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_offers).commit();
        }


    }

    public void DisplayFragmentReview_Purchases()
    {
        if (fragment_review_purchases==null)
        {
            fragment_review_purchases = Fragment_Review_Purchases.newInstance();
        }

        if (fragment_review_purchases.isAdded())
        {
            if (!fragment_review_purchases.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_review_purchases).commit();
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_my_cart_container,fragment_review_purchases,"fragment_review_purchases").addToBackStack("fragment_review_purchases").commit();
        }


        if (fragment_delivery_address!=null&&fragment_delivery_address.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delivery_address).commit();
        }

        if (fragment_payment_confirmation!=null&&fragment_payment_confirmation.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_payment_confirmation).commit();
        }

    }
    public void DisplayFragmentDelivery_Address()
    {
        if (fragment_delivery_address==null)
        {
            fragment_delivery_address = Fragment_Delivery_Address.newInstance();
        }

        if (fragment_delivery_address.isAdded())
        {
            if (!fragment_delivery_address.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_delivery_address).commit();
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_my_cart_container,fragment_delivery_address,"fragment_delivery_address").addToBackStack("fragment_delivery_address").commit();
        }


        if (fragment_review_purchases!=null&&fragment_review_purchases.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_review_purchases).commit();
        }
        if (fragment_payment_confirmation!=null&&fragment_payment_confirmation.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_payment_confirmation).commit();
        }
        fragment_myCart.UpdateBasketUI();



    }
    public void DisplayFragmentPayment_Confirmation()
    {
        if (fragment_payment_confirmation==null)
        {
            fragment_payment_confirmation = Fragment_Payment_Confirmation.newInstance();
        }

        if (fragment_payment_confirmation.isAdded())
        {
            if (!fragment_payment_confirmation.isVisible())
            {
                fragmentManager.beginTransaction().show(fragment_payment_confirmation).commit();
            }
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_my_cart_container,fragment_payment_confirmation,"fragment_payment_confirmation").addToBackStack("fragment_payment_confirmation").commit();
        }


        if (fragment_review_purchases!=null&&fragment_review_purchases.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_review_purchases).commit();
        }
        if (fragment_delivery_address!=null&&fragment_delivery_address.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delivery_address).commit();
        }
        fragment_myCart.UpdateCarUI();


    }

    public void DisplayFragmentFood_Department()
    {
        if (fragment_food_department==null)
        {
            fragment_food_department = Fragment_Food_Department.newInstance();
        }
        if (fragment_food_department.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_food_department).commit();
        }else
            {
                fragmentManager.beginTransaction().add(R.id.fragment_home_sub_fragment_container,fragment_food_department,"fragment_food_department").addToBackStack("fragment_food_department").commit();
            }

            if (fragment_charging_cards!=null && fragment_charging_cards.isAdded() && fragment_charging_cards.isVisible())
            {
                fragmentManager.beginTransaction().hide(fragment_charging_cards).commit();
            }
    }

    public void DisplayFragmentCharging_Cards()
    {
        if (fragment_charging_cards==null)
        {
            fragment_charging_cards = Fragment_Charging_Cards.newInstance();
        }
        if (fragment_charging_cards.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_charging_cards).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_home_sub_fragment_container,fragment_charging_cards,"fragment_charging_cards").addToBackStack("fragment_charging_cards").commit();
        }

        if (fragment_food_department!=null && fragment_food_department.isAdded() && fragment_food_department.isVisible())
        {
            fragmentManager.beginTransaction().hide(fragment_food_department).commit();
        }
    }

    public void DisplayFragmentMap()
    {
        fragment_map = Fragment_Map.newInstance();

        fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_map,"fragment_map").addToBackStack("fragment_map").commit();

        if (fragment_delivery_address!=null&&fragment_delivery_address.isAdded())
        {
            fragmentManager.beginTransaction().hide(fragment_delivery_address).commit();
        }

        checkLocationPermission();

    }

    public void DisplayFragmentSubCategory(MainCategory.MainCategoryItems mainCategoryItems)
    {
        fragment_subCategory = Fragment_SubCategory.newInstance(mainCategoryItems);


        if (fragment_subCategory.isAdded())
            {
                fragmentManager.beginTransaction().show(fragment_subCategory).commit();
            }else
                {
                    fragmentManager.beginTransaction().add(R.id.fragment_home_container,fragment_subCategory,"fragment_subCategory").addToBackStack("fragment_subCategory").commit();

                }

                if (fragment_home!=null&&fragment_home.isAdded())
                {
                    fragmentManager.beginTransaction().hide(fragment_home).commit();
                }

    }


    private void checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this,fineLoc)== PackageManager.PERMISSION_GRANTED
                )
        {

            if (isGpsOpen())
            {
                StartLocationUpdate();
            }else
                {
                    CreateGpsDialog();
                }

        }else
            {
                String [] perm = {fineLoc};

                ActivityCompat.requestPermissions(this,perm,loc_req);
            }
    }

    private void StartLocationUpdate()
    {
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }

        intentService = new Intent(this, ServiceUpdateLocation.class);
        startService(intentService);
    }
    private void StopLocationUpdate()
    {
        if (intentService!=null)
        {
            stopService(intentService);
        }
    }

    ///////////////////////////////////
    private void CreateGpsDialog()
    {
        gpsDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .create();
        View view = LayoutInflater.from(this).inflate(R.layout.gps_layout,null);
        Button btn_allow = view.findViewById(R.id.btn_allow);
        Button btn_deny = view.findViewById(R.id.btn_deny);
        btn_allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsDialog.dismiss();
                openGps();

            }
        });

        btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsDialog.dismiss();
            }
        });

        gpsDialog.setView(view);
        gpsDialog.setCanceledOnTouchOutside(false);
        gpsDialog.getWindow().getAttributes().windowAnimations = R.style.custom_dialog_animation;
        gpsDialog.show();
    }

    private void openGps() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent,gps_req);
    }

    private boolean isGpsOpen()
    {
        if (locationManager == null)
        {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    ///////////////////////////////////
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenToLocationUpdate(Location location)
    {
        Log.e("ddd",location.getLatitude()+"_");
        if (fragment_map != null && fragment_map.isAdded() && fragment_map.isVisible())
        {
            if (fragment_map.isMapReady)
            {
               fragment_map.UpdateLocation(location);
            }
        }
    }
    ///////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment:fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == gps_req)
        {
            if (isGpsOpen())
            {
                StartLocationUpdate();
            }else
            {
                CreateGpsDialog();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment:fragmentList)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (requestCode == loc_req)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if (isGpsOpen())
                    {
                        StartLocationUpdate();
                    }else
                    {
                        CreateGpsDialog();
                    }
                }else
                    {
                        CreateToast(getString(R.string.gps_perm_denied));
                    }
            }
        }
    }

    public void Back()
    {

        if (fragment_home!=null && fragment_home.isAdded()&& fragment_home.isVisible())
        {
            fragmentManager.popBackStack();
            finish();
        }else if (fragment_map!=null&&fragment_map.isVisible())
        {
            fragmentManager.popBackStack("fragment_map",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().show(fragment_myCart).commit();
            fragmentManager.beginTransaction().show(fragment_delivery_address).commit();

        }else
            {
                DisplayFragmentHome();
            }
    }

    private void CreateToast(String msg)
    {
        Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
    }

    public void CreateSnackBar(String msg)
    {
        Common.CreateSnackBar(this,root,msg);
    }

    @Override
    public void onBackPressed() {
        Back();
    }

    @Override
    protected void onDestroy() {
        if (intentService!=null)
        {
            StopLocationUpdate();
        }
        super.onDestroy();

    }
}
