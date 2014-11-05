
package com.bj4.dev.flurryhelper.fragments.appmetricsfragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.utils.AppMetricsData;
import com.bj4.dev.flurryhelper.utils.ChartUtils;
import com.bj4.dev.flurryhelper.utils.LoadingView;
import com.bj4.dev.flurryhelper.utils.ProjectInfo;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppMetricsInfoAdapter extends BaseAdapter {
    private final ArrayList<String> mInfoTitles = new ArrayList<String>();

    private final ArrayList<ArrayList<AppMetricsData>> mInfos = new ArrayList<ArrayList<AppMetricsData>>();

    private Context mContext;

    private LayoutInflater mInflater;

    public AppMetricsInfoAdapter(Context c) {
        mContext = c;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initInfos();
    }

    private void initInfos() {
        mInfos.clear();
        mInfoTitles.clear();
        final HashMap<String, ArrayList<AppMetricsData>> map = SharedData.getInstance()
                .getAppMetricsData();
        final Map<String, ArrayList<AppMetricsData>> tMap = new TreeMap<String, ArrayList<AppMetricsData>>(
                map);
        Iterator<String> iter = tMap.keySet().iterator();
        while (iter.hasNext()) {
            final String title = iter.next();
            mInfoTitles.add(title);
            mInfos.add(map.get(title));
        }
    }

    public void notifyDataSetChanged() {
        initInfos();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mInfos.size();
    }

    @Override
    public ArrayList<AppMetricsData> getItem(int position) {
        return mInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.app_metrics_fragment_app_metrics_infos, null);
            holder.mChartContainer = (FrameLayout)convertView.findViewById(R.id.app_metrics_info_2);
            holder.mTypeTitle = (TextView)convertView.findViewById(R.id.app_metrics_info_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        final ArrayList<AppMetricsData> info = getItem(position);
        holder.mTypeTitle.setText(mInfoTitles.get(position));
        holder.mChartContainer.removeAllViews();
        View chart = ChartUtils.getLineChart(mContext, info);
        if (chart != null) {
            holder.mChartContainer.addView(chart);
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView mTypeTitle;

        FrameLayout mChartContainer;
    }

}
