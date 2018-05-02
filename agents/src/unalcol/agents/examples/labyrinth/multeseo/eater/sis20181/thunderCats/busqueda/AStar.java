package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda;

import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.DataStructures.AStarBusqueda;
import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.DataStructures.ColeccionBusqueda;

public class AStar<T> extends Busqueda<T>{

    private Heuristica<T> heuristica;

    public AStar(Sucesor<T> _sucesor, Goal<T> _objetivo, Heuristica<T> heuristica) {
        super(_sucesor, _objetivo);
        this.heuristica = heuristica;
    }

    @Override
    public ColeccionBusqueda<T> coleccion(){
        return new AStarBusqueda<T>(100, heuristica);
    }

}
