package springframework.project.game;

import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import springframework.project.checkers.CheckersRules;
import springframework.project.checkers.ColorType;
import springframework.project.game.exceptions.GameException;
import springframework.project.player.Player;
import springframework.project.player.PlayerService;
import springframework.project.util.StringGenerator;

@Service
@Transactional
public class GameService {

    private GameRepository gameRepository;
    private PlayerService playerService;

    public GameService(GameRepository gameRepository, PlayerService playerService) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
    }

    public Cookie handleGameCreation() throws GameException {
        Game game = newGame();
        Player player = playerService.newPlayer(ColorType.BLACK);

        player.setGame(game);
        playerService.save(player);

        Cookie cookie = playerSessionCookie(player);
        return cookie;
    }

    public void save(Game game) {
        gameRepository.save(game);
    }

    public Game newGame() throws GameException {
        String code = StringGenerator.gameCodeGenerator();
        Optional<Game> gameWithSameCode = gameRepository.findByGameCode(code);
        if (gameWithSameCode.isPresent())
            throw new GameException("Game could not be created");

        Board board = CheckersRules.initBoard(8);

        Game game = new Game();
        game.setCode(code);
        game.setBoardDimension(8);
        game.setBoardString(board.toString());
        game.setPlayers(new ArrayList<>());

        save(game);

        return game;
    }

    public Cookie playerSessionCookie(Player player) {
        Cookie cookie = new Cookie("player-token", player.getGameToken());

        cookie.setMaxAge(86400);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/game/");
        return cookie;
    }

}
