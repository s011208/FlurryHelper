
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
import com.bj4.dev.flurryhelper.utils.ActiveUser;
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

    private static final HashMap<String, ArrayList<ActiveUser>> sActiveUsersMap = new HashMap<String, ArrayList<ActiveUser>>();

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
        ArrayList<ActiveUser> activeUsers = null;
        synchronized (sActiveUsersMap) {
            activeUsers = sActiveUsersMap.get(mProjectKey);
            if (activeUsers != null && activeUsers.isEmpty() == false)
                return null;
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
        }
        final String rawData = getRawData(context);
        if (rawData == null || rawData.isEmpty())
            return null;
        activeUsers = retrieveActiveUserDataFromRaw(rawData);
        synchronized (sActiveUsersMap) {
            if (activeUsers.isEmpty() == false) {
                sActiveUsersMap.put(mProjectKey, activeUsers);
            }
        }
        return null;
    }

    private void drawBarChart(Context context) {
        ArrayList<ActiveUser> activeUsers = null;
        synchronized (sActiveUsersMap) {
            activeUsers = sActiveUsersMap.get(mProjectKey);
        }
        if (activeUsers == null)
            return;
        XYSeries activeUsersSeries = new XYSeries("ActiveUsers");
        for (int i = 0; i < activeUsers.size(); i++) {
            activeUsersSeries.add(i, activeUsers.get(i).getValue());
        }
        final Resources res = context.getResources();
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(activeUsersSeries);
        XYSeriesRenderer activeUsersRenderer = new XYSeriesRenderer();
        activeUsersRenderer.setColor(Color.BLUE);
        activeUsersRenderer.setFillPoints(true);
        float lineWidth = res.getDimension(R.dimen.project_info_chart_line_width);
        activeUsersRenderer.setLineWidth(lineWidth);
        activeUsersRenderer.setPointStyle(PointStyle.CIRCLE);
        activeUsersRenderer.setDisplayChartValues(true);
        activeUsersRenderer.setChartValuesTextSize(res
                .getDimension(R.dimen.project_info_chart_textsize));
        activeUsersRenderer.setDisplayChartValuesDistance((int)res
                .getDimension(R.dimen.project_info_chart_value_distance));
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        int margin = (int)res.getDimension(R.dimen.project_info_chart_margin);
        multiRenderer.setMargins(new int[] {
                margin, margin, margin, margin
        });
        multiRenderer.clearXTextLabels();
        multiRenderer.clearYTextLabels();
        multiRenderer.setShowAxes(false);
        multiRenderer.setShowGrid(false);
        multiRenderer.setShowLabels(false);
        multiRenderer.setShowLegend(false);
        multiRenderer.setZoomButtonsVisible(false);
        multiRenderer.setPanEnabled(false, false);
        multiRenderer.setClickEnabled(false);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setAntialiasing(true);
        multiRenderer.setMarginsColor(Color.TRANSPARENT);
        multiRenderer.setBackgroundColor(Color.argb(10, 0, 0, 0));
        multiRenderer.setApplyBackgroundColor(true);

        multiRenderer.addSeriesRenderer(activeUsersRenderer);
        mBarChart = ChartFactory.getLineChartView(context, dataset, multiRenderer);
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

    private ArrayList<ActiveUser> retrieveActiveUserDataFromRaw(final String rawData) {
        ArrayList<ActiveUser> rtn = new ArrayList<ActiveUser>();
        try {
            JSONObject parent = new JSONObject(rawData);
            JSONArray data = parent.getJSONArray("day");
            for (int i = 0; i < data.length(); i++) {
                JSONObject rawJsonData = data.getJSONObject(i);
                ActiveUser au = new ActiveUser(rawJsonData.getString("@date"),
                        Long.valueOf(rawJsonData.getString("@value")));
                rtn.add(au);
            }
        } catch (JSONException e) {
            Log.w(TAG, "failed", e);
        }
        return rtn;
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
