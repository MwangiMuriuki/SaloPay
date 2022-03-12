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
    public static String CYCLE_ID = "CYCLE_ID";


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

    public void setUserPhoneNumber(String userPhoneNumber) {
        preferenceEditor.putString(USER_PHONE_NUMBER, userPhoneNumber);
        preferenceEditor.commit();
    }

    public String getUserIdNumber() {
        return sharedPreferences.getString(USER_ID_NUMBER, "");
    }

    public void setUserIdNumber(String userIdNumber) {
        preferenceEditor.putString(USER_ID_NUMBER, userIdNumber);
        preferenceEditor.commit();
    }

    public String getFirstName() {
        return sharedPreferences.getString(FIRST_NAME, "");
    }

    public void setFirstName(String firstName) {
        preferenceEditor.putString(FIRST_NAME, firstName);
        preferenceEditor.commit();
    }

    public String getLastName() {
        return sharedPreferences.getString(LAST_NAME, "");
    }

    public void setLastName(String lastName) {
        preferenceEditor.putString(LAST_NAME, lastName);
        preferenceEditor.commit();
    }

    public String getAdvanceLimit() {
        return sharedPreferences.getString(ADVANCE_LIMIT, "");
    }

    public void setAdvanceLimit(String advanceLimit) {
        preferenceEditor.putString(ADVANCE_LIMIT, advanceLimit);
        preferenceEditor.commit();
    }

    public String getCompanyName() {
        return sharedPreferences.getString(COMPANY_NAME, "");
    }

    public void setCompanyName(String companyName) {
        preferenceEditor.putString(COMPANY_NAME, companyName);
        preferenceEditor.commit();
    }

    public String getCycleId() {
        return sharedPreferences.getString(CYCLE_ID, "");
    }

    public void setCycleId(String cycleId) {
        preferenceEditor.putString(CYCLE_ID, cycleId);
        preferenceEditor.commit();
    }


}
