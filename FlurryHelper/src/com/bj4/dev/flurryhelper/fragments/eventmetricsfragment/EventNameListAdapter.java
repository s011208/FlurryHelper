
package com.bj4.dev.flurryhelper.fragments.eventmetricsfragment;

import java.util.ArrayList;

import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.utils.EventMetrics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EventNameListAdapter extends BaseAdapter {
    private Context mContext;

    private LayoutInflater mInflater;

    private String mProjectKey;

    private final ArrayList<EventMetrics> mData = new ArrayList<EventMetrics>();

    private boolean mExpand = true;

    public void setExpand(boolean expand) {
        mExpand = expand;
    }

    public EventNameListAdapter(Context context, String pk) {
        mContext = context;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mProjectKey = pk;
        initData();
    }

    private void initData() {
        mData.clear();
        ArrayList<EventMetrics> data = SharedData.getInstance().getEventMetricsData(mProjectKey);
        if (data != null) {
            mData.addAll(data);
        }
    }

    public void notifyDataSetChanged() {
        initData();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public EventMetrics getItem(int arg0) {
        return mData.get(arg0);
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
            convertView = mInflater.inflate(R.layout.event_metrics_event_list_items, null);
            holder.mEventName = (TextView)convertView.findViewById(R.id.event_metrics_event_name);

            holder.mUsersLastDay = (TextView)convertView.findViewById(R.id.users_last_day_value);
            holder.mUsersLastWeek = (TextView)convertView.findViewById(R.id.users_last_week_value);
            holder.mUsersLastMonth = (TextView)convertView
                    .findViewById(R.id.users_last_month_value);
            holder.mAvgUserLastDay = (TextView)convertView.findViewById(R.id.users_avg_day_value);
            holder.mAvgUsersLastWeek = (TextView)convertView
                    .findViewById(R.id.users_avg_week_value);
            holder.mAvgUsersLastMonth = (TextView)convertView
                    .findViewById(R.id.users_avg_month_value);
            holder.mTotalCount = (TextView)convertView.findViewById(R.id.total_count_value);
            holder.mTotalSessions = (TextView)convertView.findViewById(R.id.total_session_value);

            holder.mExpandableDetail = (LinearLayout)convertView
                    .findViewById(R.id.event_metrics_expandable_detailed);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        final EventMetrics item = getItem(position);
        holder.mEventName.setText(item.getEventName());
        if (mExpand) {
            holder.mExpandableDetail.setVisibility(View.VISIBLE);
        } else {
            holder.mExpandableDetail.setVisibility(View.GONE);
        }
        holder.mUsersLastDay.setText(item.getUsersLastDay() + "");
        holder.mUsersLastWeek.setText(item.getUsersLastWeek() + "");
        holder.mUsersLastMonth.setText(item.getUsersLastMonth() + "");
        holder.mAvgUserLastDay.setText(item.getAvgUsersLastDay() + "");
        holder.mAvgUsersLastWeek.setText(item.getAvgUsersLastWeek() + "");
        holder.mAvgUsersLastMonth.setText(item.getAvgUsersLastMonth() + "");
        holder.mTotalCount.setText(item.getTotalCount() + "");
        holder.mTotalSessions.setText(item.getTotalSessions() + "");
        return convertView;
    }

    public static class ViewHolder {
        TextView mEventName;

        LinearLayout mExpandableDetail;

        TextView mUsersLastDay, mUsersLastWeek, mUsersLastMonth, mAvgUserLastDay,
                mAvgUsersLastWeek, mAvgUsersLastMonth, mTotalCount, mTotalSessions;
    }

}
