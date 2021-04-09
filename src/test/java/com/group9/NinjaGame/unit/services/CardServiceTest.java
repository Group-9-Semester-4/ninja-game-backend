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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
    private Card card;
    private List<Card> list;
    CardService cardService;


    @Mock
    private CardRepository repository;

    //maybe should be BeforeAll but can't get it to work
    @BeforeEach
    public void setUp() {
        assertNotNull(repository);

        cardService = new CardService(repository);

        list = new ArrayList();

        card = new Card();
        card.setId(UUID.randomUUID());
        card.setName("kokot");
    }


    @Test
    public void testGetEntityById() throws NotFoundException {
        Optional<Card> result = Optional.of(card);

        doReturn(result).when(repository).findById(card.getId());
        Card foundCard = cardService.getEntityById(card.getId().toString());

        assertThat(foundCard != null);
        assertEquals(foundCard, card);
    }

    @Test
    public void testListAll() {
        Card card2 = new Card();
        card2.setId(UUID.randomUUID());
        card2.setName("kokot1");

        list.add(card);
        list.add(card2);


        doReturn(list).when(repository).findAll();
        Iterable<Card> foundCards = cardService.listAll();

        assertThat(foundCards != null);
        assertThat(((List<Card>) foundCards).size() > 0);
        assertThat(((List<Card>) foundCards).contains(card) && ((List<Card>) foundCards).contains(card2));
    }

    @Test
    public void testAddCard() {
        doReturn(card).when(repository).save(card);
        Card savedCard = cardService.addCard(card);

        assertThat(savedCard != null);
        assertThat(savedCard.getName().equalsIgnoreCase("kokot"));
    }

    @Test
    public void testDeleteCard() {
        // arrange
        doReturn(Optional.empty()).when(repository).findById(card.getId());

        // act
        cardService.deleteCard(card);

        // assert
        try {
            cardService.getEntityById(card.getId().toString());
            fail();
        } catch (NotFoundException e) {
            assertThrows(NotFoundException.class, () -> {
                cardService.getEntityById(card.getId().toString());
            });
            assertThat(e.getMessage().equals("Can't find Card set with this ID"));
        }
        verify(repository, times(1)).delete(card);
    }

}
