package springframework.project.checkers.checkersRulesTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import springframework.project.checkers.CheckersRules;
import springframework.project.checkers.ColorType;
import springframework.project.checkers.Disc;
import springframework.project.game.Board;
import springframework.project.game.Position;
import springframework.project.game.exceptions.InvalidMoveException;

@SpringBootTest
public class movePieceTest {

    String basicBoard = "empty,empty,empty,empty,empty,empty,empty,empty;" +
            "empty,empty,empty,empty,empty,empty,empty,empty;" +
            "empty,empty,empty,empty,empty,empty,empty,empty;" +
            "empty,empty,empty,empty,empty,empty,empty,empty;" +
            "empty,empty,empty,empty,empty,empty,empty,empty;" +
            "empty,empty,empty,empty,empty,empty,empty,empty;" +
            "empty,empty,empty,empty,empty,empty,empty,empty;" +
            "empty,empty,empty,empty,empty,empty,empty,empty";
    Board board;

    @BeforeEach
    void configure() {
        board = Board.toBoard(basicBoard);
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "BLACK-false", "WHITE-false" })
    void movePieceSingleStep(String arg) throws InvalidMoveException {
        // Arrange
        Disc disc = Disc.toDisc(arg);
        Position pos = new Position(4, 4);
        Board board2 = Board.toBoard(basicBoard);
        board.set(pos, disc);
        board2.set(pos, disc);

        Integer direction = disc.getColor().equals(ColorType.WHITE) ? 1 : -1;

        List<Position> moves1 = new ArrayList<>();
        List<Position> moves2 = new ArrayList<>();
        Position move1 = new Position(5, 4 + direction);
        moves1.add(move1);
        Position move2 = new Position(3, 4 + direction);
        moves2.add(move2);

        // Act
        board = CheckersRules.movePiece(board, pos, moves1);
        board2 = CheckersRules.movePiece(board2, pos, moves2);

        // Assert
        assertTrue(board.isPiece(move1), "There should be a disc in tile " + move1.toString());
        assertTrue(board2.isPiece(move2), "There should be a disc in tile " + move2.toString());
        assertFalse(board.isPiece(pos), "Tile should be empty after the move");
        assertFalse(board2.isPiece(pos), "Tile should be empty after the move");
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "BLACK-false", "WHITE-false" })
    void movePieceAttack(String arg) throws InvalidMoveException {
        // Arrange
        Disc disc = Disc.toDisc(arg);
        Position pos = new Position(4, 4);
        Board board2 = Board.toBoard(basicBoard);
        board.set(pos, disc);
        board2.set(pos, disc);

        Integer direction = disc.getColor().equals(ColorType.WHITE) ? 1 : -1;
        ColorType attackedColor = disc.getColor().equals(ColorType.WHITE) ? ColorType.BLACK : ColorType.WHITE;
        Disc underAttackDisc = new Disc(attackedColor, Boolean.FALSE);

        Position attackedDiscPos1 = new Position(3, 4 + direction);
        Position attackedDiscPos2 = new Position(5, 4 + direction);
        board.set(attackedDiscPos1, underAttackDisc);
        board2.set(attackedDiscPos2, underAttackDisc);

        List<Position> moves1 = new ArrayList<>();
        List<Position> moves2 = new ArrayList<>();
        Position move1 = new Position(2, 4 + direction * 2);
        moves1.add(move1);
        Position move2 = new Position(6, 4 + direction * 2);
        moves2.add(move2);

        // Act
        board = CheckersRules.movePiece(board, pos, moves1);
        board2 = CheckersRules.movePiece(board2, pos, moves2);

        // Assert
        assertTrue(board.isPiece(move1), "There should be a disc in tile " + move1.toString());
        assertTrue(board2.isPiece(move2), "There should be a disc in tile " + move2.toString());
        assertFalse(board.isPiece(pos), "Tile should be empty after the move");
        assertFalse(board2.isPiece(pos), "Tile should be empty after the move");
        assertFalse(board.isPiece(attackedDiscPos1), "Tile should be empty after the move");
        assertFalse(board2.isPiece(attackedDiscPos2), "Tile should be empty after the move");
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "BLACK-false", "WHITE-false" })
    void movePieceDoubleAttack(String arg) throws InvalidMoveException {
        // Arrange
        Disc disc = Disc.toDisc(arg);
        Position pos = new Position(2, disc.getColor().equals(ColorType.WHITE) ? 2 : 6);
        board.set(pos, disc);

        ColorType attackedColor = disc.getColor().equals(ColorType.WHITE) ? ColorType.BLACK : ColorType.WHITE;
        Disc underAttackDisc = new Disc(attackedColor, Boolean.FALSE);

        Position attackedDiscPos1 = new Position(3, 3);
        Position attackedDiscPos2 = new Position(3, 5);
        board.set(attackedDiscPos1, underAttackDisc);
        board.set(attackedDiscPos2, underAttackDisc);

        List<Position> moves = new ArrayList<>();
        Position move1 = new Position(4, 4);
        moves.add(move1);
        Position move2 = new Position(2, disc.getColor().equals(ColorType.WHITE) ? 6 : 2);
        moves.add(move2);

        // Act
        board = CheckersRules.movePiece(board, pos, moves);

        // Assert
        assertTrue(board.isPiece(move2), "There should be a disc in tile " + move1.toString());
        assertFalse(board.isPiece(pos), "Tile should be empty after the move");
        assertFalse(board.isPiece(attackedDiscPos1), "Tile should be empty after the move");
        assertFalse(board.isPiece(attackedDiscPos2), "Tile should be empty after the move");
    }

    @Test
    void movePieceAttackAndPromoteBlack() throws InvalidMoveException{
        // Arrange
        Disc disc = Disc.toDisc("BLACK-false");
        Position pos = new Position(7,2);
        board.set(pos, disc);

        Disc underAttackDisc = new Disc(ColorType.WHITE, Boolean.FALSE);

        Position attackedDiscPos1 = new Position(6, 1);
        Position attackedDiscPos2 = new Position(4, 1);
        board.set(attackedDiscPos1, underAttackDisc);
        board.set(attackedDiscPos2, underAttackDisc);

        List<Position> moves = new ArrayList<>();
        Position move1 = new Position(5, 0);
        moves.add(move1);
        Position move2 = new Position(3,2);
        moves.add(move2);

        // Act
        board = CheckersRules.movePiece(board, pos, moves);

        // Assert
        assertTrue(board.isPiece(move2), "There should be a disc in tile " + move1.toString());
        assertFalse(board.isPiece(pos), "Tile should be empty after the move");
        assertFalse(board.isPiece(attackedDiscPos1), "Tile should be empty after the move");
        assertFalse(board.isPiece(attackedDiscPos2), "Tile should be empty after the move");
        assertTrue(board.get(move2).getPromoted().equals(Boolean.TRUE), "Disc should have been promoted");
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "BLACK-true", "WHITE-true" })
    void movePieceSingleStepPromoted(String arg) throws InvalidMoveException{
        // Arrange
        Disc disc = Disc.toDisc(arg);
        Position pos = new Position(4, 4);
        Board board2 = Board.toBoard(basicBoard);
        board.set(pos, disc);
        board2.set(pos, disc);

        Integer direction = disc.getColor().equals(ColorType.WHITE) ? -1 : 1;

        List<Position> moves1 = new ArrayList<>();
        List<Position> moves2 = new ArrayList<>();
        Position move1 = new Position(5, 4 + direction);
        moves1.add(move1);
        Position move2 = new Position(3, 4 + direction);
        moves2.add(move2);

        // Act
        board = CheckersRules.movePiece(board, pos, moves1);
        board2 = CheckersRules.movePiece(board2, pos, moves2);

        // Assert
        assertTrue(board.isPiece(move1), "There should be a disc in tile " + move1.toString());
        assertTrue(board2.isPiece(move2), "There should be a disc in tile " + move2.toString());
        assertFalse(board.isPiece(pos), "Tile should be empty after the move");
        assertFalse(board2.isPiece(pos), "Tile should be empty after the move");
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "BLACK-false", "WHITE-false" })
    void movePieceSingleStepWrongDirection(String arg) throws InvalidMoveException{
        // Arrange
        Disc disc = Disc.toDisc(arg);
        Position pos = new Position(4, 4);
        Board board2 = Board.toBoard(basicBoard);
        board.set(pos, disc);
        board2.set(pos, disc);

        Integer direction = disc.getColor().equals(ColorType.WHITE) ? -1 : 1;

        List<Position> moves1 = new ArrayList<>();
        List<Position> moves2 = new ArrayList<>();
        Position move1 = new Position(5, 4 + direction);
        moves1.add(move1);
        Position move2 = new Position(3, 4 + direction);
        moves2.add(move2);

        // Act Assert
        var e1 = assertThrows(InvalidMoveException.class, ()->{CheckersRules.movePiece(board, pos, moves1);});
        var e2 = assertThrows(InvalidMoveException.class, ()->{CheckersRules.movePiece(board2, pos, moves2);});
        assertEquals("Disc cannot move in that direction", e1.getMessage());
        assertEquals("Disc cannot move in that direction", e2.getMessage());
    }

    @Test
    void movePieceWrongActualPos(){
        Position pos = new Position(4,4);
        List<Position> moves = new ArrayList<>();
        Position move1 = new Position(5, 0);
        moves.add(move1);
        var e = assertThrows(InvalidMoveException.class, ()->{CheckersRules.movePiece(board, pos, moves);});
        assertEquals("There is no disc in that position", e.getMessage());
    }

    @Test
    void movePieceEmptyMoves(){
        Position pos = new Position(4,4);
        Disc disc = Disc.toDisc("WHITE-false");
        List<Position> moves = new ArrayList<>();
        board.set(pos, disc);
        var e = assertThrows(InvalidMoveException.class, ()->{CheckersRules.movePiece(board, pos, moves);});
        assertEquals("There are no moves especified", e.getMessage());
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "BLACK-false", "WHITE-false" })
    void movePieceOccupiedTile(String arg){
        Disc disc = Disc.toDisc(arg);
        Position pos = new Position(4, 4);
        board.set(pos, disc);

        Integer direction = disc.getColor().equals(ColorType.WHITE) ? 1 : -1;

        List<Position> moves = new ArrayList<>();
        Position move = new Position(5, 4 + direction);
        moves.add(move);
        board.set(move, disc);

        var e = assertThrows(InvalidMoveException.class, ()->{CheckersRules.movePiece(board, pos, moves);});
        assertEquals("Cannot move to an occupied tile", e.getMessage());
    }

    @Test
    void movePieceSingleStepAfterAttackBlack(){
        Disc disc = Disc.toDisc("BLACK-false");
        Position pos = new Position(4, 4);
        board.set(pos, disc);

        List<Position> moves = new ArrayList<>();
        Position move = new Position(6, 2);
        Position move2 = new Position(7, 1);
        moves.add(move);
        moves.add(move2);

        Position underAttackPos = new Position(5, 3);
        Disc discUnderAttack = Disc.toDisc("WHITE-false");
        board.set(underAttackPos, discUnderAttack);

        var e = assertThrows(InvalidMoveException.class, ()->{CheckersRules.movePiece(board, pos, moves);});
        assertEquals("Single step is not allowed if the player has previously attacked", e.getMessage());
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "4,5", "4,3", "3,4", "5,4" })
    void movePieceSingleStepNotDiagonal(String arg){
        Disc disc = Disc.toDisc("BLACK-true");
        Position pos = new Position(4, 4);
        board.set(pos, disc);

        String[] coord = arg.split(",");
        List<Position> moves = new ArrayList<>();
        Position move = new Position(Integer.valueOf(coord[0]) , Integer.valueOf(coord[1]));
        moves.add(move);

        var e = assertThrows(InvalidMoveException.class, ()->{CheckersRules.movePiece(board, pos, moves);});
        assertEquals("Move is not diagonal", e.getMessage());
    }

    @Test
    void movePieceAttackNoPieceBlack(){
        Disc disc = Disc.toDisc("BLACK-true");
        Position pos = new Position(4, 4);
        board.set(pos, disc);

        List<Position> moves = new ArrayList<>();
        Position move = new Position(6, 2);
        moves.add(move);

        var e = assertThrows(InvalidMoveException.class, ()->{CheckersRules.movePiece(board, pos, moves);});
        assertEquals("There is no disc to attack", e.getMessage());
    }

    @Test
    void movePieceAttackSamePieceBlack(){
        Disc disc = Disc.toDisc("BLACK-true");
        Position pos = new Position(4, 4);
        board.set(pos, disc);
        board.set(5,3, disc);

        List<Position> moves = new ArrayList<>();
        Position move = new Position(6, 2);
        moves.add(move);

        var e = assertThrows(InvalidMoveException.class, ()->{CheckersRules.movePiece(board, pos, moves);});
        assertEquals("Move not allowed", e.getMessage());
    }

    @Test
    void movePieceAttackWrongDistanceBlack(){
        Disc disc = Disc.toDisc("BLACK-true");
        Position pos = new Position(4, 4);
        board.set(pos, disc);
        Disc disc2 = Disc.toDisc("WHITE-true");
        board.set(5,3, disc2);

        List<Position> moves = new ArrayList<>();
        Position move = new Position(7, 1);
        moves.add(move);

        var e = assertThrows(InvalidMoveException.class, ()->{CheckersRules.movePiece(board, pos, moves);});
        assertEquals("Move not allowed", e.getMessage());
    }

    @Test
    void movePieceSingleStepMultiple(){
        Disc disc = Disc.toDisc("BLACK-true");
        Position pos = new Position(4, 4);
        board.set(pos, disc);

        List<Position> moves = new ArrayList<>();
        Position move = new Position(5, 3);
        Position move2 = new Position(6, 2);
        moves.add(move);
        moves.add(move2);

        var e = assertThrows(InvalidMoveException.class, ()->{CheckersRules.movePiece(board, pos, moves);});
        assertEquals("Moves are not allowed", e.getMessage());
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "BLACK-false", "WHITE-false" })
    void movePieceDoubleAttackStepOnOccupiedTile(String arg) throws InvalidMoveException {
        // Arrange
        Disc disc = Disc.toDisc(arg);
        Position pos = new Position(2, disc.getColor().equals(ColorType.WHITE) ? 2 : 6);
        board.set(pos, disc);

        ColorType attackedColor = disc.getColor().equals(ColorType.WHITE) ? ColorType.BLACK : ColorType.WHITE;
        Disc underAttackDisc = new Disc(attackedColor, Boolean.FALSE);

        Position attackedDiscPos1 = new Position(3, 3);
        Position attackedDiscPos2 = new Position(3, 5);
        board.set(attackedDiscPos1, underAttackDisc);
        board.set(attackedDiscPos2, underAttackDisc);

        List<Position> moves = new ArrayList<>();
        Position move1 = new Position(4, 4);
        moves.add(move1);
        Position move2 = new Position(2, disc.getColor().equals(ColorType.WHITE) ? 6 : 2);
        moves.add(move2);

        board.set(move1, underAttackDisc);

        //Act Assert
        var e = assertThrows(InvalidMoveException.class, ()->{CheckersRules.movePiece(board, pos, moves);});
        assertEquals("Cannot move to an occupied tile", e.getMessage());
    }

    @Test
    void movePieceSingleStepOutsideBoard(){
        Disc disc = Disc.toDisc("BLACK-true");
        Position pos = new Position(0, 0);
        board.set(pos, disc);

        List<Position> moves = new ArrayList<>();
        Position move = new Position(-1, -1);
        moves.add(move);

        var e = assertThrows(InvalidMoveException.class, ()->{CheckersRules.movePiece(board, pos, moves);});
        assertEquals("Invalid position", e.getMessage());
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "1,7", "3,1", "5,7", "7,6" })
    void movePieceRamdomCoordinate(String arg){
        Disc disc = Disc.toDisc("BLACK-true");
        Position pos = new Position(4, 4);
        board.set(pos, disc);

        String[] coord = arg.split(",");
        List<Position> moves = new ArrayList<>();
        Position move = new Position(Integer.valueOf(coord[0]) , Integer.valueOf(coord[1]));
        moves.add(move);

       assertThrows(InvalidMoveException.class, ()->{CheckersRules.movePiece(board, pos, moves);});
    }

    @Test
    void movePieceWrongMoveOrder(){
        Disc disc = Disc.toDisc("BLACK-true");
        Position pos = new Position(2, 6);
        board.set(pos, disc);

        Disc underAttackDisc = Disc.toDisc("WHITE-true");

        Position attackedDiscPos1 = new Position(3, 3);
        Position attackedDiscPos2 = new Position(3, 5);
        board.set(attackedDiscPos1, underAttackDisc);
        board.set(attackedDiscPos2, underAttackDisc);

        List<Position> moves = new ArrayList<>();
        Position move1 = new Position(4, 4);
        Position move2 = new Position(2, 2);
        moves.add(move2);
        moves.add(move1);

        assertThrows(InvalidMoveException.class, ()->{CheckersRules.movePiece(board, pos, moves);});
    }

}
