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

        List<Card> cards = gameMode.playerRemainingCards.get(playerId);

        if (cards == null || cards.isEmpty()) {
            throw new Exception("No cards left");
        }

        Card card = cards.get(new Random().nextInt(cards.size()));

        gameMode.playerCurrentCard.put(playerId, card);

        return card;
    }

    //TODO: refactored accordign to myMap.merge(key, 1, Integer::sum) answer here, test!
    //https://stackoverflow.com/questions/81346/most-efficient-way-to-increment-a-map-value-in-java
    public GameInfo onComplete(UUID playerId) throws Exception {

        GameInfo gameInfo = getValidatedGameInfo(playerId);

        ConcurrentGameMode gameMode = (ConcurrentGameMode) gameInfo.gameModeData;
        Card currentCard = gameMode.playerCurrentCard.get(playerId);

        if (currentCard != null) {

            //moved from onDraw so that redraw button doesn't delete cards from deck
            gameMode.playerRemainingCards.get(playerId).remove(currentCard);

            //add extra card
            gameMode.numberOfPlayerCardsDone.merge(playerId, 1, Integer::sum);

            //add score
            gameMode.playerScores.merge(playerId, currentCard.getPoints(), Integer::sum);

            gameMode.playerCurrentCard.remove(playerId);

            //check if it's a last card
            //TODO: refactor to another method
            if(gameMode.playerRemainingCards.get(playerId).isEmpty()){
                gameMode.standings.add(playerId);
                //if enough players have reached the end
                double percent = (double)gameMode.standings.size()/gameMode.players.size()*100;
                //50 is made up, can be discussed
                if(percent >= 50){
                    //force everyone into bossfight
                    GameInfo bossGameInfo = this.onTimerEnd(playerId);
                    throw new StartBossFightException(bossGameInfo);
                }
            }
            //end TODOregion

            return gameInfo;
        }
        throw new Exception("onComplete failed flow."); //todo - I don't know what this exc. should say
    }

    public GameInfo onBossComplete(BossScoreParam bossScore, UUID playerId) throws Exception {

        GameInfo gameInfo = getValidatedGameInfo(playerId);

        ConcurrentGameMode gameMode = (ConcurrentGameMode) gameInfo.gameModeData;

        gameMode.bossFightScores.put(playerId, bossScore.score);

        return gameInfo;
    }
    //TODO: decide how many points should players get for finishing fastest
    public GameInfo onTimerEnd(UUID playerId) throws Exception {
        //for adding points
        GameInfo gameInfo = getValidatedGameInfo(playerId);

        ConcurrentGameMode gameMode = (ConcurrentGameMode) gameInfo.gameModeData;

        // TODO: calculation-balancing thingy, either .players or .standings (players/2)
        int numberOfPlayers = gameMode.standings.size();

        for(UUID playerUuid : gameMode.standings){
            //amount to be given up for discussion
            gameMode.playerScores.merge(playerUuid, 5 * numberOfPlayers--, Integer::sum);
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
