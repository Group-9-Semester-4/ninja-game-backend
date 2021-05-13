package com.group9.NinjaGame.services;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.helpers.exceptions.StartBossFightException;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.modes.ConcurrentGameMode;
import com.group9.NinjaGame.models.modes.DeathMatchGameMode;
import com.group9.NinjaGame.models.params.CardCompleteParam;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeathMatchGameModeService {
    private final GameContainer gameContainer;
    private final ICardService cardService;

    public DeathMatchGameModeService(ICardService cardService) {
        this.gameContainer = GameContainer.getInstance();
        this.cardService = cardService;
    }

    public GameInfo onReady(UUID playerId) throws Exception {
        GameInfo gameInfo = getValidatedGameInfo(playerId);
        DeathMatchGameMode gameMode = (DeathMatchGameMode) gameInfo.gameModeData;
        gameMode.playersReady.put(playerId, true);
        return gameInfo;
    }

    public boolean allPlayersReady(UUID playerId) throws Exception {
        GameInfo gameInfo = getValidatedGameInfo(playerId);
        DeathMatchGameMode gameMode = (DeathMatchGameMode) gameInfo.gameModeData;
        int players = gameMode.players.size();
        if(players == gameMode.playersReady.size()) {return true;}
        return false;
    }

    public GameInfo onComplete(CardCompleteParam param) throws Exception {

        GameInfo gameInfo = getValidatedGameInfo(param.playerId);
        DeathMatchGameMode gameMode = (DeathMatchGameMode) gameInfo.gameModeData;
        gameMode.remainingCards.remove(param.cardId);
        Card card = cardService.getEntityById(param.cardId.toString());
        //add score
        gameMode.playerScores.merge(param.playerId, card.getPoints(), Integer::sum);
        //check if it's a last card
        if(gameMode.remainingCards.isEmpty()){
            return gameInfo;
            }
        throw new Exception("onComplete failed flow."); //todo - I don't know what this exc. should say
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
