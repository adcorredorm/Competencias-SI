package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.DataStructures;

import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.Arco;

public abstract class ListaBusqueda<T> implements ColeccionBusqueda<T> {
    protected class Nodo<T>{
        protected Arco<T> arco;
        protected Nodo<T> next;
        protected double valor;
    }

    protected Nodo<T> head = null;
    protected Nodo<T> tail = null;
    protected int size = 0;

    public boolean esvacia(){ return head == null; }

    public Arco<T> obtener(){
        if( head != null ) return head.arco;
        return null;
    }

    public void remover(){
        if( head != null ){
            this.size--;
            head = head.next;
            if(head == null) tail = null;
        }
    }
}
