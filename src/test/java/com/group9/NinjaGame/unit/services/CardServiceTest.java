package com.group9.NinjaGame.unit.services;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.repositories.CardRepository;
import com.group9.NinjaGame.services.CardService;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    private Card card;
    private UUID uuid = UUID.randomUUID();
    private List<Card> list;
    CardService cardService;


    @Mock
    private CardRepository cardRepository;


    @BeforeEach
    public void setUp() {
        assertNotNull(cardRepository);

        cardService = new CardService(cardRepository);

        list = new ArrayList();

        card = new Card();
        card.setId(uuid);
        card.setName("cardName100");
    }


    @Test
    public void testGetEntityById() throws NotFoundException {
        Optional<Card> result = Optional.of(card);
        doReturn(result).when(cardRepository).findById(card.getId());

        Card foundCard = cardService.getEntityById(card.getId().toString());

        assertEquals(foundCard, card);
        verify(cardRepository,times(1)).findById(card.getId());
    }

    @Test
    public void testListAll() {
        Card card2 = new Card();
        card2.setId(UUID.randomUUID());
        card2.setName("cardName101");

        list.add(card);
        list.add(card2);


        doReturn(list).when(cardRepository).findAll();
        List<Card> foundCards = cardService.listAll();

        assertNotNull(foundCards);
        assertTrue(foundCards.size() > 0);
        assertTrue(foundCards.contains(card) && foundCards.contains(card2));
    }

    @Test
    public void testAddCard() {
        doReturn(card).when(cardRepository).save(card);

        Card savedCard = cardService.addCard(card);

        assertNotNull(savedCard);
        assertTrue(savedCard.getName().equalsIgnoreCase("cardName100"));
    }

    @Test
    public void testDeleteCard() {
        cardService.deleteCard(card);

        verify(cardRepository, times(1)).delete(card);
    }

    @Test
    public void testDeletedCardThrowsNotFoundException() throws NotFoundException {
        doReturn(Optional.empty()).when(cardRepository).findById(card.getId());

        cardService.deleteCard(card);

        try {
            cardService.getEntityById(card.getId().toString());
            fail(); // just in case, this line should not be hit and instead go into the catch block.
        } catch (NotFoundException e) {
            assertEquals(e.getMessage(), "Can't find Card set with this ID");
        }
        verify(cardRepository, times(1)).delete(card);
        verify(cardRepository, times(1)).findById(card.getId());
    }

}
