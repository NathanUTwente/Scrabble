package Scrabble.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * A Scrabble Word Checker, which stores all words in Memory.
 */
public class InMemoryScrabbleWordChecker implements ScrabbleWordChecker {

    /**
     * A static variable to store all words in memory
     * The key contains a UPPERCASE {@link String} of the word and the value is a {@link ScrabbleWordChecker.WordResponse} for that word.
     */
    private static final Map<String, ScrabbleWordChecker.WordResponse> words = new HashMap<>();

    /**
     * Process the words file when the class is loaded, and fill a static Map
     */
    static {
        try {
            InputStream resourceStream = InMemoryScrabbleWordChecker.class.getResourceAsStream("/Scrabble/Utils/collins_scrabble_words_2019.txt");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] splitLine = line.split("\t");
                    if (splitLine.length == 2) {
                        words.put(splitLine[0], new ScrabbleWordChecker.WordResponse(splitLine[0], splitLine[1]));
                    }
                }
            }
        }
        catch(IOException exception){
            System.out.println("Could not load scrabble words: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    @Override
    public ScrabbleWordChecker.WordResponse isValidWord(String word) {
        if(word == null || word.isBlank()) return null;
        return words.get(word.toUpperCase());
    }
}
