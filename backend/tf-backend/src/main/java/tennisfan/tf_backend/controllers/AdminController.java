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

    @GetMapping("/players")
    public ResponseEntity<List<AdminPlayerDTO>> getAllPlayers(
            @RequestHeader("X-User-Id") Integer adminUserId) {
        return ResponseEntity.ok(adminService.getAllPlayers(adminUserId));
    }

    @PostMapping("/players")
    public ResponseEntity<AdminPlayerDTO> createPlayer(
            @RequestHeader("X-User-Id") Integer adminUserId,
            @RequestBody CreatePlayerRequest request) {
        return ResponseEntity.ok(adminService.createPlayer(adminUserId, request));
    }

    @PutMapping("/players/{playerId}")
    public ResponseEntity<Map<String, String>> updatePlayer(
            @RequestHeader("X-User-Id") Integer adminUserId,
            @PathVariable Integer playerId,
            @RequestBody CreatePlayerRequest request) {
        adminService.updatePlayer(adminUserId, playerId, request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Player updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/players/{playerId}")
    public ResponseEntity<Map<String, String>> deletePlayer(
            @RequestHeader("X-User-Id") Integer adminUserId,
            @PathVariable Integer playerId) {
        adminService.deletePlayer(adminUserId, playerId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Player deleted successfully");
        return ResponseEntity.ok(response);
    }

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
        response.put("message", "User role updated successfully");
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
}