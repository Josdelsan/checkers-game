package springframework.project.player;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import springframework.project.checkers.ColorType;
import springframework.project.util.StringGenerator;

@Service
@Transactional
public class PlayerService {

    private PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }

    public void save(Player player){
        playerRepository.save(player);
    }

    public Player newPlayer(ColorType color){
        String token = StringGenerator.playerTokenGenerator();

        Player player = new Player();
        player.setColor(color);
        player.setGameToken(token);
        save(player);

        return player;

    }
}
