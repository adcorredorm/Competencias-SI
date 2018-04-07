package unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.DataStructures;

import unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.Arco;
import unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.Heuristica;

public class PilaBusqueda<T> extends ListaBusqueda<T> {

    public void adicionar(Arco<T> a) {
        Nodo<T> aux = new Nodo<T>();
        aux.arco = a;
        if (esvacia()) {
            head = tail = aux;
        } else {
            aux.next = head;
            head = aux;
        }
        this.size++;
    }

    @Override
    public int size() {
        return this.size;
    }

}
