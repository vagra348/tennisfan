package tennisfan.tf_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisfan.tf_backend.dto.PlayerDTO;
import tennisfan.tf_backend.repositories.PlayerViewRepository;

import java.util.List;

@Service
public class PlayerViewService {

    @Autowired
    private PlayerViewRepository playerViewRepository;

    public List<PlayerDTO> getAllPlayers() {
        return playerViewRepository.findAllPlayers();
    }

    public List<PlayerDTO> getClubPlayers() {
        return playerViewRepository.findAllClubPlayers();
    }

    public PlayerDTO getPlayerById(Integer playerId) {
        return playerViewRepository.findById(playerId);
    }

}