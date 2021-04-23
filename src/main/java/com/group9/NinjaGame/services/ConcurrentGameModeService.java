package com.group9.NinjaGame.services;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Card;
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

    private void validateGameInfo(GameInfo gameInfo) throws Exception {
        if (gameInfo == null) {
            throw new Exception("Game not found");
        }

        if (!(gameInfo.gameModeData instanceof ConcurrentGameMode)) {
            throw new Exception("Not supported for current game mode");
        }
    }

    public GameInfo onDraw(UUID playerId) throws Exception {
        GameInfo gameInfo = gameContainer.getPlayerGame(playerId);

        validateGameInfo(gameInfo);

        ConcurrentGameMode gameMode = (ConcurrentGameMode) gameInfo.gameModeData;

        List<Card> cards = gameMode.playerRemainingCards.get(playerId);

        if (cards == null || cards.isEmpty()) {
            throw new Exception("No cards left");
        }

        Card card = cards.get(new Random().nextInt(cards.size()));


        gameMode.playerCurrentCard.put(playerId, card);

        return gameInfo;
    }
    //TODO: refactor
    //https://stackoverflow.com/questions/81346/most-efficient-way-to-increment-a-map-value-in-java
    public GameInfo onComplete(UUID playerId) throws Exception {

        GameInfo gameInfo = gameContainer.getPlayerGame(playerId);

        validateGameInfo(gameInfo);

        ConcurrentGameMode gameMode = (ConcurrentGameMode) gameInfo.gameModeData;
        Card currentCard = gameMode.playerCurrentCard.get(playerId);

        if (currentCard != null) {

            //moved from onDraw so that redraw button doesn't delete cards from deck
            gameMode.playerRemainingCards.get(playerId).remove(currentCard);
            //add extra card
            int cardsAlreadyDone = gameMode.numberOfPlayerCardsDone.get(playerId);
            cardsAlreadyDone++;
            gameMode.numberOfPlayerCardsDone.put(playerId, cardsAlreadyDone);

            //add score
            int newScore = gameMode.playerScores.get(playerId);
            newScore += currentCard.getPoints();
            gameMode.playerScores.put(playerId, newScore);


            gameMode.playerCurrentCard.remove(playerId);
            //gameMode.playerCurrentCard.put(playerId, null);

            //check if it's a last card
            //TODO: refactor to another method
            if(gameMode.playerRemainingCards.get(playerId).isEmpty()){
                gameMode.standings.add(playerId);
                //if enough players have reached the end
                int percent = gameMode.players.size()/gameMode.standings.size()*100;
                //50 is made up, can be discussed
                if(percent >= 50){
                    //force everyone into bossfight
                    this.onTimerEnd(playerId);
                }
            }
            //end TODOregion

            return gameInfo;
        }
        throw new Exception("onComplete failed flow."); //todo - I don't know what this exc. should say
    }

    public GameInfo onBossComplete(BossScoreParam bossScore, UUID playerId) throws Exception {

        GameInfo gameInfo = gameContainer.getPlayerGame(playerId);

        validateGameInfo(gameInfo);

        ConcurrentGameMode gameMode = (ConcurrentGameMode) gameInfo.gameModeData;

        gameMode.bossFightScores.put(playerId, bossScore.score);

        return gameInfo;
    }
    //TODO: decide how many points should players get for finishing fastest
    public GameInfo onTimerEnd(UUID playerId) throws Exception {
        //for adding points
        GameInfo gameInfo = gameContainer.getPlayerGame(playerId);

        validateGameInfo(gameInfo);

        ConcurrentGameMode gameMode = (ConcurrentGameMode) gameInfo.gameModeData;
        int numberOfPlayers = gameMode.players.size();
        for(Player player : gameMode.players){
            //amount to be given up for discussion
            gameMode.playerScores.put(player.sessionId, numberOfPlayers--);
        }
        return gameInfo;
    }
}
