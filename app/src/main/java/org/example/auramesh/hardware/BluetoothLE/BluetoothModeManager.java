package org.example.auramesh.hardware.BluetoothLE;

import android.util.Log;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Scanner ve Advertiser'ın aynı anda çalışmasını engelleyen senkronizasyon yöneticisi
 * - Advertiser çalışırken Scanner durur
 * - Scanner çalışırken Advertiser durur
 * - Bir işlem bitince diğer başlayabilir
 */
public class BluetoothModeManager {

    private static final String TAG = "BluetoothModeManager";
    private static BluetoothModeManager instance;
    private static final Object instanceLock = new Object();

    private final ReadWriteLock modeLock = new ReentrantReadWriteLock();

    private volatile Mode currentMode = Mode.IDLE;
    private volatile boolean shouldRestartScanner = false;
    private volatile boolean shouldRestartAdvertiser = false;

    private AuraBleScanner scanner;
    private AuraBleAdvertiser advertiser;

    public enum Mode {
        IDLE,           // Hiçbir şey çalışmıyor
        SCANNING,       // Scanner aktif
        ADVERTISING     // Advertiser aktif
    }

    private BluetoothModeManager() {
    }

    public static BluetoothModeManager getInstance() {
        if (instance == null) {
            synchronized (instanceLock) {
                if (instance == null) {
                    instance = new BluetoothModeManager();
                }
            }
        }
        return instance;
    }

    public void setScannerAndAdvertiser(AuraBleScanner scanner, AuraBleAdvertiser advertiser) {
        this.scanner = scanner;
        this.advertiser = advertiser;
    }

    // ==========================================
    // SCANNER OPERASYONLARI
    // ==========================================

    /**
     * Scanner başlat - eğer Advertiser çalışıyorsa onu durdur
     */
    public void startScanning() {
        modeLock.writeLock().lock();
        try {
            if (currentMode == Mode.ADVERTISING) {
                Log.d(TAG, "⛔ Advertiser çalışıyor, Scanner başlamadan önce duracak...");
                if (advertiser != null) {
                    advertiser._internalStop();
                }
                shouldRestartAdvertiser = true;
            }

            if (scanner != null && currentMode != Mode.SCANNING) {
                scanner._internalStart();
                currentMode = Mode.SCANNING;
                Log.d(TAG, "✅ SCANNER BAŞLADI - Mode: SCANNING");
            }
        } finally {
            modeLock.writeLock().unlock();
        }
    }

    /**
     * Scanner durdur ve Advertiser'ı eski durumuna dön
     */
    public void stopScanning() {
        modeLock.writeLock().lock();
        try {
            if (currentMode == Mode.SCANNING) {
                if (scanner != null) {
                    scanner._internalStop();
                }
                currentMode = Mode.IDLE;
                Log.d(TAG, "⛔ SCANNER DURDURULDU - Mode: IDLE");

                // Eğer Advertiser durdurulmuşsa tekrar başlat
                if (shouldRestartAdvertiser && advertiser != null) {
                    Log.d(TAG, "🔄 Advertiser tekrar başlıyor...");
                    advertiser._internalStart();
                    currentMode = Mode.ADVERTISING;
                    shouldRestartAdvertiser = false;
                    Log.d(TAG, "✅ ADVERTISER YENİDEN BAŞLADI - Mode: ADVERTISING");
                }
            }
        } finally {
            modeLock.writeLock().unlock();
        }
    }

    // ==========================================
    // ADVERTISER OPERASYONLARI
    // ==========================================

    /**
     * Advertiser başlat - eğer Scanner çalışıyorsa onu durdur
     */
    public void startAdvertising() {
        modeLock.writeLock().lock();
        try {
            if (currentMode == Mode.SCANNING) {
                Log.d(TAG, "⛔ Scanner çalışıyor, Advertiser başlamadan önce duracak...");
                if (scanner != null) {
                    scanner._internalStop();
                }
                shouldRestartScanner = true;
            }

            if (advertiser != null && currentMode != Mode.ADVERTISING) {
                advertiser._internalStart();
                currentMode = Mode.ADVERTISING;
                Log.d(TAG, "✅ ADVERTISER BAŞLADI - Mode: ADVERTISING");
            }
        } finally {
            modeLock.writeLock().unlock();
        }
    }

    /**
     * Advertiser durdur ve Scanner'ı eski durumuna dön
     */
    public void stopAdvertising() {
        modeLock.writeLock().lock();
        try {
            if (currentMode == Mode.ADVERTISING) {
                if (advertiser != null) {
                    advertiser._internalStop();
                }
                currentMode = Mode.IDLE;
                Log.d(TAG, "⛔ ADVERTISER DURDURULDU - Mode: IDLE");

                // Eğer Scanner durdurulmuşsa tekrar başlat
                if (shouldRestartScanner && scanner != null) {
                    Log.d(TAG, "🔄 Scanner tekrar başlıyor...");
                    scanner._internalStart();
                    currentMode = Mode.SCANNING;
                    shouldRestartScanner = false;
                    Log.d(TAG, "✅ SCANNER YENİDEN BAŞLADI - Mode: SCANNING");
                }
            }
        } finally {
            modeLock.writeLock().unlock();
        }
    }

    // ==========================================
    // UTILITY METOTLAR
    // ==========================================

    public Mode getCurrentMode() {
        modeLock.readLock().lock();
        try {
            return currentMode;
        } finally {
            modeLock.readLock().unlock();
        }
    }

    @SuppressWarnings("unused")
    public boolean isScanningActive() {
        return getCurrentMode() == Mode.SCANNING;
    }

    @SuppressWarnings("unused")
    public boolean isAdvertisingActive() {
        return getCurrentMode() == Mode.ADVERTISING;
    }

    public void stopAll() {
        modeLock.writeLock().lock();
        try {
            if (scanner != null) {
                scanner._internalStop();
            }
            if (advertiser != null) {
                advertiser._internalStop();
            }
            currentMode = Mode.IDLE;
            shouldRestartScanner = false;
            shouldRestartAdvertiser = false;
            Log.d(TAG, "🛑 HER ŞEY DURDURULDU - Mode: IDLE");
        } finally {
            modeLock.writeLock().unlock();
        }
    }

    @SuppressWarnings("unused")
    public String getStatusString() {
        modeLock.readLock().lock();
        try {
            return "BluetoothMode: " + currentMode +
                    " | ScannerPending: " + shouldRestartScanner +
                    " | AdvertiserPending: " + shouldRestartAdvertiser;
        } finally {
            modeLock.readLock().unlock();
        }
    }
}
