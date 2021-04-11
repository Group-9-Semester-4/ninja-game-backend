package com.group9.NinjaGame.helpers;

import com.corundumstudio.socketio.AckRequest;
import com.group9.NinjaGame.models.messages.MessageType;
import com.group9.NinjaGame.models.messages.SocketIOMessage;

public class SocketIOHelper {

    public static void SendMessage(AckRequest ackRequest, MessageType type, String message) {
        SocketIOMessage ioMessage = new SocketIOMessage(type, message);
        ackRequest.sendAckData(ioMessage);
    }

    public static void SendMessage(AckRequest ackRequest, MessageType type, String message, Object data) {
        SocketIOMessage ioMessage = new SocketIOMessage(type, message, data);
        ackRequest.sendAckData(ioMessage);
    }

}
