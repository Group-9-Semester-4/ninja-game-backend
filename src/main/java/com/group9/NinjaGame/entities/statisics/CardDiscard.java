package com.group9.NinjaGame.entities.statisics;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
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

    /*
    Why Instant is chosen as Date format: https://stackoverflow.com/questions/530012/how-to-convert-java-util-date-to-java-sql-date
    */
    @Column(name = "timestamp")
    private Instant timestamp;


    public CardDiscard(UUID cardId, UUID cardSetId, UUID userId, Instant timestamp) {
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

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
