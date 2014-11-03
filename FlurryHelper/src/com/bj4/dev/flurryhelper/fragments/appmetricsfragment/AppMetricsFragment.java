
package com.bj4.dev.flurryhelper.fragments.appmetricsfragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * http://support.flurry.com/index.php?title=API/GettingStarted
 * 
 * @author Yen-Hsun_Huang
 */
public class AppMetricsFragment extends Fragment {
    public static final String TAG = "AppMetricsFragment";

    public static final String PROJECT_KEY = "project_key";

    private String mProjectKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mProjectKey = getArguments().getString(PROJECT_KEY);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
