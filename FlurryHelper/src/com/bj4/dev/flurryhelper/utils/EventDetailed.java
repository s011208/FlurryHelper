
package com.bj4.dev.flurryhelper.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDetailed {
    public static class Day {
        private String mDate;

        private long mTotalCount;

        private long mTotalSessions;

        private long mUniqueUsers;

        public Day(String date, long totalCount, long totalSession, long uniqueUsers) {
            mDate = date;
            mTotalCount = totalCount;
            mTotalSessions = totalSession;
            mUniqueUsers = uniqueUsers;
        }

        public String getDate() {
            return mDate;
        }

        public long getTotalCount() {
            return mTotalCount;
        }

        public long getTotalSessions() {
            return mTotalSessions;
        }

        public long getUniqueUsers() {
            return mUniqueUsers;
        }
    }

    private final ArrayList<Day> mDays = new ArrayList<Day>();

    private final HashMap<String, Map<String, Long>> mParameters = new HashMap<String, Map<String, Long>>();

    public void addDays(ArrayList<Day> days) {
        if (days == null)
            return;
        mDays.clear();
        mDays.addAll(days);
    }

    public void addParameter(final String key, final HashMap<String, Long> values) {
        if (key == null || values == null)
            return;
        mParameters.put(key, Utils.sortByValueDESC(values));
    }

    public ArrayList<Day> getDays() {
        return mDays;
    }

    public Map<String, Long> getParameters(final String key) {
        return mParameters.get(key);
    }

    public ArrayList<String> getAllActions() {
        ArrayList<String> rtn = new ArrayList<String>();
        List<String> actions = new ArrayList<String>(mParameters.keySet()); 
        Collections.sort(actions);
        rtn.addAll(actions);
        return rtn;
    }

}
