package unalcol.agents.examples.labyrinth.multeseo.eater.isi20181.thunderCats.busqueda.DataStructures;

import unalcol.agents.examples.labyrinth.multeseo.eater.isi20181.thunderCats.busqueda.Arco;

public interface ColeccionBusqueda<T> {
    public void adicionar(Arco<T> a);
    public boolean esvacia();
    public Arco<T> obtener();
    public void remover();
    public int size();
}
