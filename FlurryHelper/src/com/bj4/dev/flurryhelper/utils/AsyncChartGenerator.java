
package com.bj4.dev.flurryhelper.utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class AsyncChartGenerator extends AsyncTask<Void, Void, Void> {
    private WeakReference<FrameLayout> mChartContainer;

    private WeakReference<Context> mContext;

    private int mTargetIndex;

    private View mBarChart;

    private ArrayList<AppMetricsData> mInfos;

    public AsyncChartGenerator(Context context, FrameLayout chartContainer, int targetIndex,
            ArrayList<AppMetricsData> info) {
        mChartContainer = new WeakReference<FrameLayout>(chartContainer);
        mContext = new WeakReference<Context>(context);
        mTargetIndex = targetIndex;
        mInfos = info;
    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Void param) {
        if (mContext.get() == null)
            return;
        mBarChart = ChartUtils.getLineChart(mContext.get(), mInfos);
        final FrameLayout container = mChartContainer.get();
        if (container == null)
            return;
        if (mTargetIndex == (Integer)container.getTag()) {
            container.removeAllViews();
            if (mBarChart != null) {
                container.addView(mBarChart);
            }
        }

    }
}
