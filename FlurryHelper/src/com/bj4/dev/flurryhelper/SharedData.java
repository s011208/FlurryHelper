
package com.bj4.dev.flurryhelper;

import java.util.ArrayList;

public class SharedData {
    private static final Object sLock = new Object();

    private static SharedData sInstance;

    private static CompanyName sCompanyName;

    private static final ArrayList<ProjectInfo> sProjectInfos = new ArrayList<ProjectInfo>();

    public static synchronized SharedData getInstance() {
        if (sInstance == null) {
            sInstance = new SharedData();
        }
        return sInstance;
    }

    private SharedData() {
    }

    public CompanyName getCompanyName() {
        return sCompanyName;
    }

    public void setCompanyName(final CompanyName cn) {
        synchronized (sLock) {
            sCompanyName = cn;
            sProjectInfos.clear();
        }
    }

    public void addProjectInfo(final ProjectInfo info) {
        synchronized (sLock) {
            sProjectInfos.add(info);
        }
    }

    public void addProjectInfos(final ArrayList<ProjectInfo> infos) {
        synchronized (sLock) {
            sProjectInfos.addAll(infos);
        }
    }
}
