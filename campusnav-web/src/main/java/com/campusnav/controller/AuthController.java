package com.campusnav.controller;

import com.campusnav.exception.InvalidLoginException;
import com.campusnav.model.User;
import com.campusnav.service.CampusService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller handling user login and session invalidation (logout).
 * Uses standard HttpSession for session-based authentication without Spring Security.
 */
@Controller
public class AuthController {

    @Autowired
    private CampusService campusService;

    @GetMapping("/login")
    public String showLoginForm(HttpSession session, Model model,
                                @RequestParam(value = "logout", required = false) String logout,
                                @RequestParam(value = "required", required = false) String required) {
        // If already logged in, redirect to home
        if (session.getAttribute("currentUser") != null) {
            return "redirect:/";
        }

        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
        if (required != null) {
            model.addAttribute("errorMessage", "Please log in to access that page.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              HttpSession session,
                              Model model) {
        // Form input validation
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            model.addAttribute("errorMessage", "Username and password cannot be blank.");
            return "login";
        }

        try {
            User user = campusService.authenticate(username, password);
            // Store user in HttpSession
            session.setAttribute("currentUser", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("fullName", user.getFullName());
            session.setAttribute("role", user.getRole());

            return "redirect:/";
        } catch (InvalidLoginException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("enteredUsername", username);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}
