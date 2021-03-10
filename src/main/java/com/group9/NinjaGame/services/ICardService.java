package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.repositories.ICardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

public interface ICardService{
    public CardEntity getById(String uuid);
    public CardEntity getByName(String name);
    public List<CardEntity> getAll();
}

