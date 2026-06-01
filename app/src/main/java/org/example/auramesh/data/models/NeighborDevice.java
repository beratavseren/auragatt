package org.example.auramesh.data.models;


import android.bluetooth.BluetoothDevice;

import java.io.Serializable;

/**
 * Bluetooth cihazını temsil eden model sınıfı
 */
public class NeighborDevice implements Serializable {
    public String nodeId;
    public int rssi;
    public int messageCount; // Cihazdaki mesaj sayısı
    public String messageHash; // Cihazdaki mesajların hashi
    public BluetoothDevice physicalDevice; // Fiziksel cihaz nesnesi

    public NeighborDevice(String nodeId, int rssi, int messageCount, String messageHash, BluetoothDevice physicalDevice) {
        this.nodeId = nodeId;
        this.rssi = rssi;
        this.messageCount = messageCount;
        this.messageHash = messageHash;
        this.physicalDevice = physicalDevice;
    }
}
