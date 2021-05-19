package com.group9.NinjaGame.helpers;

import com.group9.NinjaGame.models.modes.*;

public class GameModeResolver {

    public static final String[] GAME_MODES = {
        BasicGameMode.GAME_MODE_ID, SinglePlayerGameMode.GAME_MODE_ID, ConcurrentGameMode.GAME_MODE_ID, DeathMatchGameMode.GAME_MODE_ID,
    };

    public static GameMode getFromString(String name) {
        if (name == null) {
            return null;
        }

        switch (name) {
            case BasicGameMode.GAME_MODE_ID : {
                return new BasicGameMode();
            }
            case SinglePlayerGameMode.GAME_MODE_ID: {
                return new SinglePlayerGameMode();
            }
            case ConcurrentGameMode.GAME_MODE_ID: {
                return new ConcurrentGameMode();
            }
            case DeathMatchGameMode.GAME_MODE_ID: {
                return new DeathMatchGameMode();
            }
        }

        return null;
    }

}
