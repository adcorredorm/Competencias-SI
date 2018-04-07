package unalcol.agents.search.Presentacion;

import unalcol.agents.Action;
import unalcol.agents.search.ActionCost;

public class Costo<T> implements ActionCost<T>{
    @Override
    public double evaluate(T state, Action action) {
        return 1.0;
    }
}
