package com.group9.NinjaGame.resources.admin;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.entities.CardSetEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.services.ICardService;
import com.group9.NinjaGame.services.ICardSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/admin/card-set")
@Controller
public class CardSetResource {

    ICardSetService cardSetService;
    ICardService cardService;

    @Autowired
    public CardSetResource(ICardService cardService, ICardSetService cardSetService) {
        this.cardService = cardService;
        this.cardSetService = cardSetService;
    }


    @GetMapping("")
    public String listCardSets(Model model) {
        List<CardSetEntity> cardSets = cardSetService.getAllCardSets();
        model.addAttribute("cardSets", cardSets);
        return "manage-card-sets";
    }


    @GetMapping("/create")
    public String showCreateCardSetForm(Model model) {
        List<CardEntity> cardList = cardService.getAll();
        model.addAttribute("cardSetEntity", new CardSetEntity());
        model.addAttribute("allCards", cardList);
        return "add-card-set";
    }

    @PostMapping("add")
    public String addCardSet(@Valid CardSetEntity cardSetEntity, BindingResult result) {
        if (result.hasErrors()) {
            return "add-card-set";
        }
        cardSetService.createCardSet(cardSetEntity);
        return "redirect:/admin/card-set";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model) {
        CardSetEntity cardSetEntity = new CardSetEntity();
        List<CardEntity> cardList = new ArrayList<>();

        try {
            cardSetEntity = cardSetService.getById(id);
            cardList = cardService.getAll();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid card set Id:" + id);
        }

        for(CardEntity c : cardSetEntity.getCards()){
            cardList.removeIf(card -> card.getId().equals(c.getId()));
        }

        model.addAttribute("cardSetEntity", cardSetEntity);
        model.addAttribute("allCards", cardList);
        return "update-card-set";
    }

    @PostMapping("/update/{id}")
    public String updateCardSet(@PathVariable("id") String id, @Valid CardSetEntity cardSetEntity, BindingResult result, Model model) {
        if (result.hasErrors()) {
            cardSetEntity.setId(UUID.fromString(id));
            return "update-card-set";
        }
        cardSetService.createCardSet(cardSetEntity);
        return "redirect:/admin/card-set";
    }


    @GetMapping("/delete/{id}")
    public String deleteCardSet(@PathVariable("id") String id, Model model) {
        CardSetEntity cardSetEntity = null;
        try {
            cardSetEntity = cardSetService.getById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid card set Id:" + id);
        }

        cardSetService.deleteCardSet(cardSetEntity);
        return "redirect:/admin/card-set";
    }

    @GetMapping("/manage-cards")
    //TODO: doesn't update automatically
    public String listCards(Model model) {
        List<CardEntity> cardList = cardService.getAll();
        model.addAttribute("cards", cardList);
        return "manage-cards";
    }
}
