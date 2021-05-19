package com.group9.NinjaGame.services;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.modes.BasicGameMode;
import com.group9.NinjaGame.models.params.BossScoreParam;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BasicGameModeService {

    private final GameContainer gameContainer;

    public BasicGameModeService() {
        this.gameContainer = GameContainer.getInstance();
    }

    public void checkAllComplete(BasicGameMode gameMode) {
        if (gameMode.completeStates.size() == gameMode.players.size()) {
            gameMode.completeStates = new LinkedList<>();

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

    public GameInfo onDraw(UUID playerId) throws Exception {

        GameInfo gameInfo = gameContainer.getPlayerGame(playerId);

        validateGameInfo(gameInfo);

        BasicGameMode gameMode = (BasicGameMode) gameInfo.gameModeData;

        if (gameMode.playerOnTurn != playerId) {
            throw new Exception("Only player on turn can draw a card");
        }

        List<Card> cards = gameMode.remainingCards;

        if (cards == null || cards.isEmpty()) {
            throw new Exception("No cards left");
        }

        Card card = cards.get(new Random().nextInt(cards.size()));

        gameMode.drawnCard = card;

        //maybe not desired? redraw deletes cards from deck
        gameMode.remainingCards.remove(card);

        //shouldn't be here?
        gameMode.score += card.getPoints();

        return gameInfo;
    }

    public GameInfo onComplete(UUID playerId) throws Exception {

        GameInfo gameInfo = gameContainer.getPlayerGame(playerId);

        validateGameInfo(gameInfo);

        BasicGameMode gameMode = (BasicGameMode) gameInfo.gameModeData;

        boolean alreadyCompleted = gameMode.completeStates.contains(playerId);

        if (!alreadyCompleted && gameMode.drawnCard != null) {

            gameMode.completeStates.add(playerId);

            checkAllComplete(gameMode);

            return gameInfo;
        }
        throw new Exception("onComplete failed flow."); //todo - I don't know what this exc. should say
    }

    public GameInfo onBossComplete(BossScoreParam bossScore, UUID playerId) throws Exception {

        GameInfo gameInfo = gameContainer.getPlayerGame(playerId);

        validateGameInfo(gameInfo);

        BasicGameMode gameMode = (BasicGameMode) gameInfo.gameModeData;

        gameMode.score(playerId, bossScore.score);

        return gameInfo;
    }
}
