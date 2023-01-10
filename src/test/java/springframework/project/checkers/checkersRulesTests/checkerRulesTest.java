package springframework.project.checkers.checkersRulesTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import springframework.project.checkers.CheckersRules;
import springframework.project.game.Board;

@SpringBootTest
public class checkerRulesTest {

    @Test
    void initBoardTest() {
        Board board = CheckersRules.initBoard(8);
        String boardString = board.toString();
        String exampleBoard = "empty,WHITE-false,empty,empty,empty,BLACK-false,empty,BLACK-false;WHITE-false,empty,WHITE-false,empty,empty,empty,BLACK-false,empty;empty,WHITE-false,empty,empty,empty,BLACK-false,empty,BLACK-false;WHITE-false,empty,WHITE-false,empty,empty,empty,BLACK-false,empty;empty,WHITE-false,empty,empty,empty,BLACK-false,empty,BLACK-false;WHITE-false,empty,WHITE-false,empty,empty,empty,BLACK-false,empty;empty,WHITE-false,empty,empty,empty,BLACK-false,empty,BLACK-false;WHITE-false,empty,WHITE-false,empty,empty,empty,BLACK-false,empty";
    
        assertEquals(exampleBoard, boardString);
    }

}
