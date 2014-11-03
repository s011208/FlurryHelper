
package com.bj4.dev.flurryhelper.utils;

public class ProjectInfo {
    private String mApiKey;

    private String mCreatedDate;

    private String mPlatform;

    private String mName;

    public ProjectInfo(String api, String cd, String p, String name) {
        mApiKey = api;
        mCreatedDate = cd;
        mPlatform = p;
        mName = name;
    }

    public String getApiKey() {
        return mApiKey;
    }

    public String getCreatedDate() {
        return mCreatedDate;
    }

    public String getPlatform() {
        return mPlatform;
    }

    public String getName() {
        return mName;
    }

    public String toString() {
        return "mApiKey: " + mApiKey + ", mCreatedDate: " + mCreatedDate + ", mPlatform: "
                + mPlatform + ", mName: " + mName;
    }
}
