package tennisfan.tf_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tennisfan.tf_backend.services.FavoriteService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/players/{playerId}")
    public ResponseEntity<Map<String, String>> addFavoritePlayer(
            @PathVariable Integer playerId,
            @RequestParam Integer userId) {
        favoriteService.addFavoritePlayer(userId, playerId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Player added to favorites");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/players/{playerId}")
    public ResponseEntity<Map<String, String>> removeFavoritePlayer(
            @PathVariable Integer playerId,
            @RequestParam Integer userId) {
        favoriteService.removeFavoritePlayer(userId, playerId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Player removed from favorites");
        return ResponseEntity.ok(response);
    }
}