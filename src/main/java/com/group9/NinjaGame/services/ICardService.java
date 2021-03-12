package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardEntity;

import java.util.Optional;

public interface ICardService {
    Optional<CardEntity> getById(String uuid);

    Iterable<CardEntity> getAll();

    Iterable<CardEntity> getAllCustom();
}

