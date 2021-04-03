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

        gameContainer.initGame(gameId, gameInfo);
    }

    @Override
    public boolean joinGame(UUID gameId, Player player) {
        return gameContainer.joinGame(gameId, player);
    }

    @Override
    public void startGame(UUID gameId, List<Card> cards) {
        GameInfo gameInfo = gameContainer.getGameInfo(gameId);

        gameInfo.started = true;
        gameInfo.remainingCards = cards;
    }
}
