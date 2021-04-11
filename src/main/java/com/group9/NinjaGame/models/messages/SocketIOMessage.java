package com.group9.NinjaGame.models.messages;

public class SocketIOMessage {

    public MessageType type;
    public String reason;
    public Object data = null;

    public SocketIOMessage(MessageType type, String reason) {
        this.type = type;
        this.reason = reason;
    }

    public SocketIOMessage(MessageType type, String reason, Object data) {
        this.type = type;
        this.reason = reason;
        this.data = data;
    }
}
