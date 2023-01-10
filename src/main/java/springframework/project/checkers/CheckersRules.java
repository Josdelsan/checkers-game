package springframework.project.checkers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import springframework.project.game.Board;
import springframework.project.game.Position;
import springframework.project.game.exceptions.InvalidMoveException;

public class CheckersRules {

    /**
     * Initializes a board placing dics given its dimension
     * 
     * @param dimension
     * @return Board
     */
    public static Board initBoard(Integer dimension) {
        Boolean place = Boolean.TRUE;
        Integer midField = dimension / 2;

        Disc whiteDisc = new Disc(ColorType.WHITE, Boolean.FALSE);
        Disc blackDisc = new Disc(ColorType.BLACK, Boolean.FALSE);

        List<Disc> byColumns = new ArrayList<>(dimension);
        List<Disc> oddColumns = new ArrayList<>(dimension);

        for (int pos = 0; pos < dimension; pos++, place = !place) {
            if (pos == midField || pos == midField - 1) {
                oddColumns.add(null);
                byColumns.add(null);
            } else if (pos < midField) {
                oddColumns.add(place ? whiteDisc : null);
                byColumns.add(!place ? whiteDisc : null);
            } else {
                oddColumns.add(place ? blackDisc : null);
                byColumns.add(!place ? blackDisc : null);
            }
        }

        List<List<Disc>> board = new ArrayList<>(dimension);
        for (int column = 0; column < dimension; column++) {
            board.add(column % 2 == 0 ? byColumns : oddColumns);
        }

        return new Board(board);
    }

    /**
     * Move a piece to the tile(s) choosen
     * 
     * @param board
     * @param actualPos
     * @param positions
     * @return
     * @throws InvalidMoveException
     */
    public static Board movePiece(Board board, Position actualPos, List<Position> positions)
            throws InvalidMoveException {
        if (board.isPiece(actualPos))
            return movePiece(board, actualPos, positions, Boolean.FALSE);
        else
            throw new InvalidMoveException("There is no disc in that position");
    }

    private static Board movePiece(Board board, Position actualPos, List<Position> positions, Boolean hasAttacked)
            throws InvalidMoveException {
        Integer numberOfMoves = positions.size();
        Position nextPos;
        switch (numberOfMoves) {
            case 0:
                throw new InvalidMoveException("There are no moves especified");
            case 1:
                // Check single step
                nextPos = positions.remove(0);
                if(!board.isInBoard(nextPos)){
                    throw new InvalidMoveException("Invalid position");
                }else if (board.isPiece(nextPos)) {
                    throw new InvalidMoveException("Cannot move to an occupied tile");
                } else if (isSingleStep(actualPos, nextPos)) {
                    if (hasAttacked.equals(Boolean.FALSE)) {
                        board = moveSingleStep(board, actualPos, nextPos);
                    } else {
                        throw new InvalidMoveException(
                                "Single step is not allowed if the player has previously attacked");
                    }
                } else {
                    board = moveAttack(board, actualPos, nextPos);
                }
                return board;
            default:
                nextPos = positions.remove(0);
                if(!board.isInBoard(nextPos)){
                    throw new InvalidMoveException("Invalid position");
                }else if (isSingleStep(actualPos, nextPos)) {
                    throw new InvalidMoveException("Moves are not allowed");
                } else if (board.isPiece(nextPos)) {
                    throw new InvalidMoveException("Cannot move to an occupied tile");
                } else {
                    board = moveAttack(board, actualPos, nextPos);
                    board = movePiece(board, nextPos, positions, Boolean.TRUE);
                    return board;
                }
        }
    }

    private static Boolean isSingleStep(Position actualPos, Position nextPos) throws InvalidMoveException {
        Integer distanceCol = Math.abs(nextPos.getCol() - actualPos.getCol());
        Integer distanceRow = Math.abs(nextPos.getRow() - actualPos.getRow());
        if (distanceCol == 1 && distanceRow == 1) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    private static Boolean isCorrectDirection(Position actualPos, Position nextPos, Disc disc) {
        if (disc.getPromoted().equals(Boolean.TRUE)) {
            return Boolean.TRUE;
        } else {
            Integer direction = nextPos.getRow() - actualPos.getRow();
            if (direction > 0) {
                return disc.getColor().equals(ColorType.WHITE) ? Boolean.TRUE : Boolean.FALSE;
            } else {
                return disc.getColor().equals(ColorType.BLACK) ? Boolean.TRUE : Boolean.FALSE;
            }
        }
    }

    private static Boolean isMoveDiagonal(Position actualPos, Position nextPos) throws InvalidMoveException {
        Integer distanceCol = Math.abs(nextPos.getCol() - actualPos.getCol());
        Integer distanceRow = Math.abs(nextPos.getRow() - actualPos.getRow());
        if (distanceCol.equals(distanceRow)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }

    }

    private static Board moveSingleStep(Board board, Position actualPos, Position nextPos) throws InvalidMoveException {
        Disc disc = board.get(actualPos);
        if (isCorrectDirection(actualPos, nextPos, disc)) {
            disc = checkPromoted(board, nextPos, disc);
            board.set(nextPos, disc);
            board.set(actualPos, null);
            return board;
        } else {
            throw new InvalidMoveException("Disc cannot move in that direction");
        }
    }

    private static Board moveAttack(Board board, Position actualPos, Position nextPos) throws InvalidMoveException {
        Disc disc = board.get(actualPos);
        if (!isMoveDiagonal(actualPos, nextPos)) {
            throw new InvalidMoveException("Move is not diagonal");
        } else if (!isCorrectDirection(actualPos, nextPos, disc)) {
            throw new InvalidMoveException("Disc cannot move in that direction");
        } else {
            disc = checkPromoted(board, nextPos, disc);

            Integer distance = (Math.abs(nextPos.getCol() - actualPos.getCol()) +
                    Math.abs(nextPos.getRow() - actualPos.getRow())) / 2;

            // Calculate position under attack
            Integer colDisplacement = (nextPos.getCol() - actualPos.getCol()) / 2;
            Integer rowDisplacement = (nextPos.getRow() - actualPos.getRow()) / 2;
            Position underAtkPosition = new Position(actualPos.getCol() + colDisplacement,
                    actualPos.getRow() + rowDisplacement);
            Disc discUnderAttack = board.get(underAtkPosition);
            if(discUnderAttack instanceof Disc){
                if (disc.getColor().equals(discUnderAttack.getColor()) || !distance.equals(2)) {
                    throw new InvalidMoveException("Move not allowed");
                } else {
                    board.set(nextPos, disc);
                    board.set(actualPos, null);
                    board.set(underAtkPosition, null);
                    return board;
                }
            }else{
                throw new InvalidMoveException("There is no disc to attack");
            }
        }
    }

    private static Disc checkPromoted(Board board, Position actualPos, Disc disc) {
        Integer promotionRow = disc.getColor().equals(ColorType.WHITE) ? board.getDimension() - 1 : 0;
        if (actualPos.getRow().equals(promotionRow)) {
            disc.setPromoted(Boolean.TRUE);
        }
        return disc;
    }

    /**
     * Look for available moves given a color
     * 
     * @param board
     * @param color
     * @return Map<Position, Set<Position>>
     * @throws InvalidMoveException
     */
    public static Map<Position, Set<Position>> availableMoves(Board board, ColorType color)
            throws InvalidMoveException {

        Map<Position, Set<Position>> availableMoves = new HashMap<>();

        Map<Position, Disc> pieces = board.getPieces();
        Set<Position> positions = pieces.keySet();

        for (Iterator<Position> it = positions.iterator(); it.hasNext();) {
            Position pos = it.next();
            Disc disc = pieces.get(pos);
            if (disc.getColor().equals(color)) {
                Set<Position> moves = checkMoves(board, pos);
                if (moves.size() > 0) {
                    availableMoves.putIfAbsent(pos, moves);
                }
            }
        }

        return availableMoves;
    }

    /**
     * Check available moves for an specific piece in the board
     * 
     * @param board
     * @param actualPos
     * @param disc
     * @return Set<Position>
     * @throws InvalidMoveException
     */
    public static Set<Position> checkMoves(Board board, Position actualPos) throws InvalidMoveException {
        Set<Position> moves = new HashSet<>();
        Disc disc = board.get(actualPos);
        if (disc instanceof Disc)
            return checkMoves(board, actualPos, disc, moves, Boolean.FALSE);
        else
            throw new InvalidMoveException("There is no dic in position " + actualPos.toString());
    }

    private static Set<Position> checkMoves(Board board, Position actualPos,
            Disc disc, Set<Position> moves, Boolean hasAttacked) {
        Integer actualCol = actualPos.getCol();
        Integer actualRow = actualPos.getRow();

        if (board.isInBoard(actualCol, actualRow)) {
            // Promote disc
            disc = checkPromoted(board, actualPos, disc);

            if (hasAttacked.equals(Boolean.FALSE)) {
                // Check available single step moves
                moves.addAll(checkSingleStepMoves(board, actualPos, disc));
            }
            // Check available
            Set<Position> attackTiles = checkAttackMoves(board, actualPos, disc, moves);
            moves.addAll(attackTiles);
            for (Iterator<Position> it = attackTiles.iterator(); it.hasNext();) {
                Position newPos = it.next();
                moves.addAll(checkMoves(board, newPos, disc, moves, Boolean.TRUE));
            }
        }
        return moves;
    }

    private static Set<Position> checkSingleStepMoves(Board board, Position actualPos, Disc disc) {
        return checkSingleStepMoves(board, actualPos, disc, 1);
    }

    private static Set<Position> checkSingleStepMoves(Board board, Position actualPos, Disc disc, Integer inversed) {
        Set<Position> moves = new HashSet<>();
        final Integer DIRECTION = disc.getColor().equals(ColorType.WHITE) ? 1 : -1;
        Integer col = actualPos.getCol();
        Integer row = actualPos.getRow();

        // Check both column options for a disc
        Integer[] options = { 1, -1 };
        for (Integer option : options) {
            Integer checkCol = col + option;
            Integer checkRow = row + (DIRECTION * inversed);
            if (board.isInBoard(checkCol, checkRow)) {
                if (!board.isPiece(checkCol, checkRow)) {
                    // Tile must exist and must be empty
                    moves.add(new Position(checkCol, checkRow));
                }
            }
        }
        // If promoted check the inverse path
        if (disc.getPromoted().equals(Boolean.TRUE) && inversed.equals(1)) {
            moves.addAll(checkSingleStepMoves(board, actualPos, disc, -1));
        }
        return moves;
    }

    private static Set<Position> checkAttackMoves(Board board, Position actualPos, Disc disc, Set<Position> prevMoves) {
        return checkAttackMoves(board, actualPos, disc, prevMoves, 1);
    }

    private static Set<Position> checkAttackMoves(Board board, Position actualPos, Disc disc, Set<Position> prevMoves,
            Integer inversed) {
        Set<Position> moves = new HashSet<>();
        ColorType color = disc.getColor();
        final Integer DIRECTION = color.equals(ColorType.WHITE) ? 1 : -1;
        Integer col = actualPos.getCol();
        Integer row = actualPos.getRow();

        Integer[] options = { 1, -1 };
        for (Integer option : options) {
            Integer checkCol = col + option;
            Integer checkRow = row + (DIRECTION * inversed);
            if (board.isInBoard(checkCol, checkRow)) {
                if (board.isPiece(checkCol, checkRow)) {
                    if (!board.get(checkCol, checkRow).getColor().equals(color)) {
                        // Check if there is an enemy disc then checks the next tile
                        Integer landCol = checkCol + option;
                        Integer landRow = checkRow + (DIRECTION * inversed);
                        if (board.isInBoard(landCol, landRow))
                            if (!board.isPiece(landCol, landRow)) {
                                Position landPos = new Position(landCol, landRow);
                                // Check if the position is already an option to avoid stepping back
                                Boolean posAlreadyFound = prevMoves.stream()
                                        .filter(p -> p.equals(landPos))
                                        .findAny()
                                        .isPresent();
                                if (posAlreadyFound.equals(Boolean.FALSE))
                                    moves.add(landPos);
                            }
                    }
                }
            }
        }
        // If promoted check the inverse path
        if (disc.getPromoted().equals(Boolean.TRUE) && inversed.equals(1)) {
            moves.addAll(checkAttackMoves(board, actualPos, disc, prevMoves, -1));
        }
        return moves;
    }

}
