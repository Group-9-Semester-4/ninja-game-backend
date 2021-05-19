package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.entities.statisics.CardDiscard;
import com.group9.NinjaGame.models.params.FinishGameParam;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IStatisticsService {
    Game insertGameStatistics(FinishGameParam finishGameParam);

    Map<String, Long> getAllCardDiscards();

    List<CardDiscard> insertCardDiscards(List<UUID> discardedCards, UUID cardSetUUID, UUID playerUUID);

    Map<String, Long> getAllCardRedraws();

    Map<String, Short> getAllTimePlayedPerGame();

    Long getAverageGameTime();
}
