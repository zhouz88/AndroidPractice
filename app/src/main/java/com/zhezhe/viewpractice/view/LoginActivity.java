package com.zhezhe.viewpractice.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.zhezhe.viewpractice.R;
import com.zhezhe.viewpractice.tiktok.Tiktok;
import com.zhezhe.viewpractice.tiktok.auth.Auth;
import com.zhezhe.viewpractice.tiktok.auth.AuthActivity;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textView = findViewById(R.id.activity_login_btn);

        //sp 一个轻量级的存储框架，以键值对形式存储数据，存储在对应的xml文件中，会随着APP被卸载而清除
        Tiktok.tryToGetAccessTokenAndUserFromDiskByUsingApplicationContextSharedPreferences(this);

        if (Tiktok.hasAccessTokenInDisk()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, AuthActivity.class);
                    intent.putExtra("urlForAuthCode",
                            Auth.useUriForAuthCodeClientIdAndRedirectURIAndScopeToBuildAuthUrl());
                    startActivityForResult(intent, 101);
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {
            final String authCode = data.getStringExtra("authcode");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String token = Auth
                                .useUriForTokenInterceptedAuthCodeClientIdClientSecretAndRedirectURIToGetAccessToken(authCode);
                        Tiktok.callGetUserApiWithToken(LoginActivity.this, token);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (IOException | JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
