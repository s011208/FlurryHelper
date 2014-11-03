
package com.bj4.dev.flurryhelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.bj4.dev.flurryhelper.utils.CompanyName;
import com.bj4.dev.flurryhelper.utils.ProjectInfo;

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

    public void clearAllData() {
        synchronized (sLock) {
            sCompanyName = null;
            sProjectInfos.clear();
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
                // TODO Auto-generated method stub
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
