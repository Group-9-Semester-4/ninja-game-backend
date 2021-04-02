package com.group9.NinjaGame.services;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.entities.CardSet;
import com.group9.NinjaGame.entities.GameEntity;
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
    private CardRepository cardRepository;
    private CardSetRepository cardSetRepository;
    private GameRepository gameRepository;

    @Autowired
    public GameService(ICardService cardService, CardSetRepository cardSetRepository, CardRepository cardRepository, GameRepository gameRepository) {
        this.cardService = cardService;
        this.cardSetRepository = cardSetRepository;
        this.cardRepository = cardRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public GameEntity initGame(int timeLimit, boolean singlePlayer, boolean playingAlone) {
        GameEntity gameEntity = new GameEntity(timeLimit, singlePlayer, playingAlone);

        gameRepository.save(gameEntity);

        return gameEntity;
    }

    @Override
    public Card draw(UUID gameId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);

        if (gameEntityOptional.isPresent()) {

            GameEntity gameEntity = gameEntityOptional.get();

            if (gameEntity.getSelectedCardSet().getCards().size() == 0) {
                return null;
            } else {
                Set<Card> cardEntities = gameEntity.getSelectedCardSet().getCards();
                List<Card> arr = new ArrayList<>(cardEntities);

                return arr.get(new Random().nextInt(arr.size()));
            }
        }

        return null;
    }
    @Override
    public GameEntity startGame(UUID gameId, UUID cardSetId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        Optional<CardSet> cardSetEntityOptional = cardSetRepository.findById(cardSetId);

        GameEntity gameEntity = null;

        if (gameEntityOptional.isPresent() && cardSetEntityOptional.isPresent()) {
            gameEntity = gameEntityOptional.get();
            CardSet cardSet = cardSetEntityOptional.get();

            gameEntity.setSelectedCardSet(cardSet);
            gameRepository.save(gameEntity);
        }

        return gameEntity;
    }

    @Override
    public GameEntity startGame(UUID gameId, List<UUID> unwantedCards) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        GameEntity gameEntity = null;

        if (gameEntityOptional.isPresent()) {
            gameEntity = gameEntityOptional.get();

            CardSet cardSet = createTemporaryCardSet(unwantedCards);

            gameEntity.setSelectedCardSet(cardSet);

            gameRepository.save(gameEntity);
        }

        return gameEntity;
    }

    public List<Card> removeDoneCard(UUID gameId, UUID cardId) {
        Optional<GameEntity> gameEntityOptional = gameRepository.findById(gameId);
        Optional<Card> cardEntity = cardRepository.findById(cardId);
        Card entity = null;
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
            BeanUtils.copyProperties(gameEntity, g);

            if (gameEntity.getSelectedCardSet().isTemporary()){
                cardSetRepository.delete(gameEntity.getSelectedCardSet());
            }

            gameRepository.delete(gameEntity);
        }
        return g;
    }

    public Iterable<GameEntity> findAll() {
        return gameRepository.findAll();
    }

    protected CardSet createTemporaryCardSet(List<UUID> unwantedCards) {
        List<Card> cards = (List<Card>) cardRepository.getCards(unwantedCards);

        CardSet cardSet = new CardSet(UUID.randomUUID(), "temp");
        cardSet.setCards(cards);

        cardSet = cardSetRepository.save(cardSet);

        return cardSet;
    }
}
