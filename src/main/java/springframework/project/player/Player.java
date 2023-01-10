package springframework.project.player;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import springframework.project.checkers.ColorType;
import springframework.project.game.Game;
import springframework.project.model.BaseEntity;

@Entity
@Getter
@Setter
public class Player extends BaseEntity {
    private ColorType color;

    @Column(length = 64)
    private String gameToken;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
}
