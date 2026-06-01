package org.example.auramesh.events.UiToRouterEvents;

import org.example.auramesh.data.models.AuraMessage;

public class UserSendMessageEvent {
    public final AuraMessage auraMessage;

    public UserSendMessageEvent(AuraMessage auraMessage) {
        this.auraMessage = auraMessage;
    }
}
