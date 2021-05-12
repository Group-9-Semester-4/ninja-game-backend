package com.group9.NinjaGame.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.models.GameInfo;

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

    @Column(name = "points", length = 50, nullable = false)
    @NotBlank(message = "Points are mandatory")
    private int points;

    @Column(name = "time_limit", length = 50, nullable = false)
    @NotBlank(message = "Time limit is mandatory")
    private int timeLimit;

    @Column(name = "cards_done", length = 50, nullable = false)
    @NotBlank(message = "This field is mandatory")
    private int cardsDone;

    @Column(name = "minigame_attempts", length = 50, nullable = false)
    @NotBlank(message = "This is mandatory")
    private int miniGameAttempts;

    @Column(name = "multiplayer", nullable = false)
    @NotBlank(message = "This is mandatory")
    private boolean multiPlayer;

    // TODO: Remove this useless field
    @Column(name = "playing_alone", nullable = false)
    @NotBlank(message = "This is mandatory")
    private boolean playingAlone;

    @Column(name = "percentage_of_done_cards")
    private int percentageOfDoneCards;

    @Column(name = "time")
    private int timeInSeconds;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "card_set_id")
    private CardSet selectedCardSet;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Game() {
    }

    public Game(int timeLimit, boolean multiPlayer, boolean playingAlone, User user) {
        this.id = null;
        this.timeLimit = timeLimit;
        this.multiPlayer = multiPlayer;
        this.playingAlone = playingAlone;
        this.user = user;
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

    public boolean isMultiPlayer() {
        return multiPlayer;
    }

    public void setMultiPlayer(boolean singlePlayer) {
        this.multiPlayer = singlePlayer;
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

    public int getPercentageOfDoneCards() {
        return percentageOfDoneCards;
    }

    public void setPercentageOfDoneCards(int percentageOfDoneCards) {
        this.percentageOfDoneCards = percentageOfDoneCards;
    }

    public int getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(int timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getGameAttempts() {
        if (points == 0 || cardsDone == 0) {
            return 0;
        }
        return points / cardsDone;
    }

    public GameInfo getGameInfo() {
        return GameContainer.getInstance().getGameInfo(this.getId());
    }
}
