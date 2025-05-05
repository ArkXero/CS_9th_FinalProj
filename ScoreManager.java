import java.io.*;
import java.util.ArrayList;

public class ScoreManager {
    private String filePath;

    public ScoreManager(String filePath) {
        this.filePath = filePath;
    }

    public void saveScore(String word, int guesses, long time) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(word + "," + guesses + "," + time);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving score: " + e.getMessage());
        }
    }

    public ArrayList<String> loadScores() {
        ArrayList<String> scores = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                scores.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error loading scores: " + e.getMessage());
        }
        return scores;
    }
}