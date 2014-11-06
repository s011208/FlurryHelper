
package com.bj4.dev.flurryhelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.bj4.dev.flurryhelper.utils.AppMetricsData;
import com.bj4.dev.flurryhelper.utils.CompanyName;
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

    private static final Object sLock = new Object();

    private static SharedData sInstance;

    private static CompanyName sCompanyName;

    private static final ArrayList<ProjectInfo> sProjectInfos = new ArrayList<ProjectInfo>();

    private static final HashMap<String, HashMap<String, ArrayList<AppMetricsData>>> sAppMetricsData = new HashMap<String, HashMap<String, ArrayList<AppMetricsData>>>();

    public static synchronized SharedData getInstance() {
        if (sInstance == null) {
            sInstance = new SharedData();
        }
        return sInstance;
    }

    private SharedData() {
    }

    public void addMetricsData(final String projectKey, final String key,
            final ArrayList<AppMetricsData> values) {
        if (values == null || key == null || projectKey == null)
            return;
        synchronized (sLock) {
            HashMap<String, ArrayList<AppMetricsData>> data = sAppMetricsData.get(projectKey);
            if (data == null) {
                data = new HashMap<String, ArrayList<AppMetricsData>>();
            }
            data.put(key, values);
            sAppMetricsData.put(projectKey, data);
        }
    }

    public HashMap<String, ArrayList<AppMetricsData>> getAppMetricsData(final String projectKey) {
        if (projectKey == null)
            return null;
        synchronized (sLock) {
            return sAppMetricsData.get(projectKey);
        }
    }

    public void clearAllData() {
        synchronized (sLock) {
            sCompanyName = null;
            sProjectInfos.clear();
            sAppMetricsData.clear();
        }
    }

    public CompanyName getCompanyName() {
        synchronized (sLock) {
            return sCompanyName;
        }
    }

    public void setCompanyName(final CompanyName cn) {
        synchronized (sLock) {
            sCompanyName = cn;
            sProjectInfos.clear();
            sAppMetricsData.clear();
        }
    }

    public void addProjectInfo(final ProjectInfo info) {
        synchronized (sLock) {
            sProjectInfos.add(info);
            sortProjectInfos();
        }
    }

    public void addProjectInfos(final ArrayList<ProjectInfo> infos) {
        synchronized (sLock) {
            sProjectInfos.addAll(infos);
            sortProjectInfos();
        }
    }

    private void sortProjectInfos() {
        Collections.sort(sProjectInfos, new Comparator<ProjectInfo>() {
            @Override
            public int compare(ProjectInfo lhs, ProjectInfo rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
    }

    public ArrayList<ProjectInfo> getProjectInfos() {
        synchronized (sLock) {
            return sProjectInfos;
        }
    }

}
