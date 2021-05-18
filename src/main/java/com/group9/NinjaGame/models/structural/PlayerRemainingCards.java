package com.group9.NinjaGame.models.structural;

import com.group9.NinjaGame.entities.Card;

import java.util.List;
import java.util.UUID;

public class PlayerRemainingCards {

    public UUID playerId;

    public List<Card> cards;

    public PlayerRemainingCards(UUID playerId, List<Card> cards) {
        this.playerId = playerId;
        this.cards = cards;
    }
}
