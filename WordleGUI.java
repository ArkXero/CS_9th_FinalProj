import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
    private JLabel statusLabel; // New label to show game status

    public WordleGUI() {
        wordBank = new WordBank();
        wordBank.loadWords("words.txt");
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
        frame.setSize(400, 550);  // Slightly taller to accommodate logo
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setPreferredSize(new Dimension(60, 60));  // Increased height for logo + space

        try {
            ImageIcon originalIcon = new ImageIcon("logo.png");
            if (originalIcon.getIconWidth() <= 0) {
                JLabel logoLabel = new JLabel("JWordle", SwingConstants.CENTER);
                logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
                logoLabel.setForeground(new Color(60, 118, 61));  // Dark green
                logoPanel.add(logoLabel);
            } else {
                Image originalImage = originalIcon.getImage();
                Image scaledImage = originalImage.getScaledInstance(100, 40, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                
                JLabel logoLabel = new JLabel(scaledIcon);
                logoPanel.add(logoLabel);
            }
        } catch (Exception e) {
            JLabel logoLabel = new JLabel("JWordle", SwingConstants.CENTER);
            logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
            logoPanel.add(logoLabel);
        }
        
        topPanel.add(logoPanel, BorderLayout.NORTH);
        
        // Add status label below the logo
        statusLabel = new JLabel("Guess the word!", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        topPanel.add(statusLabel, BorderLayout.CENTER);
        
        frame.add(topPanel, BorderLayout.NORTH);

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
            statusLabel.setText("Game over! Please restart to play again.");
            return;
        }
        String guess = inputBox.getText().trim().toLowerCase();
        if (guess.length() != wordLength) {
            statusLabel.setText("Guess must be " + wordLength + " letters.");
            return;
        }
        if (!wordBank.isValidWord(guess)) {
            statusLabel.setText("Not a valid word.");
            return;
        }
        
        LetterResult[] results = game.checkGuess(guess);
        updateGrid(results);
        inputBox.setText("");
        
        if (game.hasWon(guess)) {
            statusLabel.setText("Congratulations! You guessed the word!");
            statusLabel.setForeground(new Color(0, 128, 0)); // Dark green
            scoreManager.saveScore(guess, game.getGuessHistory().size(), 0);
            // Disable input after winning
            inputBox.setEnabled(false);
            submitButton.setEnabled(false);
        } else if (!game.hasGuessesLeft()) {
            statusLabel.setText("Game over! The word was: " + game.getTargetWord());
            statusLabel.setForeground(Color.RED);
            // Disable input after losing
            inputBox.setEnabled(false);
            submitButton.setEnabled(false);
        } else {
            // Reset status for ongoing game
            statusLabel.setText("Guess the word!");
            statusLabel.setForeground(Color.BLACK);
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
// DEBUG PRINT GET METHOD
    // This method is testing  only, delete it when game is complete
    public WordleGame getGame() {
        return this.game;
    }
}
