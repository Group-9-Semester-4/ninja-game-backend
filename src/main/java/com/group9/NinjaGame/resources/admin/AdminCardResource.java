package com.group9.NinjaGame.resources.admin;

import com.group9.NinjaGame.entities.CardEntity;
import com.group9.NinjaGame.models.Card;
import com.group9.NinjaGame.services.CardService;
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
public class AdminCardResource {

    private final String UPLOAD_DIR = "src/main/resources/public/img/card_pictures/";

    @Autowired
    ICardService cardService;

    @GetMapping("/index")
    //TODO: doesn't update automatically
    public String listCards(Model model) {
        List<Card> cardList = cardService.getAll();
        model.addAttribute("cards", cardList);
        model.addAttribute("hah", "mikijesupersnorchler");
        return "index";
    }

    @GetMapping("/signup")
    public String showSignUpForm(CardEntity cardEntity) {
        return "add-card.html";
    }

    @PostMapping("add")
    public String addCard(@RequestParam ("file") MultipartFile file, @Valid CardEntity cardEntity, BindingResult result, Model model) {
        if ((result.hasErrors()) || (file.isEmpty())) {
            return "add-card.html";
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cardEntity.setFilepath(fileName);
        cardService.addCard(cardEntity);
        return "redirect:/admin/card/index";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model) {
        CardEntity cardEntity = null;
        try {
            cardEntity = cardService.getEntityById(id);
        }
        catch (Exception e){
            throw new IllegalArgumentException("Invalid user Id:" + id);
        }

        model.addAttribute("cardEntity", cardEntity);
        return "update-card";
    }

    @PostMapping("/update/{id}")
    public String updateCard(@RequestParam ("file") MultipartFile file, @PathVariable("id") String id, @Valid CardEntity cardEntity,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            cardEntity.setId(UUID.fromString(id));
            return "update-card";
        }
        String oldPath = cardService.getEntityById(id).getFilepath();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if(!((oldPath.equals(fileName)) || (file.isEmpty()))) {
            try {
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                cardEntity.setFilepath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cardService.addCard(cardEntity);
        return "redirect:/admin/card/index";
    }

    @GetMapping("/delete/{id}")
    public String deleteCard(@PathVariable("id") String id, Model model) {
        CardEntity cardEntity = null;
        try {
            cardEntity = cardService.getEntityById(id);
        }
        catch (Exception e){
            throw new IllegalArgumentException("Invalid card Id:" + id);
        }

        cardService.deleteCard(cardEntity);
        return "redirect:/admin/card/index";
    }


}
