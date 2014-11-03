
package com.bj4.dev.flurryhelper.fragments.projectfragment;

import java.util.ArrayList;

import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedData;
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

public class ProjectInfoAdapter extends BaseAdapter {
    private final ArrayList<ProjectInfo> mInfos = new ArrayList<ProjectInfo>();

    private Context mContext;

    private int mLoadingViewRadius;

    private LayoutInflater mInflater;

    public ProjectInfoAdapter(Context c) {
        mContext = c;
        mLoadingViewRadius = (int)c.getResources().getDimension(
                R.dimen.project_info_chart_loading_view_radius);
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initInfos();
    }

    private void initInfos() {
        mInfos.clear();
        mInfos.addAll(SharedData.getInstance().getProjectInfos());
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
    public ProjectInfo getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.project_fragment_project_infos, null);
            holder.mName = (TextView)convertView.findViewById(R.id.project_info_name);
            holder.mCreatedDate = (TextView)convertView
                    .findViewById(R.id.project_info_created_date);
            holder.mPlatform = (TextView)convertView.findViewById(R.id.project_info_platform);
            holder.mChartContainer = (FrameLayout)convertView.findViewById(R.id.project_info_3);
            holder.mLoadingView = (LoadingView)convertView.findViewById(R.id.loading_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        final ProjectInfo info = getItem(position);
        holder.mCreatedDate.setText(info.getCreatedDate());
        holder.mName.setText(info.getName());
        holder.mPlatform.setText(info.getPlatform());
        holder.mLoadingView.setRadius(mLoadingViewRadius);
        holder.mLoadingView.setVisibility(View.VISIBLE);
        holder.mChartContainer.setTag(position);
        if (holder.mChartContainer.getChildCount() > 1) {
            for (int i = 0; i < holder.mChartContainer.getChildCount(); i++) {
                if (holder.mChartContainer.getChildAt(i) instanceof LoadingView == false) {
                    holder.mChartContainer.removeViewAt(i);
                }
            }
        }
        final LoadingView loadingView = holder.mLoadingView;
        loadingView.setBackgroundColor(Color.argb(15, 0, 0, 0));
        ViewTreeObserver observer = loadingView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                ViewTreeObserver observer = loadingView.getViewTreeObserver();
                if (observer.isAlive())
                    observer.removeOnGlobalLayoutListener(this);
                loadingView.setRadius(mLoadingViewRadius);
            }
        });
        new BarChartTask(holder.mChartContainer, holder.mLoadingView, mContext, position,
                info.getApiKey()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return convertView;
    }

    private static class ViewHolder {
        TextView mName, mCreatedDate, mPlatform;

        FrameLayout mChartContainer;

        LoadingView mLoadingView;
    }

}
