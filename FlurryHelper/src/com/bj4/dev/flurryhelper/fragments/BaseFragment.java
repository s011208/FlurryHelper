
package com.bj4.dev.flurryhelper.fragments;

import com.bj4.dev.flurryhelper.MainActivity;

import android.app.Activity;
import android.app.Fragment;

public abstract class BaseFragment extends Fragment {
    public abstract void notifyDataChanged();

    public void checkLoadingView(boolean show, boolean animated) {
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
}
