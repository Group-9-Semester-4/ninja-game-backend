package com.group9.NinjaGame.unit.services;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.repositories.CardRepository;
import com.group9.NinjaGame.services.CardService;
import com.group9.NinjaGame.services.ICardService;
import javassist.NotFoundException;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
    private Card card;
    CardService cardService;


    @Mock
    private CardRepository repository;



    @Test
    public void testGetEntityById() throws NotFoundException {
        assertNotNull(repository);

        cardService = new CardService(repository);

        card = new Card();
        card.setId(UUID.randomUUID());
        card.setName("kokot");

        Optional<Card> result = Optional.of(card);

        doReturn(result).when(repository).findById(card.getId());
        Card foundCard = cardService.getEntityById(card.getId().toString());

        assertThat(foundCard != null);
        assertEquals(foundCard,card);
    }

    @Test
    public void testAddCard(){
//        Card card = new Card();
//        card.setName("kokot");

    }
    @Test
    public void testDeleteCard(){

    }
    @Test
    public void testListAll(){

    }
}
