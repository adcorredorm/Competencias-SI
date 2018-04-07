package unalcol.agents.search.Presentacion;

import unalcol.agents.Action;
import unalcol.agents.search.classic.ClassicSearchNode;
import unalcol.types.collection.vector.*;

public class NodoBusqueda<NodoPuzzle> extends ClassicSearchNode<NodoPuzzle>{

    NodoBusqueda(Vector<Action> path, double cost) {
        super(path, cost);
    }
}
