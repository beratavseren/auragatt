package org.example.auramesh.events.HardwareToRegisterEvents;

import org.example.auramesh.data.models.AuraMessage;

import java.util.List;

public class NewMessagesSavedToDatabaseEvent {
    public List<AuraMessage>  auraMessages;

    public NewMessagesSavedToDatabaseEvent(List<AuraMessage> auraMessages) {
        this.auraMessages = auraMessages;
    }
}
