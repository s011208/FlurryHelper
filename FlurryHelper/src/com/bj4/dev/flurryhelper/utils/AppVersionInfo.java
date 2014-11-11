
package com.bj4.dev.flurryhelper.utils;

import java.util.ArrayList;
import java.util.Collections;

public class AppVersionInfo {
    public static final String VERSION_NOT_SET = "";

    private String mSelectedVersion = VERSION_NOT_SET;

    public int getSelectedVersionIndex() {
        if (mSelectedVersion == null || VERSION_NOT_SET.equals(mSelectedVersion))
            return 0;
        for (int i = 0; i < mVersions.size(); i++) {
            if (mSelectedVersion.equals(mVersions.get(i))) {
                return i + 1;
            }
        }
        return 0;
    }

    public String getSelectedVersion() {
        return mSelectedVersion;
    }

    public void setSelectedVersion(String v) {
        mSelectedVersion = v;
    }

    private final ArrayList<String> mVersions = new ArrayList<String>();

    public AppVersionInfo(ArrayList<String> versions) {
        if (versions == null)
            return;
        mVersions.addAll(versions);
        Collections.sort(mVersions);
    }

    public ArrayList<String> getVersions() {
        return mVersions;
    }
}
