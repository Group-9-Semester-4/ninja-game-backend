package com.group9.NinjaGame.services;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.group9.NinjaGame.container.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;
import com.group9.NinjaGame.models.messages.MessageType;
import com.group9.NinjaGame.models.messages.SocketIOMessage;
import com.group9.NinjaGame.models.params.JoinGameParam;
import com.group9.NinjaGame.models.params.LeaveGameParam;
import com.group9.NinjaGame.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class MultiplayerGameService implements IMultiplayerGameService {

    private final GameRepository gameRepository;

    private final GameContainer gameContainer;

    private final SocketIONamespace namespace;

    @Autowired
    public MultiplayerGameService(SocketIOServer server, GameRepository gameRepository) {
        this.gameRepository = gameRepository;

        gameContainer = GameContainer.getInstance();

        this.namespace = server.addNamespace("/game");
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());

        this.namespace.addEventListener("join", JoinGameParam.class, onJoin());
        this.namespace.addEventListener("leave", LeaveGameParam.class, onLeave());
    }

    public void initGame(UUID gameId, String lobbyCode) {

        GameInfo gameInfo = new GameInfo(gameId, lobbyCode);

        gameContainer.initGame(gameId, gameInfo);
    }

    @Override
    public void startGame(UUID gameId, List<Card> cards) {
        GameInfo gameInfo = gameContainer.getGameInfo(gameId);

        gameInfo.started = true;
        gameInfo.remainingCards = cards;

        namespace.getRoomOperations(gameId.toString()).sendEvent("start", gameInfo);
    }


    // Socket.io related methods

    private ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
            System.out.println("Client[{}] - Connected to chat module through '{}'" + client.getSessionId().toString() + handshakeData.getUrl());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            System.out.println("Client[{}] - Disconnected from chat module." + client.getSessionId().toString());
        };
    }

    private DataListener<JoinGameParam> onJoin() {
        return ((client, param, ackRequest) -> {
            GameInfo gameInfo = gameContainer.getGameInfo(param.lobbyCode);

            if (gameInfo != null && !gameInfo.started) {
                UUID gameId = gameInfo.gameId;

                Player player = new Player(param.userName, client.getSessionId());

                client.joinRoom(gameId.toString());

                boolean connected = gameContainer.joinGame(gameId, player);

                if (connected) {

                    SendMessage(ackRequest, MessageType.SUCCESS, "Successfully joined", gameInfo);

                    namespace.getRoomOperations(gameId.toString()).sendEvent("join", gameInfo);

                } else {

                    SendMessage(ackRequest, MessageType.ERROR, "User already joined", gameInfo);
                }

            } else {
                SendMessage(ackRequest, MessageType.ERROR, "Game not found");
            }


        });
    }

    private DataListener<LeaveGameParam> onLeave() {
        return ((client, param, ackRequest) -> {
            GameInfo gameInfo = gameContainer.getGameInfo(param.gameId);

            if (gameInfo != null) {

                gameInfo.removePlayer(client.getSessionId());

                namespace.getRoomOperations(gameInfo.gameId.toString()).sendEvent("leave", gameInfo);

            }

            client.leaveRoom(param.gameId.toString());

            SendMessage(ackRequest, MessageType.SUCCESS, "Successfully disconnected");

        });
    }

    private void SendMessage(AckRequest ackRequest, MessageType type, String message) {
        SocketIOMessage ioMessage = new SocketIOMessage(type, message);
        ackRequest.sendAckData(ioMessage);
    }

    private void SendMessage(AckRequest ackRequest, MessageType type, String message, Object data) {
        SocketIOMessage ioMessage = new SocketIOMessage(type, message, data);
        ackRequest.sendAckData(ioMessage);
    }
}
