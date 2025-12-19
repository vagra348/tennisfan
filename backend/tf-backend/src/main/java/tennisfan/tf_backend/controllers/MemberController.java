package tennisfan.tf_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tennisfan.tf_backend.dto.MyMatchesDTO;
import tennisfan.tf_backend.dto.RegisterForTournamentDTO;
import tennisfan.tf_backend.dto.TournamentStatusDTO;
import tennisfan.tf_backend.services.MemberService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/tournament-status")
    public ResponseEntity<TournamentStatusDTO> getTournamentStatus(@RequestHeader("X-User-Id") Integer userId) {
        return ResponseEntity.ok(memberService.getCurrentTournamentStatus(userId));
    }

    @PostMapping("/register-for-tournament")
    public ResponseEntity<Map<String, String>> registerForTournament(
            @RequestHeader("X-User-Id") Integer userId,
            @RequestBody RegisterForTournamentDTO request) {
        memberService.registerForTournament(userId, request.getTournamentId());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Вы успешно зарегистрированы на турнир!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-matches")
    public ResponseEntity<List<MyMatchesDTO>> getMyMatches(@RequestHeader("X-User-Id") Integer userId) {
        return ResponseEntity.ok(memberService.getMyMatches(userId));
    }

    @GetMapping("/tournament-history")
    public ResponseEntity<List<TournamentStatusDTO>> getTournamentHistory(@RequestHeader("X-User-Id") Integer userId) {
        return ResponseEntity.ok(memberService.getTournamentHistory(userId));
    }
}