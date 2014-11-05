package com.bj4.dev.flurryhelper.utils;

public class AppMetricsData {
    private String mDate;

    private long mValue;

    public AppMetricsData(String date, long value) {
        mDate = date;
        mValue = value;
    }

    public String getDate() {
        return mDate;
    }

    public long getValue() {
        return mValue;
    }
}
