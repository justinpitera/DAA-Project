import com.sun.jdi.IntegerValue;

import java.util.LinkedList;
import java.util.List;

public class Clause
{

    int numVars; //stores how many variables are in actually in the clause
    int maxVars; //stores how many possible variables could be in the clause based on the cnf file format
    LinkedList<Integer> variables = new LinkedList<Integer>();

    public Clause(int numVars, int maxVars, LinkedList<Integer> variables)
    {
        this.numVars = numVars;
        this.numVars = maxVars;
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
}
