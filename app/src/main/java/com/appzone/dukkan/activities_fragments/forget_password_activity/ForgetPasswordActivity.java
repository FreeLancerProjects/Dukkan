package com.appzone.dukkan.activities_fragments.forget_password_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.language_helper.LanguageHelper;
import com.appzone.dukkan.share.Common;

import java.util.Locale;

import io.paperdb.Paper;

public class ForgetPasswordActivity extends AppCompatActivity {

    private LinearLayout ll_back;
    private ImageView image_back;
    private EditText edt_phone,edt_email;
    private Button btn_recover_password;
    private String current_lang = "";
    private AlertDialog alertDialog;
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
        setContentView(R.layout.activity_forget_password);
        initView();

    }

    private void initView() {
        root = findViewById(R.id.root);
        image_back = findViewById(R.id.image_back);
        ll_back = findViewById(R.id.ll_back);


        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);

        }else
        {
            image_back.setImageResource(R.drawable.arrow_left);

        }

        edt_phone = findViewById(R.id.edt_phone);
        edt_email = findViewById(R.id.edt_email);
        btn_recover_password = findViewById(R.id.btn_recover_password);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_recover_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

    }

    private void checkData() {
        String m_phone = edt_phone.getText().toString().trim();
        String m_email = edt_email.getText().toString().trim();

        if (!TextUtils.isEmpty(m_phone)&&
                m_phone.length()==9&&
                !TextUtils.isEmpty(m_email))
        {
            edt_email.setError(null);
            edt_phone.setError(null);
            Common.CloseKeyBoard(this,edt_phone);
            ResetPassword(m_phone,m_email);
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

            if (TextUtils.isEmpty(m_email))
            {
                edt_email.setError(getString(R.string.field_req));

            }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())

            {
                edt_email.setError(getString(R.string.inv_email));

            }else
                {
                    edt_email.setError(null);
                }
        }
    }



    private void ResetPassword(String m_phone, String m_email) {
        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        //dialog.show();
        //CreateAlertDialog(this,getString(R.string.we_will_send_you_a_link_to_on_the_email_recover_your_password));
        //Common.CreateSnackBar(this,root,getString(R.string.something));
    }

    public  void CreateAlertDialog(Context context,String msg)
    {
         alertDialog = new AlertDialog.Builder(context)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog,null);
        Button doneBtn = view.findViewById(R.id.doneBtn);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        tv_msg.setText(msg);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });

        alertDialog.getWindow().getAttributes().windowAnimations=R.style.custom_dialog_animation;
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setView(view);
        alertDialog.show();
    }




}
