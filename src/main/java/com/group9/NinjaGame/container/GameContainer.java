package com.group9.NinjaGame.container;

import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;

import java.util.HashMap;
import java.util.UUID;

public class GameContainer {

    private static final GameContainer instance = new GameContainer();

    private final HashMap<UUID, GameInfo> games;

    private final HashMap<UUID, GameInfo> playerLobby;

    private GameContainer() {
        games = new HashMap<>();
        playerLobby = new HashMap<>();
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

            playerLobby.put(player.sessionId, gameInfo);

            if (gameInfo.players.contains(player)) {
                return false;
            }

            if (gameInfo.players.isEmpty()) {
                gameInfo.lobbyOwnerId = player.sessionId;
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
            if (info.multiPlayer && info.lobbyCode.equals(lobbyCode)) {
                return info;
            }
        }

        return null;
    }

    public void removeGame(UUID gameId) {
        games.remove(gameId);
    }

    public GameInfo getPlayerLobby(UUID playerId) {
        return playerLobby.get(playerId);
    }

    public void removePlayerFromLobby(UUID playerId, GameInfo info) {
        info.players.removeIf(player -> player.sessionId.equals(playerId));

        if (info.lobbyOwnerId.equals(playerId) && !info.players.isEmpty()) {
            Player player = info.players.get(0);
            info.lobbyOwnerId = player.sessionId;
        }

        playerLobby.remove(playerId);
    }
}
