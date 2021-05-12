package com.group9.NinjaGame.entities.statisics;


import javax.persistence.*;
import java.util.UUID;
import java.time.Instant;

@Entity
@Table(name = "statistics_card_redraw")
public class CardRedraw extends Statistic {

    @Column(name = "card_id", nullable = false)
    private UUID cardId;

    @Column(name = "card_set_id", nullable = false)
    private UUID cardSetId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "timestamp")
    private Instant timestamp;

    public CardRedraw(UUID cardId, UUID cardSetId, UUID userId, Instant timestamp) {
        this.cardId = cardId;
        this.cardSetId = cardSetId;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public CardRedraw() {

    }

    public UUID getCardId() {
        return cardId;
    }

    public void setCardId(UUID cardId) {
        this.cardId = cardId;
    }

    public UUID getCardSetId() {
        return cardSetId;
    }

    public void setCardSetId(UUID cardSetId) {
        this.cardSetId = cardSetId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
