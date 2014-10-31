
package com.bj4.dev.flurryhelper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private Context mContext;

    private SharedPreferences mPref;

    private static SharedPreferencesHelper sInstance;

    private static final String KEY_API_KEY = "api_key";

    private static final String KEY_SHOW_INTRODUCTION = "show_introduction";

    private static final String TAG = "SharedPreferencesHelper";

    public static synchronized SharedPreferencesHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharedPreferencesHelper(context);
        }
        return sInstance;
    }

    private SharedPreferencesHelper(Context context) {
        mContext = context.getApplicationContext();
        mPref = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    public boolean hasShowIntroduction() {
        return mPref.getBoolean(KEY_SHOW_INTRODUCTION, false);
    }

    public void setShowIntroduction(boolean hasShow) {
        mPref.edit().putBoolean(KEY_SHOW_INTRODUCTION, hasShow).commit();
    }

    public String getAPIKey() {
        return mPref.getString(KEY_API_KEY, "");
    }

    public void setAPIKey(String key) {
        mPref.edit().putString(KEY_API_KEY, key).commit();
    }
}
