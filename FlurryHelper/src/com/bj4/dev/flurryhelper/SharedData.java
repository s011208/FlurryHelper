
package com.bj4.dev.flurryhelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.bj4.dev.flurryhelper.utils.AppMetricsData;
import com.bj4.dev.flurryhelper.utils.CompanyName;
import com.bj4.dev.flurryhelper.utils.EventDetailed;
import com.bj4.dev.flurryhelper.utils.EventMetrics;
import com.bj4.dev.flurryhelper.utils.ProjectInfo;

public class SharedData {
    // APPMETRICS
    public static final String APPMETRICS_TYPE_ACTIVE_USERS = "ActiveUsers";

    public static final String APPMETRICS_TYPE_ACTIVE_USERS_BY_WEEK = "ActiveUsersByWeek";

    public static final String APPMETRICS_TYPE_ACTIVE_USERS_BY_MONTH = "ActiveUsersByMonth";

    public static final String APPMETRICS_TYPE_NEW_USERS = "NewUsers";

    public static final String APPMETRICS_TYPE_MEDIAN_SESSION_LENGTH = "MedianSessionLength";

    public static final String APPMETRICS_TYPE_AVG_SESSION_LENGTH = "AvgSessionLength";

    public static final String APPMETRICS_TYPE_SESSIONS = "Sessions";

    public static final String APPMETRICS_TYPE_RETAINED_USERS = "RetainedUsers";

    public static final String APPMETRICS_TYPE_PAGEVIEWS = "PageViews";

    public static final String APPMETRICS_TYPE_AVG_PAGEVIEWS_PER_SESSION = "AvgPageViewsPerSession";

    public static final ArrayList<String> APPMETRICS_LIST = new ArrayList<String>();
    static {
        APPMETRICS_LIST.add(APPMETRICS_TYPE_ACTIVE_USERS);
        APPMETRICS_LIST.add(APPMETRICS_TYPE_ACTIVE_USERS_BY_WEEK);
        APPMETRICS_LIST.add(APPMETRICS_TYPE_ACTIVE_USERS_BY_MONTH);
        APPMETRICS_LIST.add(APPMETRICS_TYPE_NEW_USERS);
        APPMETRICS_LIST.add(APPMETRICS_TYPE_MEDIAN_SESSION_LENGTH);
        APPMETRICS_LIST.add(APPMETRICS_TYPE_AVG_SESSION_LENGTH);
        APPMETRICS_LIST.add(APPMETRICS_TYPE_SESSIONS);
        APPMETRICS_LIST.add(APPMETRICS_TYPE_RETAINED_USERS);
        APPMETRICS_LIST.add(APPMETRICS_TYPE_PAGEVIEWS);
        APPMETRICS_LIST.add(APPMETRICS_TYPE_AVG_PAGEVIEWS_PER_SESSION);
    }

    private static final Object sLock = new Object();

    private static CompanyName sCompanyName;

    private static SharedPreferencesHelper sSharedPreferencesHelper;

    public static void setPrefHelper(SharedPreferencesHelper helper) {
        sSharedPreferencesHelper = helper;
    }

    private SharedData() {
    }

    private static final ArrayList<ProjectInfo> sProjectInfos = new ArrayList<ProjectInfo>();

    private static final HashMap<String, HashMap<String, ArrayList<AppMetricsData>>> sAppMetricsData = new HashMap<String, HashMap<String, ArrayList<AppMetricsData>>>();

    private static final HashMap<String, ArrayList<EventMetrics>> sEventMetrics = new HashMap<String, ArrayList<EventMetrics>>();

    private static final HashMap<String, EventDetailed> sEventDetailed = new HashMap<String, EventDetailed>();

    public static void addEventDetailedData(final String eventName, EventDetailed detailed,
            int timePeriod, int version) {
        if (eventName == null || detailed == null)
            return;
        synchronized (sLock) {
            if (timePeriod != sSharedPreferencesHelper.getDisplayPeriod())
                return;
            sEventDetailed.put(eventName, detailed);
        }
    }

    public static EventDetailed getEventDetailedData(final String eventName) {
        if (eventName == null)
            return null;
        synchronized (sLock) {
            return sEventDetailed.get(eventName);
        }
    }

    public static void addEventMetricsData(final String projectKey,
            ArrayList<EventMetrics> eventMatrics, int timePeriod, int version) {
        if (eventMatrics == null || projectKey == null)
            return;
        synchronized (sLock) {
            if (timePeriod != sSharedPreferencesHelper.getDisplayPeriod())
                return;
            Collections.sort(eventMatrics, new Comparator<EventMetrics>() {

                @Override
                public int compare(EventMetrics lhs, EventMetrics rhs) {
                    if (lhs == null)
                        return 0;
                    return lhs.getEventName().compareToIgnoreCase(rhs.getEventName());
                }
            });
            sEventMetrics.put(projectKey, eventMatrics);
        }
    }

    public static void onDateOrVersionChanged() {
        synchronized (sLock) {
            sAppMetricsData.clear();
            sEventMetrics.clear();
            sEventDetailed.clear();
        }
    }

    public static ArrayList<EventMetrics> getEventMetricsData(final String projectKey) {
        synchronized (sLock) {
            return sEventMetrics.get(projectKey);
        }
    }

    public static void addAppMetricsData(final String projectKey, final String key,
            final ArrayList<AppMetricsData> values, int timePeriod, int version) {
        if (values == null || key == null || projectKey == null)
            return;
        synchronized (sLock) {
            HashMap<String, ArrayList<AppMetricsData>> data = sAppMetricsData.get(projectKey);
            if (data == null) {
                data = new HashMap<String, ArrayList<AppMetricsData>>();
            }
            if (timePeriod != sSharedPreferencesHelper.getDisplayPeriod())
                return;
            data.put(key, values);
            sAppMetricsData.put(projectKey, data);
        }
    }

    public static HashMap<String, ArrayList<AppMetricsData>> getAppMetricsData(
            final String projectKey) {
        if (projectKey == null)
            return null;
        synchronized (sLock) {
            return sAppMetricsData.get(projectKey);
        }
    }

    public static void clearAllData() {
        synchronized (sLock) {
            sCompanyName = null;
            sProjectInfos.clear();
            sAppMetricsData.clear();
            sEventMetrics.clear();
            sEventDetailed.clear();
        }
    }

    public static CompanyName getCompanyName() {
        synchronized (sLock) {
            return sCompanyName;
        }
    }

    public static void setCompanyName(final CompanyName cn) {
        synchronized (sLock) {
            sCompanyName = cn;
            sProjectInfos.clear();
            sAppMetricsData.clear();
            sEventMetrics.clear();
        }
    }

    public static void addProjectInfo(final ProjectInfo info) {
        synchronized (sLock) {
            sProjectInfos.add(info);
            sortProjectInfos();
        }
    }

    public static void addProjectInfos(final ArrayList<ProjectInfo> infos) {
        synchronized (sLock) {
            sProjectInfos.addAll(infos);
            sortProjectInfos();
        }
    }

    private static void sortProjectInfos() {
        Collections.sort(sProjectInfos, new Comparator<ProjectInfo>() {
            @Override
            public int compare(ProjectInfo lhs, ProjectInfo rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
    }

    public static ArrayList<ProjectInfo> getProjectInfos() {
        synchronized (sLock) {
            return sProjectInfos;
        }
    }

}
