package com.group9.NinjaGame.models.structural;

import com.group9.NinjaGame.entities.Card;

import java.util.UUID;

public class CardLockInfo {

    public Card card;
    public UUID playerId;
    public boolean locked;

    public CardLockInfo(Card card) {
        this.card = card;
        playerId = null;
        locked = false;
    }

}
