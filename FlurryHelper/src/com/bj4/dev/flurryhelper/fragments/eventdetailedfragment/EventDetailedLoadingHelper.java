
package com.bj4.dev.flurryhelper.fragments.eventdetailedfragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.SharedPreferencesHelper;
import com.bj4.dev.flurryhelper.utils.EventDetailed;
import com.bj4.dev.flurryhelper.utils.EventMetrics;
import com.bj4.dev.flurryhelper.utils.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class EventDetailedLoadingHelper extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "EventDetailedLoadingHelper";

    private static final boolean DEBUG = false;

    public interface Callback {
        public void notifyDataChanged();
    }

    private final String mProjectKey;

    private final String mEventName;

    private final WeakReference<Context> mContext;

    private final WeakReference<Callback> mCallback;

    public EventDetailedLoadingHelper(Context context, String projectKey, String eventName,
            Callback cb) {
        mProjectKey = projectKey;
        mEventName = eventName;
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
        loadEventDetail(apiKey, startDate, endDate);
        if (mCallback != null && mCallback.get() != null)
            mCallback.get().notifyDataChanged();
        return null;
    }

    private void loadEventDetail(final String apiKey, final String startDate, final String endDate) {
        final EventDetailed detailed = SharedData.getInstance().getEventDetailedData(mEventName);
        if (detailed != null) {
            if (DEBUG)
                Log.v(TAG, "data has loaded, ignore");
            return;
        }
        final String rawData = Utils.parseOnInternet("http://api.flurry.com/eventMetrics/Event"
                + "?apiAccessCode=" + apiKey + "&apiKey=" + mProjectKey + "&startDate=" + startDate
                + "&endDate=" + endDate + "&eventName=" + mEventName);
        if (DEBUG) {
            Log.d(TAG, "rawData: " + rawData);
        }
        SharedData.getInstance().addEventDetailedData(mEventName,
                Utils.retrieveEventDetailedDataFromRaw(rawData));
    }
}
