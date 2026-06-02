package org.example.auramesh;

import android.app.Application;
import org.example.auramesh.hardware.Bluetooth.AuraGattService;
import org.example.auramesh.hardware.BluetoothLE.AuraBleAdvertiser;
import org.example.auramesh.hardware.BluetoothLE.AuraBleScanner;
import org.example.auramesh.hardware.BluetoothLE.BluetoothModeManager;
import org.example.auramesh.routing.GossipRouter;
import org.example.auramesh.routing.StateManager;

public class AuraMeshApplication extends Application {

    private AuraGattService bluetoothService;
    private AuraBleAdvertiser bleAdvertiser;
    private AuraBleScanner bleScanner;
    private GossipRouter gossipRouter;
    private StateManager stateManager;

    // ✅ BluetoothModeManager referansı
    private BluetoothModeManager modeManager;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void startMeshServices() {
        if (bluetoothService == null) {
            stateManager = new StateManager(this);
            bluetoothService = new AuraGattService(this);
            gossipRouter = new GossipRouter(this);
            bleAdvertiser = new AuraBleAdvertiser();
            bleScanner = new AuraBleScanner();

            // ✅ ModeManager'ı al ve Scanner/Advertiser'ı kaydı et
            modeManager = BluetoothModeManager.getInstance();
            modeManager.setScannerAndAdvertiser(bleScanner, bleAdvertiser);

            // ✅ Başlangıçta Scanner'ı başlat
            bleScanner.start();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (bluetoothService != null) bluetoothService.onDestroy();
        if (gossipRouter != null) gossipRouter.onDestroy();
        if (stateManager != null) stateManager.onDestroy();

        // ✅ Her şeyi güvenli bir şekilde durdur
        if (modeManager != null) modeManager.stopAll();
    }
}
