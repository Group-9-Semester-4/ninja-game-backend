package com.group9.NinjaGame.repositories;

import com.group9.NinjaGame.entities.Card;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardRepository extends CrudRepository<Card, UUID> {
    /*
        There is not much to put in here, as CrudRepository already has all the basic Database Input/Output methods.

        This file can be populated with custom methods (with custom SQL commands) annotated by the @Query annotation

        How to Update a Row in the database -> https://stackoverflow.com/questions/36693300/how-to-update-an-attribute-with-spring-crudrepository
     */
    @Query("SELECT c FROM Card c WHERE c.points > 1")
    Iterable<Card> getCustomCard();

    @Query("SELECT c.id from Card c where c.id not in :unwantedCards")
    Iterable<UUID> getCardIds(@Param("unwantedCards") List<UUID> unwantedCards);
}
