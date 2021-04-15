package com.group9.NinjaGame.services;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocketIOService {

    private final SocketIONamespace namespace;
    private final BasicGameModeService basicGameModeService;
    private final MultiplayerGameService multiplayerGameService;

    @Autowired
    public SocketIOService(SocketIOServer server, BasicGameModeService basicGameModeService, MultiplayerGameService multiplayerGameService) {
        this.basicGameModeService = basicGameModeService;
        this.multiplayerGameService = multiplayerGameService;

        this.namespace = server.addNamespace("/game");
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());

        basicGameModeService.registerListeners(namespace);
        multiplayerGameService.registerListeners(namespace);
    }
    // Socket.io related methods

    public ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            System.out.println("Client[{}] - Connected to chat module through '{}'" + client.getSessionId().toString() + handshakeData.getUrl());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {

            multiplayerGameService.disconnectPlayerFromPreviousLobbies(client);

            System.out.println("Client[{}] - Disconnected from chat module." + client.getSessionId().toString());
        };
    }
}
