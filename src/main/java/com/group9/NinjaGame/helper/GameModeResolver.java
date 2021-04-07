package com.group9.NinjaGame.helper;

import com.group9.NinjaGame.models.modes.BasicGameMode;
import com.group9.NinjaGame.models.modes.GameMode;
import com.group9.NinjaGame.models.modes.SinglePlayerGameMode;

public class GameModeResolver {

    public static final String[] GAME_MODES = {
        BasicGameMode.GAME_MODE_ID, SinglePlayerGameMode.GAME_MODE_ID
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
        }

        return null;
    }

}
