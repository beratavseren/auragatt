package org.example.auramesh.events.HardwareToRouterEvents.Server;

import java.util.List;

public class RemoteInventoryReceivedAsServerEvent {
    public List<String> remoteInventory;

    public RemoteInventoryReceivedAsServerEvent(List<String> remoteInventory) {
        this.remoteInventory = remoteInventory;
    }
}
