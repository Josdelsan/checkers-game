package springframework.project.checkers.checkersRulesTests;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

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
public class checkMovesTest {

    String basicBoard = "empty,empty,empty,empty,empty,empty,empty,empty;"+
                        "empty,empty,empty,empty,empty,empty,empty,empty;"+
                        "empty,empty,empty,empty,empty,empty,empty,empty;"+
                        "empty,empty,empty,empty,empty,empty,empty,empty;"+
                        "empty,empty,empty,empty,empty,empty,empty,empty;"+
                        "empty,empty,empty,empty,empty,empty,empty,empty;"+
                        "empty,empty,empty,empty,empty,empty,empty,empty;"+
                        "empty,empty,empty,empty,empty,empty,empty,empty";
    Board board;

    @BeforeEach
    void configure() {
        board = Board.toBoard(basicBoard);
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "BLACK-false", "WHITE-false" })
    void checkMovesSingleStep(String arg) throws InvalidMoveException {
        //Arrange
        Disc disc = Disc.toDisc(arg);

        Integer actualCol = 4;
        Integer actualRow = 4;
        Integer direction = disc.getColor().equals(ColorType.WHITE) ? 1 : -1;

        Integer expectedMoves = 2;
        Position expectedPos1 = new Position(3, actualRow + direction);//From {4 4} expecting {3 3}Black {3 5}White
        Position expectedPos2 = new Position(5, actualRow + direction);//From {4 4} expecting {5 3}Black {5 5}White
        Position actualPos = new Position(actualCol, actualRow);
        board.set(actualRow, actualCol, disc);

        //Act
        Set<Position> positions = CheckersRules.checkMoves(board, actualPos);

        //Assert
        Boolean pos1Found = positions.stream().filter(p -> p.equals(expectedPos1)).findAny().isPresent();
        Boolean pos2Found = positions.stream().filter(p -> p.equals(expectedPos2)).findAny().isPresent();

        assertTrue(pos1Found, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to " + expectedPos1.toString());
        assertTrue(pos2Found, disc.toString()+" disc should be able to move to from "+actualPos.toString()+" to " + expectedPos2.toString());
        assertTrue(positions.size() == expectedMoves, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to" + expectedMoves + " different tiles");
    }


    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "BLACK-false", "WHITE-false" })
    void checkMovesNoStep(String arg) throws InvalidMoveException {
        //Arrange
        Disc disc = Disc.toDisc(arg);

        Integer actualCol = 4;
        Integer actualRow = 4;
        Integer direction = disc.getColor().equals(ColorType.WHITE) ? 1 : -1;

        Integer expectedMoves = 0;
        Position actualPos = new Position(actualCol, actualRow);

        board.set(3, actualRow + direction, disc);//Blocking disc
        board.set(5, actualRow + direction, disc);//Blocking disc
        board.set(actualRow, actualCol, disc);

        //Act
        Set<Position> positions = CheckersRules.checkMoves(board, actualPos);

        //Assert
        assertTrue(positions.size() == expectedMoves, disc.toString()+" disc should not be able to move from "+actualPos.toString());
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "BLACK-false", "WHITE-false" })
    void checkMovesAttack(String arg) throws InvalidMoveException {
        //Arrange
        Disc disc = Disc.toDisc(arg);

        Integer actualCol = 4;
        Integer actualRow = 4;
        Integer direction = disc.getColor().equals(ColorType.WHITE) ? 1 : -1;

        Integer expectedMoves = 2;
        Position expectedPos1 = new Position(2, actualRow + direction*2);//From {4 4} expecting {2 2}Black {2 6}White
        Position expectedPos2 = new Position(6, actualRow + direction*2);//From {4 4} expecting {6 2}Black {6 6}White
        Position actualPos = new Position(actualCol, actualRow);

        ColorType color = disc.getColor().equals(ColorType.WHITE) ? ColorType.BLACK:ColorType.WHITE;
        Disc underAtkDisc = new Disc(color,Boolean.FALSE);
        board.set(3, actualRow + direction, underAtkDisc);//Under attack disc
        board.set(5, actualRow + direction, underAtkDisc);//Under attack disc
        board.set(actualRow, actualCol, disc);

        //Act
        Set<Position> positions = CheckersRules.checkMoves(board, actualPos);

        //Assert
        Boolean pos1Found = positions.stream().filter(p -> p.equals(expectedPos1)).findAny().isPresent();
        Boolean pos2Found = positions.stream().filter(p -> p.equals(expectedPos2)).findAny().isPresent();

        assertTrue(pos1Found, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to " + expectedPos1.toString());
        assertTrue(pos2Found, disc.toString()+" disc should be able to move to from "+actualPos.toString()+" to " + expectedPos2.toString());
        assertTrue(positions.size() == expectedMoves, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to" + expectedMoves + " different tiles");
    }


    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "BLACK-false", "WHITE-false" })
    void checkMovesAttackDouble(String arg) throws InvalidMoveException {
        //Arrange
        Disc disc = Disc.toDisc(arg);

        //White starts from {2 2}
        //black from {2 6}
        Integer actualCol = 2;
        Integer actualRow = disc.getColor().equals(ColorType.WHITE) ? 2 : 6;
        Integer direction = disc.getColor().equals(ColorType.WHITE) ? 1 : -1;

        //Expecting {2 6}, {1 3}, {4 4}, {6 6} for white pieces
        //Expecting {2 2}, {1 5}, {4 4}, {6 2} for black pieces
        Integer expectedMoves = 4;
        Position expectedPos1 = new Position(4, 4);
        Position expectedPos2 = new Position(6, disc.getColor().equals(ColorType.WHITE) ? 6 : 2);
        Position expectedPos3 = new Position(2, disc.getColor().equals(ColorType.WHITE) ? 6 : 2);
        Position actualPos = new Position(actualCol, actualRow);

        ColorType color = disc.getColor().equals(ColorType.WHITE) ? ColorType.BLACK:ColorType.WHITE;
        Disc underAtkDisc = new Disc(color,Boolean.FALSE);
        board.set(3, actualRow + direction*3, underAtkDisc);//Under attack disc
        board.set(3, actualRow + direction, underAtkDisc);//Under attack disc
        board.set(5, actualRow + direction*3, underAtkDisc);//Under attack disc
        board.set(actualCol, actualRow, disc);

        //Act
        Set<Position> positions = CheckersRules.checkMoves(board, actualPos);

        //Assert
        Boolean pos1Found = positions.stream().filter(p -> p.equals(expectedPos1)).findAny().isPresent();
        Boolean pos2Found = positions.stream().filter(p -> p.equals(expectedPos2)).findAny().isPresent();
        Boolean pos3Found = positions.stream().filter(p -> p.equals(expectedPos3)).findAny().isPresent();

        assertTrue(pos1Found, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to " + expectedPos1.toString());
        assertTrue(pos2Found, disc.toString()+" disc should be able to move to from "+actualPos.toString()+" to " + expectedPos2.toString());
        assertTrue(pos3Found, disc.toString()+" disc should be able to move to from "+actualPos.toString()+" to " + expectedPos3.toString());
        assertTrue(positions.size() == expectedMoves, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to" + expectedMoves + " different tiles");
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "BLACK-false", "WHITE-false" })
    void checkMovesAttackTriple(String arg) throws InvalidMoveException {
        //Arrange
        Disc disc = Disc.toDisc(arg);

        //White starts from {7 0}
        //black from {1 6}
        Position posWhite = new Position(7,0);
        Position posBlack = new Position(1,6);

        //Expecting {3 4}, {5 2}, {1 6} for white pieces
        //Expecting {3 4}, {5 2}, {7 0} for black pieces
        Integer expectedMoves = 3;
        Position expectedPos1 = new Position(3, 4);
        Position expectedPos2 = new Position(5, 2);
        Position actualPos = disc.getColor().equals(ColorType.WHITE) ? posWhite : posBlack;
        Position expectedPosLast = disc.getColor().equals(ColorType.WHITE) ? posBlack : posWhite;

        ColorType color = disc.getColor().equals(ColorType.WHITE) ? ColorType.BLACK:ColorType.WHITE;
        Disc underAtkDisc = new Disc(color,Boolean.FALSE);
        board.set(0, 5, underAtkDisc);//Under attack disc
        board.set(2, 5, underAtkDisc);//Under attack disc
        board.set(4, 3, underAtkDisc);//Under attack disc
        board.set(6, 1, underAtkDisc);//Under attack disc
        board.set(actualPos, disc);

        //Act
        Set<Position> positions = CheckersRules.checkMoves(board, actualPos);

        //Assert
        Boolean pos1Found = positions.stream().filter(p -> p.equals(expectedPos1)).findAny().isPresent();
        Boolean pos2Found = positions.stream().filter(p -> p.equals(expectedPos2)).findAny().isPresent();
        Boolean pos3Found = positions.stream().filter(p -> p.equals(expectedPosLast)).findAny().isPresent();

        assertTrue(pos1Found, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to " + expectedPos1.toString());
        assertTrue(pos2Found, disc.toString()+" disc should be able to move to from "+actualPos.toString()+" to " + expectedPos2.toString());
        assertTrue(pos3Found, disc.toString()+" disc should be able to move to from "+actualPos.toString()+" to " + expectedPosLast.toString());
        assertTrue(positions.size() == expectedMoves, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to" + expectedMoves + " different tiles");
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "BLACK-false", "WHITE-false" })
    void checkMovesOutOfIndex(String arg) throws InvalidMoveException {
        //Arrange
        Disc disc = Disc.toDisc(arg);

        //Put disc in opposite corners
        Integer maxListInt = board.getDimension()-1;
        Integer actualCol = disc.getColor().equals(ColorType.WHITE) ? maxListInt : 0;
        Integer actualRow = disc.getColor().equals(ColorType.WHITE) ? maxListInt : 0;

        Integer expectedMoves = 1;
        Position actualPos = new Position(actualCol, actualRow);
        board.set(actualPos, disc);

        //Act
        Set<Position> positions = CheckersRules.checkMoves(board, actualPos);

        //Assert
        //Should only move to 1 place of the 4 diagonals
        assertTrue(positions.size() == expectedMoves, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to" + expectedMoves + " different tiles");
    }


    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "BLACK-true", "WHITE-true" })
    void checkMovesSingleStepPromoted(String arg) throws InvalidMoveException {
        //Arrange
        Disc disc = Disc.toDisc(arg);

        Integer actualCol = 4;
        Integer actualRow = 4;

        Integer expectedMoves = 4;
        Position expectedPos1 = new Position(3, 3);
        Position expectedPos2 = new Position(3, 5);
        Position expectedPos3 = new Position(5, 3);
        Position expectedPos4 = new Position(5, 5);
        Position actualPos = new Position(actualCol, actualRow);

        board.set(actualPos, disc);

        //Act
        Set<Position> positions = CheckersRules.checkMoves(board, actualPos);

        //Assert
        Boolean pos1Found = positions.stream().filter(p -> p.equals(expectedPos1)).findAny().isPresent();
        Boolean pos2Found = positions.stream().filter(p -> p.equals(expectedPos2)).findAny().isPresent();
        Boolean pos3Found = positions.stream().filter(p -> p.equals(expectedPos3)).findAny().isPresent();
        Boolean pos4Found = positions.stream().filter(p -> p.equals(expectedPos4)).findAny().isPresent();

        assertTrue(pos1Found, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to " + expectedPos1.toString());
        assertTrue(pos2Found, disc.toString()+" disc should be able to move to from "+actualPos.toString()+" to " + expectedPos2.toString());
        assertTrue(pos3Found, disc.toString()+" disc should be able to move to from "+actualPos.toString()+" to " + expectedPos3.toString());
        assertTrue(pos4Found, disc.toString()+" disc should be able to move to from "+actualPos.toString()+" to " + expectedPos4.toString());
        assertTrue(positions.size() == expectedMoves, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to" + expectedMoves + " different tiles");
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = { "BLACK-true", "WHITE-true" })
    void checkMovesMultipleAttacksPromoted(String arg) throws InvalidMoveException {
        //Arrange
        Disc disc = Disc.toDisc(arg);

        Integer actualCol = 7;
        Integer actualRow = 4;

        Integer expectedMoves = 4;
        Position expectedPos1 = new Position(3, 4);
        Position expectedPos2 = new Position(5, 2);
        Position expectedPos3 = new Position(5, 6);
        Position expectedPos4 = new Position(6, 5);
        Position actualPos = new Position(actualCol, actualRow);

        ColorType color = disc.getColor().equals(ColorType.WHITE) ? ColorType.BLACK:ColorType.WHITE;
        Disc underAtkDisc = new Disc(color,Boolean.FALSE);
        board.set(6, 3, underAtkDisc);//Under attack disc
        board.set(4, 3, underAtkDisc);//Under attack disc
        board.set(4, 5, underAtkDisc);//Under attack disc
        board.set(actualPos, disc);

        //Act
        Set<Position> positions = CheckersRules.checkMoves(board, actualPos);

        //Assert
        Boolean pos1Found = positions.stream().filter(p -> p.equals(expectedPos1)).findAny().isPresent();
        Boolean pos2Found = positions.stream().filter(p -> p.equals(expectedPos2)).findAny().isPresent();
        Boolean pos3Found = positions.stream().filter(p -> p.equals(expectedPos3)).findAny().isPresent();
        Boolean pos4Found = positions.stream().filter(p -> p.equals(expectedPos4)).findAny().isPresent();

        assertTrue(pos1Found, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to " + expectedPos1.toString());
        assertTrue(pos2Found, disc.toString()+" disc should be able to move to from "+actualPos.toString()+" to " + expectedPos2.toString());
        assertTrue(pos3Found, disc.toString()+" disc should be able to move to from "+actualPos.toString()+" to " + expectedPos3.toString());
        assertTrue(pos4Found, disc.toString()+" disc should be able to move to from "+actualPos.toString()+" to " + expectedPos4.toString());
        assertTrue(positions.size() == expectedMoves, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to" + expectedMoves + " different tiles");
    }

    @Test
    void checkMovesPromotingBlack() throws InvalidMoveException{
        //Arrange
        Disc disc = Disc.toDisc("BLACK-false");

        Integer actualCol = 7;
        Integer actualRow = 2;

        Integer expectedMoves = 2;
        Position expectedPos1 = new Position(5, 0);
        Position expectedPos2 = new Position(3, 2);
        Position actualPos = new Position(actualCol, actualRow);

        Disc underAtkDisc = Disc.toDisc("WHITE-false");
        board.set(6, 1, underAtkDisc);//Under attack disc
        board.set(4, 1, underAtkDisc);//Under attack disc
        board.set(actualCol, actualRow, disc);

        //Act
        Set<Position> positions = CheckersRules.checkMoves(board, actualPos);

        //Assert
        Boolean pos1Found = positions.stream().filter(p -> p.equals(expectedPos1)).findAny().isPresent();
        Boolean pos2Found = positions.stream().filter(p -> p.equals(expectedPos2)).findAny().isPresent();

        assertTrue(pos1Found, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to " + expectedPos1.toString());
        assertTrue(pos2Found, disc.toString()+" disc should be able to move to from "+actualPos.toString()+" to " + expectedPos2.toString());
        assertTrue(positions.size() == expectedMoves, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to" + expectedMoves + " different tiles");
    }

    @Test
    void checkMovesPromotingWhite() throws InvalidMoveException{
        //Arrange
        Disc disc = Disc.toDisc("WHITE-false");

        Integer actualCol = 7;
        Integer actualRow = 5;

        Integer expectedMoves = 2;
        Position expectedPos1 = new Position(5, 7);
        Position expectedPos2 = new Position(3, 5);
        Position actualPos = new Position(actualCol, actualRow);

        Disc underAtkDisc = Disc.toDisc("BLACK-false");
        board.set(6, 6, underAtkDisc);//Under attack disc
        board.set(4, 6, underAtkDisc);//Under attack disc
        board.set(actualCol, actualRow, disc);

        //Act
        Set<Position> positions = CheckersRules.checkMoves(board, actualPos);

        //Assert
        Boolean pos1Found = positions.stream().filter(p -> p.equals(expectedPos1)).findAny().isPresent();
        Boolean pos2Found = positions.stream().filter(p -> p.equals(expectedPos2)).findAny().isPresent();

        assertTrue(pos1Found, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to " + expectedPos1.toString());
        assertTrue(pos2Found, disc.toString()+" disc should be able to move to from "+actualPos.toString()+" to " + expectedPos2.toString());
        assertTrue(positions.size() == expectedMoves, disc.toString()+" disc should be able to move from "+actualPos.toString()+" to" + expectedMoves + " different tiles");
    }

    @Test
    void checkMovesFail(){
        Position ramdomPos = new Position(4,4);
        assertThrows(InvalidMoveException.class, () -> {CheckersRules.checkMoves(board, ramdomPos);});
    }
}
