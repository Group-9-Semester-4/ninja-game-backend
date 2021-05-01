package com.group9.NinjaGame.entities.statisics;


import javax.persistence.*;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "statistics_card_discard")
public class CardDiscard extends Statistic {

    @Column(name = "card_id", nullable = false)
    private UUID cardId;

    @Column(name = "card_set_id", nullable = false)
    private UUID cardSetId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "timestamp")
    private long timestamp;

    public CardDiscard(UUID cardId, UUID cardSetId, UUID userId, long timestamp) {
        this.cardId = cardId;
        this.cardSetId = cardSetId;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public CardDiscard() {

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
