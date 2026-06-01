package org.example.auramesh.events.HardwareToRouterEvents.Server;

import android.bluetooth.BluetoothDevice;

import org.example.auramesh.data.models.NeighborDevice;

public class IncomingBluetoothConnectionEvent {
    public BluetoothDevice bluetoothDevice;

    public IncomingBluetoothConnectionEvent(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }
}
