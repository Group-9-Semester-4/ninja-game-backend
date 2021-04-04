package com.group9.NinjaGame.container;

import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;

import java.util.HashMap;
import java.util.UUID;

public class GameContainer {

    private static final GameContainer instance = new GameContainer();

    private final HashMap<UUID, GameInfo> games;

    private GameContainer() {
        games = new HashMap<>();
    }

    public static GameContainer getInstance() {
        return instance;
    }

    public void initGame(UUID gameId, GameInfo gameInfo) {
        games.put(gameId, gameInfo);
    }

    public boolean joinGame(UUID gameId, Player player) {
        GameInfo gameInfo = games.get(gameId);

        if (!gameInfo.started) {

            if (gameInfo.players.contains(player)) {
                return false;
            }

            return gameInfo.players.add(player);
        }

        return false;
    }

    public GameInfo getGameInfo(UUID gameId) {
        return games.get(gameId);
    }


    public GameInfo getGameInfo(String lobbyCode) {
        for (GameInfo info : games.values()) {
            if (info.lobbyCode.equals(lobbyCode)) {
                return info;
            }
        }

        return null;
    }

    public void removeGame(UUID gameId) {
        games.remove(gameId);
    }
}
