package org.example.auramesh.events.RouterToHardwareEvents;

import java.util.List;

public class TransmitInventoryCommandEvent {
    public List<String> myInventory;

    public TransmitInventoryCommandEvent(List<String> myInventory) {
        this.myInventory = myInventory;
    }
}
