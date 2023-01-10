package springframework.project.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Position {
    private Integer col;
    private Integer row;

    public Position(Integer col, Integer row) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "{" + col.toString() + " " + row.toString() + "}";
    }

    @Override
    public boolean equals(Object e) {
        if (e instanceof Position) {
            if (((Position) e).getCol().equals(this.col) && ((Position) e).getRow().equals(this.row)) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else {
            return Boolean.FALSE;
        }
    }
}
