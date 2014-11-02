
package com.bj4.dev.flurryhelper;

public class CompanyName {
    private String mCompanyName;

    private String mGenerateDate;

    private String mVersion;

    public CompanyName(String cn, String gd, String v) {
        mCompanyName = cn;
        mGenerateDate = gd;
        mVersion = v;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public String getGeneratedDate() {
        return mGenerateDate;
    }

    public String getVersion() {
        return mVersion;
    }

    public String toString() {
        return "company name: " + getCompanyName() + ", date: " + getGeneratedDate()
                + ", version: " + getVersion();
    }
}
