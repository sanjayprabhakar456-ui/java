package com.campusnav.controller;

import com.campusnav.exception.LocationNotFoundException;
import com.campusnav.model.IssueReport;
import com.campusnav.model.Location;
import com.campusnav.model.RouteResult;
import com.campusnav.service.CampusService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Main Controller handling:
 * 1. Location searching and directory
 * 2. Location details
 * 3. Shortest route navigation via BFS
 * 4. Issue reporting for Students and status monitoring
 */
@Controller
public class CampusController {

    @Autowired
    private CampusService campusService;

    // --- Home / Locations Directory ---

    @GetMapping("/")
    public String index(HttpSession session, Model model,
                        @RequestParam(value = "q", required = false) String query,
                        @RequestParam(value = "category", required = false) String category) {
        // Retrieve filtered or all locations
        List<Location> locations = campusService.searchLocations(query, category);

        model.addAttribute("locations", locations);
        model.addAttribute("searchQuery", query != null ? query : "");
        model.addAttribute("selectedCategory", category != null ? category : "");
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        model.addAttribute("role", session.getAttribute("role"));

        return "index";
    }

    // --- Location Details ---

    @GetMapping("/locations/{id}")
    public String locationDetails(@PathVariable("id") int id, HttpSession session, Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            Location location = campusService.getLocationById(id);
            model.addAttribute("location", location);
            model.addAttribute("currentUser", session.getAttribute("currentUser"));
            model.addAttribute("role", session.getAttribute("role"));
            return "location-details";
        } catch (LocationNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }
    }

    // --- Route Finder (BFS) ---

    @GetMapping("/route")
    public String routeFinderForm(@RequestParam(value = "startId", required = false) Integer startId,
                                  @RequestParam(value = "destId", required = false) Integer destId,
                                  HttpSession session, Model model) {
        List<Location> allLocations = campusService.getAllLocations();
        model.addAttribute("locations", allLocations);
        model.addAttribute("startId", startId);
        model.addAttribute("destId", destId);
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        model.addAttribute("role", session.getAttribute("role"));

        if (startId != null && destId != null) {
            RouteResult result = campusService.findShortestRoute(startId, destId);
            model.addAttribute("routeResult", result);
        }

        return "route";
    }

    @PostMapping("/route")
    public String calculateRoute(@RequestParam("startId") Integer startId,
                                 @RequestParam("destId") Integer destId,
                                 HttpSession session, Model model) {
        List<Location> allLocations = campusService.getAllLocations();
        model.addAttribute("locations", allLocations);
        model.addAttribute("startId", startId);
        model.addAttribute("destId", destId);
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        model.addAttribute("role", session.getAttribute("role"));

        // Form Validation
        if (startId == null || destId == null) {
            model.addAttribute("errorMessage", "Please select both a starting location and destination.");
            return "route";
        }

        RouteResult result = campusService.findShortestRoute(startId, destId);
        model.addAttribute("routeResult", result);

        return "route";
    }

    // --- Issue Reports ---

    @GetMapping("/reports")
    public String viewReports(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String role = (String) session.getAttribute("role");
        String username = (String) session.getAttribute("username");

        if (role == null) {
            return "redirect:/login?required=true";
        }

        List<Location> locations = campusService.getAllLocations();
        model.addAttribute("locations", locations);
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        model.addAttribute("role", role);

        if ("ADMIN".equalsIgnoreCase(role)) {
            // Admin sees all reports
            model.addAttribute("reports", campusService.getAllReports());
        } else {
            // Student sees only their submitted reports
            model.addAttribute("reports", campusService.getReportsForStudent(username));
        }

        return "reports";
    }

    @PostMapping("/reports/submit")
    public String submitReport(@RequestParam("locationId") Integer locationId,
                               @RequestParam("category") String category,
                               @RequestParam("description") String description,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        String role = (String) session.getAttribute("role");
        String username = (String) session.getAttribute("username");

        if (role == null) {
            return "redirect:/login?required=true";
        }

        // Form Validation
        if (locationId == null || category == null || category.trim().isEmpty() ||
            description == null || description.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "All fields are required to report an issue.");
            return "redirect:/reports";
        }

        try {
            IssueReport report = new IssueReport();
            report.setStudentUsername(username);
            report.setLocationId(locationId);
            report.setCategory(category.trim());
            report.setDescription(description.trim());
            report.setStatus("PENDING");

            campusService.submitReport(report);
            redirectAttributes.addFlashAttribute("successMessage", "Issue report submitted successfully! Campus facility staff will review it.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error submitting report: " + e.getMessage());
        }

        return "redirect:/reports";
    }

    @PostMapping("/reports/update-status")
    public String updateReportStatus(@RequestParam("reportId") int reportId,
                                     @RequestParam("status") String status,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        String role = (String) session.getAttribute("role");
        // Role check
        if (!"ADMIN".equalsIgnoreCase(role)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Access Denied: Only Admins can modify report status.");
            return "redirect:/reports";
        }

        try {
            campusService.updateReportStatus(reportId, status);
            redirectAttributes.addFlashAttribute("successMessage", "Report #" + reportId + " status updated to " + status + ".");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating status: " + e.getMessage());
        }

        return "redirect:/reports";
    }
}
