package com.appzone.dukkan.activities_fragments.chat_activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.dukkan.R;
import com.appzone.dukkan.language_helper.LanguageHelper;
import com.appzone.dukkan.models.UserChatModel;
import com.appzone.dukkan.models.UserModel;
import com.appzone.dukkan.preferences.Preferences;
import com.appzone.dukkan.singletone.UserSingleTone;
import com.appzone.dukkan.tags.Tags;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import pl.tajchert.waitingdots.DotsTextView;

public class ChatActivity extends AppCompatActivity {
    private LinearLayout ll_back,ll_rate,ll_typing;
    private ImageView image_back,image_client,image_call,image_client_typing,image_upload;
    private CircleImageView image_delegate,image_delegate_typing;
    private TextView tv_delegate,tv_name,tv_rate;
    private SimpleRatingBar rateBar;
    private RecyclerView recView;
    private LinearLayoutManager manager;
    private ProgressBar progBar;
    private DotsTextView tv_wait_dot;
    private Button btn_send;
    private EditText edt_msg_content;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private UserChatModel userChatModel;
    private Preferences preferences;
    private String current_lang;
    private final String read_perm = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final int read_req = 1,img_req = 22;

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        super.attachBaseContext(LanguageHelper.onAttach(base,current_lang));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        getDataFromIntent();
    }



    private void initView() {
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        preferences = Preferences.getInstance();
        Paper.init(this);
        current_lang = Paper.book().read("lang",Locale.getDefault().getLanguage());
        LanguageHelper.setLocality(this,current_lang);

        image_back = findViewById(R.id.image_back);
        if (current_lang.equals("ar"))
        {
            image_back.setImageResource(R.drawable.arrow_right);
        }else
        {
            image_back.setImageResource(R.drawable.arrow_left);

        }

        ll_back = findViewById(R.id.ll_back);
        ll_rate = findViewById(R.id.ll_rate);
        ll_typing = findViewById(R.id.ll_typing);
        image_client = findViewById(R.id.image_client);
        image_call = findViewById(R.id.image_call);
        image_client_typing = findViewById(R.id.image_client_typing);
        image_upload = findViewById(R.id.image_upload);
        image_delegate = findViewById(R.id.image_delegate);
        image_delegate_typing = findViewById(R.id.image_delegate_typing);
        tv_delegate = findViewById(R.id.tv_delegate);
        tv_name = findViewById(R.id.tv_name);
        tv_rate = findViewById(R.id.tv_rate);
        rateBar = findViewById(R.id.rateBar);
        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        manager.setStackFromEnd(true);
        manager.setReverseLayout(false);
        recView.setLayoutManager(manager);
        progBar = findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        tv_wait_dot = findViewById(R.id.tv_wait_dot);
        btn_send = findViewById(R.id.btn_send);
        edt_msg_content = findViewById(R.id.edt_msg_content);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        image_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+"00966"+userChatModel.getPhone()));
                startActivity(intent);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edt_msg_content.getText().toString().trim();
                if (!TextUtils.isEmpty(msg))
                {
                    SendMessage(msg);
                }
            }
        });

        image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckReadPermission();
            }
        });
    }



    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            this.userChatModel = (UserChatModel) intent.getSerializableExtra("user_chat_data");
            UpdateUI(this.userChatModel);
        }
    }

    private void UpdateUI(UserChatModel userChatModel)
    {

        preferences.create_update_chat_user_id(this,String.valueOf(userChatModel.getId()));
        if (userModel.getUser().getRole().equals(Tags.user_client))
        {
            Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+userChatModel.getImage())).fit().into(image_delegate);
            image_delegate.setVisibility(View.VISIBLE);
            tv_delegate.setVisibility(View.VISIBLE);

            tv_rate.setText("("+userChatModel.getRate()+")");
            SimpleRatingBar.AnimationBuilder builder = rateBar.getAnimationBuilder();
            builder.setDuration(1500);
            builder.setRepeatCount(0);
            builder.setRatingTarget((float) userChatModel.getRate());
            builder.setInterpolator(new AccelerateInterpolator());
            builder.start();
            ll_rate.setVisibility(View.VISIBLE);

            Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+userChatModel.getImage())).fit().into(image_delegate_typing);
            image_delegate_typing.setVisibility(View.VISIBLE);


        }else if (userModel.getUser().getRole().equals(Tags.user_delegate))
        {
            image_client.setVisibility(View.VISIBLE);
            image_client_typing.setVisibility(View.VISIBLE);
        }
        tv_name.setText(userChatModel.getName());
    }

    private void CheckReadPermission()
    {
        if (ContextCompat.checkSelfPermission(this,read_perm)!= PackageManager.PERMISSION_GRANTED)
        {
            String perm [] ={read_perm};
            ActivityCompat.requestPermissions(this,perm,read_req);
        }else
            {
                SelectImage();
            }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == read_req)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    SelectImage();
                }else
                    {
                        Toast.makeText(this, R.string.access_image_denied, Toast.LENGTH_LONG).show();
                    }
            }
        }
    }

    private void SelectImage() {

        Intent intent ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }else
        {
            intent = new Intent(Intent.ACTION_GET_CONTENT);

        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(intent,img_req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == img_req && resultCode == RESULT_OK && data!=null)
        {
            Uri uri = data.getData();
            UploadImage(uri);
        }
    }

    private void UploadImage(Uri uri) {
        //MultipartBody.Part image_part = Common.getMultiPart(this,uri,"");
    }

    private void SendMessage(String msg) {
    }
}
