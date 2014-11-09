
package com.bj4.dev.flurryhelper.fragments.eventdetailedfragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.bj4.dev.flurryhelper.MainActivity;
import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.utils.EventDetailed;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class EventDetailedFragment extends Fragment implements EventDetailedLoadingHelper.Callback {
    public static final String TAG = "EventDetailedFragment";

    public static final String PROJECT_KEY = "project_key";

    public static final String EVENT_NAME_KEY = "event_name_key";

    private String mProjectKey;

    private String mEventName;

    private View mContent;

    private Spinner mActionsSpinner;

    private ArrayAdapter<String> mActionSpinnerAdapter;

    private ListView mDetailContent;

    private EventDetailListAdapter mEventDetailListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mProjectKey = getArguments().getString(PROJECT_KEY);
        mEventName = getArguments().getString(EVENT_NAME_KEY);
        mContent = inflater.inflate(R.layout.event_detail_fragment, null);
        mActionsSpinner = (Spinner)mContent.findViewById(R.id.event_detailed_spinner);
        mDetailContent = (ListView)mContent.findViewById(R.id.event_detailed_content_list);
        mEventDetailListAdapter = new EventDetailListAdapter(getActivity(), mEventName);
        mDetailContent.setAdapter(mEventDetailListAdapter);
        final EventDetailed detailed = SharedData.getInstance().getEventDetailedData(mEventName);
        if (detailed != null) {
            setSpinnerAdapter();
        } else {
            checkLoadingView(true, true);
            new EventDetailedLoadingHelper(getActivity(), mProjectKey, mEventName, this).execute();
        }
        ((MainActivity)getActivity()).setProjectKey(mProjectKey);
        return mContent;
    }

    private void setSpinnerAdapter() {
        final EventDetailed detailed = SharedData.getInstance().getEventDetailedData(mEventName);
        mActionSpinnerAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.event_detail_spinner_text, detailed.getAllActions());
        mActionsSpinner.setAdapter(mActionSpinnerAdapter);
        mActionsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final String action = mActionSpinnerAdapter.getItem(arg2);
                refreshEventDetailContent(action);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void refreshEventDetailContent(final String action) {
        if (mEventDetailListAdapter == null)
            return;
        mEventDetailListAdapter.setListContent(action);
        mDetailContent.smoothScrollToPosition(0);
    }

    private void checkLoadingView(boolean show, boolean animated) {
        Activity activity = getActivity();
        if (activity == null)
            return;
        if (activity instanceof MainActivity == false)
            return;
        if (show) {
            ((MainActivity)activity).showLoadingView(true);
        } else {
            ((MainActivity)activity).hideLoadingView(true);
        }
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
                checkLoadingView(false, true);
                setSpinnerAdapter();
            }
        });
    }

    public static class EventDetailListAdapter extends BaseAdapter {
        private Context mContext;

        private LayoutInflater mInflater;

        private final String mEventName;

        private final ArrayList<String> mName = new ArrayList<String>();

        private final ArrayList<Long> mValue = new ArrayList<Long>();

        private long mTotalCount;

        public void setListContent(final String action) {
            final EventDetailed detailed = SharedData.getInstance()
                    .getEventDetailedData(mEventName);
            if (detailed == null)
                return;
            final Map<String, Long> parameters = detailed.getParameters(action);
            if (parameters == null)
                return;
            mName.clear();
            mValue.clear();
            mTotalCount = 0;
            Iterator<String> iter = parameters.keySet().iterator();
            while (iter.hasNext()) {
                final String key = iter.next();
                final Long value = parameters.get(key);
                if (value == null || key == null)
                    continue;
                mName.add(key);
                mValue.add(value);
                mTotalCount += value;
            }
            notifyDataSetChanged();
        }

        public EventDetailListAdapter(Context context, String eventName) {
            mContext = context;
            mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mEventName = eventName;
        }

        @Override
        public int getCount() {
            return mName.size();
        }

        @Override
        public String getItem(int arg0) {
            return mName.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.event_detail_fragment_list_items, null);
                holder.mName = (TextView)convertView.findViewById(R.id.name);
                holder.mValue = (TextView)convertView.findViewById(R.id.value);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.mName.setText(mName.get(position));
            long value = mValue.get(position);
            int percentage = 0;
            if (mTotalCount == 0) {
                percentage = 0;
            } else {
                percentage = (int)((value / (float)mTotalCount) * 100);
            }
            holder.mValue.setText(mValue.get(position).toString() + "( " + percentage + "% )");
            return convertView;
        }

        private static class ViewHolder {
            TextView mName;

            TextView mValue;
        }
    }
}
