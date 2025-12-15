package tennisfan.tf_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import tennisfan.tf_backend.dto.*;
import tennisfan.tf_backend.repositories.AdminRepository;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AdminRepository adminRepository;

    public List<AdminPlayerDTO> getAllPlayers(Integer adminUserId) {
        if (!isUserAdmin(adminUserId)) {
            throw new SecurityException("Only admins can perform this action");
        }
        return adminRepository.getAllPlayersForAdmin();
    }

    public AdminPlayerDTO createPlayer(Integer adminUserId, CreatePlayerRequest request) {
        if (!isUserAdmin(adminUserId)) {
            throw new SecurityException("Only admins can perform this action");
        }
        return adminRepository.createPlayer(request);
    }

    public void updatePlayer(Integer adminUserId, Integer playerId, CreatePlayerRequest request) {
        if (!isUserAdmin(adminUserId)) {
            throw new SecurityException("Only admins can perform this action");
        }
        adminRepository.updatePlayer(playerId, request);
    }

    public void deletePlayer(Integer adminUserId, Integer playerId) {
        if (!isUserAdmin(adminUserId)) {
            throw new SecurityException("Only admins can perform this action");
        }
        adminRepository.deletePlayer(playerId);
    }

    public List<AdminMatchDTO> getAllMatches(Integer adminUserId) {
        if (!isUserAdmin(adminUserId)) {
            throw new SecurityException("Only admins can perform this action");
        }
        return adminRepository.getAllMatchesForAdmin();
    }

    public AdminMatchDTO createMatch(Integer adminUserId, CreateMatchRequest request) {
        if (!isUserAdmin(adminUserId)) {
            throw new SecurityException("Only admins can perform this action");
        }
        return adminRepository.createMatch(request);
    }

    public void updateMatch(Integer adminUserId, Integer matchId, CreateMatchRequest request) {
        if (!isUserAdmin(adminUserId)) {
            throw new SecurityException("Only admins can perform this action");
        }
        adminRepository.updateMatch(matchId, request);
    }

    public void deleteMatch(Integer adminUserId, Integer matchId) {
        if (!isUserAdmin(adminUserId)) {
            throw new SecurityException("Only admins can perform this action");
        }
        adminRepository.deleteMatch(matchId);
    }

    public List<AdminUserDTO> getAllUsers(Integer adminUserId) {
        if (!isUserAdmin(adminUserId)) {
            throw new SecurityException("Only admins can perform this action");
        }
        return adminRepository.getAllUsers();
    }

    public void updateUserRole(Integer adminUserId, Integer userId, UpdateRoleRequest request) {
        if (!isUserAdmin(adminUserId)) {
            throw new SecurityException("Only admins can perform this action");
        }
        adminRepository.updateUserRole(userId, request.getRole());
    }

    public void deleteUser(Integer adminUserId, Integer userId) {
        if (!isUserAdmin(adminUserId)) {
            throw new SecurityException("Only admins can perform this action");
        }
        if (adminUserId.equals(userId)) {
            throw new SecurityException("Admin cannot delete themselves");
        }
        adminRepository.deleteUser(userId);
    }

    public boolean isUserAdmin(Integer userId) {
        String sql = "SELECT role FROM users WHERE id = ?";
        try {
            String role = jdbcTemplate.queryForObject(sql, String.class, userId);
            return "ADMIN".equals(role);
        } catch (Exception e) {
            return false;
        }
    }
}