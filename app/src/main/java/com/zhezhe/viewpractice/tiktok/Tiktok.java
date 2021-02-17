package com.zhezhe.viewpractice.tiktok;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.zhezhe.viewpractice.objects.Shot;
import com.zhezhe.viewpractice.objects.User;
import com.zhezhe.viewpractice.utils.ModelUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Tiktok {
    private static final String TAG = "Tiktok API";

    public static final int COUNT_PER_PAGE = 12;

    private static final String API_URL = "https://api.tiktok.com/v1/";

    private static final String SHOTS_END_POINT = API_URL + "shots";

    private static final String USER_END_POINT = API_URL + "user";

    private static final String SHARED_PREFERENCE_AUTH = "auth";

    private static final String ACCESS_TOKEN_KEY = "access_token";

    private static final String USER_KEY = "user";

    private static final TypeToken<List<Shot>> SHOT_LIST_TYPE = new TypeToken<List<Shot>>() {
    };

    private static final TypeToken<User> USER_TYPE = new TypeToken<User>() {
    };

    private static final OkHttpClient client = new OkHttpClient();

    // id + password
    private static String accessToken;
    private static User user;


    //used for building request
    private static Request.Builder authRequestBuilder(String url) {
        return new Request.Builder().addHeader("Authorization", "Bearer " + accessToken).url(url);
    }

    // run request
    private static Response makeHttpRequest(Request request) throws IOException {
        Response response = client.newCall(request).execute();
        return response;
    }

    public static <T> T parseResponse(Response response, TypeToken<T> token) {
        String responseString = response.body().toString();
        return ModelUtils.toObject(responseString, token);
    }

    public static void tryToGetAccessTokenAndUserFromDiskByUsingApplicationContextSharedPreferences(Context context) {
        Tiktok.accessToken = loadAccessTokenFromDisk(context);
        if (accessToken != null) {
            user = loadUserFromDisk(context);
        }
    }

    public static boolean hasAccessTokenInDisk() {
        return accessToken != null;
    }

    public static void logout(Context context) {
        storeAccessTokenToDisk(context, null);
        storeUserToDisk(context, null);

        accessToken = null;
        user = null;
    }

    public static void callGetUserApiWithToken(Context context, String accessToken) throws IOException, JsonSyntaxException {
        Tiktok.accessToken = accessToken;
        storeAccessTokenToDisk(context, accessToken);

        Tiktok.user = callGetUserApi();
        storeUserToDisk(context, Tiktok.user);
    }

    public static User getCurrentUser() {
        return user;
    }

    public static void storeAccessTokenToDisk(Context context, String token) {
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(SHARED_PREFERENCE_AUTH, Context.MODE_PRIVATE);
        sp.edit().putString(ACCESS_TOKEN_KEY, token).apply();
    }

    public static String loadAccessTokenFromDisk(Context context) {
        SharedPreferences sp = context.getApplicationContext()
                .getSharedPreferences(SHARED_PREFERENCE_AUTH, Context.MODE_PRIVATE);
        return sp.getString(ACCESS_TOKEN_KEY, null);
    }

    public static void storeUserToDisk(Context context, User user) {
        ModelUtils.save(context, USER_KEY, user);
    }

    public static User loadUserFromDisk(Context context) {
        return ModelUtils.read(context, USER_KEY, new TypeToken<User>(){});
    }

    public static User callGetUserApi() throws IOException, JsonSyntaxException {
        return parseResponse(makeHttpRequest(authRequestBuilder(USER_END_POINT).build()), USER_TYPE);
    }

    public static List<Shot> getShots(int page) throws IOException, JsonSyntaxException {
        return parseResponse(makeHttpRequest(authRequestBuilder(SHOTS_END_POINT + "?page"+page).build()), SHOT_LIST_TYPE);
    }
}
