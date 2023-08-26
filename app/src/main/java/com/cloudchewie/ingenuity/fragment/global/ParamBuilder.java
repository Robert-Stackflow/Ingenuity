package com.cloudchewie.ingenuity.fragment.global;

import android.os.Bundle;

import java.io.Serializable;

public class ParamBuilder {
    protected Object obj = null;
    protected int userId = -1;
    protected boolean enabledRefresh = true;
    protected boolean enabledLoadMore = true;
    protected boolean enabledOverscrollDrag = true;
    protected String title;
    BaseFragment.TYPE type;

    public static ParamBuilder init() {
        return new ParamBuilder();
    }

    public ParamBuilder setType(BaseFragment.TYPE paramType) {
        type = paramType;
        return this;
    }

    public ParamBuilder setObj(Object paramObj) {
        obj = paramObj;
        return this;
    }

    public ParamBuilder setUserId(int paramUserId) {
        userId = paramUserId;
        return this;
    }

    public ParamBuilder enableRefresh(boolean enable) {
        enabledRefresh = enable;
        return this;
    }

    public ParamBuilder enableLoadMore(boolean enable) {
        enabledLoadMore = enable;
        return this;
    }

    public ParamBuilder enableOverscrollDrag(boolean enable) {
        enabledOverscrollDrag = enable;
        return this;
    }

    public Bundle build() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BaseFragment.EXTRA_TYPE, type);
        bundle.putInt(BaseFragment.EXTRA_USERID, userId);
        if (obj instanceof Serializable)
            bundle.putSerializable(BaseFragment.EXTRA_OBJECT, (Serializable) obj);
        bundle.putBoolean(BaseFragment.EXTRA_ENABLE_REFRESH, enabledRefresh);
        bundle.putBoolean(BaseFragment.EXTRA_ENABLE_OVERSCROLLDRAG, enabledOverscrollDrag);
        bundle.putBoolean(BaseFragment.EXTRA_ENABLE_LOADMORE, enabledLoadMore);
        return bundle;
    }
}