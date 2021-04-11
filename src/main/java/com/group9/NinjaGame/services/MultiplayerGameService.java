package com.group9.NinjaGame.services;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.helpers.GameModeResolver;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;
import com.group9.NinjaGame.models.messages.MessageType;
import com.group9.NinjaGame.models.messages.SocketIOMessage;
import com.group9.NinjaGame.models.modes.GameMode;
import com.group9.NinjaGame.models.params.JoinGameParam;
import com.group9.NinjaGame.models.params.LeaveGameParam;
import com.group9.NinjaGame.models.params.StartGameParam;
import com.group9.NinjaGame.repositories.CardSetRepository;
import com.group9.NinjaGame.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.group9.NinjaGame.helpers.SocketIOHelper.SendMessage;

@Component
public class MultiplayerGameService {

    private final GameRepository gameRepository;

    private final CardSetRepository cardSetRepository;

    private final GameContainer gameContainer;

    private final BasicGameModeService basicGameModeService;

    private final SocketIONamespace namespace;

    @Autowired
    public MultiplayerGameService(SocketIOServer server, GameRepository gameRepository, CardSetRepository cardSetRepository, BasicGameModeService basicGameModeService) {
        this.gameRepository = gameRepository;
        this.cardSetRepository = cardSetRepository;

        gameContainer = GameContainer.getInstance();

        this.basicGameModeService = basicGameModeService;

        this.namespace = server.addNamespace("/game");
        this.namespace.addConnectListener(onConnected());
        this.namespace.addDisconnectListener(onDisconnected());

        this.namespace.addEventListener("join", JoinGameParam.class, onJoin());
        this.namespace.addEventListener("leave", LeaveGameParam.class, onLeave());
        this.namespace.addEventListener("start", StartGameParam.class, onStart());

        basicGameModeService.registerListeners(namespace);
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

            disconnectPlayerFromPreviousLobbies(client);

            System.out.println("Client[{}] - Disconnected from chat module." + client.getSessionId().toString());
        };
    }

    private DataListener<JoinGameParam> onJoin() {
        return ((client, param, ackRequest) -> {
            GameInfo gameInfo = gameContainer.getGameInfoByLobbyCode(param.lobbyCode);

            if (gameInfo != null && !gameInfo.started && gameInfo.multiPlayer) {
                UUID gameId = gameInfo.gameId;

                Player player = new Player(param.userName, client.getSessionId());

                disconnectPlayerFromPreviousLobbies(client);

                client.joinRoom(gameId.toString());

                boolean connected = gameContainer.joinGame(gameId, player);

                if (connected) {

                    SendMessage(ackRequest, MessageType.SUCCESS, "Successfully joined", gameInfo);

                    namespace.getRoomOperations(gameId.toString()).sendEvent("lobby-update", gameInfo);

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
                disconnectPlayerFromPreviousLobbies(client);
            }

            client.leaveRoom(param.gameId.toString());

            SendMessage(ackRequest, MessageType.SUCCESS, "Successfully disconnected");

        });
    }

    private DataListener<StartGameParam> onStart() {
        return ((client, param, ackRequest) -> {

            UUID playerId = client.getSessionId();

            GameInfo gameInfo = gameContainer.getPlayerGame(playerId);

            if (!gameInfo.lobby.lobbyOwnerId.equals(playerId)) {
                SendMessage(ackRequest, MessageType.ERROR, "Only lobby owner can start a game");
                return;
            }

            Optional<Game> gameEntityOptional = gameRepository.findById(gameInfo.gameId);
            Optional<CardSet> cardSetEntityOptional = cardSetRepository.findById(param.cardSetId);

            if (gameEntityOptional.isPresent() && cardSetEntityOptional.isPresent()) {

                String gameModeName = param.gameMode;

                GameMode gameMode = GameModeResolver.getFromString(gameModeName);

                if (gameMode == null) {
                    SendMessage(ackRequest, MessageType.ERROR, "Game mode does not exist");
                    return;
                }

                Game game = gameEntityOptional.get();
                CardSet cardSet = cardSetEntityOptional.get();

                List<Card> cards = new ArrayList<>();

                for (Card card : cardSet.getCards()) {
                    if (!param.unwantedCards.contains(card.getId())) {
                        cards.add(card);
                    }
                }

                game.setSelectedCardSet(cardSet);
                game = gameRepository.save(game);

                gameInfo.started = true;

                gameMode.setCards(cards);
                gameMode.init(gameInfo);

                gameInfo.gameModeData = gameMode;
                gameInfo.gameModeId = gameModeName;

                namespace.getRoomOperations(game.getId().toString()).sendEvent("start", gameInfo);

                return;
            }

            SendMessage(ackRequest, MessageType.ERROR, "Can not start a game");
        });
    }

    private void disconnectPlayerFromPreviousLobbies(SocketIOClient client) {
        UUID playerId = client.getSessionId();

        GameInfo gameInfo = gameContainer.getPlayerGame(playerId);

        if (gameInfo != null) {
            client.leaveRoom(gameInfo.gameId.toString());

            gameContainer.removePlayerFromLobby(playerId, gameInfo);

            if (gameInfo.lobby.players.isEmpty()) {
                destroyGame(gameInfo.gameId);
            } else {
                namespace.getRoomOperations(gameInfo.gameId.toString()).sendEvent("lobby-update", gameInfo);
            }
        }
    }

    private void destroyGame(UUID gameId) {

        gameContainer.removeGame(gameId);

        gameRepository.deleteById(gameId);

        namespace.getRoomOperations(gameId.toString()).disconnect();
    }
}
