import java.util.ArrayList;

public class WordleGame {
    private String targetWord;
    private int maxGuesses;
    private int currentGuess;
    private ArrayList<String> guessHistory;
    private boolean gameOver;

    public WordleGame(String targetWord, int maxGuesses) {
        this.targetWord = targetWord;
        this.maxGuesses = maxGuesses;
        this.currentGuess = 0;
        this.guessHistory = new ArrayList<>();
        this.gameOver = false;
    }

    public LetterResult[] checkGuess(String guess) {
        LetterResult[] results = new LetterResult[5];
        // Create a frequency map of letters in the target word
        int[] targetLetterCount = new int[26]; // for a-z
        
        for (char c : targetWord.toCharArray()) {
            targetLetterCount[c - 'a']++;
        }
        
        // First pass: Mark correct letters (green)
        for (int i = 0; i < 5; i++) {
            char guessLetter = guess.charAt(i);
            if (guessLetter == targetWord.charAt(i)) {
                results[i] = new LetterResult(guessLetter, LetterState.CORRECT);
                // Reduce the count of available letters
                targetLetterCount[guessLetter - 'a']--;
            }
        }
        
        // Second pass: Mark present but misplaced letters (yellow) or absent letters
        for (int i = 0; i < 5; i++) {
            if (results[i] != null) continue;
            
            char guessLetter = guess.charAt(i);
            if (targetLetterCount[guessLetter - 'a'] > 0) {
                results[i] = new LetterResult(guessLetter, LetterState.PRESENT);
                // Reduce the count of available letters
                targetLetterCount[guessLetter - 'a']--;
            } else {
                results[i] = new LetterResult(guessLetter, LetterState.ABSENT);
            }
        }

        guessHistory.add(guess);
        currentGuess++;
        
        if (guess.equals(targetWord) || currentGuess >= maxGuesses) {
            gameOver = true;
        }
        
        return results;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean hasWon(String guess) {
        return guess.equals(targetWord);
    }

    public boolean hasGuessesLeft() {
        return currentGuess < maxGuesses;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public ArrayList<String> getGuessHistory() {
        return guessHistory;
    }
}