package com.group9.NinjaGame.models.modes;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;
import com.group9.NinjaGame.models.structural.*;

import java.util.*;

public class ConcurrentGameMode implements GameMode{

    public static final String GAME_MODE_ID = "concurrent";
    public List<Player> players;

    // game time should be in seconds
    public int gameTime;

    // hashmap players and the current card they are playing with now
    public List<PlayerCurrentCard> playerCurrentCard;

    //hashmap players and list of remaining cards for each player
    public List<PlayerRemainingCards> playerRemainingCards;

    // hashmap storing uuids of all players and their respective scores (points totaling cards done)
    public List<PlayerScore> playerScores;

    // hashmap storing uuids of all players and the number of cards they completed- to be shown in FE all the time
    public List<CardsDone> numberOfPlayerCardsDone;

    // hashmap storing uuds of all players and their bossfight scores to make the final leaderboard
    public List<BossFightScore> bossFightScores;

    // we need standings to know who finished the game 1st 2nd... to give ammo bonuses
    public List<UUID> standings;

    private List<Card> cards;

    @Override
    public void init(GameInfo gameInfo, List<Card> cards, int timeLimit) {
        players = gameInfo.lobby.players;
        playerCurrentCard = new LinkedList<>();
        playerRemainingCards = new LinkedList<>();
        playerScores = new LinkedList<>();
        numberOfPlayerCardsDone = new LinkedList<>();
        bossFightScores = new LinkedList<>();
        standings = new ArrayList<>();
        gameTime = timeLimit;

        for (Player player : players) {
            playerRemainingCards.add(
                    new PlayerRemainingCards(player.sessionId, cards)
            );
        }
    }
    @Override
    public String getGameModeId() {
        return GAME_MODE_ID;
    }

    public List<Card> getRemainingCards(UUID playerId) {
        for (PlayerRemainingCards remainingCards : playerRemainingCards) {
            if (remainingCards.playerId.equals(playerId)) {
                return remainingCards.cards;
            }
        }

        return null;
    }

    public Card getPlayerCurrentCard(UUID playerId) {
        for (PlayerCurrentCard currentCard : playerCurrentCard) {
            if (currentCard.playerId.equals(playerId)) {
                return currentCard.card;
            }
        }

        return null;
    }

    public void setPlayerCurrentCard(UUID playerId, Card card) {
        for (PlayerCurrentCard currentCard : playerCurrentCard) {
            if (currentCard.playerId.equals(playerId)) {
                currentCard.card = card;
                return;
            }
        }

        playerCurrentCard.add(new PlayerCurrentCard(playerId, card));
    }

    public void incrementCardsDone(UUID playerId) {
        for (CardsDone cardsDone : numberOfPlayerCardsDone) {
            if (cardsDone.playerId.equals(playerId)) {
                cardsDone.cardsDone++;
                break;
            }
        }

        numberOfPlayerCardsDone.add(new CardsDone(playerId, 1));
    }

    public void addScore(UUID playerId, int score) {
        for (PlayerScore playerScore : playerScores) {
            if (playerScore.playerId.equals(playerId)) {
                playerScore.score += score;
                break;
            }
        }

        playerScores.add(new PlayerScore(playerId, score));
    }

    public void bossScore(UUID playerId, int score) {
        BossFightScore bossFightScore = new BossFightScore(playerId, score);

        bossFightScores.add(bossFightScore);
    }

}
