package com.group9.NinjaGame.containers;

import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;

import java.util.HashMap;
import java.util.UUID;

public class GameContainer {

    private static final GameContainer instance = new GameContainer();

    private final HashMap<UUID, GameInfo> games;

    private final HashMap<UUID, GameInfo> playerGame;

    private GameContainer() {
        games = new HashMap<>();
        playerGame = new HashMap<>();
    }

    public static GameContainer getInstance() {
        return instance;
    }

    public void initGame(GameInfo gameInfo) {
        games.put(gameInfo.gameId, gameInfo);
    }

    public boolean joinGame(UUID gameId, Player player) {
        GameInfo gameInfo = games.get(gameId);

        if (!gameInfo.started) {

            playerGame.put(player.sessionId, gameInfo);

            if (gameInfo.lobby.players.contains(player)) {
                return false;
            }

            if (gameInfo.lobby.players.isEmpty()) {
                gameInfo.lobby.lobbyOwnerId = player.sessionId;
            }

            return gameInfo.lobby.players.add(player);
        }

        return false;
    }

    public GameInfo getGameInfo(UUID gameId) {
        return games.get(gameId);
    }


    public GameInfo getGameInfoByLobbyCode(String lobbyCode) {
        for (GameInfo info : games.values()) {
            if (info.multiPlayer && info.lobby.lobbyCode.equals(lobbyCode)) {
                return info;
            }
        }

        return null;
    }

    public void removeGame(UUID gameId) {
        games.remove(gameId);
    }

    public GameInfo getPlayerGame(UUID playerId) {
        return playerGame.get(playerId);
    }

    public void removePlayerFromLobby(UUID playerId, GameInfo info) {
        info.lobby.players.removeIf(player -> player.sessionId.equals(playerId));

        if (info.lobby.lobbyOwnerId.equals(playerId) && !info.lobby.players.isEmpty()) {
            Player player = info.lobby.players.get(0);
            info.lobby.lobbyOwnerId = player.sessionId;
        }

        playerGame.remove(playerId);
    }
}
