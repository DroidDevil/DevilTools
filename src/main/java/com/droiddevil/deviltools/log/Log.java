package com.droiddevil.deviltools.log;

public class Log {

    public static final String TAG = "DevilTools";

    public static void v(final String msg) {
        android.util.Log.v(TAG, msg);
    }

    public static void v(final String msg, final Throwable tr) {
        android.util.Log.v(TAG, msg, tr);
    }

    public static void d(final String msg) {
        d(TAG, msg);
    }

    public static void d(final String tag, final String msg) {
        android.util.Log.d(tag, msg);
    }

    public static void d(final String msg, final Throwable tr) {
        android.util.Log.d(TAG, msg, tr);
    }

    public static void i(final String msg) {
        android.util.Log.i(TAG, msg);
    }

    public static void i(final String msg, final Throwable tr) {
        android.util.Log.i(TAG, msg, tr);
    }

    public static void w(final String msg) {
        android.util.Log.w(TAG, msg);
    }

    public static void w(final String msg, final Throwable tr) {
        android.util.Log.w(TAG, msg, tr);
    }

    public static void e(final String msg) {
        android.util.Log.e(TAG, msg);
    }

    public static void e(final String msg, final Throwable tr) {
        android.util.Log.e(TAG, msg, tr);
    }

    public static void wtf(final String msg) {
        android.util.Log.e(TAG, msg);
    }

    public static void wtf(final String msg, final Throwable tr) {
        android.util.Log.e(TAG, msg, tr);
    }

}
