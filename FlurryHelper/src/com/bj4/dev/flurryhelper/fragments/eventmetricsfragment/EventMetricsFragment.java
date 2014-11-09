
package com.bj4.dev.flurryhelper.fragments.eventmetricsfragment;

import java.util.ArrayList;

import com.bj4.dev.flurryhelper.MainActivity;
import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.fragments.eventmetricsfragment.EventNameListAdapter.ViewHolder;
import com.bj4.dev.flurryhelper.utils.EventMetrics;
import com.bj4.dev.flurryhelper.utils.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * http://support.flurry.com/index.php?title=API/GettingStarted
 * 
 * @author Yen-Hsun_Huang
 */
public class EventMetricsFragment extends Fragment implements EventMetricsLoadingHelper.Callback {
    public static final String TAG = "EventMetricsFragment";

    public static final String PROJECT_KEY = "project_key";

    private String mProjectKey;

    private View mContent;

    private ListView mEventNameList;

    private EventNameListAdapter mEventNameListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mProjectKey = getArguments().getString(PROJECT_KEY);
        mContent = inflater.inflate(R.layout.event_metrics_fragment, null);
        mEventNameList = (ListView)mContent.findViewById(R.id.event_metrics_event_name_list);
        mEventNameListAdapter = new EventNameListAdapter(getActivity(), mProjectKey);
        mEventNameListAdapter.setExpand(((MainActivity)getActivity()).isExpanded());
        mEventNameList.setAdapter(mEventNameListAdapter);
        mEventNameList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                final Activity activity = getActivity();
                EventMetrics em = mEventNameListAdapter.getItem(arg2);
                if (activity == null)
                    return;
                if (activity instanceof MainActivity == false)
                    return;
                ((MainActivity)activity).enterEventDetailedFragment(mProjectKey, em.getEventName());
            }
        });
        final ArrayList<EventMetrics> data = SharedData.getInstance().getEventMetricsData(
                mProjectKey);
        if (data != null && data.isEmpty() == false) {
            // data exist
        } else {
            checkLoadingView(true, true);
            new EventMetricsLoadingHelper(getActivity(), mProjectKey, this).execute();
        }
        return mContent;
    }

    private void collapseAllView() {
        mEventNameListAdapter.setExpand(false);
        for (int i = 0; i < mEventNameList.getChildCount(); i++) {
            EventNameListAdapter.ViewHolder holder = (ViewHolder)mEventNameList.getChildAt(i)
                    .getTag();
            Utils.collapse(holder.mExpandableDetail);
        }
    }

    private void expandAllView() {
        mEventNameListAdapter.setExpand(true);
        for (int i = 0; i < mEventNameList.getChildCount(); i++) {
            EventNameListAdapter.ViewHolder holder = (ViewHolder)mEventNameList.getChildAt(i)
                    .getTag();
            Utils.expand(holder.mExpandableDetail);
        }
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
                mEventNameListAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onExpandStatusChanged() {
        if (((MainActivity)getActivity()).isExpanded()) {
            expandAllView();
        } else {
            collapseAllView();
        }
    }
}
