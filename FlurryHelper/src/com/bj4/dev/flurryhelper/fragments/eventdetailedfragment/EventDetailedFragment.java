
package com.bj4.dev.flurryhelper.fragments.eventdetailedfragment;

import com.bj4.dev.flurryhelper.MainActivity;
import com.bj4.dev.flurryhelper.R;
import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.utils.EventDetailed;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class EventDetailedFragment extends Fragment implements EventDetailedLoadingHelper.Callback {
    public static final String TAG = "EventDetailedFragment";

    public static final String PROJECT_KEY = "project_key";

    public static final String EVENT_NAME_KEY = "event_name_key";

    private String mProjectKey;

    private String mEventName;

    private View mContent;

    private Spinner mActionsSpinner;

    private ArrayAdapter<String> mActionSpinnerAdapter;

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
}
