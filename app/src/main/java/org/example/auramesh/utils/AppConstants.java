package org.example.auramesh.utils;

import android.os.ParcelUuid;
import java.util.UUID;

public final class AppConstants {

    private AppConstants() {}

    // AURA yerine geçerli bir HEX (Örn: A0EA, A11A, ABCD, vb.) kullanıldı
    public static final ParcelUuid AURA_MESH_UUID = new ParcelUuid(UUID.fromString("0000A0EA-0000-1000-8000-00805F9B34FB"));

    // RFCOMM için de geçerli bir HEX kullanıldı
    public static final UUID RFCOMM_SOCKET_UUID = UUID.fromString("2222A0EA-0000-1000-8000-00805F9B34FB");

    public static final String TARGET_PUBLIC = "BROADCAST_ALL";
    public static final long DEFAULT_MESSAGE_TTL = 86400000L; // 24 Saat
}