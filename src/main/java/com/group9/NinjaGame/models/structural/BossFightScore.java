package com.group9.NinjaGame.models.structural;

import java.util.UUID;

public class BossFightScore {

    public UUID playerId;

    public int score;

    public BossFightScore(UUID playerId, int score) {
        this.playerId = playerId;
        this.score = score;
    }
}
