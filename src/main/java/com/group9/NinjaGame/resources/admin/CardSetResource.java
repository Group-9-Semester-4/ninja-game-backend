package com.group9.NinjaGame.resources.admin;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.entities.CardSetEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.services.ICardService;
import com.group9.NinjaGame.services.ICardSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequestMapping("/admin/card-set")
@Controller
public class CardSetResource {
    private final String UPLOAD_DIR = "src/main/resources/public/img/card_pictures/";

    @Autowired
    ICardSetService cardSetService;
    ICardService cardService;



    @GetMapping("")
    public String listCardSets(Model model) {
        List<CardSetEntity> cardList = cardSetService.getAllCardSets();
        model.addAttribute("cardSets", cardList);
        return "manage-card-sets";
    }


    @GetMapping("/create-card-set")
    public String showCreateCardSetForm(Model model) {
        List<Card> cardList = cardService.getAll();
        model.addAttribute("cards", cardList);
        model.addAttribute("cardSetEntity", new CardSetEntity());
        return "add-card-set.html";
    }

    @PostMapping("add-card-set")
    public String addCardSet(@Valid CardSetEntity cardSetEntity, BindingResult result) {
        if (result.hasErrors()) {
            return "add-card-set.html";
        }
        cardSetService.createCardSet(cardSetEntity);
        return "redirect:/admin/card-set";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model, HttpSession session) {
        CardSetEntity cardSetEntity = null;
        try {
            cardSetEntity = cardSetService.getById(id);
        }
        catch (Exception e){
            throw new IllegalArgumentException("Invalid cardset Id:" + id);
        }
        List<CardEntity> allCards = cardService.findAll();
        Set<CardEntity> cardSetCards = cardSetEntity.getCards();
        allCards.removeAll(cardSetCards);

        model.addAttribute("cardSetEntity", cardSetEntity);
        session.setAttribute("allCards", allCards);
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
        }
        catch (Exception e){
            throw new IllegalArgumentException("Invalid card set Id:" + id);
        }

        cardSetService.deleteCardSet(cardSetEntity);
        return "redirect:/admin/card-set";
    }



    @GetMapping("/manage-cards")
    //TODO: doesn't update automatically
    public String listCards(Model model) {
        List<Card> cardList = cardService.getAll();
        model.addAttribute("cards", cardList);
        return "manage-cards";
    }
}
