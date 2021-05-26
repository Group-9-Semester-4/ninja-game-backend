package com.group9.NinjaGame.services;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.modes.ConcurrentGameMode;
import com.group9.NinjaGame.models.modes.DeathMatchGameMode;
import com.group9.NinjaGame.models.params.CardCompleteParam;
import com.group9.NinjaGame.models.params.LockCardParam;
import com.group9.NinjaGame.models.structural.CardLockInfo;
import com.group9.NinjaGame.models.structural.PlayerScore;
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
        gameMode.playersReady.add(playerId);
        return gameInfo;
    }

    public boolean allPlayersReady(UUID playerId) throws Exception {
        GameInfo gameInfo = getValidatedGameInfo(playerId);
        DeathMatchGameMode gameMode = (DeathMatchGameMode) gameInfo.gameModeData;
        int players = gameMode.players.size();

        return players == gameMode.playersReady.size();
    }

    public GameInfo onLock(LockCardParam param) throws Exception {

        GameInfo gameInfo = getValidatedGameInfo(param.playerId);
        DeathMatchGameMode gameMode = (DeathMatchGameMode) gameInfo.gameModeData;

        CardLockInfo cardLockInfo = gameMode.getLockInfo(param.cardId);

        if (cardLockInfo.locked) {
            throw new Exception("Card locked");
        }

        cardLockInfo.playerId = param.playerId;
        cardLockInfo.locked = true;

        return gameInfo;
    }

    public GameInfo onUnlock(LockCardParam param) throws Exception {

        GameInfo gameInfo = getValidatedGameInfo(param.playerId);
        DeathMatchGameMode gameMode = (DeathMatchGameMode) gameInfo.gameModeData;

        CardLockInfo cardLockInfo = gameMode.getLockInfo(param.cardId);

        cardLockInfo.playerId = null;
        cardLockInfo.locked = false;

        return gameInfo;
    }

    public GameInfo onComplete(CardCompleteParam param) throws Exception {

        GameInfo gameInfo = getValidatedGameInfo(param.playerId);
        DeathMatchGameMode gameMode = (DeathMatchGameMode) gameInfo.gameModeData;
        gameMode.removeCard(param.cardId);
        Card card = cardService.getEntityById(param.cardId.toString());

        //add score
        PlayerScore score = gameMode.getPlayerScore(param.playerId);

        if (score == null) {
            score = new PlayerScore(param.playerId, card.getPoints());
            gameMode.playerScores.add(score);
        } else {
            score.score += card.getPoints();
        }

        return gameInfo;
    }


    private void validateGameInfo(GameInfo gameInfo) throws Exception {
        if (gameInfo == null) {
            throw new Exception("Game not found");
        }

        if (!(gameInfo.gameModeData instanceof DeathMatchGameMode)) {
            throw new Exception("Not supported for current game mode");
        }
    }

    private GameInfo getValidatedGameInfo(UUID playerId) throws Exception {
        GameInfo gameInfo = gameContainer.getPlayerGame(playerId);
        validateGameInfo(gameInfo);
        return gameInfo;
    }
}
