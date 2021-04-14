package com.group9.NinjaGame.services;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.listener.DataListener;
import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.messages.MessageType;
import com.group9.NinjaGame.models.modes.BasicGameMode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.group9.NinjaGame.helpers.SocketIOHelper.SendMessage;

@Component
public class BasicGameModeService {

    private final GameContainer gameContainer;

    private SocketIONamespace namespace;

    public BasicGameModeService() {
        this.gameContainer = GameContainer.getInstance();
    }

    public void registerListeners(SocketIONamespace namespace) {

        this.namespace = namespace;

        this.namespace.addEventListener("basic.draw", Object.class, onDraw());
        this.namespace.addEventListener("basic.complete", Object.class, onComplete());
    }

    public DataListener<Object> onDraw() {
        return (client, data, ackSender) -> {

            GameInfo gameInfo = gameContainer.getPlayerGame(client.getSessionId());

            validateGameInfo(gameInfo);

            BasicGameMode gameMode = (BasicGameMode) gameInfo.gameModeData;

            if (gameMode.playerOnTurn != client.getSessionId()) {
                throw new Exception("Only player on turn can draw a card");
            }

            List<Card> cards = gameMode.remainingCards;

            if (cards == null || cards.isEmpty()) {
                SendMessage(ackSender, MessageType.ERROR, "No cards left");
                return;
            }

            Card card = cards.get(new Random().nextInt(cards.size()));

            gameMode.drawnCard = card;
            gameMode.remainingCards.remove(card);

            namespace.getRoomOperations(gameInfo.gameId.toString()).sendEvent("game-update", gameInfo);
        };
    }

    public DataListener<Object> onComplete() {
        return (client, data, ackSender) -> {

            GameInfo gameInfo = gameContainer.getPlayerGame(client.getSessionId());

            validateGameInfo(gameInfo);

            BasicGameMode gameMode = (BasicGameMode) gameInfo.gameModeData;

            boolean alreadyCompleted = gameMode.completeStates.getOrDefault(client.getSessionId(), false);

            if (!alreadyCompleted && gameMode.drawnCard != null) {
                gameMode.completeStates.put(client.getSessionId(), true);

                checkAllComplete(gameMode);

                namespace.getRoomOperations(gameInfo.gameId.toString()).sendEvent("game-update", gameInfo);
            }
        };
    }

    public void checkAllComplete(BasicGameMode gameMode) {
        if (gameMode.completeStates.size() == gameMode.players.size()) {
            gameMode.completeStates = new HashMap<>();

            UUID playerOnTurn = gameMode.playerOnTurn;

            int playerIndex = 0;

            for (int i = 0; i < gameMode.players.size(); i++) {
                if (gameMode.players.get(i).sessionId.equals(playerOnTurn)) {
                    playerIndex = i;
                    break;
                }
            }

            if (playerIndex == gameMode.players.size() - 1) {
                gameMode.playerOnTurn = gameMode.players.get(0).sessionId;
            } else {
                playerIndex++;
                gameMode.playerOnTurn = gameMode.players.get(playerIndex).sessionId;
            }

            gameMode.drawnCard = null;
        }
    }

    // Helper method

    private void validateGameInfo(GameInfo gameInfo) throws Exception {
        if (gameInfo == null) {
            throw new Exception("Game not found");
        }

        if (!(gameInfo.gameModeData instanceof BasicGameMode)) {
            throw new Exception("Not supported for current game mode");
        }
    }

}
