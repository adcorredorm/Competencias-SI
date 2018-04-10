package unalcol.agents.examples.labyrinth.multeseo.eater.isi20181.thunderCats.busqueda;

import unalcol.agents.examples.labyrinth.multeseo.eater.isi20181.thunderCats.busqueda.DataStructures.ColeccionBusqueda;

import java.util.Vector;

public abstract class Busqueda<T> {
    protected Sucesor<T> sucesor;
    protected Goal<T> objetivo;

    public Busqueda(Sucesor<T> sucesor, Goal<T> objetivo){
        this.sucesor = sucesor;
        this.objetivo = objetivo;
    }

    protected abstract ColeccionBusqueda<T> coleccion();

    public Arco<T> aplicar( T inicial ){
        ColeccionBusqueda<T> c = coleccion();
        c.adicionar(new Arco<T>(inicial, new Vector<Accion>(),0.0));
        Arco<T> actual = c.obtener();

        Vector<Arco<T>> h;

        while(!c.esvacia() && !objetivo.isGoal(actual.getEstado())){
            c.remover();
            h = sucesor.obtener(actual);
            for( Arco<T> a:h ){
                c.adicionar(a);
            }
            actual = c.obtener();
        }
        if( !c.esvacia() ) return c.obtener();
        return null;
    }
}
