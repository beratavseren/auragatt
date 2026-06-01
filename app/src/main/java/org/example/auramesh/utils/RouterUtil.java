package org.example.auramesh.utils;

import org.example.auramesh.Register.BleAdvertiseRegister;
import org.example.auramesh.Register.NeighborRegister;
import org.example.auramesh.data.models.NeighborDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouterUtil {
        public static boolean amITheInitiator(String myNodeId, String neighborNodeId) {
            if (myNodeId == null || neighborNodeId == null) return false;

            String cleanMyNodeId = myNodeId.replace(":", "").toUpperCase();
            String cleanNeighborNodeId= neighborNodeId.replace(":", "").toUpperCase();

            return cleanMyNodeId.compareTo(cleanNeighborNodeId) > 0;
        }

        public static List<String> getUnsyncedNeighbors(){
            List<String> notSyncedNeighbors = new ArrayList<>();

            String myMessageHash = BleAdvertiseRegister.getInstance().getMessageHash();
            int myMessageCount = BleAdvertiseRegister.getInstance().getMessageCount();

            for (String macAddress : NeighborRegister.getInstance().getNeighborMap().keySet()) {

                NeighborDevice neighborDevice = NeighborRegister.getInstance().getNeighbor(macAddress);

                if (neighborDevice != null && (!Objects.equals(myMessageHash, neighborDevice.messageHash) || myMessageCount != neighborDevice.messageCount)){
                    notSyncedNeighbors.add(macAddress);
                }
            }

            return notSyncedNeighbors;
        }
}
