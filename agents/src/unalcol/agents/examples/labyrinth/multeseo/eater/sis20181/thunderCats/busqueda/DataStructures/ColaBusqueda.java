package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.DataStructures;

import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.Arco;

public class ColaBusqueda<T> extends ListaBusqueda<T>{
    public void adicionar( Arco<T> a ){
        this.size++;
        Nodo<T> aux = new Nodo<T>();
        aux.arco = a;
        if(esvacia()){
            head = tail = aux;
        }else{
            tail.next = aux;
            tail = aux;
        }

    }

    @Override
    public int size() {
        return this.size;
    }
}
