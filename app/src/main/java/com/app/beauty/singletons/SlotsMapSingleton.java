package com.app.beauty.singletons;

import android.util.Log;

import com.app.beauty.Info.Info;

import java.util.HashMap;
import java.util.Map;

public class SlotsMapSingleton implements Info {
    private static Map<Integer, String> instance;

    private SlotsMapSingleton() {
    }

    public static Map<Integer, String> getInstance() {
        int childCount = 24;
        if (instance == null) {
            instance = new HashMap<>();
            int starting = 10;
            for (int i = 0; i < childCount; i++) {
                int x = (int) (starting + (Math.round((float) i * 0.5)));
                Log.i(TAG, "getInstance: " + x);
                String ampm = "AM";
                if (x >= 12) {
                    ampm = "PM";
                    if (x > 12)
                        x -= 11;
                }
                if (i % 2 == 0)
                    instance.put(i, x + " " + ampm + " - " + x + ":30" + " " + ampm);
                else if (x == 12)
                    instance.put(i, (x - 1) + ":30 AM - " + x + " PM");
                else
                    instance.put(i, (x - 1) + ":30 " + ampm + " - " + x + " " + ampm);
            }
        }
        return instance;
    }
}
