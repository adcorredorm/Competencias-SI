package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.DataStructures;

import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.Arco;
import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.Heuristica;

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