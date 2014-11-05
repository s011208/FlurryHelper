
package com.bj4.dev.flurryhelper.fragments.projectfragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedPreferencesHelper;
import com.bj4.dev.flurryhelper.utils.AppMetricsData;
import com.bj4.dev.flurryhelper.utils.ChartUtils;
import com.bj4.dev.flurryhelper.utils.LoadingView;
import com.bj4.dev.flurryhelper.utils.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class BarChartTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "QQQQ";

    private WeakReference<FrameLayout> mChartContainer;

    private WeakReference<LoadingView> mLoadingView;

    private WeakReference<Context> mContext;

    private int mTargetIndex;

    private String mProjectKey;

    private View mBarChart;

    private static final HashMap<String, ArrayList<AppMetricsData>> sActiveUsersMap = new HashMap<String, ArrayList<AppMetricsData>>();

    public static final boolean hasData(final String projectKey) {
        synchronized (sActiveUsersMap) {
            return sActiveUsersMap.get(projectKey) != null;
        }
    }

    public BarChartTask(FrameLayout chartContainer, LoadingView loadingView, Context context,
            int targetIndex, String projectKey) {
        mChartContainer = new WeakReference<FrameLayout>(chartContainer);
        mLoadingView = new WeakReference<LoadingView>(loadingView);
        mContext = new WeakReference<Context>(context);
        mTargetIndex = targetIndex;
        mProjectKey = projectKey;
    }

    @Override
    protected Void doInBackground(Void... params) {
        final Context context = mContext.get();
        if (context == null)
            return null;
        ArrayList<AppMetricsData> activeUsers = null;
        synchronized (sActiveUsersMap) {
            activeUsers = sActiveUsersMap.get(mProjectKey);
            if (activeUsers != null && activeUsers.isEmpty() == false)
                return null;
        }
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
        }
        final String rawData = getRawData(context);
        if (rawData == null || rawData.isEmpty())
            return null;
        activeUsers = Utils.retrieveAppMetricsDataFromRaw(rawData);
        synchronized (sActiveUsersMap) {
            if (activeUsers.isEmpty() == false) {
                sActiveUsersMap.put(mProjectKey, activeUsers);
            }
        }
        return null;
    }

    private void drawBarChart(Context context) {
        ArrayList<AppMetricsData> activeUsers = null;
        synchronized (sActiveUsersMap) {
            activeUsers = sActiveUsersMap.get(mProjectKey);
        }
        if (activeUsers == null)
            return;
        mBarChart = ChartUtils.getLineChart(context, activeUsers);
    }

    private String getRawData(Context context) {
        // get data on the Internet
        final String apiKey = SharedPreferencesHelper.getInstance(context).getAPIKey();
        Calendar calendar = Calendar.getInstance();
        final String endDate = calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        final String startDate = (calendar.get(Calendar.YEAR) - 1) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        final String rawData = Utils
                .parseOnInternet("http://api.flurry.com/appMetrics/ActiveUsersByWeek?apiAccessCode="
                        + apiKey
                        + "&apiKey="
                        + mProjectKey
                        + "&startDate="
                        + startDate
                        + "&endDate=" + endDate);
        Log.d(TAG, "rawData: " + rawData);
        return rawData;
    }

    @Override
    public void onPostExecute(Void param) {
        final FrameLayout chartContainer = mChartContainer.get();
        if (chartContainer == null)
            return;
        final Context context = mContext.get();
        if (context == null)
            return;
        int currentIndex = (Integer)chartContainer.getTag();
        if (currentIndex != mTargetIndex)
            return;
        drawBarChart(context);
        if (mBarChart != null) {
            chartContainer.addView(mBarChart);
            final LoadingView loadingView = mLoadingView.get();
            if (loadingView != null) {
                loadingView.setVisibility(View.GONE);
            }
        }
    }
}
