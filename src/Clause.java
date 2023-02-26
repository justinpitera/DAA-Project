import java.util.LinkedList;
import java.util.List;

public class Clause
{


    private LinkedList<Integer> variables = new LinkedList<Integer>();

    public Clause(LinkedList<Integer> variables)
    {

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

    public int get(int i)
    {
        return variables.get(i);
    }

}
