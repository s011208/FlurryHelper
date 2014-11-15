
package com.bj4.dev.flurryhelper.fragments.eventmetricsfragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.SharedPreferencesHelper;
import com.bj4.dev.flurryhelper.utils.AppVersionInfo;
import com.bj4.dev.flurryhelper.utils.EventMetrics;
import com.bj4.dev.flurryhelper.utils.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class EventMetricsLoadingHelper extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "EventMetricsLoadingHelper";

    private static final boolean DEBUG = false;

    public interface Callback {
        public void notifyDataChanged();
    }

    private final String mProjectKey;

    private final WeakReference<Context> mContext;

    private final WeakReference<Callback> mCallback;

    private final int mCurrentTimePeriod;

    private final int mCurrentVersion = 0;

    public EventMetricsLoadingHelper(Context context, String projectKey, Callback cb) {
        mProjectKey = projectKey;
        mContext = new WeakReference<Context>(context);
        mCallback = new WeakReference<Callback>(cb);
        mCurrentTimePeriod = SharedPreferencesHelper.getInstance(context).getDisplayPeriod();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        final Context context = mContext.get();
        if (context == null)
            return null;
        final String apiKey = SharedPreferencesHelper.getInstance(context).getAPIKey();
        final String endDate = SharedPreferencesHelper.getInstance(context).getEndDate();
        final String startDate = SharedPreferencesHelper.getInstance(context).getStartDate();
        final boolean loadSuccess = loadAppMetric(apiKey, startDate, endDate);
        if (loadSuccess && mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        return null;
    }

    private boolean loadAppMetric(final String apiKey, final String startDate, final String endDate) {
        final ArrayList<EventMetrics> data = SharedData.getEventMetricsData(mProjectKey);
        if (data != null && data.isEmpty() == false) {
            if (DEBUG)
                Log.v(TAG, "data has loaded, ignore");
            return false;
        }
        if (SharedData.getVersionInfo(mProjectKey) == null)
            return false;
        String versionName = SharedData.getVersionInfo(mProjectKey).getSelectedVersion();
        if (versionName == null)
            return false;
        if (!AppVersionInfo.VERSION_NOT_SET.equals(versionName)) {
            versionName = "&versionName=" + versionName;
        }
        final String rawData = Utils.parseOnInternet("http://api.flurry.com/eventMetrics/Summary"
                + "?apiAccessCode=" + apiKey + "&apiKey=" + mProjectKey + "&startDate=" + startDate
                + "&endDate=" + endDate + versionName);
        if (DEBUG)
            Log.d(TAG, "rawData: " + rawData);
        SharedData.addEventMetricsData(mProjectKey, Utils.retrieveEventMetricsDataFromRaw(rawData),
                mCurrentTimePeriod, mCurrentVersion);
        return true;
    }
}
