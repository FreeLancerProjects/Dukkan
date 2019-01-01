package com.appzone.dukkan.activities_fragments.sign_in_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.forget_password_activity.ForgetPasswordActivity;
import com.appzone.dukkan.activities_fragments.sign_up_activity.SignUpActivity;
import com.appzone.dukkan.language_helper.LanguageHelper;
import com.appzone.dukkan.share.Common;

import java.util.Locale;

import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity {

    private LinearLayout ll_back;
    private ImageView image_back;
    private EditText edt_phone,edt_password;
    private Button btn_sign_in,btn_new_account,btn_forget_password,btn_change_language;
    private LinearLayout ll_skip,ll_add_delegate;
    private String current_lang = "";
    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        super.attachBaseContext(LanguageHelper.onAttach(base,current_lang));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();

    }

    private void initView() {

        image_back = findViewById(R.id.image_back);
        ll_back = findViewById(R.id.ll_back);

        LanguageHelper.setLocality(this,current_lang);

        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);

        }else
            {
                image_back.setImageResource(R.drawable.arrow_left);

            }

        edt_phone = findViewById(R.id.edt_phone);
        edt_password = findViewById(R.id.edt_password);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_new_account = findViewById(R.id.btn_new_account);
        btn_forget_password = findViewById(R.id.btn_forget_password);
        btn_change_language = findViewById(R.id.btn_change_language);
        ll_add_delegate = findViewById(R.id.ll_add_delegate);
        ll_skip = findViewById(R.id.ll_skip);

         current_lang = Paper.book().read("lang",Locale.getDefault().getLanguage());
        if (current_lang.equals("ar"))
        {

            btn_change_language.setText(getString(R.string.change_language_to_english));
        }else
            {
                btn_change_language.setText(getString(R.string.change_language_to_arabic));

            }



        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_change_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_lang.equals("ar"))
                {
                    LanguageHelper.setLocality(SignInActivity.this,"en");
                    Paper.book().write("lang","en");
                    Refresh();
                }else
                    {
                        LanguageHelper.setLocality(SignInActivity.this,"ar");
                        Paper.book().write("lang","ar");
                        Refresh();
                    }
            }
        });


        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateToSignUpActivity("1");
            }
        });

        ll_add_delegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 NavigateToSignUpActivity("2");
            }
        });

        btn_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void NavigateToSignUpActivity(String type)
    {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
        finish();
    }
    private void CheckData() {
        String m_phone = edt_phone.getText().toString().trim();
        String m_password = edt_password.getText().toString().trim();

        if (!TextUtils.isEmpty(m_phone)&&
                m_phone.length()==9&&
                !TextUtils.isEmpty(m_password))
        {
            edt_password.setError(null);
            edt_phone.setError(null);
            Common.CloseKeyBoard(this,edt_phone);
            login(m_phone,m_password);
        }else
            {
                if (TextUtils.isEmpty(m_phone))
                {
                    edt_phone.setError(getString(R.string.field_req));
                }else if (m_phone.length()!=9)
                {
                    edt_phone.setError(getString(R.string.inv_phone));

                }else
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
            }

    }

    private void login(String m_phone, String m_password) {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.signning_in));
        //dialog.show();
        String phone = "00966"+m_phone;
    }

    private void Refresh()
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


}
