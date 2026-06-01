package org.example.auramesh.events.RouterToHardwareEvents;

import org.example.auramesh.data.models.AuraMessage;

import java.util.List;

public class TransmitMessagesCommandEvent {
    public List<AuraMessage> messagesToSend;

    public TransmitMessagesCommandEvent(List<AuraMessage> messagesToSend) {
        this.messagesToSend = messagesToSend;
    }
}
