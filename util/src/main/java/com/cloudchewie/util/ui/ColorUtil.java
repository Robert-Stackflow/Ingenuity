package com.cloudchewie.util.ui;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

public class ColorUtil {
    /**
     * 判断某个颜色是否为深色
     *
     * @param color 待判断颜色
     * @return 是否为深色
     */
    public static boolean isDarkColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) < 0.5;
    }
}
