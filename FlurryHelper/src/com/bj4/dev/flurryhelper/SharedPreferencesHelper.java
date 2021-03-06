
package com.bj4.dev.flurryhelper;

import java.lang.ref.WeakReference;
import java.sql.Date;
import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    public interface Callback {
        public void onVersionChanged();

        public void onDatePeriodChanged();

        public void onShowChartOnProjectFragmentChanged();
    }

    private WeakReference<Callback> mCallback;

    public void setCallback(Callback cb) {
        mCallback = new WeakReference<Callback>(cb);
    }

    public void removeCallback() {
        mCallback.clear();
        mCallback = null;
    }

    private Context mContext;

    private SharedPreferences mPref;

    private static SharedPreferencesHelper sInstance;

    private static final String KEY_API_KEY = "api_key";

    private static final String KEY_SHOW_INTRODUCTION = "show_introduction";

    private static final String KEY_DISPLAY_PERIOD = "display_period";

    public static final int DISPLAY_PERIOD_ONE_DAY = 0;

    public static final int DISPLAY_PERIOD_ONE_WEEK = 1;

    public static final int DISPLAY_PERIOD_ONE_MONTH = 2;

    public static final int DISPLAY_PERIOD_THREE_MONTHS = 3;

    public static final int DISPLAY_PERIOD_HALF_YEAR = 4;

    public static final int DISPLAY_PERIOD_YEAR = 5;

    public static final String KEY_SHOW_CHART_IN_PROJECT_FRAGMENT = "key_show_chart_in_project_fragment";

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

    public int getDisplayPeriod() {
        return mPref.getInt(KEY_DISPLAY_PERIOD, DISPLAY_PERIOD_YEAR);
    }

    public void setDisplayPeriod(int option) {
        mPref.edit().putInt(KEY_DISPLAY_PERIOD, option).commit();
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().onDatePeriodChanged();
    }

    public String getStartDate() {
        Calendar calendar = Calendar.getInstance();
        final int period = getDisplayPeriod();
        switch (period) {
            case DISPLAY_PERIOD_ONE_DAY:
                calendar.add(Calendar.DATE, -1);
                break;
            case DISPLAY_PERIOD_ONE_WEEK:
                calendar.add(Calendar.DATE, -7);
                break;
            case DISPLAY_PERIOD_ONE_MONTH:
                calendar.add(Calendar.DATE, -30);
                break;
            case DISPLAY_PERIOD_THREE_MONTHS:
                calendar.add(Calendar.DATE, -91);
                break;
            case DISPLAY_PERIOD_HALF_YEAR:
                calendar.add(Calendar.DATE, -183);
                break;
            case DISPLAY_PERIOD_YEAR:
                calendar.add(Calendar.DATE, -365);
                break;
        }
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH);
    }

    public String getEndDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH);
    }

    public boolean isShowChartInProjectFragment() {
        return mPref.getBoolean(KEY_SHOW_CHART_IN_PROJECT_FRAGMENT, true);
    }

    public void setShowChartInProjectFragment(boolean show) {
        mPref.edit().putBoolean(KEY_SHOW_CHART_IN_PROJECT_FRAGMENT, show).commit();
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().onShowChartOnProjectFragmentChanged();
    }

}
