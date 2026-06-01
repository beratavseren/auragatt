package org.example.auramesh.events.RouterToHardwareEvents;

import org.example.auramesh.data.models.NeighborDevice;

public class ConnectWithBluetoothCommandEvent {
    public final NeighborDevice targetDevice;

    public ConnectWithBluetoothCommandEvent(NeighborDevice targetDevice) {
        this.targetDevice = targetDevice;
    }
}
