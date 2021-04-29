package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.models.params.FinishGameParam;

import java.util.UUID;

public interface IStatisticsService {
    Game insertGameStatistics(FinishGameParam finishGameParam);
}
