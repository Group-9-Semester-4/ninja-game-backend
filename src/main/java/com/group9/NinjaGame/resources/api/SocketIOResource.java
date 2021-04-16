package com.group9.NinjaGame.resources.api;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.messages.MessageType;
import com.group9.NinjaGame.models.params.JoinGameParam;
import com.group9.NinjaGame.models.params.LeaveGameParam;
import com.group9.NinjaGame.models.params.StartGameParam;
import com.group9.NinjaGame.services.BasicGameModeService;
import com.group9.NinjaGame.services.MultiplayerGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.group9.NinjaGame.helpers.SocketIOHelper.SendMessage;

@Component
public class SocketIOResource {

    private final SocketIONamespace namespace;
    private final BasicGameModeService basicGameModeService;
    private final MultiplayerGameService multiplayerGameService;

    @Autowired
    public SocketIOResource(SocketIOServer server, BasicGameModeService basicGameModeService, MultiplayerGameService multiplayerGameService) {
        this.basicGameModeService = basicGameModeService;
        this.multiplayerGameService = multiplayerGameService;

        this.namespace = server.addNamespace("/game");
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());
        this.namespace.addEventListener("leave", LeaveGameParam.class, this::onLeave);

        this.namespace.addEventListener("join", JoinGameParam.class, this::onJoin);
        this.namespace.addEventListener("start", StartGameParam.class, this::onStart);

        basicGameModeService.registerListeners(namespace);
        multiplayerGameService.registerListeners(namespace);
    }

    public ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            System.out.println("Client[{}] - Connected to chat module through '{}'" + client.getSessionId().toString() + handshakeData.getUrl());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {

            disconnectPlayerFromPreviousLobbies(client);

            System.out.println("Client[{}] - Disconnected from chat module." + client.getSessionId().toString());
        };
    }

    public void onLeave(SocketIOClient client, LeaveGameParam param, AckRequest ackRequest) {
        boolean res = multiplayerGameService.onLeave(param);
        if (res) {
            disconnectPlayerFromPreviousLobbies(client);
        }

        client.leaveRoom(param.gameId.toString());

        SendMessage(ackRequest, MessageType.SUCCESS, "Successfully disconnected");
    }

    public void disconnectPlayerFromPreviousLobbies(SocketIOClient client) {
        UUID playerId = client.getSessionId();
        GameInfo gameInfo = multiplayerGameService.getGameInfoByPlayerId(client.getSessionId());

        client.leaveRoom(gameInfo.gameId.toString()); // this is the reason why we come back

        boolean res = multiplayerGameService.removePlayerFromLobby(playerId, gameInfo);

        if (!res) {
            destroyGame(gameInfo.gameId);
            this.namespace.getRoomOperations(gameInfo.gameId.toString()).sendEvent("lobby-update", gameInfo);
        }

    }

    public void destroyGame(UUID gameId) {
        multiplayerGameService.destroyGame(gameId);
        namespace.getRoomOperations(gameId.toString()).disconnect();
    }

    public void onJoin(SocketIOClient client, JoinGameParam param, AckRequest ackRequest) {
        disconnectPlayerFromPreviousLobbies(client);

        GameInfo gameInfo = multiplayerGameService.onJoin(param, client.getSessionId());

        if (gameInfo != null) {
            client.joinRoom(gameInfo.gameId.toString());

            SendMessage(ackRequest, MessageType.SUCCESS, "Successfully joined", gameInfo);

            namespace.getRoomOperations(gameInfo.gameId.toString()).sendEvent("lobby-update", gameInfo);
        } else {
            SendMessage(ackRequest, MessageType.ERROR, "Game not found");
        }
    }

    public void onStart(SocketIOClient client, StartGameParam param, AckRequest ackRequest) {
        try {
            Pair<Game, GameInfo> res = multiplayerGameService.onStart(param, client.getSessionId());
            if (res != null) {
                namespace.getRoomOperations(res.getFirst().getId().toString()).sendEvent("start", res.getSecond());
            }
        } catch (Exception exception) {
            SendMessage(ackRequest, MessageType.ERROR, exception.getMessage());
        }

    }

}

