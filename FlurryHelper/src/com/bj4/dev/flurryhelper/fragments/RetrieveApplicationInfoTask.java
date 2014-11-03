package com.bj4.dev.flurryhelper.fragments;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.bj4.dev.flurryhelper.CompanyName;
import com.bj4.dev.flurryhelper.MainActivity;
import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.SharedPreferencesHelper;
import com.bj4.dev.flurryhelper.utils.Utils;

public class RetrieveApplicationInfoTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<ProjectFragment> mProjectFragment;

    private WeakReference<Context> mContext;

    public RetrieveApplicationInfoTask(ProjectFragment pf, Context context) {
        mProjectFragment = new WeakReference<ProjectFragment>(pf);
        mContext = new WeakReference<Context>(context);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        final Context context = mContext.get();
        if (context == null)
            return null;
        final String apiKey = SharedPreferencesHelper.getInstance(context).getAPIKey();
        final String rawData = Utils
                .parseOnInternet("http://api.flurry.com/appInfo/getAllApplications?apiAccessCode="
                        + apiKey);
        if (rawData != null) {
            final CompanyName cn = Utils.retrieveCompanyName(rawData);
            final SharedData sd = SharedData.getInstance();
            if (cn != null) {
                sd.setCompanyName(cn);
                sd.addProjectInfos(Utils.retrieveProjectInfos(rawData));
            }
        }
        return null;
    }

    @Override
    public void onPostExecute(Void arg0) {
        ProjectFragment pf = mProjectFragment.get();
        if (pf != null) {
            Activity activity = pf.getActivity();
            if (activity instanceof MainActivity) {
                ((MainActivity)activity).hideLoadingView();
                pf.notifyDataChanged();
            }
        }
    }
}