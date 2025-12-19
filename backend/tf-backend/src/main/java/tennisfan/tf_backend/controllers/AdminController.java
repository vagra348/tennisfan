package tennisfan.tf_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tennisfan.tf_backend.dto.*;
import tennisfan.tf_backend.services.AdminService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/matches")
    public ResponseEntity<List<AdminMatchDTO>> getAllMatches(
            @RequestHeader("X-User-Id") Integer adminUserId) {
        return ResponseEntity.ok(adminService.getAllMatches(adminUserId));
    }

    @PostMapping("/matches")
    public ResponseEntity<AdminMatchDTO> createMatch(
            @RequestHeader("X-User-Id") Integer adminUserId,
            @RequestBody CreateMatchRequest request) {
        return ResponseEntity.ok(adminService.createMatch(adminUserId, request));
    }

    @PutMapping("/matches/{matchId}")
    public ResponseEntity<Map<String, String>> updateMatch(
            @RequestHeader("X-User-Id") Integer adminUserId,
            @PathVariable Integer matchId,
            @RequestBody CreateMatchRequest request) {
        adminService.updateMatch(adminUserId, matchId, request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Match updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/matches/{matchId}")
    public ResponseEntity<Map<String, String>> deleteMatch(
            @RequestHeader("X-User-Id") Integer adminUserId,
            @PathVariable Integer matchId) {
        adminService.deleteMatch(adminUserId, matchId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Match deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers(
            @RequestHeader("X-User-Id") Integer adminUserId) {
        return ResponseEntity.ok(adminService.getAllUsers(adminUserId));
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<Map<String, String>> updateUserRole(
            @RequestHeader("X-User-Id") Integer adminUserId,
            @PathVariable Integer userId,
            @RequestBody UpdateRoleRequest request) {
        adminService.updateUserRole(adminUserId, userId, request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User role updated successfully to " + request.getRole());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(
            @RequestHeader("X-User-Id") Integer adminUserId,
            @PathVariable Integer userId) {
        adminService.deleteUser(adminUserId, userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tournaments")
    public ResponseEntity<List<TournamentDTO>> getAllTournaments(
            @RequestHeader("X-User-Id") Integer adminUserId) {
        return ResponseEntity.ok(adminService.getAllTournaments(adminUserId));
    }

    @PostMapping("/tournaments")
    public ResponseEntity<TournamentDTO> createTournament(
            @RequestHeader("X-User-Id") Integer adminUserId,
            @RequestBody CreateTournamentDTO request) {
        return ResponseEntity.ok(adminService.createTournament(adminUserId, request));
    }

    @PutMapping("/tournaments/{tournamentId}")
    public ResponseEntity<Map<String, String>> updateTournament(
            @RequestHeader("X-User-Id") Integer adminUserId,
            @PathVariable Integer tournamentId,
            @RequestBody UpdateTournamentDTO request) {
        adminService.updateTournament(adminUserId, tournamentId, request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Турнир обновлен успешно");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/tournaments/{tournamentId}")
    public ResponseEntity<Map<String, String>> deleteTournament(
            @RequestHeader("X-User-Id") Integer adminUserId,
            @PathVariable Integer tournamentId) {
        adminService.deleteTournament(adminUserId, tournamentId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Турнир удален успешно");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tournaments/{tournamentId}/open-registration")
    public ResponseEntity<Map<String, String>> openTournamentRegistration(
            @RequestHeader("X-User-Id") Integer adminUserId,
            @PathVariable Integer tournamentId) {
        adminService.openTournamentRegistration(adminUserId, tournamentId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Регистрация на турнир открыта");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tournaments/{tournamentId}/close-registration")
    public ResponseEntity<Map<String, String>> closeTournamentRegistration(
            @RequestHeader("X-User-Id") Integer adminUserId,
            @PathVariable Integer tournamentId) {
        adminService.closeTournamentRegistration(adminUserId, tournamentId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Регистрация на турнир закрыта");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tournaments/{tournamentId}/participants")
    public ResponseEntity<List<TournamentParticipantDTO>> getTournamentParticipants(
            @RequestHeader("X-User-Id") Integer adminUserId,
            @PathVariable Integer tournamentId) {
        return ResponseEntity.ok(adminService.getTournamentParticipants(adminUserId, tournamentId));
    }

}