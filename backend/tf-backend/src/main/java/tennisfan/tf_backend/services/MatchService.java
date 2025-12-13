package tennisfan.tf_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisfan.tf_backend.dto.MatchDTO;
import tennisfan.tf_backend.repositories.MatchRepository;

import java.util.List;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    public List<MatchDTO> getAllMatches(Integer userId) {
        return matchRepository.findAllMatches(userId);
    }

    public List<MatchDTO> getMatchesByFavoritePlayers(Integer userId) {
        return matchRepository.findMatchesByFavoritePlayers(userId);
    }
}