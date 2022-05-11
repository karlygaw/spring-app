package kz.narxoz.springapp.controllers;


import kz.narxoz.springapp.model.Flower;
import kz.narxoz.springapp.model.Users;
import kz.narxoz.springapp.repository.FlowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private FlowerRepository flowerRepository;


    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "index";
    }

    @GetMapping(value = "/login")
    public String login(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "login";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model) {
        List<Flower> flowers = flowerRepository.findAll();
        model.addAttribute("flowers", flowers);
        model.addAttribute("currentUser", getCurrentUser());
        return "profile";
    }

    @GetMapping(value = "/addflower")
    @PreAuthorize("isAuthenticated()")
    public String addflower(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "addflower";
    }

    @PostMapping(value = "/toaddflower")
    @PreAuthorize("isAuthenticated()")
    public String toaddflower(
            @RequestParam(name = "f_name") String name,
            @RequestParam(name = "f_price") int price,
            @RequestParam(name = "f_composition") String composition){

        Flower flower = new Flower();
        flower.setName(name);
        flower.setPrice(price);
        flower.setComposition(composition);

        flowerRepository.save(flower);
        return "redirect:/";

    }

    @PostMapping(value = "/saveflower")
    @PreAuthorize("isAuthenticated()")
    public String saveFlower(
            @RequestParam(name = "f_id") Long id,
            @RequestParam(name = "f_name") String name,
            @RequestParam(name = "f_price") int price,
            @RequestParam(name = "f_composition") String composition) throws IOException {
        Flower flower = flowerRepository.findById(id).orElse(null);

        if (flower!=null){

            flower.setName(name);
            flower.setPrice(price);
            flower.setComposition(composition);
            flowerRepository.save(flower);
            return "redirect:/details/" + id;

        }
        return "redirect:/";
    }

    @PostMapping(value = "/deleteflower")
    @PreAuthorize("isAuthenticated()")
    public String deleteFlower(@RequestParam(name = "f_id") Long id, Model model){
        Flower flower = flowerRepository.findById(id).orElse(null);
        model.addAttribute("currentUser", getCurrentUser());
        if (flower!=null){
            flowerRepository.delete(flower);
        }
        return "redirect:/";
    }

    @GetMapping(value = "/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public String detailsFlower(@PathVariable(name = "id")Long id, Model model){
        model.addAttribute("currentUser", getCurrentUser());
        Flower flower = flowerRepository.findById(id).orElse(null);
        model.addAttribute("flower", flower);
        return "/details";
    }


    @GetMapping(value = "/adminpanel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String admin(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "adminpanel";
    }

    @GetMapping(value = "/moderatorpanel")
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR')")
    public String moderator(Model model) {
        model.addAttribute("currentUser", getCurrentUser());
        return "moderatorpanel";
    }

    @GetMapping(value = "/403")
    public String accessDeniedPage(Model model){
        model.addAttribute("currentUser", getCurrentUser());
        return "403";
    }

    private Object getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users currentUser = (Users) authentication.getPrincipal();
            return currentUser;
        }
        return null;
    }
}
