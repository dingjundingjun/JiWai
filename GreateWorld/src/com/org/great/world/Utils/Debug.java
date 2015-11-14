package com.org.great.world.Utils;

import android.util.Log;

/**
 * Created by dj on 2015/7/18.
 * email:dingjun0225@gmail.com
 */
public class Debug {
    private static String TAG = "JIWAI";
    private static final boolean DEBUG = false;

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }
}
