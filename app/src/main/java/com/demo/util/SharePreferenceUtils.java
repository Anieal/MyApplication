package com.demo.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.demo.App;

public class SharePreferenceUtils {
    private static final String NAME = "SharePreference";

    private static SharedPreferences getSharePreference() {
        return App.getApp().getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor() {
        return getSharePreference().edit();
    }

    public static void setData(String key, Object allTypeObject) {
        if (allTypeObject instanceof String) {
            getEditor().putString(key, (String) allTypeObject).apply();
        } else if (allTypeObject instanceof Integer) {
            getEditor().putInt(key, (Integer) allTypeObject).apply();
        } else if (allTypeObject instanceof Boolean) {
            getEditor().putBoolean(key, (Boolean) allTypeObject).apply();
        } else if (allTypeObject instanceof Float) {
            getEditor().putFloat(key, (Float) allTypeObject).apply();
        } else if (allTypeObject instanceof Long) {
            getEditor().putLong(key, (Long) allTypeObject).apply();
        }
    }

    public static Object getData(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return getSharePreference().getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return getSharePreference().getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return getSharePreference().getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return getSharePreference().getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return getSharePreference().getLong(key, (Long) defaultObject);
        }
        return null;
    }

    public static void remove(String key) {
        getEditor().remove(key).apply();
    }

    public static void clear() {
        getEditor().clear().apply();
    }

    public static boolean containsKey(String key) {
        return getSharePreference().contains(key);
    }

}
