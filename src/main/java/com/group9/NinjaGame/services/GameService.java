package com.group9.NinjaGame.services;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.entities.CardSetEntity;
import com.group9.NinjaGame.entities.GameEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.models.Game;
import com.group9.NinjaGame.repositories.CardRepository;
import com.group9.NinjaGame.repositories.CardSetRepository;
import com.group9.NinjaGame.repositories.GameRepository;
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
        GameEntity gameEntity = new GameEntity();
        gameEntity.setTimeLimit(timeLimit);
        gameEntity.setSinglePlayer(singlePlayer);
        gameEntity.setPlayingAlone(playingAlone);
        gameRepository.save(gameEntity);
        Game game = Game.fromGameEntity(gameEntity);
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
            return Card.fromCardEntity(cardEntity);
        }
    }

    @Override
    public Game startGame(UUID gameId, UUID cardSetId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        Optional<CardSetEntity> cardSetEntityOptional = cardSetRepository.findById(cardSetId);
        GameEntity gameEntity = null;
        Game g = null;
        if (gameEntityOptional.isPresent()) {
            gameEntity = gameEntityOptional.get();
            g = Game.fromGameEntity(gameEntity);
        }
        if (cardSetEntityOptional.isPresent()) {
            CardSetEntity cardSetEntity = cardSetEntityOptional.get();
            g.setSelectedCardSet(cardSetEntity);
        }
        return g;
    }

    @Override
    public Game startGame(UUID gameId, List<UUID> unwantedCards) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        GameEntity gameEntity = null;
        Game g = null;
        if (gameEntityOptional.isPresent()) {
            gameEntity = gameEntityOptional.get();
            g = Game.fromGameEntity(gameEntity);
        }
        if (unwantedCards.size() == 0) return g;
        for (UUID cardId : unwantedCards) {
            g.removeCard(cardId);
        }
        return g;
    }



    public List<CardEntity> removeDoneCard(UUID gameId, UUID cardId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        Optional<CardEntity> cardEntity = repository.findById(cardId);
        CardEntity entity = null;
        GameEntity gameEntity = null;
        Game g = null;
        if (gameEntityOptional.isPresent()) {
            gameEntity = gameEntityOptional.get();
            g = Game.fromGameEntity(gameEntity);
        }

        if (cardEntity.isPresent()) {
            entity = cardEntity.get();
        }
        Card card = Card.fromCardEntity(entity);

        g.removeCard(card.getId());
        g.setPoints(g.getPoints() + card.getPoints());
        g.setCardsDone(g.getCardsDone() + 1);
        return g.getAllCards();
    }

    public Game finishGame(UUID gameId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        GameEntity gameEntity = null;
        Game g = null;
        if (gameEntityOptional.isPresent()) {
            gameEntity = gameEntityOptional.get();
            g = Game.fromGameEntity(gameEntity);
            gameRepository.delete(gameEntity);
        }
        return g;
    }

    public List<Card> fromIterator(Iterable<CardEntity> cardEntities) {
        List<Card> cards = new ArrayList<Card>();
        Iterator<CardEntity> itr = cardEntities.iterator();
        while (itr.hasNext()) {
            cards.add(Card.fromCardEntity(itr.next()));
        }
        return cards;
    }

    // Unused but kept as an example for Custom Queries.
    public List<Card> getAllCustom() {
        Iterable<CardEntity> cardEntities = repository.getCustomCard();

        return fromIterator(cardEntities);
    }
}
