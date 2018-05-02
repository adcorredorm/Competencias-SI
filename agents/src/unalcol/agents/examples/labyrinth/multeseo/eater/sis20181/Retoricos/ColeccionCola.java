
package unalcol.agents.examples.labyrinth.multeseo.eater.SIS20181.Retoricos;

import java.util.Vector;


public class ColeccionCola implements Coleccion{

    Vector<Arco> c;

    public ColeccionCola() {
        this.c = new Vector<Arco>();
    }
    
    @Override
    public void adicionar(Arco a) {
        c.add(a);
    }

    @Override
    public boolean esvacia() {
        return c.isEmpty();
    }

    @Override
    public Arco obtener() {
        if(!this.esvacia()){
            return c.get(0);
        }
        return null;
    }

    @Override
    public void remover() {
        c.remove(0);
    }

    @Override
    public int tam() {
        return c.size();
    }
    
    
    
}
