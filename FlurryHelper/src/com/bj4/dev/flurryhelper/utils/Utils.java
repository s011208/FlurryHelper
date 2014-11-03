
package com.bj4.dev.flurryhelper.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class Utils {
    private static final String TAG = "QQQQ";

    @SuppressWarnings("deprecation")
    public static String parseOnInternet(String url) {
        URL u;
        InputStream is = null;
        DataInputStream dis;
        String s;
        StringBuilder sb = new StringBuilder();
        try {
            u = new URL(url);
            is = u.openStream();
            dis = new DataInputStream(new BufferedInputStream(is));
            while ((s = dis.readLine()) != null) {
                sb.append(s);
            }
        } catch (Exception e) {
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException ioe) {
            }
        }
        return sb.toString();
    }

    public static CompanyName retrieveCompanyName(final String rawData) {
        CompanyName cd = null;
        try {
            JSONObject json = new JSONObject(rawData);
            cd = new CompanyName(json.getString("@companyName"), json.getString("@generatedDate"),
                    json.getString("@version"));
            Log.d(TAG, cd.toString());
        } catch (Exception e) {
            Log.w(TAG, "failed", e);
        }
        return cd;
    }

    public static ArrayList<ProjectInfo> retrieveProjectInfos(final String rawData) {
        final ArrayList<ProjectInfo> rtn = new ArrayList<ProjectInfo>();
        try {
            JSONObject json = new JSONObject(rawData);
            JSONArray rawProjectInfos = json.getJSONArray("application");
            for (int i = 0; i < rawProjectInfos.length(); i++) {
                JSONObject rawProjectInfo = rawProjectInfos.getJSONObject(i);
                ProjectInfo pInfo = new ProjectInfo(rawProjectInfo.getString("@apiKey"),
                        rawProjectInfo.getString("@createdDate"),
                        rawProjectInfo.getString("@platform"), rawProjectInfo.getString("@name"));
                rtn.add(pInfo);
                Log.v(TAG, pInfo.toString());
            }
        } catch (JSONException e) {
            Log.w(TAG, "failed", e);
        }
        return rtn;
    }
}
