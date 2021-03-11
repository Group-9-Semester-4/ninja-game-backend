package com.group9.NinjaGame.repositories;

import com.group9.NinjaGame.entities.CardEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends CrudRepository<CardEntity, UUID> {

}
