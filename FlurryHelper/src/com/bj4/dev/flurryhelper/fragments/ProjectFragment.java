
package com.bj4.dev.flurryhelper.fragments;

import java.io.IOException;
import java.lang.ref.WeakReference;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.bj4.dev.flurryhelper.CompanyName;
import com.bj4.dev.flurryhelper.MainActivity;
import com.bj4.dev.flurryhelper.SharedData;
import com.bj4.dev.flurryhelper.utils.Utils;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * http://api.flurry.com/appInfo/getAllApplications?apiAccessCode=7
 * XGFNKM2BQX8YPN55BM2
 * 
 * @author yenhsunhuang
 */
public class ProjectFragment extends Fragment {
    public static final String TAG = "ProjectFragment";

    public static class RetrieveApplicationInfoTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<ProjectFragment> mProjectFragment;

        public RetrieveApplicationInfoTask(ProjectFragment pf) {
            mProjectFragment = new WeakReference<ProjectFragment>(pf);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            final String rawData = Utils
                    .parseOnInternet("http://api.flurry.com/appInfo/getAllApplications?apiAccessCode=7XGFNKM2BQX8YPN55BM2");
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

    public void notifyDataChanged() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedData sd = SharedData.getInstance();
        final CompanyName currentCompany = sd.getCompanyName();
        if (currentCompany == null) {
            new RetrieveApplicationInfoTask(this).execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
