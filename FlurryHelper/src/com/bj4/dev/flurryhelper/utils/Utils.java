
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
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Utils {
    private static final String TAG = "Utils";

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

    public static ArrayList<AppMetricsData> retrieveAppMetricsDataFromRaw(final String rawData) {
        final ArrayList<AppMetricsData> rtn = new ArrayList<AppMetricsData>();
        try {
            JSONObject parent = new JSONObject(rawData);
            JSONArray data = parent.getJSONArray("day");
            for (int i = 0; i < data.length(); i++) {
                JSONObject rawJsonData = data.getJSONObject(i);
                AppMetricsData au = new AppMetricsData(rawJsonData.getString("@date"),
                        Long.valueOf(rawJsonData.getString("@value")));
                rtn.add(au);
            }
        } catch (JSONException e) {
            Log.w(TAG, "failed", e);
        }
        return rtn;
    }

    public static ArrayList<EventMetrics> retrieveEventMetricsDataFromRaw(final String rawData) {
        final ArrayList<EventMetrics> rtn = new ArrayList<EventMetrics>();
        try {
            JSONObject parent = new JSONObject(rawData);
            JSONArray data = parent.getJSONArray("event");
            for (int i = 0; i < data.length(); i++) {
                JSONObject rawJsonData = data.getJSONObject(i);
                EventMetrics metrics = new EventMetrics(rawJsonData.getLong("@avgUsersLastDay"),
                        rawJsonData.getLong("@avgUsersLastMonth"),
                        rawJsonData.getLong("@avgUsersLastWeek"),
                        rawJsonData.getLong("@totalCount"), rawJsonData.getLong("@totalSessions"),
                        rawJsonData.getLong("@usersLastDay"),
                        rawJsonData.getLong("@usersLastMonth"),
                        rawJsonData.getLong("@usersLastWeek"), rawJsonData.getString("@eventName"));
                rtn.add(metrics);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rtn;
    }

    public static void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight
                            - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
