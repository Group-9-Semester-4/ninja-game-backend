package com.group9.NinjaGame.repositories;

import com.group9.NinjaGame.entities.statisics.CardDiscard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardDiscardRepository extends JpaRepository<CardDiscard, UUID> {
}
