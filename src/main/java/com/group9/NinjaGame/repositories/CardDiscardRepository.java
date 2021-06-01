package com.group9.NinjaGame.repositories;

import com.group9.NinjaGame.entities.statisics.CardDiscard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CardDiscardRepository extends JpaRepository<CardDiscard, UUID> {
    @Query(value = "SELECT cards.name, COUNT(*) as c from statistics_card_discard LEFT JOIN cards ON statistics_card_discard.card_id = cards.id GROUP BY cards.name ORDER BY c DESC LIMIT 20", nativeQuery = true)
    List<Object[]> getAllDiscardCardsWithCount();

}
