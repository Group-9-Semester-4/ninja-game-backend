package com.group9.NinjaGame.services;

import com.group9.NinjaGame.container.GameContainer;
import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class MultiplayerGameService implements IMultiplayerGameService {

    private final GameContainer gameContainer;

    public MultiplayerGameService() {
        gameContainer = GameContainer.getInstance();
    }

    public void initGame(UUID gameId, String lobbyCode) {
        GameInfo gameInfo = new GameInfo(gameId, lobbyCode);

        try {
            gameContainer.initGame(gameId, gameInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean joinGame(UUID gameId, Player player) {
        try {
            return gameContainer.joinGame(gameId, player);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void startGame(UUID gameId, List<Card> cards) {
        GameInfo gameInfo = null;
        try {
            gameInfo = gameContainer.getGameInfo(gameId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gameInfo.started = true;
        gameInfo.remainingCards = cards;
    }
}
