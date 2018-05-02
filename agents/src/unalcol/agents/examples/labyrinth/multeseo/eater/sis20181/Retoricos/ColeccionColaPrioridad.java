package unalcol.agents.examples.labyrinth.multeseo.eater.SIS20181.Retoricos;

import java.util.PriorityQueue;

public class ColeccionColaPrioridad implements Coleccion{
    
    PriorityQueue<Arco> c;
     
    public ColeccionColaPrioridad() {
        this.c = new PriorityQueue<>(10, (Arco a, Arco b) -> {
            if (a.getCostoAcumulado()+ a.getCostoEstimado() < b.getCostoAcumulado()+ b.getCostoEstimado() ) return -1;
            if (a.getCostoAcumulado()+ a.getCostoEstimado() > b.getCostoAcumulado()+ b.getCostoEstimado() ) return 1;
            return 0;
        });
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
        return c.peek();
    }

    @Override
    public void remover() {
        c.poll();
    }
    
    @Override
    public int tam() {
        return c.size();
    }
}
