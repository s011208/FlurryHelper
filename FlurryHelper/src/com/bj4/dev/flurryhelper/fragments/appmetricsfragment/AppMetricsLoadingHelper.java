
package com.bj4.dev.flurryhelper.fragments.appmetricsfragment;

import java.lang.ref.WeakReference;
import java.util.Calendar;

import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.SharedPreferencesHelper;
import com.bj4.dev.flurryhelper.utils.Utils;

import android.content.Context;
import android.os.AsyncTask;

public class AppMetricsLoadingHelper extends AsyncTask<Void, Void, Void> {
    public interface Callback {
        public void notifyDataChanged();
    }

    private final String mProjectKey;

    private final WeakReference<Context> mContext;

    private final WeakReference<Callback> mCallback;

    public AppMetricsLoadingHelper(Context context, String projectKey, Callback cb) {
        mProjectKey = projectKey;
        mContext = new WeakReference<Context>(context);
        mCallback = new WeakReference<Callback>(cb);
    }

    @Override
    protected Void doInBackground(Void... params) {
        final Context context = mContext.get();
        if (context == null)
            return null;
        final String apiKey = SharedPreferencesHelper.getInstance(context).getAPIKey();
        Calendar calendar = Calendar.getInstance();
        final String endDate = calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        final String startDate = (calendar.get(Calendar.YEAR) - 1) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        loadActiveUsers(apiKey, startDate, endDate);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        loadActiveUsersByMonth(apiKey, startDate, endDate);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        loadActiveUsersByWeek(apiKey, startDate, endDate);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        loadNewUsers(apiKey, startDate, endDate);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        loadMedianSessionLength(apiKey, startDate, endDate);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        loadAvgSessionLength(apiKey, startDate, endDate);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        loadSessions(apiKey, startDate, endDate);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        loadRetainedUsers(apiKey, startDate, endDate);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        return null;
    }

    private void loadActiveUsers(final String apiKey, final String startDate, final String endDate) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        final String rawData = Utils.parseOnInternet("http://api.flurry.com/appMetrics/"
                + SharedData.APPMETRICS_TYPE_ACTIVE_USERS + "?apiAccessCode=" + apiKey + "&apiKey="
                + mProjectKey + "&startDate=" + startDate + "&endDate=" + endDate);
        SharedData.getInstance().addMetricsData(SharedData.APPMETRICS_TYPE_ACTIVE_USERS,
                Utils.retrieveAppMetricsDataFromRaw(rawData));
    }

    private void loadActiveUsersByMonth(final String apiKey, final String startDate,
            final String endDate) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        final String rawData = Utils.parseOnInternet("http://api.flurry.com/appMetrics/"
                + SharedData.APPMETRICS_TYPE_ACTIVE_USERS_BY_MONTH + "?apiAccessCode=" + apiKey
                + "&apiKey=" + mProjectKey + "&startDate=" + startDate + "&endDate=" + endDate);
        SharedData.getInstance().addMetricsData(SharedData.APPMETRICS_TYPE_ACTIVE_USERS_BY_MONTH,
                Utils.retrieveAppMetricsDataFromRaw(rawData));
    }

    private void loadActiveUsersByWeek(final String apiKey, final String startDate,
            final String endDate) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        final String rawData = Utils.parseOnInternet("http://api.flurry.com/appMetrics/"
                + SharedData.APPMETRICS_TYPE_ACTIVE_USERS_BY_WEEK + "?apiAccessCode=" + apiKey
                + "&apiKey=" + mProjectKey + "&startDate=" + startDate + "&endDate=" + endDate);
        SharedData.getInstance().addMetricsData(SharedData.APPMETRICS_TYPE_ACTIVE_USERS_BY_WEEK,
                Utils.retrieveAppMetricsDataFromRaw(rawData));
    }

    private void loadNewUsers(final String apiKey, final String startDate, final String endDate) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        final String rawData = Utils.parseOnInternet("http://api.flurry.com/appMetrics/"
                + SharedData.APPMETRICS_TYPE_NEW_USERS + "?apiAccessCode=" + apiKey + "&apiKey="
                + mProjectKey + "&startDate=" + startDate + "&endDate=" + endDate);
        SharedData.getInstance().addMetricsData(SharedData.APPMETRICS_TYPE_NEW_USERS,
                Utils.retrieveAppMetricsDataFromRaw(rawData));
    }

    private void loadMedianSessionLength(final String apiKey, final String startDate,
            final String endDate) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        final String rawData = Utils.parseOnInternet("http://api.flurry.com/appMetrics/"
                + SharedData.APPMETRICS_TYPE_MEDIAN_SESSION_LENGTH + "?apiAccessCode=" + apiKey
                + "&apiKey=" + mProjectKey + "&startDate=" + startDate + "&endDate=" + endDate);
        SharedData.getInstance().addMetricsData(SharedData.APPMETRICS_TYPE_MEDIAN_SESSION_LENGTH,
                Utils.retrieveAppMetricsDataFromRaw(rawData));
    }

    private void loadAvgSessionLength(final String apiKey, final String startDate,
            final String endDate) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        final String rawData = Utils.parseOnInternet("http://api.flurry.com/appMetrics/"
                + SharedData.APPMETRICS_TYPE_AVG_SESSION_LENGTH + "?apiAccessCode=" + apiKey
                + "&apiKey=" + mProjectKey + "&startDate=" + startDate + "&endDate=" + endDate);
        SharedData.getInstance().addMetricsData(SharedData.APPMETRICS_TYPE_AVG_SESSION_LENGTH,
                Utils.retrieveAppMetricsDataFromRaw(rawData));
    }

    private void loadSessions(final String apiKey, final String startDate, final String endDate) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        final String rawData = Utils.parseOnInternet("http://api.flurry.com/appMetrics/"
                + SharedData.APPMETRICS_TYPE_SESSIONS + "?apiAccessCode=" + apiKey + "&apiKey="
                + mProjectKey + "&startDate=" + startDate + "&endDate=" + endDate);
        SharedData.getInstance().addMetricsData(SharedData.APPMETRICS_TYPE_SESSIONS,
                Utils.retrieveAppMetricsDataFromRaw(rawData));
    }

    private void loadRetainedUsers(final String apiKey, final String startDate, final String endDate) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        final String rawData = Utils.parseOnInternet("http://api.flurry.com/appMetrics/"
                + SharedData.APPMETRICS_TYPE_RETAINED_USERS + "?apiAccessCode=" + apiKey
                + "&apiKey=" + mProjectKey + "&startDate=" + startDate + "&endDate=" + endDate);
        SharedData.getInstance().addMetricsData(SharedData.APPMETRICS_TYPE_RETAINED_USERS,
                Utils.retrieveAppMetricsDataFromRaw(rawData));
    }
}
