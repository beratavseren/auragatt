package org.example.auramesh.events.HardwareToRouterEvents.Client;

import java.util.List;

public class RemoteInventoryReceivedAsClientEvent {
    public List<String> remoteInventory;

    public RemoteInventoryReceivedAsClientEvent(List<String> remoteInventory) {
        this.remoteInventory = remoteInventory;
    }
}
