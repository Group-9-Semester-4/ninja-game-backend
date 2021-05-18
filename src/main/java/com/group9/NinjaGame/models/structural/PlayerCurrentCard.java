package com.group9.NinjaGame.models.structural;

import com.group9.NinjaGame.entities.Card;

import java.util.UUID;

// TODO: Extend all of these with super object containing playerId and equals for easier list retrieval.
public class PlayerCurrentCard {

    public UUID playerId;

    public Card card;

    public PlayerCurrentCard(UUID playerId, Card card) {
        this.playerId = playerId;
        this.card = card;
    }
}
