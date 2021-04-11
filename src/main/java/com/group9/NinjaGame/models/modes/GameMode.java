package com.group9.NinjaGame.models.modes;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.GameInfo;

import java.util.List;

public interface GameMode {

    void setCards(List<Card> cards);

    void init(GameInfo gameInfo);

    String getGameModeId();

}
