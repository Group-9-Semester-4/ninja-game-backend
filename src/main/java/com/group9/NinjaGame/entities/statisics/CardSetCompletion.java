package com.group9.NinjaGame.entities.statisics;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "statistics_cardset_completion")
public class CardSetCompletion extends Statistic {

    @Column(name = "game_id", nullable = false)
    private UUID gameId;

    @Column(name = "card_set_id", nullable = false)
    private UUID cardSetId;

    @Column(name = "percentage", nullable = false)
    private int percentage;

    /*
    Why Instant is chosen as Date format: https://stackoverflow.com/questions/530012/how-to-convert-java-util-date-to-java-sql-date
    */
    @Column(name = "timestamp")
    private Instant timestamp;




    public CardSetCompletion(UUID gameId, UUID cardSetId, int percentage, Instant timestamp) {
        this.gameId = gameId;
        this.percentage = percentage;
        this.cardSetId = cardSetId;
        this.timestamp = timestamp;
    }

    public CardSetCompletion() {

    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID cardId) {
        this.gameId = cardId;
    }

    public UUID getCardSetId() {
        return cardSetId;
    }

    public void setCardSetId(UUID cardSetId) {
        this.cardSetId = cardSetId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
