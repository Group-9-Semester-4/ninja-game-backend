package com.group9.NinjaGame.services;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.models.Game;
import com.group9.NinjaGame.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameService implements IGameService {

    private ICardService cardService;
    private CardRepository repository;
    private GameContainer gameContainer;

    @Autowired
    public GameService(ICardService cardService, CardRepository repository, GameContainer gameContainer) {
        this.cardService = cardService;
        this.repository = repository;
        this.gameContainer = gameContainer;
    }

    @Override
    public Game initGame(int timeLimit, boolean singlePlayer, boolean playingAlone) {
        Game game = new Game(timeLimit,singlePlayer,playingAlone);
        game.setAllCards(cardService.getAll());
        gameContainer.addGame(game);
        return game;
    }

    @Override
    public Card draw(UUID uuid) {
        Game game = gameContainer.findGame(uuid);
        return game.getAllCards().get(new Random().nextInt(game.getAllCards().size()));
    }


    // TODO: take array of IDs not cards
    @Override
    public Game startGame(UUID gameId, List<UUID> unwantedCards) {
        Game game = gameContainer.findGame(gameId);
        if(unwantedCards.size() == 0) return game;
        for (UUID cardId : unwantedCards) {
            game.removeCard(cardId);
        }
        return game;
    }

    public List<Card> removeDoneCard(UUID gameId, UUID cardId) {
        Game game = gameContainer.findGame(gameId);
        Optional<CardEntity> cardEntity = repository.findById(cardId);
        CardEntity entity = null;
        if(cardEntity.isPresent()) {
            entity = cardEntity.get();
        }
        Card card = Card.fromCardEntity(entity);
        game.getAllCards().remove(card);
        int points = game.getPoints();
        points += card.getPoints();
        game.setPoints(points);
        game.setCardsDone(+1);
        return game.getAllCards();
    }

    public Game finishGame(UUID gameId) {
        Game game = gameContainer.findGame(gameId);
        gameContainer.endGame(game);
        // TODO: is the game obj still kept after container deletion tho?
        return game;
    }

    // not used for now, just to give an idea
    // from CrudRepo we are getting a Iterable<CardEntity>, but we wanna work with models, so here in the service
    // the retyping is done using a new list and an iterator
    public List<Card> getAllCustom() {
        Iterable<CardEntity> cardEntities = repository.getCustomCard();

        return fromIterator(cardEntities);
    }

    public List<Card> fromIterator(Iterable<CardEntity> cardEntities) {
        List<Card> cards = new ArrayList<Card>();
        Iterator<CardEntity> itr = cardEntities.iterator();
        while (itr.hasNext()) {
            cards.add(Card.fromCardEntity(itr.next()));
        }
        return cards;
    }
}
