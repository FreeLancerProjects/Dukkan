package com.appzone.dukkan.activities_fragments.sign_up_activity.fragment_sign_up;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.sign_up_activity.SignUpActivity;
import com.appzone.dukkan.share.Common;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Delegate_SignUp extends Fragment{

    private EditText edt_name,edt_phone,edt_password;
    private Button btn_male,btn_female,btn_terms_conditions;
    private ImageView image_personal,image_icon1,image_id,image_icon2,image_vehicle_license,image_icon3,image_driving_license,image_icon4,image_front_photo,image_back_photo,image_arrow;
    private CheckBox checkbox_rule;
    private FrameLayout fl_sign_up;
    private Bitmap bitmap_personal,bitmap_id,bitmap_vehicle_licence,bitmap_driving_license,bitmap_front_photo,bitmap_back_photo;
    private final int IMG1=1,IMG2=2,IMG3=3,IMG4=4,IMG5=5,IMG6=6;
    private Uri uri1=null,uri2=null,uri3=null,uri4=null,uri5=null,uri6=null;
    private String gender = "1";
    private boolean accept_rule = false;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private SignUpActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delegate_signup,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Delegate_SignUp newInstance()
    {
        return new Fragment_Delegate_SignUp();
    }

    private void initView(View view) {
        activity = (SignUpActivity) getActivity();

        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_password = view.findViewById(R.id.edt_password);
        btn_male = view.findViewById(R.id.btn_male);
        btn_female = view.findViewById(R.id.btn_female);
        btn_terms_conditions = view.findViewById(R.id.btn_terms_conditions);
        image_personal = view.findViewById(R.id.image_personal);
        image_icon1 = view.findViewById(R.id.image_icon1);
        image_id = view.findViewById(R.id.image_id);
        image_icon2 = view.findViewById(R.id.image_icon2);
        image_vehicle_license = view.findViewById(R.id.image_vehicle_license);
        image_icon3 = view.findViewById(R.id.image_icon3);
        image_driving_license = view.findViewById(R.id.image_driving_license);
        image_icon4 = view.findViewById(R.id.image_icon4);
        image_front_photo = view.findViewById(R.id.image_front_photo);
        image_back_photo = view.findViewById(R.id.image_back_photo);
        image_arrow = view.findViewById(R.id.image_arrow);

        checkbox_rule = view.findViewById(R.id.checkbox_rule);
        fl_sign_up = view.findViewById(R.id.fl_sign_up);


        Paper.init(getActivity());
        String lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (lang.equals("ar"))
        {
            image_arrow.setImageResource(R.drawable.arrow_right);
        }else
            {
                image_arrow.setImageResource(R.drawable.arrow_left);

            }

        image_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(IMG1);
            }
        });

        image_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(IMG2);
            }
        });

        image_vehicle_license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(IMG3);
            }
        });
        image_driving_license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(IMG4);
            }
        });

        image_front_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(IMG5);
            }
        });
        image_back_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(IMG6);
            }
        });

        btn_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_male.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                btn_male.setBackgroundResource(R.drawable.btn_login_bg);

                gender = "1";

                btn_female.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray_text));
                btn_female.setBackgroundResource(R.drawable.btn_female_bg);
            }
        });

        btn_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_female.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
                btn_female.setBackgroundResource(R.drawable.btn_login_bg);
                gender = "2";

                btn_male.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray_text));
                btn_male.setBackgroundResource(R.drawable.btn_female_bg);
            }
        });

        checkbox_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox_rule.isChecked())
                {
                    accept_rule = true;
                }else
                    {
                        accept_rule = false;
                    }
            }
        });

        fl_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

        btn_terms_conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentTerms_Conditions();
            }
        });
    }


    private void Check_ReadPermission(int img_req)
    {
        if (ContextCompat.checkSelfPermission(getActivity(),read_permission)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{read_permission},img_req);
        }else
            {
                select_photo(img_req);
            }
    }


    private void select_photo(int img1) {
        Intent intent ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }else
            {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/*");
                startActivityForResult(intent,img1);

            }
    }

    private void CheckData() {
        String m_name = edt_name.getText().toString().trim();
        String m_phone = edt_phone.getText().toString().trim();
        String m_password = edt_password.getText().toString().trim();

        if (!TextUtils.isEmpty(m_name)&&
                !TextUtils.isEmpty(m_phone)&&
                m_phone.length()==9&&
                !TextUtils.isEmpty(m_password)&&
                accept_rule&&
                !TextUtils.isEmpty(gender)&&
                uri1!=null&&
                uri2!=null&&
                uri3!=null&&
                uri4!=null&&
                uri5!=null&&
                uri6!=null

                )
        {
            Common.CloseKeyBoard(getActivity(),edt_name);
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_password.setError(null);

            Sign_Up(m_name,m_phone,m_password,gender,uri1,uri2,uri3,uri5,uri5,uri6);
        }else
            {
                if (TextUtils.isEmpty(m_name))
                {
                    edt_name.setError(getString(R.string.field_req));
                }else
                    {
                        edt_name.setError(null);

                    }
                if (TextUtils.isEmpty(m_phone))
                {
                    edt_phone.setError(getString(R.string.field_req));
                }else if (m_phone.length()!=9)
                {
                    edt_phone.setError(getString(R.string.inv_phone));
                }
                else
                {
                    edt_phone.setError(null);

                }

                if (TextUtils.isEmpty(m_password))
                {
                    edt_password.setError(getString(R.string.field_req));
                }else
                {
                    edt_password.setError(null);

                }


                if (uri1 == null)
                {
                    CreateToast(getString(R.string.pers_photo_req));
                }

                if (uri2 == null)
                {
                    CreateToast(getString(R.string.id_photo_req));
                }

                if (uri3 == null)
                {
                    CreateToast(getString(R.string.license_photo_required));
                }

                if (uri4 == null)
                {
                    CreateToast(getString(R.string.driving_photo_req));
                }

                if (uri5 == null)
                {
                    CreateToast(getString(R.string.front_vehicle_photo));
                }

                if (uri6 == null)
                {
                    CreateToast(getString(R.string.back_vehicle_photo));
                }

                if (!accept_rule)
                {
                    CreateToast(getString(R.string.cnt_sign_up));
                }
            }

    }

    private void Sign_Up(String m_name, String m_phone, String m_password, String gender, Uri uri1, Uri uri2, Uri uri3, Uri uri5, Uri uri51, Uri uri6) {

    }
    public void update_checkbox(boolean isChecked)
    {
        accept_rule = isChecked;
        checkbox_rule.setChecked(isChecked);
    }
    private void CreateToast(String msg)
    {
        Toast.makeText(getActivity(),msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG1 && resultCode == Activity.RESULT_OK && data!=null)
        {
            image_icon1.setVisibility(View.GONE);
            uri1 = data.getData();
            bitmap_personal = BitmapFactory.decodeFile(Common.getImagePath(getActivity(),uri1));
            image_personal.setImageBitmap(bitmap_personal);
        }else if (requestCode == IMG2 && resultCode == Activity.RESULT_OK && data!=null)
        {
            image_icon2.setVisibility(View.GONE);
            uri2 = data.getData();
            bitmap_id = BitmapFactory.decodeFile(Common.getImagePath(getActivity(),uri2));
            image_id.setImageBitmap(bitmap_id);
        }
        else if (requestCode == IMG3 && resultCode == Activity.RESULT_OK && data!=null)
        {
            image_icon3.setVisibility(View.GONE);
            uri3 = data.getData();
            bitmap_vehicle_licence = BitmapFactory.decodeFile(Common.getImagePath(getActivity(),uri3));
            image_vehicle_license.setImageBitmap(bitmap_vehicle_licence);
        }
        else if (requestCode == IMG4 && resultCode == Activity.RESULT_OK && data!=null)
        {
            image_icon4.setVisibility(View.GONE);
            uri4 = data.getData();
            bitmap_driving_license = BitmapFactory.decodeFile(Common.getImagePath(getActivity(),uri4));
            image_driving_license.setImageBitmap(bitmap_driving_license);
        }
        else if (requestCode == IMG5 && resultCode == Activity.RESULT_OK && data!=null)
        {
            uri5 = data.getData();
            bitmap_front_photo = BitmapFactory.decodeFile(Common.getImagePath(getActivity(),uri5));
            image_front_photo.setImageBitmap(bitmap_front_photo);
        }
        else if (requestCode == IMG6 && resultCode == Activity.RESULT_OK && data!=null)
        {
            uri6 = data.getData();
            bitmap_back_photo = BitmapFactory.decodeFile(Common.getImagePath(getActivity(),uri6));
            image_back_photo.setImageBitmap(bitmap_back_photo);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG1)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    select_photo(IMG1);
                }else
                    {
                      CreateToast(getString(R.string.perm_image_denied));
                    }
            }
        }else if (requestCode == IMG2)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    select_photo(IMG2);
                }else
                {
                    CreateToast(getString(R.string.perm_image_denied));
                }
            }
        }
        else if (requestCode == IMG3)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    select_photo(IMG3);
                }else
                {
                    CreateToast(getString(R.string.perm_image_denied));
                }
            }
        }else if (requestCode == IMG4)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    select_photo(IMG4);
                }else
                {
                    CreateToast(getString(R.string.perm_image_denied));
                }
            }
        }
        else if (requestCode == IMG5)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    select_photo(IMG5);
                }else
                {
                    CreateToast(getString(R.string.perm_image_denied));
                }
            }
        }
        else if (requestCode == IMG6)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    select_photo(IMG6);
                }else
                {
                    CreateToast(getString(R.string.perm_image_denied));
                }
            }
        }
    }
}
