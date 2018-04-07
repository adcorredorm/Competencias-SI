package unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.DataStructures;

import unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.Arco;

public interface ColeccionBusqueda<T> {
    public void adicionar(Arco<T> a);
    public boolean esvacia();
    public Arco<T> obtener();
    public void remover();
    public int size();
}
