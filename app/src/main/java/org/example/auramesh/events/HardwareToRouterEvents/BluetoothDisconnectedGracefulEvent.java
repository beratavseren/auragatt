package org.example.auramesh.events.HardwareToRouterEvents;

import org.example.auramesh.data.models.AuraMessage;

import java.util.List;

public class BluetoothDisconnectedGracefulEvent {
    public List<AuraMessage> remoteMessages;

    public BluetoothDisconnectedGracefulEvent(List<AuraMessage> remoteMessages) {
        this.remoteMessages = remoteMessages;
    }
}
