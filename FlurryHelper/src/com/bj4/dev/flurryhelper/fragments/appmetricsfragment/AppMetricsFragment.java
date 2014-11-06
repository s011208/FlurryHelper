
package com.bj4.dev.flurryhelper.fragments.appmetricsfragment;

import com.bj4.dev.flurryhelper.MainActivity;
import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedData;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
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
public class AppMetricsFragment extends Fragment implements AppMetricsLoadingHelper.Callback {
    public static final String TAG = "AppMetricsFragment";

    public static final String PROJECT_KEY = "project_key";

    private String mProjectKey;

    private View mContent;

    private ListView mAppMetricsList;

    private AppMetricsInfoAdapter mAppMetricsInfoAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mProjectKey = getArguments().getString(PROJECT_KEY);
        mContent = inflater.inflate(R.layout.appmetrics_fragment, null);
        mAppMetricsList = (ListView)mContent.findViewById(R.id.app_metrics_info);
        mAppMetricsInfoAdapter = new AppMetricsInfoAdapter(getActivity(), mProjectKey);
        mAppMetricsList.setAdapter(mAppMetricsInfoAdapter);
        loadData();
        return mContent;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkLoadingView(true);
    }

    private void checkLoadingView(boolean animated) {
        Activity activity = getActivity();
        if (activity == null)
            return;
        if (activity instanceof MainActivity == false)
            return;
        if (SharedData.getInstance().getAppMetricsData(mProjectKey) == null
                || SharedData.getInstance().getAppMetricsData(mProjectKey).isEmpty()) {
            ((MainActivity)activity).showLoadingView(true);
        } else {
            ((MainActivity)activity).hideLoadingView(true);
        }
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
                mAppMetricsInfoAdapter.notifyDataSetChanged();
                checkLoadingView(true);
            }
        });
    }

}
