package com.group9.NinjaGame.models.structural;

import java.util.UUID;

public class CardsDone {

    public UUID playerId;

    public int cardsDone;

    public CardsDone(UUID playerId, int cardsDone) {
        this.playerId = playerId;
        this.cardsDone = cardsDone;
    }
}
