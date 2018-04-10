package unalcol.agents.examples.labyrinth.multeseo.eater.isi20181.thunderCats.busqueda;


import java.util.Vector;

public interface Sucesor<T> {
    Vector<Arco<T>> obtener(Arco<T> estado);
}
