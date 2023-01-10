package springframework.project.game;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import springframework.project.checkers.CheckersRules;
import springframework.project.model.BaseEntity;
import springframework.project.player.Player;

@Getter
@Setter
@Entity
public class Game extends BaseEntity {

    @Column(length = 5, unique = true)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String boardString;
    private Integer boardDimension;

    private Integer turn;

    @OneToMany(mappedBy = "game")
    private List<Player> players;

    @Transient
    private Board board;

    public static Board createBoard() {
        Board newBoard = CheckersRules.initBoard(8);
        return newBoard;
    }
}
