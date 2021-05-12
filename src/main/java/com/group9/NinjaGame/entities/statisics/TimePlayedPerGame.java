package com.group9.NinjaGame.entities.statisics;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "statistics_time_played_per_game")
public class TimePlayedPerGame extends Statistic {

    @Column(name = "game_id", nullable = false)
    private UUID gameId;

    @Column(name = "card_set_id", nullable = false)
    private UUID cardSetId;

    @Column(name = "time_in_seconds", nullable = false)
    private int timeInSeconds;

    /*
    Why Instant is chosen as Date format: https://stackoverflow.com/questions/530012/how-to-convert-java-util-date-to-java-sql-date
    */
    @Column(name = "timestamp")
    private Instant timestamp;




    public TimePlayedPerGame(UUID gameId, UUID cardSetId, int timeInSeconds, Instant timestamp) {
        this.gameId = gameId;
        this.timeInSeconds = timeInSeconds;
        this.cardSetId = cardSetId;
        this.timestamp = timestamp;
    }

    public TimePlayedPerGame() {

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
    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(int percentage) {
        this.timeInSeconds = percentage;
    }
}
