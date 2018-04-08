package unalcol.agents.examples.labyrinth.multeseo.eater.isi20181.busqueda;

import unalcol.agents.examples.labyrinth.multeseo.eater.isi20181.busqueda.DataStructures.ColaBusqueda;
import unalcol.agents.examples.labyrinth.multeseo.eater.isi20181.busqueda.DataStructures.ColeccionBusqueda;

public class BFS<T> extends Busqueda<T> {
    public BFS(Sucesor<T> _sucesor, Goal<T> _objetivo, int max_prof ){
        super( _sucesor, _objetivo, max_prof);
    }

    public ColeccionBusqueda<T> coleccion(){
        return new ColaBusqueda<T>();
    }
}