package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardEntity;

import java.util.Optional;

public interface ICardService {
    public Optional<CardEntity> getById(String uuid);

    public Iterable<CardEntity> getAll();

    public Iterable<CardEntity> getAllCustom();
}

