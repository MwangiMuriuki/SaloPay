package com.dev.salopay.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor preferenceEditor;
    private static final String PREF_NAME = Config.Companion.getGLOBAL_SHARED_PREF_KEY();
    public static String API_KEY = "API_KEY";
    public static String USER_PHONE_NUMBER = "USER_PHONE_NUMBER";

    public PreferenceManager(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferenceEditor = sharedPreferences.edit();
    }

    public String getApiKey() {
        return sharedPreferences.getString(API_KEY, "");
    }

    public void setApiKey(String apiKey) {
        preferenceEditor.putString(API_KEY, apiKey);
        preferenceEditor.commit();
    }

    public String getUserPhoneNumber() {
        return sharedPreferences.getString(USER_PHONE_NUMBER, "");
    }

    public void setUserPhoneNumber(String apiKey) {
        preferenceEditor.putString(USER_PHONE_NUMBER, apiKey);
        preferenceEditor.commit();
    }
}
