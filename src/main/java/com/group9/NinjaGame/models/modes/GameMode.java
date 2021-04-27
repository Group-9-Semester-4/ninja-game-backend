package com.group9.NinjaGame.models.modes;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.GameInfo;

import java.util.List;
import java.util.UUID;

public interface GameMode {

    void init(GameInfo gameInfo, List<Card> cards, int timeLimit);

    String getGameModeId();

}
