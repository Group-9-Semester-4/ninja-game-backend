package com.group9.NinjaGame.helper;

import com.group9.NinjaGame.models.modes.BasicGameMode;
import com.group9.NinjaGame.models.modes.GameMode;

public class GameModeResolver {

    public static final String[] GAME_MODES = {
        "basic"
    };

    public static GameMode getFromString(String name) {
        if (name == null) {
            return null;
        }

        switch (name) {
            case "basic" : {
                return new BasicGameMode();
            }
        }

        return null;
    }

}
