package tennisfan.tf_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import tennisfan.tf_backend.annotations.RequiresAdmin;
import tennisfan.tf_backend.dto.*;
import tennisfan.tf_backend.repositories.AdminRepository;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AdminRepository adminRepository;

    @RequiresAdmin
    public List<AdminMatchDTO> getAllMatches(Integer adminUserId) {
        return adminRepository.getAllMatchesForAdmin();
    }

    @RequiresAdmin
    public AdminMatchDTO createMatch(Integer adminUserId, CreateMatchRequest request) {
        return adminRepository.createMatch(request);
    }

    @RequiresAdmin
    public void updateMatch(Integer adminUserId, Integer matchId, CreateMatchRequest request) {
        adminRepository.updateMatch(matchId, request);
    }

    @RequiresAdmin
    public void deleteMatch(Integer adminUserId, Integer matchId) {
        adminRepository.deleteMatch(matchId);
    }

    @RequiresAdmin
    public List<AdminUserDTO> getAllUsers(Integer adminUserId) {
        return adminRepository.getAllUsers();
    }

    @RequiresAdmin
    public void updateUserRole(Integer adminUserId, Integer userId, UpdateRoleRequest request) {
        adminRepository.updateUserRole(userId, request.getRole());
    }

    @RequiresAdmin
    public void deleteUser(Integer adminUserId, Integer userId) {
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

    @RequiresAdmin
    public List<TournamentDTO> getAllTournaments(Integer adminUserId) {
        return adminRepository.getAllTournaments();
    }

    @RequiresAdmin
    public TournamentDTO createTournament(Integer adminUserId, CreateTournamentDTO request) {
        return adminRepository.createTournament(request);
    }

    @RequiresAdmin
    public void updateTournament(Integer adminUserId, Integer tournamentId, UpdateTournamentDTO request) {
        adminRepository.updateTournament(tournamentId, request);
    }

    @RequiresAdmin
    public void deleteTournament(Integer adminUserId, Integer tournamentId) {
        adminRepository.deleteTournament(tournamentId);
    }

    @RequiresAdmin
    public void openTournamentRegistration(Integer adminUserId, Integer tournamentId) {
        adminRepository.openTournamentRegistration(tournamentId);
    }

    @RequiresAdmin
    public void closeTournamentRegistration(Integer adminUserId, Integer tournamentId) {
        adminRepository.closeTournamentRegistration(tournamentId);
    }

    @RequiresAdmin
    public List<TournamentParticipantDTO> getTournamentParticipants(Integer adminUserId, Integer tournamentId) {
        return adminRepository.getTournamentParticipants(tournamentId);
    }

}