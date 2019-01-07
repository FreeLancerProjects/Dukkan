package com.appzone.dukkan.activities_fragments.sign_up_activity.fragment_sign_up;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.appzone.dukkan.R;
import com.appzone.dukkan.activities_fragments.sign_up_activity.SignUpActivity;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class Fragment_Terms_Conditions extends Fragment{

    private TextView tv_content;
    private FrameLayout fl_accept;
    private SmoothProgressBar smooth_progress;
    private ListenForTermsAndCondition listener;
    private SignUpActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms_condition,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Terms_Conditions newInstance()
    {
        return new Fragment_Terms_Conditions();
    }

    private void initView(View view) {

        tv_content = view.findViewById(R.id.tv_content);
        fl_accept = view.findViewById(R.id.fl_accept);
        smooth_progress = view.findViewById(R.id.smooth_progress);

        fl_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChecked(true);
            }
        });
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SignUpActivity) context;
        listener = activity;
    }

    public interface ListenForTermsAndCondition
    {
        void onChecked(boolean isChecked);
    }


}
