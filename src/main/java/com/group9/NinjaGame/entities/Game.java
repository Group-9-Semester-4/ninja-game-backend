package com.group9.NinjaGame.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "points", length = 50, nullable = false, unique = false)
    @NotBlank(message = "Points are mandatory")
    private int points;

    @Column(name = "time_limit", length = 50, nullable = false, unique = false)
    @NotBlank(message = "Time limit is mandatory")
    private int timeLimit;

    @Column(name = "cards_done", length = 50, nullable = false, unique = false)
    @NotBlank(message = "This field is mandatory")
    private int cardsDone;

    @Column(name = "minigame_attempts", length = 50, nullable = false, unique = false)
    @NotBlank(message = "This is mandatory")
    private int miniGameAttempts;

    @Column(name = "singleplayer", nullable = false, unique = false)
    @NotBlank(message = "This is mandatory")
    private boolean singlePlayer;

    @Column(name = "playing_alone", nullable = false, unique = false)
    @NotBlank(message = "This is mandatory")
    private boolean playingAlone;

    @ManyToOne
    @JoinColumn(name = "card_set_id")
    private CardSet selectedCardSet;

    public Game() {
    }

    public Game(int timeLimit, boolean singlePlayer, boolean playingAlone) {
        this.id = null;
        this.timeLimit = timeLimit;
        this.singlePlayer = singlePlayer;
        this.playingAlone = playingAlone;
        this.selectedCardSet = null;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getCardsDone() {
        return cardsDone;
    }

    public void setCardsDone(int cardsDone) {
        this.cardsDone = cardsDone;
    }

    public int getMiniGameAttempts() {
        return miniGameAttempts;
    }

    public void setMiniGameAttempts(int miniGameAttempts) {
        this.miniGameAttempts = miniGameAttempts;
    }

    public boolean isSinglePlayer() {
        return singlePlayer;
    }

    public void setSinglePlayer(boolean singlePlayer) {
        this.singlePlayer = singlePlayer;
    }

    public boolean isPlayingAlone() {
        return playingAlone;
    }

    public void setPlayingAlone(boolean playingAlone) {
        this.playingAlone = playingAlone;
    }

    public CardSet getSelectedCardSet() {
        return selectedCardSet;
    }

    public void setSelectedCardSet(CardSet cardSet) {
        this.selectedCardSet = cardSet;
    }

    public int getGameAttempts() {
        if (points == 0 || cardsDone == 0) {
            return 0;
        }
        return points / cardsDone;
    }
}
