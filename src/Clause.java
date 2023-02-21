import com.sun.jdi.IntegerValue;

import java.util.LinkedList;
import java.util.List;

public class Clause
{

    private int numVars; //stores how many variables are in actually in the clause
    private int numClauses;
    private LinkedList<Integer> variables = new LinkedList<Integer>();

    public Clause(int numVars, int numClauses, LinkedList<Integer> variables)
    {
        this.numVars = numVars;
        this.numClauses = numClauses;
        this.variables = variables;
    }

    public void addVariable(int var)
    {
        variables.add(var);
    }

    @Override
    public String toString() {
        return variables.toString();
    }

    public List<Integer> getVariables() {
        return variables;
    }

    // Get literals                   x:  1  2  3  4  5
    // [4, -3, 2] with 5 vars should be: [0, 1, 0, 1, 0] = curreennt var
    // In terms of the bruteforcer, if you find a variation in 2^5 where
    // currentVariation = [0, 1, 0, 1, 0] then the clause is satisfied.
    // Now do this for all of the clauses in the formula
    // if all of the clauses are satisfied, the formula is satisfied


    public int[] getLiterals() {
        // Initialize literal with all false values up to numVars number of variables
        int[] literals = new int[numVars];
        // Go through each numVar
        for (int i = 0; i < numVars; i++)
        {
            literals[i] = 0;
            //Set current var to literal value in literal
            for (int var : variables)
            {
                int literalValue;
                if (var > 0)
                {
                    literalValue = 1;
                } else
                {
                    literalValue = 0;
                }
                literals[Math.abs(var) - 1] = literalValue;
            }

        }

        // Return literals

        return literals;
    }


}
