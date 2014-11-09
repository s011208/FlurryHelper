
package com.bj4.dev.flurryhelper.fragments.eventmetricsfragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.SharedPreferencesHelper;
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

    public EventMetricsLoadingHelper(Context context, String projectKey, Callback cb) {
        mProjectKey = projectKey;
        mContext = new WeakReference<Context>(context);
        mCallback = new WeakReference<Callback>(cb);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        final Context context = mContext.get();
        if (context == null)
            return null;
        final String apiKey = SharedPreferencesHelper.getInstance(context).getAPIKey();
        Calendar calendar = Calendar.getInstance();
        final String endDate = calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        final String startDate = (calendar.get(Calendar.YEAR) - 1) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        loadAppMetric(apiKey, startDate, endDate);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        return null;
    }

    private void loadAppMetric(final String apiKey, final String startDate, final String endDate) {
        final ArrayList<EventMetrics> data = SharedData.getInstance().getEventMetricsData(
                mProjectKey);
        if (data != null && data.isEmpty() == false) {
            if (DEBUG)
                Log.v(TAG, "data has loaded, ignore");
            return;
        }
        final String rawData = Utils.parseOnInternet("http://api.flurry.com/eventMetrics/Summary"
                + "?apiAccessCode=" + apiKey + "&apiKey=" + mProjectKey + "&startDate=" + startDate
                + "&endDate=" + endDate);
        if (DEBUG)
            Log.d(TAG, "rawData: " + rawData);
        SharedData.getInstance().addEventMetricsData(mProjectKey,
                Utils.retrieveEventMetricsDataFromRaw(rawData));
    }
}