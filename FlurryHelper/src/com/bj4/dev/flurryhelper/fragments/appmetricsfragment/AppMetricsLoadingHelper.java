
package com.bj4.dev.flurryhelper.fragments.appmetricsfragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.SharedPreferencesHelper;
import com.bj4.dev.flurryhelper.utils.AppMetricsData;
import com.bj4.dev.flurryhelper.utils.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AppMetricsLoadingHelper extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "AppMetricsLoadingHelper";

    private static final boolean DEBUG = true;

    public interface Callback {
        public void notifyDataChanged();
    }

    private final String mProjectKey;

    private final WeakReference<Context> mContext;

    private final WeakReference<Callback> mCallback;

    private final int mCurrentTimePeriod;

    private final int mCurrentVersion = 0;

    public AppMetricsLoadingHelper(Context context, String projectKey, Callback cb) {
        mProjectKey = projectKey;
        mContext = new WeakReference<Context>(context);
        mCallback = new WeakReference<Callback>(cb);
        mCurrentTimePeriod = SharedPreferencesHelper.getInstance(context).getDisplayPeriod();
    }

    @Override
    protected Void doInBackground(Void... params) {
        final Context context = mContext.get();
        if (context == null)
            return null;
        final String apiKey = SharedPreferencesHelper.getInstance(context).getAPIKey();
        final String endDate = SharedPreferencesHelper.getInstance(context).getEndDate();
        final String startDate = SharedPreferencesHelper.getInstance(context).getStartDate();
        loadAppMetric(apiKey, startDate, endDate, SharedData.APPMETRICS_TYPE_ACTIVE_USERS);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        loadAppMetric(apiKey, startDate, endDate, SharedData.APPMETRICS_TYPE_ACTIVE_USERS_BY_MONTH);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        loadAppMetric(apiKey, startDate, endDate, SharedData.APPMETRICS_TYPE_ACTIVE_USERS_BY_WEEK);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        loadAppMetric(apiKey, startDate, endDate, SharedData.APPMETRICS_TYPE_NEW_USERS);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        loadAppMetric(apiKey, startDate, endDate, SharedData.APPMETRICS_TYPE_MEDIAN_SESSION_LENGTH);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        loadAppMetric(apiKey, startDate, endDate, SharedData.APPMETRICS_TYPE_AVG_SESSION_LENGTH);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        loadAppMetric(apiKey, startDate, endDate, SharedData.APPMETRICS_TYPE_SESSIONS);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        loadAppMetric(apiKey, startDate, endDate, SharedData.APPMETRICS_TYPE_RETAINED_USERS);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        loadAppMetric(apiKey, startDate, endDate, SharedData.APPMETRICS_TYPE_PAGEVIEWS);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        loadAppMetric(apiKey, startDate, endDate,
                SharedData.APPMETRICS_TYPE_AVG_PAGEVIEWS_PER_SESSION);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        return null;
    }

    private void loadAppMetric(final String apiKey, final String startDate, final String endDate,
            final String metric) {
        HashMap<String, ArrayList<AppMetricsData>> projectData = SharedData
                .getAppMetricsData(mProjectKey);
        if (projectData != null && projectData.get(metric) != null) {
            if (DEBUG)
                Log.d(TAG, metric + " has loaded, drop task");
            return;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        final String rawData = Utils.parseOnInternet("http://api.flurry.com/appMetrics/" + metric
                + "?apiAccessCode=" + apiKey + "&apiKey=" + mProjectKey + "&startDate=" + startDate
                + "&endDate=" + endDate);
        SharedData.addAppMetricsData(mProjectKey, metric,
                Utils.retrieveAppMetricsDataFromRaw(rawData), mCurrentTimePeriod, mCurrentVersion);
    }

}
