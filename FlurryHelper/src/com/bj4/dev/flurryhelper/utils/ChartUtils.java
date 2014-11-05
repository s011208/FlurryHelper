
package com.bj4.dev.flurryhelper.utils;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.bj4.dev.flurryhelper.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;

public class ChartUtils {
    public static View getLineChart(Context context, ArrayList<AppMetricsData> activeUsers) {
        if (activeUsers == null)
            return null;
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
//        activeUsersRenderer.setDisplayChartValues(true);
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
        return ChartFactory.getLineChartView(context, dataset, multiRenderer);
    }
}
