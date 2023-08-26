package com.lzy.imagepicker.util;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * 用于解决provider冲突的util
 * <p>
 * Author: nanchen
 * Email: liushilin520@foxmail.com
 * Date: 2017-03-22  18:55
 */

public class ProviderUtil {

    @NonNull
    public static String getFileProviderName(@NonNull Context context) {
        return context.getPackageName() + ".provider";
    }
}
