
package com.bj4.dev.flurryhelper.utils;

public class ActiveUser {
    private String mDate;

    private long mValue;

    public ActiveUser(String date, long value) {
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
