public class LetterResult {
    public char letter;
    public LetterState state;

    public LetterResult(char letter, LetterState state) {
        this.letter = letter;
        this.state = state;
    }
}

enum LetterState {
    CORRECT,    // Green - right letter, right spot
    PRESENT,    // Yellow - right letter, wrong spot
    ABSENT      // Gray - letter not in word
}