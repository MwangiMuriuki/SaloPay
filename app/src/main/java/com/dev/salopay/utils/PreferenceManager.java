package com.dev.salopay.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor preferenceEditor;
    private static final String PREF_NAME = Config.Companion.getGLOBAL_SHARED_PREF_KEY();
    public static String API_KEY = "API_KEY";
    public static String USER_PHONE_NUMBER = "USER_PHONE_NUMBER";
    public static String USER_ID_NUMBER = "USER_ID_NUMBER";
    public static String FIRST_NAME = "FIRST_NAME";
    public static String LAST_NAME = "LAST_NAME";
    public static String ADVANCE_LIMIT = "ADVANCE_LIMIT";
    public static String COMPANY_NAME = "COMPANY_NAME";


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

    public String getUserIdNumber() {
        return sharedPreferences.getString(USER_ID_NUMBER, "");
    }

    public void setUserIdNumber(String apiKey) {
        preferenceEditor.putString(USER_ID_NUMBER, apiKey);
        preferenceEditor.commit();
    }

    public String getFirstName() {
        return sharedPreferences.getString(FIRST_NAME, "");
    }

    public void setFirstName(String apiKey) {
        preferenceEditor.putString(FIRST_NAME, apiKey);
        preferenceEditor.commit();
    }

    public String getLastName() {
        return sharedPreferences.getString(LAST_NAME, "");
    }

    public void setLastName(String apiKey) {
        preferenceEditor.putString(LAST_NAME, apiKey);
        preferenceEditor.commit();
    }

    public String getAdvanceLimit() {
        return sharedPreferences.getString(ADVANCE_LIMIT, "");
    }

    public void setAdvanceLimit(String apiKey) {
        preferenceEditor.putString(ADVANCE_LIMIT, apiKey);
        preferenceEditor.commit();
    }

    public String getCompanyName() {
        return sharedPreferences.getString(COMPANY_NAME, "");
    }

    public void setCompanyName(String apiKey) {
        preferenceEditor.putString(COMPANY_NAME, apiKey);
        preferenceEditor.commit();
    }


}
