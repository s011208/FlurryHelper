
package com.bj4.dev.flurryhelper.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Utils {
    private static final String TAG = "Utils";

    @SuppressWarnings("deprecation")
    public static String parseOnInternet(String urlStr) {
        InputStream is = null;
        DataInputStream dis;
        String s;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(),
                    url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            is = url.openStream();
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
            try {
                JSONArray data = parent.getJSONArray("day");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject rawJsonData = data.getJSONObject(i);
                    AppMetricsData au = new AppMetricsData(rawJsonData.getString("@date"),
                            Long.valueOf(rawJsonData.getString("@value")));
                    rtn.add(au);
                }
            } catch (Exception e) {
                JSONObject rawJsonData = parent.getJSONObject("day");
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
            try {
                JSONArray data = parent.getJSONArray("event");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject rawJsonData = data.getJSONObject(i);
                    EventMetrics metrics = new EventMetrics(
                            rawJsonData.getLong("@avgUsersLastDay"),
                            rawJsonData.getLong("@avgUsersLastMonth"),
                            rawJsonData.getLong("@avgUsersLastWeek"),
                            rawJsonData.getLong("@totalCount"),
                            rawJsonData.getLong("@totalSessions"),
                            rawJsonData.getLong("@usersLastDay"),
                            rawJsonData.getLong("@usersLastMonth"),
                            rawJsonData.getLong("@usersLastWeek"),
                            rawJsonData.getString("@eventName"));
                    rtn.add(metrics);
                }
            } catch (Exception e) {
                JSONObject rawJsonData = parent.getJSONObject("event");
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

    public static EventDetailed retrieveEventDetailedDataFromRaw(final String rawData) {
        EventDetailed rtn = new EventDetailed();
        final ArrayList<EventDetailed.Day> days = new ArrayList<EventDetailed.Day>();
        try {
            JSONObject parent = new JSONObject(rawData);
            JSONArray jArrayDays = parent.getJSONArray("day");
            for (int i = 0; i < jArrayDays.length(); i++) {
                JSONObject jsonDay = jArrayDays.getJSONObject(i);
                EventDetailed.Day day = new EventDetailed.Day(jsonDay.getString("@date"),
                        jsonDay.getLong("@totalCount"), jsonDay.getLong("@totalSessions"),
                        jsonDay.getLong("@uniqueUsers"));
                days.add(day);
            }
            rtn.addDays(days);
            JSONArray jArrayKeys = parent.getJSONObject("parameters").getJSONArray("key");
            for (int i = 0; i < jArrayKeys.length(); i++) {
                JSONObject jsonItems = jArrayKeys.getJSONObject(i);
                final String action = jsonItems.getString("@name");
                HashMap<String, Long> label = new HashMap<String, Long>();
                try {
                    JSONArray jArrayLabels = jsonItems.getJSONArray("value");
                    for (int j = 0; j < jArrayLabels.length(); j++) {
                        JSONObject jsonLabel = jArrayLabels.getJSONObject(j);
                        label.put(jsonLabel.getString("@name"), jsonLabel.getLong("@totalCount"));
                    }
                } catch (Exception e) {
                    JSONObject jObjectLabels = jsonItems.getJSONObject("value");
                    label.put(jObjectLabels.getString("@name"),
                            jObjectLabels.getLong("@totalCount"));
                }
                rtn.addParameter(action, label);
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

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueASC(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDESC(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
