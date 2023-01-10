package springframework.project.controller;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import springframework.project.game.GameService;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/games")
public class GameController {

    private final static String PLAYER_COOKIE = "player-token";
    private final static String CREATE_GAME_VIEW = "games/createGameForm";
    private final static String JOIN_GAME_VIEW = "games/joinGameForm";

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping(value = "/new")
    public String newGame(ModelMap model, HttpServletResponse respone, HttpServletRequest request) {
        Boolean tokenFound = Boolean.FALSE;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            tokenFound = Arrays.stream(cookies).filter(c -> c.getName().equals(PLAYER_COOKIE))
                    .findAny().isPresent();
        }

        return tokenFound ? "error":CREATE_GAME_VIEW;
    }

    @GetMapping(value="/{gameId}")
    public String game(@RequestParam(name = "gameId") String gameId) {
        return "";
    }

    @GetMapping(value = "/join")
    public String joinGame(){
        return JOIN_GAME_VIEW;
    }
    

}
