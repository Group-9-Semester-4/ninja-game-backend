package com.group9.NinjaGame.services;

import com.group9.NinjaGame.containers.DefaultCardSetsContainer;
import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.models.CardSet;
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
    private DefaultCardSetsContainer defaultCardSetsContainer;

    @Autowired
    public GameService(ICardService cardService, CardRepository repository, GameContainer gameContainer, DefaultCardSetsContainer defaultCardSetsContainer) {
        this.cardService = cardService;
        this.repository = repository;
        this.gameContainer = gameContainer;
        this.defaultCardSetsContainer = defaultCardSetsContainer;
    }

    @Override
    public Game initGame(int timeLimit, boolean singlePlayer, boolean playingAlone) {
        Game game = new Game(timeLimit, singlePlayer, playingAlone);
        //game.setAllCards(cardService.getAll());
        ArrayList<String> listOfIds = new ArrayList<String>();
        listOfIds.add("03b4e565-8be0-11eb-a9af-00163e4e5003");
        listOfIds.add("03c541a4-8be0-11eb-a9af-00163e4e5003");
        listOfIds.add("03d6785c-8be0-11eb-a9af-00163e4e5003");

        ArrayList<String> listOfIds1 = new ArrayList<String>();
        listOfIds1.add("03b4e565-8be0-11eb-a9af-00163e4e5003");
        listOfIds1.add("03c541a4-8be0-11eb-a9af-00163e4e5003");
        listOfIds1.add("03d6785c-8be0-11eb-a9af-00163e4e5003");

        List<Card> cardList = cardService.createDefaultCardList(listOfIds);
        List<Card> cardList1 = cardService.createDefaultCardList(listOfIds1);
        defaultCardSetsContainer.addDefaultCardSet(cardList, "default", 3600, true, 0);
        defaultCardSetsContainer.addDefaultCardSet(cardList1, "proKokoty", 9090, false, 9);

        //simulating user input
        game.setSelectedCardSet(defaultCardSetsContainer.defaultCardSets.get(0));

        gameContainer.addGame(game);
        return game;
    }

    @Override
    public Card draw(UUID gameId) {
        Game game = gameContainer.findGame(gameId);
        if (game.getAllCards().size() == 0) {
            return null;
        } else {
            return game.getAllCards().get(new Random().nextInt(game.getAllCards().size()));
        }
    }

    @Override
    public Game startGame(UUID gameId, UUID cardSetId) {
        Game game = gameContainer.findGame(gameId);
        CardSet cardSet = defaultCardSetsContainer.findCardSet(cardSetId);

        if(cardSet != null){
            try {
                throw new Exception("dopice");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        game.setSelectedCardSet(cardSet);
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



    public List<Card> removeDoneCard(UUID gameId, UUID cardId) {
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
