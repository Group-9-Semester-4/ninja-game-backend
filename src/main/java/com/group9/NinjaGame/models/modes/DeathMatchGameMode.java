package com.group9.NinjaGame.models.modes;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.Player;
import com.group9.NinjaGame.models.structural.CardLockInfo;
import com.group9.NinjaGame.models.structural.PlayerScore;
import org.springframework.security.core.parameters.P;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class DeathMatchGameMode implements GameMode{

    public static final String GAME_MODE_ID = "deathmatch arena";

    public List<CardLockInfo> remainingCards;

    public List<Player> players;

    // hashmap storing uuids of all players and their respective scores (points totaling cards done)
    public List<PlayerScore> playerScores;

    // is int sufficient here or do we need list<UUID> or sth?
    public List<UUID> playersReady;

    @Override
    public void init(GameInfo gameInfo, List<Card> cards, int timeLimit) {
        players = gameInfo.lobby.players;
        remainingCards = new LinkedList<>();

        for (Card card : cards) {
            remainingCards.add(new CardLockInfo(card));
        }

        playerScores = new LinkedList<>();
        playersReady = new LinkedList<>();
    }

    @Override
    public String getGameModeId() {
        return GAME_MODE_ID;
    }

    public List<UUID> getPlayersReady() {
        return playersReady;
    }

    public PlayerScore getPlayerScore(UUID playerId) {
        for (PlayerScore score : playerScores) {
            if (score.playerId.equals(playerId)) {
                return score;
            }
        }

        return null;
    }

    public boolean removeCard(UUID cardId) {
        for (CardLockInfo cardLockInfo : remainingCards) {
            if (cardLockInfo.card.getId().equals(cardId)) {
                return remainingCards.remove(cardLockInfo);
            }
        }

        return false;
    }

    public CardLockInfo getLockInfo(UUID cardId) {
        for (CardLockInfo cardLockInfo : remainingCards) {
            if (cardLockInfo.card.getId().equals(cardId)) {
                return cardLockInfo;
            }
        }

        return null;
    }
}
