package com.group9.NinjaGame.unit;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.repositories.CardRepository;
import com.group9.NinjaGame.services.ICardService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardServiceTest {

    @Mock
    private CardRepository repository;

    ICardService cardService;

    @BeforeAll
    void testSetup() {
        //Mockito.when(cardService.getEntityById(Mockito.anyString())).thenReturn();
    }

    @Test
    void testGetEntityByID() {
        UUID uuid = UUID.randomUUID();
        Card card = new Card();
        card.setId(uuid);
        Mockito.when(repository.findById(uuid)).thenReturn(java.util.Optional.of(card));


        Optional<Card> optionalCard = repository.findById(uuid);

        assertEquals(optionalCard,card);

    }
}
