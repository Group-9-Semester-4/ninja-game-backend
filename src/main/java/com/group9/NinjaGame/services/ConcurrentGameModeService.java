package com.group9.NinjaGame.services;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.helpers.exceptions.StartBossFightException;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;
import com.group9.NinjaGame.models.modes.BasicGameMode;
import com.group9.NinjaGame.models.modes.ConcurrentGameMode;
import com.group9.NinjaGame.models.params.BossScoreParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class ConcurrentGameModeService {
    private final GameContainer gameContainer;

    public ConcurrentGameModeService() {
        this.gameContainer = GameContainer.getInstance();
    }


    public Card onDraw(UUID playerId) throws Exception {
        GameInfo gameInfo = getValidatedGameInfo(playerId);

        ConcurrentGameMode gameMode = (ConcurrentGameMode) gameInfo.gameModeData;

        List<Card> cards = gameMode.getRemainingCards(playerId);

        if (cards == null || cards.isEmpty()) {
            throw new Exception("No cards left");
        }

        Card card = cards.get(new Random().nextInt(cards.size()));

        gameMode.setPlayerCurrentCard(playerId, card);

        return card;
    }

    public GameInfo onComplete(UUID playerId) throws Exception {

        GameInfo gameInfo = getValidatedGameInfo(playerId);

        ConcurrentGameMode gameMode = (ConcurrentGameMode) gameInfo.gameModeData;
        Card currentCard = gameMode.getPlayerCurrentCard(playerId);

        if (currentCard != null) {

            //moved from onDraw so that redraw button doesn't delete cards from deck
            gameMode.getRemainingCards(playerId).remove(currentCard);

            //add extra card
            gameMode.incrementCardsDone(playerId);

            //add score
            gameMode.addScore(playerId, currentCard.getPoints());

            gameMode.setPlayerCurrentCard(playerId, null);

            //check if it's a last card
            if(gameMode.getRemainingCards(playerId).isEmpty()){
                gameMode.standings.add(playerId);
                //if enough players have reached the end
                double percent = (double)gameMode.standings.size()/gameMode.players.size()*100;

                if(percent >= 50){
                    //force everyone into bossfight
                    GameInfo bossGameInfo = this.onTimerEnd(playerId);
                    throw new StartBossFightException(bossGameInfo);
                }
            }

            return gameInfo;
        }
        throw new Exception("Game flow failed.");
    }

    public GameInfo onBossComplete(BossScoreParam bossScore, UUID playerId) throws Exception {

        GameInfo gameInfo = getValidatedGameInfo(playerId);

        ConcurrentGameMode gameMode = (ConcurrentGameMode) gameInfo.gameModeData;

        gameMode.bossScore(playerId, bossScore.score);

        return gameInfo;
    }
    public GameInfo onTimerEnd(UUID playerId) throws Exception {
        //for adding points
        GameInfo gameInfo = getValidatedGameInfo(playerId);

        ConcurrentGameMode gameMode = (ConcurrentGameMode) gameInfo.gameModeData;

        int numberOfPlayers = gameMode.standings.size();

        for(UUID playerUuid : gameMode.standings){
            gameMode.addScore(playerUuid, 5 * numberOfPlayers--);
        }
        return gameInfo;
    }

    private void validateGameInfo(GameInfo gameInfo) throws Exception {
        if (gameInfo == null) {
            throw new Exception("Game not found");
        }

        if (!(gameInfo.gameModeData instanceof ConcurrentGameMode)) {
            throw new Exception("Not supported for current game mode");
        }
    }

    private GameInfo getValidatedGameInfo(UUID playerId) throws Exception {
        GameInfo gameInfo = gameContainer.getPlayerGame(playerId);
        validateGameInfo(gameInfo);
        return gameInfo;
    }
}
