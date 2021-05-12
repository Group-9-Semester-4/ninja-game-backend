package com.group9.NinjaGame.repositories;

import com.group9.NinjaGame.entities.statisics.CardSetCompletion;
import com.group9.NinjaGame.entities.statisics.TimePlayedPerGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TimePlayedPerGameRepository extends JpaRepository<TimePlayedPerGame, UUID> {

    @Query(value = "SELECT SUM(time_in_seconds) FROM statistics_time_played_per_game",nativeQuery = true)
    long getSumOfAllTimeSpentPlayingNinjaGame();

    @Query(value = "SELECT AVG(time_in_seconds) FROM statistics_time_played_per_game", nativeQuery = true)
    long getAvgGameTimePlayingNinjaGame();

    @Query(value = "SELECT time_in_seconds FROM statistics_time_played_per_game",nativeQuery = true)
    List<Object[]> getAllPlayTimes();
}