package com.quasicontrol.wordle.data;

import com.google.common.io.Resources;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class WordListProvider {
    private static String FILE_NAME = "short-word-list.txt";
    private List<String> words;
    private boolean imported = false;
    public WordListProvider() {
    }

    public List<String> get() {
        if (!imported) {
            runImport();
        }
        return words;
    }

    private void runImport() {
        try {
            words = Resources.readLines(
                Resources.getResource("short-word-list.txt"),
                StandardCharsets.US_ASCII
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
