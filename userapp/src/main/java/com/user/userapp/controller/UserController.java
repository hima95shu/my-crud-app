package com.user.userapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.user.userapp.model.User;
import com.user.userapp.service.UserService;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "index";
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "add-user";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user, @RequestParam("image") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath);
            user.setImagePath("/uploads/" + fileName);
        }
        userService.addUser(user);
        return "redirect:/";
    }

    @GetMapping("/search")
    public String showSearchForm() {
        return "search-user";
    }

    @PostMapping("/search")
    public String searchUser(@RequestParam String name, Model model) {
        Optional<User> user = userService.findUserByName(name);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
        } else {
            model.addAttribute("notFound", true);
        }
        return "search-user";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "edit-user";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user, @RequestParam("image") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath);
            user.setImagePath("/uploads/" + fileName);
        }
        userService.updateUser(user);
        return "redirect:/";
    }
}
