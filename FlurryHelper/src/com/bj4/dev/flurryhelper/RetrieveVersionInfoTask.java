
package com.bj4.dev.flurryhelper;

import java.lang.ref.WeakReference;

import com.bj4.dev.flurryhelper.utils.AppVersionInfo;
import com.bj4.dev.flurryhelper.utils.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class RetrieveVersionInfoTask extends AsyncTask<Void, Void, Void> {
    private String mProjectKey;

    private WeakReference<Context> mContext;

    public RetrieveVersionInfoTask(final Context context, final String projectKey) {
        mProjectKey = projectKey;
        mContext = new WeakReference<Context>(context);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        AppVersionInfo info = SharedData.getVersionInfo(mProjectKey);
        if (info != null)
            return null;
        final Context context = mContext.get();
        if (context == null)
            return null;
        final String apiKey = SharedPreferencesHelper.getInstance(context).getAPIKey();
        if (apiKey == null || mProjectKey == null)
            return null;
        final String rawData = Utils
                .parseOnInternet("http://api.flurry.com/appInfo/getApplication?apiAccessCode="
                        + apiKey + "&apiKey=" + mProjectKey);
        info = Utils.retrieveVersionInfoFromRaw(rawData);
        if (info == null)
            return null;
        SharedData.addVersionInfo(mProjectKey, info);
        return null;
    }

}
