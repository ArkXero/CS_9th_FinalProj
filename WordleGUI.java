import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class WordleGUI {
    private JFrame frame;
    private JPanel gridPanel;
    private JTextField inputBox;
    private JButton submitButton;
    private WordleGame game;
    private WordBank wordBank;
    private ScoreManager scoreManager;
    private int wordLength = 5;
    private int maxGuesses = 6;
    private JLabel[][] gridLabels;

    public WordleGUI() {
        wordBank = new WordBank();
        wordBank.loadWords("/Users/ronitsingh/Desktop/CS_9th_FinalProj/words.txt");
        if (!wordBank.isValidWord("apple")) {
            JOptionPane.showMessageDialog(null, "Failed to load words.txt or file is empty!");
        }
        String target = wordBank.getRandomWord();
        game = new WordleGame(target, maxGuesses);
        scoreManager = new ScoreManager("scores.txt");
        buildUI();
    }

    public void buildUI() {
        frame = new JFrame("Wordle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        gridPanel = new JPanel(new GridLayout(maxGuesses, wordLength));
        gridLabels = new JLabel[maxGuesses][wordLength];
        for (int i = 0; i < maxGuesses; i++) {
            for (int j = 0; j < wordLength; j++) {
                gridLabels[i][j] = new JLabel("", SwingConstants.CENTER);
                gridLabels[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                gridLabels[i][j].setFont(new Font("Arial", Font.BOLD, 24));
                gridPanel.add(gridLabels[i][j]);
            }
        }
        frame.add(gridPanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputBox = new JTextField(10);
        submitButton = new JButton("Submit");
        inputPanel.add(inputBox);
        inputPanel.add(submitButton);
        frame.add(inputPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });
        inputBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });

        frame.setVisible(true);
    }

    public void handleSubmit() {
        System.out.println("handleSubmit called"); // Debug print
        if (game.isGameOver()) {
            JOptionPane.showMessageDialog(frame, "Game over! Please restart to play again.");
            return;
        }
        String guess = inputBox.getText().trim().toLowerCase();
        if (guess.length() != wordLength) {
            JOptionPane.showMessageDialog(frame, "Guess must be " + wordLength + " letters.");
            return;
        }
        if (!wordBank.isValidWord(guess)) {
            JOptionPane.showMessageDialog(frame, "Not a valid word.");
            return;
        }
        
        LetterResult[] results = game.checkGuess(guess);
        updateGrid(results);
        inputBox.setText("");
        
        if (game.hasWon(guess)) {
            JOptionPane.showMessageDialog(frame, "Congratulations! You guessed the word.");
            scoreManager.saveScore(guess, game.getGuessHistory().size(), 0);
        } else if (!game.hasGuessesLeft()) {
            JOptionPane.showMessageDialog(frame, "Out of guesses! The word was: " + game.getTargetWord());
        }
    }
    
    // Fix: Update updateGrid method to use getGuessHistory
    public void updateGrid(LetterResult[] results) {
        int row = game.getGuessHistory().size() - 1;
        for (int i = 0; i < results.length; i++) {
            gridLabels[row][i].setText(String.valueOf(results[i].letter).toUpperCase());
            switch (results[i].state) {
                case CORRECT:
                    gridLabels[row][i].setBackground(Color.GREEN);
                    gridLabels[row][i].setOpaque(true);
                    break;
                case PRESENT:
                    gridLabels[row][i].setBackground(Color.YELLOW);
                    gridLabels[row][i].setOpaque(true);
                    break;
                case ABSENT:
                    gridLabels[row][i].setBackground(Color.LIGHT_GRAY);
                    gridLabels[row][i].setOpaque(true);
                    break;
            }
        }
    }
}