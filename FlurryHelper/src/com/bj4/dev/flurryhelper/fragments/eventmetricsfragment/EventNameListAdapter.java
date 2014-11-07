
package com.bj4.dev.flurryhelper.fragments.eventmetricsfragment;

import java.util.ArrayList;

import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.utils.EventMetrics;
import com.bj4.dev.flurryhelper.utils.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventNameListAdapter extends BaseAdapter {
    private Context mContext;

    private LayoutInflater mInflater;

    private String mProjectKey;

    private final ArrayList<EventMetrics> mData = new ArrayList<EventMetrics>();

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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        final EventMetrics item = getItem(position);
        holder.mEventName.setText(item.getEventName());
        return convertView;
    }

    private static class ViewHolder {
        TextView mEventName;
    }

}
