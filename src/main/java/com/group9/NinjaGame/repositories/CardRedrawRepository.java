package com.group9.NinjaGame.repositories;

import com.group9.NinjaGame.entities.statisics.CardRedraw;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardRedrawRepository extends JpaRepository<CardRedraw, UUID> {
}
