package unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.DataStructures;

import unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.Arco;
import unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.Heuristica;

public class AStarBusqueda<T> extends PriorityBusqueda<T> {

    private Heuristica<T> heuristica;

    public AStarBusqueda(int capacidad, Heuristica<T> heuristica){
        super(capacidad);
        this.heuristica = heuristica;
    }

    @Override
    protected double evaluar(Arco<T> arco){
        return arco.costoTotal() + heuristica.estimar(arco.getEstado());
    }

}
