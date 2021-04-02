package com.group9.NinjaGame.repositories;

import com.group9.NinjaGame.entities.CardSet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CardSetRepository extends CrudRepository<CardSet, UUID> {
}
