package com.group9.NinjaGame.models.modes;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.GameInfo;

import java.util.List;
import java.util.UUID;

public interface GameMode {

    void setCards(List<Card> cards, UUID... uuid);

    void init(GameInfo gameInfo, int...timeLimit);

    String getGameModeId();

    List<Card> getCards(UUID... uuid);

}
