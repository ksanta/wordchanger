package lol.karl.wordchanger;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

@Slf4j
public class WordChanger {

    // A node in a linked list. Manual implementation so I can easily chain different lists together.
    private static class Node {
        private String word;
        private Node previous;
        private int numWords;

        public Node(String word, Node previous) {
            this.word = word;
            this.previous = previous;
            if (previous == null) {
                numWords = 1;
            } else {
                numWords = previous.numWords + 1;
            }
        }

        public List<String> toList() {
            LinkedList<String> list = new LinkedList<>();

            Node currentNode = this;
            while (currentNode != null) {
                list.addFirst(currentNode.word);
                currentNode = currentNode.previous;
            }

            return list;
        }

        public Set<String> toSet() {
            Set<String> set = new HashSet<>();

            Node currentNode = this;
            while (currentNode != null) {
                set.add(currentNode.word);
                currentNode = currentNode.previous;
            }

            return set;
        }

        public int length() {
            return numWords;
        }
    }

    private Dictionary dictionary;

    public WordChanger() {
        try {
            this.dictionary = new Dictionary();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds a path from startWord to endWord if one exists
     *
     * @return a list of words from start to end with all words in between
     */
    public List<String> changeWord(String startWord, String endWord) {
        Deque<Node> stack = new ArrayDeque<>();

        stack.push(new Node(startWord, null));

        while (!stack.isEmpty()) {
            Node node = stack.pop();
            String word = node.word;

            if (word.equals(endWord)) {
                return node.toList();
            }

            List<String> newPermutations = getAllNewPermutations(node);

            if (!newPermutations.isEmpty()) {
                newPermutations.sort((word1, word2) -> {
                    int count1 = countMatchingCharacters(word1, endWord);
                    int count2 = countMatchingCharacters(word2, endWord);

                    // Highest counts move to end of list - and at top of stack
                    return Integer.compare(count1, count2);
                });

                // Create new linked lists and add each to the stack
                for (String newWord : newPermutations) {
                    Node newNode = new Node(newWord, node);
                    stack.push(newNode);
                }
            }
        }

        return new ArrayList<>();
    }

    private int countMatchingCharacters(String testWord, String endWord) {
        int countOfMatchingChars = 0;
        for (int i = 0; i < testWord.length(); i++) {
            if (testWord.charAt(i) == endWord.charAt(i)) {
                countOfMatchingChars++;
            }
        }
        return countOfMatchingChars;
    }

    private List<String> getAllNewPermutations(Node node) {
        List<String> newWords = new ArrayList<>();

        String word = node.word;
        final int wordLength = word.length();
        Set<String> alreadyUsedWords = node.toSet();
        Set<String> dictionaryWordsOfLength = dictionary.getWordsByLength(wordLength);

        for (int wordIndex = 0; wordIndex < wordLength; wordIndex++) {
            for (char newChar = 'A'; newChar <= 'Z'; newChar++) {
                String newWord = replaceChar(word, newChar, wordIndex);
                if (dictionaryWordsOfLength.contains(newWord) && !alreadyUsedWords.contains(newWord)) {
                    newWords.add(newWord);
                }
            }
        }

        return newWords;
    }

    private String replaceChar(String str, char ch, int index) {
        StringBuilder myString = new StringBuilder(str);
        myString.setCharAt(index, ch);
        return myString.toString();
    }
}
