package com.daa.sudoku;

import org.sat4j.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Sudoku Solver
 * Justin Pitera
 * Start Date: 10.3.23
 * Finalized Date: ?
 * Design and Analysis of Algorithms - Spring 2023
 * -----------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * sudoku.Main.java
 * The sudoku.Main class serves as the driver class for the Sudoku Solver
 * It allows the user to load a Sudoku puzzles and solves the Sudoku puzzles.
 */
public class Main
{

    ArrayList<Puzzle> PuzzleList = new ArrayList<Puzzle>();
    /**
     * X Each cell must contain a number between 1 and 9.
     *   Each row must contain each number between 1 and 9 exactly once.
     *   Each column must contain each number between 1 and 9 exactly once.
     *   Each 3x3 box must contain each number between 1 and 9 exactly once.
     * X Given clues are satisfied

     * @param args
     */
    public static void main(String[] args) throws IOException {

        generateCNFFiles();
    }


    public static void generateCNFFiles() throws IOException {
        File formulasFolderObject = new File(System.getProperty("user.dir") + "/Sudoku Solver/puzzles");
        File[] formulasFileList = formulasFolderObject.listFiles();


        for (File file : formulasFileList)
        {
            Puzzle newPuzzle = new Puzzle(file.getPath());
            newPuzzle.generateCNFFile();
        }

    }
    public void SolvePuzzles()
    {

    }
}
