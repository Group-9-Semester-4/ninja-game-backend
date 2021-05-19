package com.group9.NinjaGame.models.structural;

import java.util.UUID;

public class PlayerScore {

    public UUID playerId;

    public int score;

    public PlayerScore(UUID playerId, int score) {
        this.playerId = playerId;
        this.score = score;
    }
}
