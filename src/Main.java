import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger; // Only being used to show to the user how many generations it could take to solve not actually generating the variations themselves.
import java.util.*;
import java.util.concurrent.*;

public class Main
{
    private static ArrayList<Clause> formula = new ArrayList<Clause>();
    private static int numClauses = 0; // How many clauses are present in the formula.
    private static int numVars = 0; // The number of variables that can be in a clause at any given time throughout the formula

    boolean isSatisfied = false;

    public static void main(String[] args) throws InterruptedException, ExecutionException
    {
        // Test code
        File testFolderObject = new File(System.getProperty("user.dir") + "/Tests/");

        File[] testFileList = testFolderObject.listFiles();

        for (File file : testFileList)
        {
            loadFile(file);
        }

        System.out.println(testFileList.length + " files read and solved.");

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
                    String[] args = line.split(" ");
                    numVars = Integer.parseInt(args[2]); //variables
                    numClauses = Integer.parseInt(args[3]); //clauses
                    for (int i = 0; i < numClauses; i++)
                    {
                        // Base casen no more integers to be read
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
            BigInteger maxVariations = BigInteger.valueOf(2).pow(numVars);
            System.out.println("Number of Variables: " + numVars + ", Number of Clauses: " + formula.size() + ", Maxiumum Variations: " + maxVariations.toString());
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
        int[] currentVariation = new int[numVars];

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
                continue;
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




