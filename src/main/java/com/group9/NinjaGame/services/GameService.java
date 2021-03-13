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

    private CardService cardService;
    private CardRepository repository;
    private List<Card> allCards;
    private GameContainer gameContainer;

    @Autowired
    public GameService(CardService cardService, CardRepository repository, GameContainer gameContainer) {
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


    @Override
    public Game startGame(UUID gameId, List<Card> unwantedCards) {
        Game game = gameContainer.findGame(gameId);
        if(unwantedCards.size() == 0) return game;
        for (Card c : unwantedCards) {
            game.getAllCards().remove(c);
        }
        return game;
    }
    public List<Card> removeDoneCard(UUID gameId, Card cardDone) {
        Game game = gameContainer.findGame(gameId);
        game.getAllCards().remove(cardDone);
        return game.getAllCards();
    }

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
