import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger; // Only being used to show to the user how many generations it could take to solve not actually generating the variations themselves.
import java.util.*;
import java.util.concurrent.*;

public class Main
{
    private static ArrayList<Clause> formula = new ArrayList<Clause>();
    private static int numberOfVariables = 0; // The number of variables that can be in a clause at any given time throughout the formula
    private static int numberOfClauses = 0; // How many clauses are present in the formula.

    public static void main(String[] args) throws InterruptedException, ExecutionException
    {
        Scanner scanner = new Scanner(System.in);

        int userChoice = 0;

        while (userChoice != 4)
        {
            File formulasFolderObject = new File(System.getProperty("user.dir") + "/formulas/");
            File[] formulasFileList = formulasFolderObject.listFiles();

            System.out.println("Welcome to the SAT CNF Solver:");
            System.out.println("1. Bruteforce formula");
            System.out.println("2. Bruteforce all formulas");
            System.out.println("3. List all files in formulas directory");
            System.out.println("4. Exit");
            System.out.print("Make a selection (1-4) : ");


            userChoice = scanner.nextInt();

            switch (userChoice) {
                case 1:
                    System.out.print("Enter file name: ");
                    String fileName = scanner.next();
                    loadFile(new File(System.getProperty("user.dir") + "/formulas/" + fileName));
                    break;
                case 2:
                    System.out.println("Attempting to solve all formulas in formulas directory...");
                    for (File file : formulasFileList)
                    {
                        loadFile(file);
                    }
                    System.out.println(formulasFileList.length + " files read and solved.");
                    break;
                case 3:
                    System.out.println("All files in formulas directory:");
                    for (File file : formulasFileList)
                    {
                        System.out.println(file.getName() + " ... " + file.getPath());
                    }
                    break;
                case 4:
                    System.out.println("Closing...");
                    break;
                default:
                    System.out.println("Invalid choice, try again.");
                    break;
            }


        }



    }


    public static void loadFile(File cnfFile) throws InterruptedException, ExecutionException
    {
        formula.clear(); // Clears the list from the last one
        try
        {
            // Grab the file from the parameters
            Scanner scanner = new Scanner(cnfFile);
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                if (line.startsWith("c"))
                {
                    continue;
                }

                if (line.startsWith("p"))
                {
                    /*
                    The trim() method removes any leading or trailing whitespace characters from the input string.
                     The split("\\s+") method splits the string into an array of substrings based on one or more whitespace characters.
                     */
                    String[] args = line.trim().split("\\s+");
                    numberOfVariables = Integer.parseInt(args[2]); //variables
                    numberOfClauses = Integer.parseInt(args[3]); //clauses
                    for (int i = 0; i < numberOfClauses; i++)
                    {
                        // Base case no more integers to be read
                        if (!(scanner.hasNextInt()))
                        {
                            break;
                        }

                        // Initialize new clause
                        Clause newClause = new Clause(new LinkedList<Integer>());

                        // Initialize current variable being read
                        int currentVariable = scanner.nextInt();

                        while (currentVariable != 0)
                        {
                            newClause.addVariable(currentVariable);
                            currentVariable = scanner.nextInt();
                        }
                        formula.add(newClause);
                    }
                }
            }
            // print the list for the user to see
            System.out.println("Working on: " + cnfFile.getPath());
            // Keeping this to display to the user how many variations they can possibly have
            BigInteger maxVariations = BigInteger.valueOf(2).pow(numberOfVariables);
            System.out.println("Number of Variables: " + numberOfVariables + ", Number of Clauses: " + formula.size() + ", Maxiumum Variations: " + maxVariations.toString());
            System.out.println("Attempting to bruteforce...");
            bruteForce();
            System.out.println("------------------------------------------------------------------------------------------------------------");
        } catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }


    public static void bruteForce ()
    {
        long startTime = System.currentTimeMillis();
        boolean formulaSatisfied = false;

        //initialize first variation, all false. but as this goes through the for loop, the final variation should be all true
        int[] currentVariation = new int[numberOfVariables];

        //Test the first variation against the list of clauses
        formulaSatisfied = isSatisfied(currentVariation);

        while (!isFinalVariation(currentVariation) && !formulaSatisfied)
        {
            currentVariation = getNextVariation(currentVariation);
            formulaSatisfied = isSatisfied(currentVariation);
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        if (formulaSatisfied)
        {
            System.out.println("Satisfiable: Yes");
            // Print out variation that satisfies the formula
            System.out.print("Solution: ");
            System.out.print("[ ");
            for (int variable : currentVariation)
            {
                System.out.print(variable + " ");
            }
            System.out.print("]\n");
        } else
        {
            System.out.println("Satisfiable: No");
        }
        formulaSatisfied = false;
        System.out.println("Elapsed time: " + elapsedTime + " milliseconds");
    }

    private static boolean isSatisfied(int[] currentVariation)
    {
        // Initialize as unsatisfied
        boolean formulaSatisfied = false;

        // Go through each clause in the formula
        for (Clause currentClause : formula)
        {
            boolean clauseSatisfied = false;
            // Go through each literal in the clause
            for (int literal : currentClause.getVariables())
            {
                int state = 0; // initialize as off
                if (literal > 0)
                {
                    state = 1; //on
                }

                // Check if the literal is satisfied by the current variation
                if (currentVariation[Math.abs(literal) - 1] == state)
                {
                    clauseSatisfied = true;
                    break;
                }
            }

            // If the clause is not satisfied, try the next variation
            if (!clauseSatisfied)
            {
                formulaSatisfied = false;
                break;
            }
            else
            {
                formulaSatisfied = true;
            }
        }
        return formulaSatisfied;
    }


    // Determinies if all of the ints in the current variation = 1.
    // if it is not all 1's - > create next variation
    public static boolean isFinalVariation(int[] currentVariation)
    {
        for (int i = 0; i < currentVariation.length; i++)
        {
            if (currentVariation[i] != 1)
            {
                return false;
            }
        }
        return true;
    }

    public static int[] getNextVariation(int[] vars)
    {
        int[] nextVariation = Arrays.copyOf(vars, vars.length);
        int index = nextVariation.length - 1;
        // Find the rightmost 0 value
        while (index >= 0 && nextVariation[index] == 1)
        {
            index--;
        }
        if (index >= 0)
        {
            // Flip the rightmost 0 value to 1
            nextVariation[index] = 1;
            // Change all 1 values to the right of the rightmost 0 to 0
            for (int i = index + 1; i < nextVariation.length; i++)
            {
                nextVariation[i] = 0;
            }
        }
        return nextVariation;
    }


}




