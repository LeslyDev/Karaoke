package com.karaoke.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class Logic {
    private String firstLine;
    private String secondLine;
    private ArrayList<String> textOddLines = new ArrayList<String>();
    private ArrayList<String> textEvenLines = new ArrayList<String>();
    private int indexOfLine = 0;
    private int indexOfWord = 0;
    private int timeCounter = 0;
    private String currentMainLine = "first";
    private boolean isFirst = true;
    private boolean changeFirstString = false;
    private boolean changeSecondString = false;
    private String filename;

    Logic(String name) {
        filename = name;
        fillArrays();
    }

    private void fillArrays() {
        textOddLines = new ArrayList<String>();
        textEvenLines = new ArrayList<String>();
        File allStrings = new File("./songsText/" + filename + ".txt");
        Scanner textScanner = null;
        int co = 0;
        try {
            textScanner = new Scanner(allStrings);
        } catch (FileNotFoundException exc) {
            System.out.println("Файл с текстом не найден");
        }
        assert textScanner != null;
        while (textScanner.hasNextLine()) {
            if (co % 2 == 0) {
                textOddLines.add(textScanner.nextLine());
                co += 1;
            } else {
                textEvenLines.add(textScanner.nextLine());
                co += 1;
            }
        }
        firstLine = textOddLines.get(0);
        secondLine = textEvenLines.get(0);
    }

    private String getCurrentLine(String mainLine, int indexOfLine) {
        String line;
        if (mainLine.equals("first")) {
            line = textOddLines.get(indexOfLine);
        } else {
            line = textEvenLines.get(indexOfLine);
        }
        return line;
    }

    private String getFinalString(int indexOfWord, String[] words) {
        final String red = "<font color=\"red\">";
        final String close = "</font> ";
        final String white = "<font color=\"white\">";
        String[] partOne = new String[indexOfWord];
        String[] partTwo = new String[words.length - indexOfWord];
        System.arraycopy(words, 0, partOne, 0, indexOfWord);
        System.arraycopy(words, indexOfWord, partTwo, 0, words.length - indexOfWord);
        String partOneString = String.join(" ", partOne);
        String partTwoString = String.join(" ", partTwo);
        return "<html>" + red + partOneString + close + white + partTwoString + close + "</html>";
    }

    private void changeStrings() {
        if (changeFirstString) {
            firstLine = textOddLines.get(indexOfLine + 1);
            changeFirstString = false;
        } else if (changeSecondString) {
            secondLine = textEvenLines.get(indexOfLine);
            changeSecondString = false;
        }
    }

    private void actionAfterStringEnd(String[] words, String currentLine) {
        if (indexOfWord == (words.length) || currentLine.equals("\n")) {
            indexOfWord = 0;
            if (currentMainLine.equals("second"))  {
                indexOfLine += 1;
                currentMainLine = "first";
                changeSecondString = true;
            } else {
                currentMainLine = "second";
                changeFirstString = true;
            }
        }
    }

    String[] getString() {
        if (isFirst) {
            isFirst = false;
            return new String[0];
        }
        changeStrings();
        String currentLine = getCurrentLine(currentMainLine, indexOfLine);
        String[] words = currentLine.split(" ");
        final String firstLine = getFinalString(indexOfWord, words);
        String[] data = new String[2];
        if (currentMainLine.equals("first")) {
            this.firstLine = firstLine;
        }
        else {
            secondLine = firstLine;
        }
        actionAfterStringEnd(words, currentLine);
        indexOfWord += 1;
        data[0] = this.firstLine;
        data[1] = secondLine;
        return data;
    }

    int getDelay() {
        Scanner timingScanner = null;
        ArrayList<Integer> timeLines = new ArrayList<Integer>();
        File timings = new File("./songsTimings/" + filename + "_time.txt");
        try {
            timingScanner = new Scanner(timings);
        } catch (FileNotFoundException exc) {
            System.out.println("Файл с таймингами не найден");
        }
        assert timingScanner != null;
        while (timingScanner.hasNextLine()) {
            timeLines.add(Integer.valueOf(timingScanner.nextLine()));
        }
        int currentTime = timeLines.get(timeCounter);
        timeCounter += 1;
        return currentTime;
    }
}
