package org.example.auramesh.events.RouterToHardwareEvents;

import android.bluetooth.BluetoothDevice;

import org.example.auramesh.data.models.NeighborDevice;

public class RejectConnectionRequestCommandEvent {
    public BluetoothDevice bluetoothDevice;

    public RejectConnectionRequestCommandEvent(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }
}
