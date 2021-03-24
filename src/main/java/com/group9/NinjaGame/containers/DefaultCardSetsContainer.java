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
        //this.addDefaultCardSet();
    }

    public static DefaultCardSetsContainer getInstance() {
        if (defaultCardSetsContainer == null) {
            defaultCardSetsContainer = new DefaultCardSetsContainer();
        }
        return defaultCardSetsContainer;
    }

    public UUID addDefaultCardSet(List<Card> listOfCards){
        CardSet newCardSet = new CardSet(listOfCards, "default", 3600, true, 0);

        defaultCardSets.add(newCardSet);

        //CardSet newCardSet1 = new CardSet(listOfCards, "kokot1", 1800, false, 1);

        //defaultCardSets.add(newCardSet1);
        return newCardSet.id;
    }

    public CardSet findCardSet(UUID uuid) {
        for (CardSet cs : defaultCardSets) {
            if (cs.getId().equals(uuid)) return cs;
        }
        return null;
    }


}
