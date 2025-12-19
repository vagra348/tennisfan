package tennisfan.tf_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tennisfan.tf_backend.dto.PlayerDTO;
import tennisfan.tf_backend.services.PlayerViewService;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerViewService playerViewService;

    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        return ResponseEntity.ok(playerViewService.getAllPlayers());
    }

    @GetMapping("/club")
    public ResponseEntity<List<PlayerDTO>> getClubPlayers() {
        return ResponseEntity.ok(playerViewService.getClubPlayers());
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Integer playerId) {
        return ResponseEntity.ok(playerViewService.getPlayerById(playerId));
    }

}