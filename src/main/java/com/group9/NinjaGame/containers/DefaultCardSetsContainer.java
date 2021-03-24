package com.group9.NinjaGame.containers;

import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.models.CardSet;
import com.group9.NinjaGame.models.Game;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class DefaultCardSetsContainer {


    private static DefaultCardSetsContainer defaultCardSetsContainer;
    public List<CardSet> defaultCardSets;


    private DefaultCardSetsContainer() {
        this.defaultCardSets = new ArrayList<CardSet>();
    }

    public static DefaultCardSetsContainer getInstance() {
        if (defaultCardSetsContainer == null) {
            defaultCardSetsContainer = new DefaultCardSetsContainer();
        }
        return defaultCardSetsContainer;
    }

    public void addDefaultCardSet(List<Card> listOfCards, String name, int completeTimeLimit, boolean multiplayerSuitable, int difficulty){
        CardSet newCardSet = new CardSet(listOfCards, name, completeTimeLimit, multiplayerSuitable, difficulty);
        defaultCardSets.add(newCardSet);
    }

    public CardSet findCardSet(UUID uuid) {
        for (CardSet cs : defaultCardSets) {
            if (cs.getId().equals(uuid)) return cs;
        }
        return null;
    }


}
