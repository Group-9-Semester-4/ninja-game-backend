package com.group9.NinjaGame.repositories;

import com.group9.NinjaGame.entities.statisics.CardSetCompletion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CardSetCompletionRepository extends JpaRepository<CardSetCompletion, UUID> {
    @Query(value = "SELECT card_sets.name, COUNT(*) as c from statistics_cardset_completion LEFT JOIN card_sets ON statistics_cardset_completion.card_set_id = card_sets.id GROUP BY card_sets.name ORDER BY c DESC", nativeQuery = true)
    List<Object[]> getAllCardSetCompletionsWithCount();
}