package org.example.auramesh.Register;

import org.example.auramesh.data.models.NeighborDevice;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NeighborRegister {
    public static volatile NeighborRegister instance;

    private final Map<String, NeighborDevice> neighborMap = new ConcurrentHashMap<>();

    private NeighborRegister() {
    }

    public static NeighborRegister getInstance() {
        if (instance == null) {
            synchronized (NeighborRegister.class) {
                if (instance == null) {
                    instance = new NeighborRegister();
                }
            }
        }
        return instance;
    }

    public void addOrUpdateNeighbor(String nodeId, NeighborDevice neighborDevice) {
        neighborMap.put(nodeId, neighborDevice);
    }

    public void deleteNeighbor(String macAddress) {
        neighborMap.remove(macAddress);
    }

    public NeighborDevice getNeighbor(String macAddress) {
        return neighborMap.get(macAddress);
    }

    public Map<String, NeighborDevice> getNeighborMap() {
        return Collections.unmodifiableMap(neighborMap);
    }

    public void clearAllNeighbors() {
        neighborMap.clear();
    }

    public boolean isEmpty() {
        return neighborMap.isEmpty();
    }
}