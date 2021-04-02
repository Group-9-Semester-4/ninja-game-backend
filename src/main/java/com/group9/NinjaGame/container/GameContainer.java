package com.group9.NinjaGame.container;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameContainer {

    private static final GameContainer instance = new GameContainer();

    private final HashMap<UUID, List<UUID>> gameCards;

    private GameContainer() {
        gameCards = new HashMap<>();
    }

    public static GameContainer getInstance() {
        return instance;
    }

    public List<UUID> getGameCards(UUID gameId) {
        return gameCards.get(gameId);
    }

    public void setGameCards(UUID gameId, List<UUID> cards) {
        gameCards.put(gameId, cards);
    }

    public void removeGame(UUID gameId) {
        gameCards.remove(gameId);
    }
}
