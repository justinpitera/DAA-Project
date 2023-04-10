package com.daa.sudoku;

import org.sat4j.*;

import java.io.*;
import java.util.ArrayList;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.InstanceReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.IProblem;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;

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
    public static void main(String[] args) throws IOException, ContradictionException, TimeoutException, ParseFormatException {

        generateCNFFiles();

    }

    public static void generateCNFFiles() throws IOException, ContradictionException, TimeoutException, ParseFormatException {
        File puzzlesFolderObject = new File(System.getProperty("user.dir") + "/Sudoku Solver/puzzles");
        File[] puzzlesFileList = puzzlesFolderObject.listFiles();



        for (File file : puzzlesFileList)
        {
            Puzzle newPuzzle = new Puzzle(file.getPath());
            newPuzzle.generateCNFFile();

        }




        File formulasFolderObject = new File(System.getProperty("user.dir") + "/Sudoku Solver/formulas");
        File[] formulasFileList = formulasFolderObject.listFiles();
        for (File file : formulasFileList)
        {
            SolvePuzzle(file.getPath());
        }


    }
    public static void SolvePuzzle(String puzzlePath) throws TimeoutException, IOException, ContradictionException, ParseFormatException {
        ISolver solver = SolverFactory.newDefault();
        ModelIterator mi = new ModelIterator(solver);
        solver.setTimeout(3600); // 1 hour timeout
        InstanceReader reader = new InstanceReader(mi);


        // filename is given on the command line
        try {
            boolean unsat = true;
            IProblem problem = reader.parseInstance(puzzlePath);
            while (problem.isSatisfiable()) {
                unsat = false;
                int [] model = problem.model();
                // do something with each model
            }
            if(unsat)
            {
                System.out.println("Puzzle unsatisfiable");
            } else {
                System.out.println("Puzzle satisfied");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ContradictionException e) {
            System.out.println("Unsatisfiable (trivial)!");
        } catch (TimeoutException e) {
            System.out.println("Timeout, sorry!");
        }
    }
    public static String decode(int code, int inputSize) {
        int k = code % inputSize;
        int j = (code / inputSize) % inputSize;
        int i = code / (inputSize * inputSize);
        return String.format("(%d, %d, %d)", i , j, k + 1);
    }
}
