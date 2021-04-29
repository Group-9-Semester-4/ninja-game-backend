package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.entities.statisics.CardDiscard;
import com.group9.NinjaGame.entities.statisics.CardRedraw;
import com.group9.NinjaGame.models.params.FinishGameParam;

import java.util.List;
import java.util.UUID;

public interface IStatisticsService {
    Game insertGameStatistics(FinishGameParam finishGameParam);

    List<CardDiscard> getAllCardDiscards();

    List<CardDiscard> createCardDiscards(List<CardDiscard> cardDiscards);

    List<CardRedraw> getAllCardRedraws();

    List<CardRedraw> createCardRedraws(List<CardRedraw> cardRedraws);
}
