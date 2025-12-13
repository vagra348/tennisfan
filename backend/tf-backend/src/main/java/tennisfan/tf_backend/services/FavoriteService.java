package tennisfan.tf_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisfan.tf_backend.repositories.FavoritePlayerRepository;

@Service
public class FavoriteService {

    @Autowired
    private FavoritePlayerRepository favoriteRepository;

    public void addFavoritePlayer(Integer userId, Integer playerId) {
        favoriteRepository.addFavorite(userId, playerId);
    }

    public void removeFavoritePlayer(Integer userId, Integer playerId) {
        favoriteRepository.removeFavorite(userId, playerId);
    }
}