import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

// To Do:
//        (1) Add bruteforce functionality
//        (2) Add code to test how many ms it takes to bruteforce
//        Remaining two of these tasks being completed would result in Mission complete.

public class Main
{


    static ArrayList<Clause> formula = new ArrayList<>();

    static int numVars = 0; // How many variables are in the formula to be built. Remember: it takes 2^n attempts at worse to solve this
    static int numClauses = 0; // How many clauses are present in the formula.
    static int maxNumVars = 0; // The maximum number of variables that can be in a clause at any given time throughout the formula


    public static void main(String[] args)
    {
        // Test code
        File testFolderObject = new File(System.getProperty("user.dir") + "/Tests/");
        File[] testFileList = testFolderObject.listFiles();

        for (File file : testFileList)
        {
            loadFile(file);
        }
        System.out.println(testFileList.length + " files read and solved.");

        System.out.println(createVariations(3));
    }


    // This function reads a cnf file. The input can be described as a File object type that should be formatted in Dimacs format.
    public static void loadFile(File cnfFile)
    {
        formula.clear(); // Clears the list from the last one
        try
        {
            // Grab the file from the parameters
            Scanner scanner = new Scanner(cnfFile);
            while (scanner.hasNextLine())
            {

                String line = scanner.nextLine();
                // Determines if current line is a comment - denoted by 'c'. If it is comment, skip the line
                if (line.startsWith("c"))
                {
                    continue;
                }

                //if line starts with a p, read the format
                // p cnf [v][c]
                //        v - num of variables
                //        c - num of clauses

                if (line.startsWith("p"))
                {
                    String[] args = line.split(" ");
                    maxNumVars = Integer.parseInt(args[2]); //variables
                    numClauses = Integer.parseInt(args[3]); //clauses
                    System.out.println("Maximum Variables of Variables: " + maxNumVars + ", Number of Clauses: " + numClauses);

                    for (int i = 0; i < numClauses; i++ )
                    {
                        //initialize a new clause to store the variables
                        Clause newClause = new Clause(numVars, maxNumVars, new LinkedList<Integer>());
                        if(scanner.hasNextInt())
                        {
                            int nextInt = scanner.nextInt();
                            while (!(nextInt == 0))
                            {
                                newClause.addVariable(nextInt);
                                nextInt = scanner.nextInt();
                            }
                            // Add clause to formula
                            formula.add(newClause);
                        }
                    }
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        // print the list for the user to see
        System.out.println("Printing: " + cnfFile.getPath());
        System.out.println(formula.toString());
        System.out.println("Satisfiable: " + null); //replace null with satisfiability checker function call to see if formula is satisfiable also include system time milis
        System.out.println("--------------");
        System.out.print(createVariations(maxNumVars));

    }


    public static ArrayList<boolean[]> createVariations(int inputVars) {
        int numVars = inputVars; // the number of variables in each clause
        ArrayList<boolean[]> variations = new ArrayList<>();

        for (int i = 0; i < Math.pow(2, numVars); i++) {
            boolean[] varValues = new boolean[numVars];
            int counter = i;
            for (int j = numVars - 1; j >= 0; j--) {
                int temp = counter / (int) Math.pow(2, j);
                int bit = temp % 2;
                varValues[j] = (bit == 1);
            }
            variations.add(varValues);
            System.out.println(Arrays.toString(varValues));
        }
        return variations;
    }



}

==========================




        import java.io.File;
        import java.io.FileNotFoundException;
        import java.util.*;

        import java.util.concurrent.*;
// To Do:
//        (1) Add bruteforce functionality
//        (2) Add code to test how many ms it takes to bruteforce
//        Remaining two of these tasks being completed would result in Mission complete.

public class Main
{


    static ArrayList<Clause> formula = new ArrayList<>();

    static int numVars = 0; // How many variables are in the formula to be built. Remember: it takes 2^n attempts at worse to solve this
    static int numClauses = 0; // How many clauses are present in the formula.
    static int maxNumVars = 0; // The maximum number of variables that can be in a clause at any given time throughout the formula


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Test code
        File testFolderObject = new File(System.getProperty("user.dir") + "/Tests/");
        File[] testFileList = testFolderObject.listFiles();

        for (File file : testFileList)
        {
            loadFile(file);
        }
        System.out.println(testFileList.length + " files read and solved.");

    }


    // This function reads a cnf file. The input can be described as a File object type that should be formatted in Dimacs format.
    public static void loadFile(File cnfFile) throws InterruptedException, ExecutionException {
        formula.clear(); // Clears the list from the last one
        try
        {
            // Grab the file from the parameters
            Scanner scanner = new Scanner(cnfFile);
            while (scanner.hasNextLine())
            {

                String line = scanner.nextLine();
                // Determines if current line is a comment - denoted by 'c'. If it is comment, skip the line
                if (line.startsWith("c"))
                {
                    continue;
                }

                //if line starts with a p, read the format
                // p cnf [v][c]
                //        v - num of variables
                //        c - num of clauses

                if (line.startsWith("p"))
                {
                    String[] args = line.split(" ");
                    maxNumVars = Integer.parseInt(args[2]); //variables
                    numClauses = Integer.parseInt(args[3]); //clauses

                    for (int i = 0; i < numClauses; i++ )
                    {
                        //initialize a new clause to store the variables
                        Clause newClause = new Clause(numVars, maxNumVars, new LinkedList<Integer>());
                        if(scanner.hasNextInt())
                        {
                            int nextInt = scanner.nextInt();
                            while (!(nextInt == 0))
                            {
                                newClause.addVariable(nextInt);
                                nextInt = scanner.nextInt();
                            }
                            // Add clause to formula
                            formula.add(newClause);
                        }
                    }
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        // print the list for the user to see
        System.out.println("Maximum Variables of Variables: " + maxNumVars + ", Number of Clauses: " + numClauses);
        System.out.println("Amount of variations: " + createVariations(maxNumVars).size());
        System.out.println("Satisfiable: " + isSatisfiable(formula,createVariations(maxNumVars))); //replace null with satisfiability checker function call to see if formula is satisfiable also include system time milis
        System.out.println("--------------");


    }



    public static boolean isSatisfiable(ArrayList<Clause[]> formula, boolean[] values) {
        for (Clause[] clauseArray : formula) {
            boolean clauseSatisfied = false;
            for (Clause clause : clauseArray) {
                List<Integer> variables = clause.getVariables();
                boolean literalSatisfied = false;
                for (int literal : variables) {
                    int var = Math.abs(literal) - 1; // convert to zero-based index
                    boolean value = values[var];
                    if ((literal > 0 && value) || (literal < 0 && !value)) {
                        literalSatisfied = true;
                        break;
                    }
                }
                if (literalSatisfied) {
                    clauseSatisfied = true;
                    break;
                }
            }
            if (!clauseSatisfied) {
                return false; // at least one clause is not satisfied
            }
        }
        return true; // all clauses are satisfied
    }







    public static ArrayList<boolean[]> createVariations(int inputVars)
    {
        int numVars = inputVars;
        ArrayList<boolean[]> variations = new ArrayList<>();

        //initialize first variation
        boolean[] vars = new boolean[numVars];

        // execute this 2^(inputVars) times
        for (int i = 0; i < Math.pow(2, numVars); i++)
        {
            //add current set of variations to the variation list
            variations.add(vars);
            // get the next variation and set it to vars
            vars = getNextVariation(vars);
        }

        return variations;
    }


    public static boolean[] getNextVariation(boolean[] vars)
    {
        boolean[] nextVariation = Arrays.copyOf(vars, vars.length);
        int index = nextVariation.length - 1;

        // Find the rightmost false value
        while (index >= 0 && nextVariation[index])
        {
            index--;
        }

        if (index >= 0)
        {
            // Flip the rightmost false value to true
            nextVariation[index] = true;

            // Change all true values to the right of the rightmost false to false
            for (int i = index + 1; i < nextVariation.length; i++)
            {
                nextVariation[i] = false;
            }
        }

        return nextVariation;
    }



}

