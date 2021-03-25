package com.group9.NinjaGame.services;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.entities.CardSetEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.models.Game;
import com.group9.NinjaGame.repositories.CardRepository;
import com.group9.NinjaGame.repositories.CardSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameService implements IGameService {

    private ICardService cardService;
    private CardRepository repository;
    private CardSetRepository cardSetRepository;
    private GameContainer gameContainer;

    @Autowired
    public GameService(ICardService cardService, CardSetRepository cardSetRepository, CardRepository repository, GameContainer gameContainer) {
        this.cardService = cardService;
        this.cardSetRepository = cardSetRepository;
        this.repository = repository;
        this.gameContainer = gameContainer;
    }

    @Override
    public Game initGame(int timeLimit, boolean singlePlayer, boolean playingAlone) {
        Game game = new Game(timeLimit, singlePlayer, playingAlone);
        gameContainer.addGame(game);
        return game;
    }

    @Override
    public Card draw(UUID gameId) {
        Game game = gameContainer.findGame(gameId);
        if (game.getAllCards().size() == 0) {
            return null;
        } else {
            CardEntity cardEntity = game.getAllCards().get(new Random().nextInt(game.getAllCards().size()));
            return Card.fromCardEntity(cardEntity);
        }
    }

    @Override
    public Game startGame(UUID gameId, UUID cardSetId) {
        Game game = gameContainer.findGame(gameId);
        Optional<CardSetEntity> cardSetEntityOptional = cardSetRepository.findById(cardSetId);
        if (cardSetEntityOptional.isPresent()) {
            CardSetEntity cardSetEntity = cardSetEntityOptional.get();
            game.setSelectedCardSet(cardSetEntity);
        }
        return game;
    }

    @Override
    public Game startGame(UUID gameId, List<UUID> unwantedCards) {
        Game game = gameContainer.findGame(gameId);
        if (unwantedCards.size() == 0) return game;
        for (UUID cardId : unwantedCards) {
            game.removeCard(cardId);
        }
        return game;
    }



    public List<CardEntity> removeDoneCard(UUID gameId, UUID cardId) {
        Game game = gameContainer.findGame(gameId);
        Optional<CardEntity> cardEntity = repository.findById(cardId);
        CardEntity entity = null;
        if (cardEntity.isPresent()) {
            entity = cardEntity.get();
        }
        Card card = Card.fromCardEntity(entity);
        game.removeCard(card.getId());
        game.setPoints(game.getPoints() + card.getPoints());
        game.setCardsDone(game.getCardsDone() + 1);
        return game.getAllCards();
    }

    public Game finishGame(UUID gameId) {
        Game game = gameContainer.findGame(gameId);
        gameContainer.endGame(game);
        return game;
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
