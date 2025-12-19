package tennisfan.tf_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tennisfan.tf_backend.dto.MatchDTO;
import tennisfan.tf_backend.services.MatchService;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping
    public ResponseEntity<List<MatchDTO>> getAllMatches(@RequestParam(required = false) Integer userId) {
        return ResponseEntity.ok(matchService.getAllMatches(userId));
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<MatchDTO>> getFavoriteMatches(@RequestParam Integer userId) {
        return ResponseEntity.ok(matchService.getMatchesByFavoritePlayers(userId));
    }
}