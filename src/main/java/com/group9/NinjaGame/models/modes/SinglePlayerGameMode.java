package com.group9.NinjaGame.models.modes;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.GameInfo;

import java.util.List;
import java.util.UUID;

public class SinglePlayerGameMode implements GameMode {

    public static final String GAME_MODE_ID = "singleplayer";

    public List<Card> remainingCards;

    @Override
    public void setCards(List<Card> cards) {
        remainingCards = cards;
    }
    @Override
    public List<Card> getCards() {
        return remainingCards;
    }

    @Override
    public void init(GameInfo gameInfo) {

    }

    @Override
    public String getGameModeId() {
        return GAME_MODE_ID;
    }

    public boolean removeCardById(UUID cardId) {
        for (Card card : remainingCards) {
            if (card.getId().equals(cardId)) {
                return remainingCards.remove(card);
            }
        }

        return false;
    }

}
