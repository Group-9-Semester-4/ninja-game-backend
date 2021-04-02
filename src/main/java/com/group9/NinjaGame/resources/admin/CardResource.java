package com.group9.NinjaGame.resources.admin;

import com.group9.NinjaGame.entities.Card;
import com.group9.NinjaGame.services.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RequestMapping("/admin/card")
@Controller
public class CardResource {

    private final String UPLOAD_DIR = "src/main/resources/public/img/card_pictures/";

    ICardService cardService;

    @Autowired
    public CardResource(ICardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/index")
    public String showManageCards() {
        return "index";
    }


    @GetMapping("/manage")
    //TODO: doesn't update automatically
    public String listCards(Model model) {
        List<Card> cardList = cardService.listAll();
        model.addAttribute("cards", cardList);
        return "manage-cards";
    }



    @GetMapping("/create")
    public String showCreateCardForm(Card card) {
        return "add-card";
    }


    @PostMapping("/add")
    public String addCard(@RequestParam ("file") MultipartFile file, @Valid Card card, BindingResult result) {
        if ((result.hasErrors()) || (file.isEmpty())) {
            return "add-card";
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        card.setFilepath(fileName);
        cardService.addCard(card);
        return "redirect:/admin/card/index";
    }



    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model) {
        Card card = null;
        try {
            card = cardService.getEntityById(id);
        }
        catch (Exception e){
            throw new IllegalArgumentException("Invalid card Id:" + id);
        }

        model.addAttribute("cardEntity", card);
        return "update-card";
    }

    @PostMapping("/update/{id}")
    public String updateCard(@RequestParam ("file") MultipartFile file, @PathVariable("id") String id, @Valid Card card,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            card.setId(UUID.fromString(id));
            return "update-card";
        }
        String oldPath = cardService.getEntityById(id).getFilepath();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if(!((oldPath.equals(fileName)) || (file.isEmpty()))) {
            try {
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                card.setFilepath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cardService.addCard(card);
        return "redirect:/admin/card/manage";
    }

    @GetMapping("/delete/{id}")
    public String deleteCard(@PathVariable("id") String id, Model model) {
        Card card = null;
        try {
            card = cardService.getEntityById(id);
        }
        catch (Exception e){
            throw new IllegalArgumentException("Invalid card Id:" + id);
        }

        cardService.deleteCard(card);
        return "redirect:/admin/card/index";
    }


}
