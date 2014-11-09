
package com.bj4.dev.flurryhelper.fragments.appmetricsfragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.bj4.dev.flurryhelper.MainActivity;
import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.fragments.BaseFragment;
import com.bj4.dev.flurryhelper.utils.AppMetricsData;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;

/**
 * http://support.flurry.com/index.php?title=API/GettingStarted
 * 
 * @author Yen-Hsun_Huang
 */
public class AppMetricsFragment extends BaseFragment implements AppMetricsLoadingHelper.Callback {
    public static final String TAG = "AppMetricsFragment";

    public static final String PROJECT_KEY = "project_key";

    private String mProjectKey;

    private View mContent;

    private Spinner mAppMetricsSpinner;

    private FrameLayout mChartContainer;

    private String mCurrentAppMetrics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mProjectKey = getArguments().getString(PROJECT_KEY);
        mContent = inflater.inflate(R.layout.app_metrics_fragment, null);
        mAppMetricsSpinner = (Spinner)mContent.findViewById(R.id.app_metrics_spinner);
        ArrayAdapter<String> appMetricsAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.app_metrics_spinner_text, SharedData.APPMETRICS_LIST);
        appMetricsAdapter.setDropDownViewResource(R.layout.app_metrics_spinner_text);
        mAppMetricsSpinner.setAdapter(appMetricsAdapter);
        mAppMetricsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                fillUpContainer();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        mChartContainer = (FrameLayout)mContent.findViewById(R.id.app_metrics_chart_container);
        loadData();
        fillUpContainer();
        return mContent;
    }

    private void fillUpContainer() {
        final ArrayList<AppMetricsData> data = new ArrayList<AppMetricsData>();
        final String selectedAppMetrics = mAppMetricsSpinner.getSelectedItem().toString();
        if (selectedAppMetrics == null || selectedAppMetrics.equals(mCurrentAppMetrics)) {
            // simple check
            return;
        }
        final HashMap<String, ArrayList<AppMetricsData>> mapData = SharedData
                .getAppMetricsData(mProjectKey);
        if (mapData == null) {
            // npe check
            checkLoadingView(true, true);
            return;
        }
        mCurrentAppMetrics = selectedAppMetrics;
        final ArrayList<AppMetricsData> metricData = mapData.get(mCurrentAppMetrics);
        if (metricData == null) {
            // npe check
            checkLoadingView(true, true);
            return;
        }
        data.addAll(metricData);
        if (data.isEmpty()) {
            checkLoadingView(true, true);
            mCurrentAppMetrics = null;
        } else {
            checkLoadingView(false, true);
        }
        drawChart(data);
    }

    private void drawChart(ArrayList<AppMetricsData> metricsData) {
        mChartContainer.removeAllViews();
        View chart = getLineChart(getActivity(), metricsData);
        if (chart != null) {
            mChartContainer.addView(chart);
        }
    }

    public static View getLineChart(Context context, ArrayList<AppMetricsData> metricsData) {
        if (metricsData == null)
            return null;
        XYSeries metricsDataSeries = new XYSeries("ActiveUsers");
        for (int i = 0; i < metricsData.size(); i++) {
            metricsDataSeries.add(i, metricsData.get(i).getValue());
        }
        final Resources res = context.getResources();
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(metricsDataSeries);
        XYSeriesRenderer metricsDataRenderer = new XYSeriesRenderer();
        metricsDataRenderer.setColor(Color.BLUE);
        metricsDataRenderer.setFillPoints(true);
        float lineWidth = res.getDimension(R.dimen.app_metrics_chart_value_line_width);
        metricsDataRenderer.setLineWidth(lineWidth);
        metricsDataRenderer.setPointStyle(PointStyle.CIRCLE);
        metricsDataRenderer.setDisplayChartValues(true);
        metricsDataRenderer.setChartValuesSpacing(res
                .getDimension(R.dimen.app_metrics_chart_value_spacing));
        metricsDataRenderer.setChartValuesTextSize(res
                .getDimension(R.dimen.app_metrics_chart_value_textsize));
        metricsDataRenderer.setDisplayChartValuesDistance((int)res
                .getDimension(R.dimen.app_metrics_chart_value_distance));
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.addSeriesRenderer(metricsDataRenderer);
        int margin = (int)res.getDimension(R.dimen.app_metrics_chart_margin);

        for (int i = 0; i < metricsData.size(); i++) {
            multiRenderer.addXTextLabel(i, metricsData.get(i).getDate());
        }

        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setAntialiasing(true);
        multiRenderer.setGridColor(Color.rgb(124, 181, 236));
        multiRenderer.setBackgroundColor(Color.argb(80, 0, 0, 0));
        multiRenderer.setMarginsColor(Color.rgb(0xee, 0xee, 0xee));
        multiRenderer.setMargins(new int[] {
                margin, margin, margin, margin
        });
        multiRenderer.setShowAxes(false);
        multiRenderer.setShowLabels(true);
        multiRenderer.setShowGridX(true);
        multiRenderer.setShowGridY(false);
        multiRenderer.setShowLegend(true);
        multiRenderer.setLegendTextSize(res.getDimension(R.dimen.app_metrics_chart_value_textsize));
        multiRenderer.setPanEnabled(true, true);
        multiRenderer.setClickEnabled(false);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setXLabels(0);
        multiRenderer.setXLabelsAngle(30);
        multiRenderer
                .setLabelsTextSize(res.getDimension(R.dimen.app_metrics_chart_value_textsize) * 0.5f);
        multiRenderer.setYLabels(15);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
        multiRenderer.setYLabelsVerticalPadding(res
                .getDimension(R.dimen.app_metrics_chart_y_label_v_padding));
        multiRenderer.setYLabelsPadding(res
                .getDimension(R.dimen.app_metrics_chart_y_label_h_padding));
        multiRenderer.setZoomButtonsVisible(true);
        multiRenderer.setZoomEnabled(true, true);
        multiRenderer.setZoomRate(2.5f);
        return ChartFactory.getLineChartView(context, dataset, multiRenderer);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void loadData() {
        new AppMetricsLoadingHelper(getActivity(), mProjectKey, this)
                .executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    @Override
    public void notifyDataChanged() {
        final Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fillUpContainer();
            }
        });
    }

}
