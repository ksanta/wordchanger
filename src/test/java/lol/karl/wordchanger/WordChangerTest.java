package lol.karl.wordchanger;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class WordChangerTest {

    private WordChanger wordChanger = new WordChanger();

    @Test
    public void changeFourLetterWords() {
        // Magazine puzzle gave hints to work from POOR to RICH
        runAndLog("POOR", "HUMP");
        runAndLog("HUMP", "LATE");
        runAndLog("LATE", "RICH");
        // This program found a shorter path
        runAndLog("POOR", "RICH");
    }

    @Test
    public void changeFiveLetterWords() {
        runAndLog("STARS", "MOONS");
        runAndLog("BORED", "PARTY");
        runAndLog("GREAT", "AGAIN");
    }

    private void runAndLog(String word1, String word2) {
        log.info("{} -> {} = {}", word1, word2, wordChanger.changeWord(word1, word2));
    }
}
