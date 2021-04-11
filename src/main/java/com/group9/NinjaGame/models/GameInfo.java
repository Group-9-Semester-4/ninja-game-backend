package com.group9.NinjaGame.models;

import com.group9.NinjaGame.models.modes.GameMode;

import java.util.UUID;

public class GameInfo {

    public UUID gameId;

    public boolean started;

    public boolean multiPlayer;

    public Lobby lobby;

    public GameMode gameModeData;

    public String gameModeId;

    public GameInfo(UUID gameId, String lobbyCode) {

        this.gameId = gameId;

        this.lobby = new Lobby(lobbyCode);

        started = false;
        multiPlayer = true;
    }

    public GameInfo(UUID gameId) {

        this.gameId = gameId;
        this.lobby = null;

        started = false;
        multiPlayer = false;
    }

}
