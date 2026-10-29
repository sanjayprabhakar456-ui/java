package com.campusnav.controller;

import com.campusnav.model.Location;
import com.campusnav.service.CampusService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Admin Controller handling Location management and Graph edge management.
 * Strictly verifies ADMIN role from HttpSession.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CampusService campusService;

    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "ADMIN".equalsIgnoreCase(role);
    }

    @GetMapping("/locations")
    public String manageLocations(HttpSession session, Model model, RedirectAttributes redirectAttributes,
                                  @RequestParam(value = "editId", required = false) Integer editId) {
        if (!isAdmin(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Access Denied: Admin privileges required.");
            return "redirect:/login?required=true";
        }

        List<Location> locations = campusService.getAllLocations();
        model.addAttribute("locations", locations);
        model.addAttribute("connections", campusService.getAllConnections());
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        model.addAttribute("role", session.getAttribute("role"));

        Location editLocation = new Location();
        if (editId != null) {
            try {
                editLocation = campusService.getLocationById(editId);
            } catch (Exception ignored) {}
        }
        model.addAttribute("locationForm", editLocation);

        return "admin-locations";
    }

    @PostMapping("/locations/save")
    public String saveLocation(@RequestParam(value = "id", defaultValue = "0") int id,
                               @RequestParam("name") String name,
                               @RequestParam("building") String building,
                               @RequestParam("floor") String floor,
                               @RequestParam("roomNumber") String roomNumber,
                               @RequestParam("category") String category,
                               @RequestParam("description") String description,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Access Denied: Admin privileges required.");
            return "redirect:/login?required=true";
        }

        // Form Validation
        if (name == null || name.trim().isEmpty() ||
            building == null || building.trim().isEmpty() ||
            floor == null || floor.trim().isEmpty() ||
            roomNumber == null || roomNumber.trim().isEmpty() ||
            category == null || category.trim().isEmpty() ||
            description == null || description.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "All location fields are mandatory.");
            return "redirect:/admin/locations" + (id > 0 ? "?editId=" + id : "");
        }

        try {
            Location loc = new Location(id, name.trim(), building.trim(), floor.trim(), roomNumber.trim(), category.trim(), description.trim());
            campusService.saveLocation(loc);
            redirectAttributes.addFlashAttribute("successMessage", (id > 0 ? "Updated location '" : "Created new location '") + name + "' successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving location: " + e.getMessage());
        }

        return "redirect:/admin/locations";
    }

    @PostMapping("/locations/delete")
    public String deleteLocation(@RequestParam("id") int id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Access Denied: Admin privileges required.");
            return "redirect:/login?required=true";
        }

        try {
            campusService.deleteLocation(id);
            redirectAttributes.addFlashAttribute("successMessage", "Location successfully removed from the system.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting location: " + e.getMessage());
        }

        return "redirect:/admin/locations";
    }

    @PostMapping("/connections/add")
    public String addConnection(@RequestParam("fromId") int fromId,
                                @RequestParam("toId") int toId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Access Denied: Admin privileges required.");
            return "redirect:/login?required=true";
        }

        if (fromId == toId) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot create a connection from a location to itself.");
            return "redirect:/admin/locations";
        }

        try {
            campusService.addConnection(fromId, toId);
            redirectAttributes.addFlashAttribute("successMessage", "New navigational pathway connected successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding connection: " + e.getMessage());
        }

        return "redirect:/admin/locations";
    }
}
