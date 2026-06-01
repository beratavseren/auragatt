package org.example.auramesh.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

public class AuraIdentityManager {
    private static final String PREFS_NAME = "AuraMeshPrefs";
    private static final String KEY_NODE_ID = "MyVirtualNodeId";

    public static String getMyNodeId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String nodeId = prefs.getString(KEY_NODE_ID, null);

        if (nodeId == null) {
            // "A8F3B2..." formatında 12 haneli rastgele bir Sanal Kimlik üretir
            nodeId = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
            prefs.edit().putString(KEY_NODE_ID, nodeId).apply();
        }

        return nodeId;
    }
}