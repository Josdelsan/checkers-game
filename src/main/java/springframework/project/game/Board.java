package springframework.project.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import springframework.project.checkers.Disc;

public class Board {
    private List<List<Disc>> board;
    private Integer dimension;

    private static final String ELEMENT_SEPARATOR = ",";
    private static final String LIST_SEPARATOR = ";";
    public static final String EMPTY = "empty";

    public Board(List<List<Disc>> board) {
        this.board = board;
        this.dimension = board.size();
    }

    public int getDimension() {
        return board.size();
    }

    public Disc get(int col, int row) {
        return board.get(col).get(row);
    }

    public void set(int col, int row, Disc disc) {
        List<Disc> colList = board.get(col);
        colList.set(row, disc);
        board.set(col, colList);
    }

    public Boolean isPiece(int col, int row) {
        return get(col, row) instanceof Disc;
    }

    public Boolean isInBoard(int col, int row) {
        if ((col >= 0 && col < dimension) && (row >= 0 && row < dimension)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Disc get(Position pos) {
        return board.get(pos.getCol()).get(pos.getRow());
    }

    public void set(Position pos, Disc disc) {
        List<Disc> colList = board.get(pos.getCol());
        colList.set(pos.getRow(), disc);
        board.set(pos.getCol(), colList);
    }

    public Boolean isPiece(Position pos) {
        return get(pos.getCol(), pos.getRow()) instanceof Disc;
    }

    public Boolean isInBoard(Position pos) {
        if ((pos.getCol() >= 0 && pos.getCol() < dimension) && (pos.getRow() >= 0 && pos.getRow() < dimension)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public Map<Position, Disc> getPieces() {
        Map<Position, Disc> pieces = new HashMap<>();
        for (int col = 0; col < dimension; col++) {
            for (int row = 0; row < dimension; row++) {
                Disc disc = get(col, row);
                if (disc instanceof Disc) {
                    pieces.putIfAbsent(new Position(col, row), disc);
                }
            }
        }
        return pieces;
    }

    /**
     * Format e00,e01,..,e0n;e10,e11,..,e1n;...;en0,en1,...enn
     */
    @Override
    public String toString() {
        String bString = "";
        for (int col = 0; col < dimension; col++) {
            for (int row = 0; row < dimension; row++) {
                Disc d = board.get(col).get(row);
                if (d == null) {
                    bString += EMPTY;
                } else {
                    bString += d.toString();
                }
                bString += row == dimension - 1 ? "" : ELEMENT_SEPARATOR;

            }
            bString += col == dimension - 1 ? "" : LIST_SEPARATOR;
        }
        return bString;
    }

    public static Board toBoard(String bString) {
        List<List<Disc>> listOfColumns = new ArrayList<>();
        String[] arrayOfColumns = bString.split(LIST_SEPARATOR);

        for (String columnString : arrayOfColumns) {
            String[] elements = columnString.split(ELEMENT_SEPARATOR);
            List<Disc> column = new ArrayList<>();
            for (String e : elements) {
                if (e.equals(EMPTY)) {
                    column.add(null);
                } else {
                    Disc disc = Disc.toDisc(e);
                    column.add(disc);
                }
            }
            listOfColumns.add(column);
        }
        return new Board(listOfColumns);
    }
}
