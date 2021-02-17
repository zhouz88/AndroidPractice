package com.zhezhe.viewpractice.tiktok.auth;

import android.net.Uri;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Auth {
    public static final int AUTH_CODE = 100;

    private static final String CLIENT_ID_KEY = "client_id";
    private static final String CLIENT_SECRET_KEY = "client_secret";
    private static final String REDIRECT_URI_KEY = "redirect_uri";
    private static final String SCOPE_KEY = "scope";
    private static final String ACCESS_TOKEN_KEY = "access_token";

    private static final String CLIENT_ID = "221122121212121122121";
    private static final String CLIENT_SECRET = "DAFASFASDFASDF";

    private static final String SCOPE = "public+write";

    private static final String URI_FOR_AUTHORIZE_CODE = "https://tiktok.com/oauth/authorize";
    private static final String URI_FOR_TOKEN = "https://tiktok.com/oauth/token";

    public static final String REDIRECT_URI = "http://www.tiktok.com";


    public static  String useUriForAuthCodeClientIdAndRedirectURIAndScopeToBuildAuthUrl() {
        String url = Uri.parse(URI_FOR_AUTHORIZE_CODE)
                .buildUpon()
                .appendQueryParameter(CLIENT_ID_KEY, CLIENT_ID)
                .build()
                .toString();
        url += "&" + REDIRECT_URI_KEY + "=" + REDIRECT_URI;
        url += "&" + SCOPE_KEY + "=" + SCOPE;
        return url;
    }


    public static String
    useUriForTokenInterceptedAuthCodeClientIdClientSecretAndRedirectURIToGetAccessToken(String authCode) throws IOException {
        OkHttpClient  client = new OkHttpClient();
        RequestBody post = new FormBody.Builder()
                .add(CLIENT_ID_KEY, CLIENT_ID)
                .add(CLIENT_SECRET_KEY, CLIENT_SECRET)
                .add(REDIRECT_URI_KEY, REDIRECT_URI)
                .add("code", authCode).build();

        Request rq = new Request.Builder().url(URI_FOR_TOKEN).post(post).build();
        Response response = client.newCall(rq).execute();
        try {
            JSONObject object = new JSONObject(response.body().toString());
            return object.getString(ACCESS_TOKEN_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
