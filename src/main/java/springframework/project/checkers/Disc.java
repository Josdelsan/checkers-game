package springframework.project.checkers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Disc {
    private ColorType color;
    private Boolean promoted;

    private static final String ATTRIBUTE_SEPARATOR = "-";

    public Disc(ColorType color, Boolean promoted) {
        this.color = color;
        this.promoted = promoted;
    }

    @Override
    public String toString() {
        return this.color.toString() + ATTRIBUTE_SEPARATOR + this.promoted.toString();
    }

    public static Disc toDisc(String discString) {
        String[] attributes = discString.split(ATTRIBUTE_SEPARATOR);
        ColorType color = ColorType.valueOf(attributes[0]);
        Boolean promoted = Boolean.valueOf(attributes[1]);
        return new Disc(color,promoted);
    }
}
