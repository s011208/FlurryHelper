
package com.bj4.dev.flurryhelper.utils;

public class EventMetrics {
    private long mAvgUsersLastDay;

    private long mAvgUsersLastMonth;

    private long mAvgUsersLastWeek;

    private long mTotalCount;

    private long mTotalSessions;

    private long mUsersLastDay;

    private long mUsersLastMonth;

    private long mUsersLastWeek;

    private String mEventName;

    public EventMetrics(long auld, long aulm, long aulw, long tc, long ts, long uld, long ulm,
            long ulw, String en) {
        mAvgUsersLastDay = auld;
        mAvgUsersLastMonth = aulm;
        mAvgUsersLastWeek = aulw;
        mTotalCount = tc;
        mTotalSessions = ts;
        mUsersLastDay = uld;
        mUsersLastMonth = ulm;
        mUsersLastWeek = ulw;
        mEventName = en;
    }

    public String getEventName() {
        return mEventName;
    }

    public long getUsersLastWeek() {
        return mUsersLastWeek;
    }

    public long getUsersLastMonth() {
        return mUsersLastMonth;
    }

    public long getUsersLastDay() {
        return mUsersLastDay;
    }

    public long getAvgUsersLastDay() {
        return mAvgUsersLastDay;
    }

    public long getAvgUsersLastMonth() {
        return mAvgUsersLastMonth;
    }

    public long getAvgUsersLastWeek() {
        return mAvgUsersLastWeek;
    }

    public long getTotalCount() {
        return mTotalCount;
    }

    public long getTotalSessions() {
        return mTotalSessions;
    }
}
