package com.mobile.test.testmonitor.collector;

import android.content.Context;

public abstract class CollectorBase {
    protected Context mContext;
    public String mCollectorTag;

    CollectorBase(Context context) {
        mContext = context;
        mCollectorTag = "Base";
    }

    public abstract Object updateInformation();
    public abstract Object defaultInformation();
}
