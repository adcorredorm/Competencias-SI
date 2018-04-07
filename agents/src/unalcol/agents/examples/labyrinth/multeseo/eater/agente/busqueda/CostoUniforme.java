package unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda;

import unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.DataStructures.ColeccionBusqueda;
import unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.DataStructures.PriorityBusqueda;

public class CostoUniforme<T> extends Busqueda<T>{

    public CostoUniforme(Sucesor<T> _sucesor, Goal<T> _objetivo, int max_prof ){
        super( _sucesor, _objetivo, max_prof);
    }

    public ColeccionBusqueda<T> coleccion(){
        return new PriorityBusqueda<T>(max_prof);
    }
}
