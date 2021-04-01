package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.entities.CardSetEntity;
import com.group9.NinjaGame.entities.GameEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.models.Game;
import com.group9.NinjaGame.repositories.CardRepository;
import com.group9.NinjaGame.repositories.CardSetRepository;
import com.group9.NinjaGame.repositories.GameRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameService implements IGameService {

    private ICardService cardService;
    private CardRepository repository;
    private CardSetRepository cardSetRepository;
    private GameRepository gameRepository;

    @Autowired
    public GameService(ICardService cardService, CardSetRepository cardSetRepository, CardRepository repository, GameRepository gameRepository) {
        this.cardService = cardService;
        this.cardSetRepository = cardSetRepository;
        this.repository = repository;
        this.gameRepository = gameRepository;
    }

    @Override
    public Game initGame(int timeLimit, boolean singlePlayer, boolean playingAlone) {
        Game game = new Game(timeLimit, singlePlayer, playingAlone);
        GameEntity gameEntity = new GameEntity();
        //might be performance heavyish, alternatives include MapStruct or Orika
        BeanUtils.copyProperties(game, gameEntity, "selectedCardSet", "id");


        gameRepository.save(gameEntity);

        game.setId(gameEntity.getId());
        return game;
    }

    @Override
    public Card draw(UUID gameId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        GameEntity gameEntity = null;
        if (gameEntityOptional.isPresent()) {
            gameEntity = gameEntityOptional.get();
        }

        if (gameEntity.getSelectedCardSet().getCards().size() == 0) {
            return null;
        } else {
            Set<CardEntity> cardEntities = gameEntity.getSelectedCardSet().getCards();
            List<CardEntity> arr = new ArrayList<>(cardEntities);
            CardEntity cardEntity = arr.get(new Random().nextInt(arr.size()));
            Card card = new Card();
            BeanUtils.copyProperties(cardEntity, card);
            return card;
        }
    }
    @Override
    public Game startGame(UUID gameId, UUID cardSetId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        Optional<CardSetEntity> cardSetEntityOptional = cardSetRepository.findById(cardSetId);
        GameEntity gameEntity = null;
        Game g = new Game();
        if (gameEntityOptional.isPresent() && cardSetEntityOptional.isPresent()) {
            gameEntity = gameEntityOptional.get();
            CardSetEntity cardSetEntity = cardSetEntityOptional.get();
            gameEntity.setSelectedCardSet(cardSetEntity);
            gameRepository.save(gameEntity);
            BeanUtils.copyProperties(gameEntity, g);
        }
        return g;
    }

    @Override
    public Game startGame(UUID gameId, List<UUID> unwantedCards) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        GameEntity gameEntity = null;
        Game g = new Game();
        if (gameEntityOptional.isPresent()) {
            gameEntity = gameEntityOptional.get();
            BeanUtils.copyProperties(gameEntity, g);
        }
        if (unwantedCards.size() == 0) return g;
        for (UUID cardId : unwantedCards) {
            g.removeCard(cardId);
        }
        gameEntity.setSelectedCardSet(g.getSelectedCardSet());
        gameRepository.save(gameEntity);
        return g;
    }

    public List<CardEntity> removeDoneCard(UUID gameId, UUID cardId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        Optional<CardEntity> cardEntity = repository.findById(cardId);
        CardEntity entity = null;
        GameEntity gameEntity = null;
        Game g = new Game();
        if (gameEntityOptional.isPresent() && cardEntity.isPresent()) {
            gameEntity = gameEntityOptional.get();
            BeanUtils.copyProperties(gameEntity, g);

            entity = cardEntity.get();
        }
        g.removeCard(entity.getId());
        g.setPoints(g.getPoints() + entity.getPoints());
        g.setCardsDone(g.getCardsDone() + 1);
        return g.getAllCards();
    }

    public Game finishGame(UUID gameId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        GameEntity gameEntity = null;
        Game g = new Game();
        if (gameEntityOptional.isPresent()) {
            gameEntity = gameEntityOptional.get();
            BeanUtils.copyProperties(gameEntity, g, "selectedCardSet");
            gameRepository.delete(gameEntity);
        }
        return g;
    }

    public Iterable<GameEntity> findAll() {
        return gameRepository.findAll();
    }
}
