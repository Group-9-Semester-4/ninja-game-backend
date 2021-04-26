package com.group9.NinjaGame.helpers.exceptions;

import com.group9.NinjaGame.models.GameInfo;

public class StartBossFightException extends Exception {

    private GameInfo gameInfo;

    public StartBossFightException(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }
}
