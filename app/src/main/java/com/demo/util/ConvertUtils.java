package com.demo.util;

import android.content.Context;
import android.util.TypedValue;

import java.text.DecimalFormat;

public class ConvertUtils {

    public static String formatNumToTwoDecimals(double num) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(num);
    }

    public static String formatNumToOneDecimals(double num) {
        DecimalFormat format = new DecimalFormat("0");
        return format.format(num);
    }

    public static int double2Int(double i) {
        return (int) i;
    }

    public static int dp2px(Context context, int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    public static int px2dp(Context context, int pxVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pxVal, context.getResources().getDisplayMetrics());
    }

}
