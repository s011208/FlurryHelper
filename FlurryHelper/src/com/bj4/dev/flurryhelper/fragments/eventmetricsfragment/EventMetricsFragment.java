
package com.bj4.dev.flurryhelper.fragments.eventmetricsfragment;

import java.util.ArrayList;

import com.bj4.dev.flurryhelper.MainActivity;
import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.utils.EventMetrics;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        checkLoadingView(true, true);
        mContent = inflater.inflate(R.layout.event_metrics_fragment, null);
        mEventNameList = (ListView)mContent.findViewById(R.id.event_metrics_event_name_list);
        mEventNameListAdapter = new EventNameListAdapter(getActivity(), mProjectKey);
        mEventNameList.setAdapter(mEventNameListAdapter);
        new EventMetricsLoadingHelper(getActivity(), mProjectKey, this).execute();
        return mContent;
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
}
