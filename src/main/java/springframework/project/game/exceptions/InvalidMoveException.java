package springframework.project.game.exceptions;

public class InvalidMoveException extends Exception {
    public InvalidMoveException(String errorMessage){
        super(errorMessage);
    }
}
