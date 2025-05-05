import java.io.*;
import java.util.*;

public class WordBank {
    private ArrayList<String> words = new ArrayList<>();

    public void loadWords(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String word;
            while ((word = reader.readLine()) != null) {
                words.add(word.trim().toLowerCase());
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Could not load words from file");
        }
    }

    public String getRandomWord() {
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    public boolean isValidWord(String word) {
        return words.contains(word.toLowerCase());
    }
}