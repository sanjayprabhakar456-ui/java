package com.campusnav;

import com.campusnav.exception.InvalidLoginException;
import com.campusnav.model.Location;
import com.campusnav.model.RouteResult;
import com.campusnav.service.CampusService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Required 4 JUnit 5 Unit & Integration Tests:
 * 1. BFS finds the shortest path.
 * 2. BFS handles unreachable destinations.
 * 3. Invalid login throws the custom exception.
 * 4. Search returns matching locations.
 */
@SpringBootTest
class CampusNavApplicationTests {

    @Autowired
    private CampusService campusService;

    @Test
    @DisplayName("Test 1: BFS finds the shortest path between connected locations")
    void testBfsFindsShortestPath() {
        // Main Gate (ID 1) connected to Central Library (ID 3) directly in seed data
        RouteResult result = campusService.findShortestRoute(1, 3);

        assertTrue(result.found(), "BFS should discover an available path between connected nodes");
        assertNotNull(result.path(), "Path should not be null");
        assertEquals(2, result.path().size(), "Direct neighbor path length should be 2 nodes");
        assertEquals("Main Gate", result.path().get(0).getName());
        assertEquals("Central Library", result.path().get(1).getName());
    }

    @Test
    @DisplayName("Test 2: BFS handles unreachable destinations gracefully")
    void testBfsHandlesUnreachableDestinations() throws Exception {
        // Create an isolated island location with no edges
        Location isolated = new Location(0, "Isolated Observatory", "Perimeter Hill", "Roof", "OBS-99", "Research", "Far isolated observatory with no connecting pathway");
        campusService.saveLocation(isolated);

        // Search for newly created location ID
        List<Location> search = campusService.searchLocations("Isolated Observatory", null);
        assertFalse(search.isEmpty(), "Isolated location should be present in database");
        int isolatedId = search.get(0).getId();

        // Attempt BFS route from Main Gate (ID 1) to Isolated location
        RouteResult routeResult = campusService.findShortestRoute(1, isolatedId);

        assertFalse(routeResult.found(), "BFS should return false for disconnected nodes");
        assertTrue(routeResult.message().toLowerCase().contains("no accessible route found")
                || routeResult.message().toLowerCase().contains("no route found"),
                "Result message should indicate that no route exists");
    }

    @Test
    @DisplayName("Test 3: Invalid login throws InvalidLoginException")
    void testInvalidLoginThrowsException() {
        assertThrows(InvalidLoginException.class, () -> {
            campusService.authenticate("fake_user", "wrong_password");
        }, "Authenticating with non-existent credentials must throw InvalidLoginException");

        assertThrows(InvalidLoginException.class, () -> {
            campusService.authenticate("student1", "incorrect_pwd");
        }, "Authenticating with wrong password must throw InvalidLoginException");
    }

    @Test
    @DisplayName("Test 4: Search returns matches by keyword and category")
    void testSearchReturnsMatches() {
        // Search by name keyword
        List<Location> libMatches = campusService.searchLocations("Library", null);
        assertFalse(libMatches.isEmpty(), "Search for 'Library' should return matches");
        assertTrue(libMatches.stream().anyMatch(l -> l.getName().contains("Library")));

        // Search by category
        List<Location> labMatches = campusService.searchLocations("", "Laboratory");
        assertFalse(labMatches.isEmpty(), "Search for category 'Laboratory' should return matches");
        for (Location loc : labMatches) {
            assertEquals("Laboratory", loc.getCategory(), "Returned location must belong to Laboratory category");
        }
    }
}
