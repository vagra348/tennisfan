package tennisfan.tf_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisfan.tf_backend.dto.MyMatchesDTO;
import tennisfan.tf_backend.dto.TournamentStatusDTO;
import tennisfan.tf_backend.repositories.MemberRepository;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public TournamentStatusDTO getCurrentTournamentStatus(Integer userId) {
        return memberRepository.getCurrentTournamentStatus(userId);
    }

    public void registerForTournament(Integer userId, Integer tournamentId) {
        memberRepository.registerForTournament(userId, tournamentId);
    }

    public List<MyMatchesDTO> getMyMatches(Integer userId) {
        return memberRepository.getMyMatches(userId);
    }

    public List<TournamentStatusDTO> getTournamentHistory(Integer userId) {
        return memberRepository.getTournamentHistory(userId);
    }
}