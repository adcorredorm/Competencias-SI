package unalcol.agents.examples.labyrinth.multeseo.eater.SIS20181.Retoricos;

import java.util.TreeMap;
import java.util.Vector;

public abstract class Busqueda {

    int energiaMaxima = 0;
    int energiaPorRecurso = 10;
    int energiaAvanzar = 1;
    int objetivo;
    TreeMap<Integer, TreeMap<Integer, Nodo>> objetivos;

    public abstract Coleccion coleccion();

    public Vector<Integer> aplicar(Nodo inicial, TreeMap<Integer, TreeMap<Integer, Nodo>> _objetivos, int direccion, int seguridad, int _objetivo, int _energiaMaxima,int _energiaAvanzar) {
        this.objetivo = _objetivo;
        this.objetivos = _objetivos;
        this.energiaMaxima = _energiaMaxima;
        this.energiaAvanzar = _energiaAvanzar;
        Coleccion c = coleccion();
        c.adicionar(new Arco(inicial, null, -1, 0, direccion, seguridad, energiaMaxima,energiaAvanzar));
        Arco actual = c.obtener();
        while (!c.esvacia() && !objetivo(actual.getNodo())) {
            c.remover();
            Vector<Arco> h = sucesor(actual);
            for (Arco e : h) {
                c.adicionar(e);
            }
            actual = c.obtener();
        }
        if (!c.esvacia()) {
            //System.out.println("OBJ X " + actual.getNodo().coordX + "  Y " + actual.getNodo().coordY); // debug
            return camino(actual, direccion);
        }
        return null;
    }

    public boolean objetivo(Nodo nodo) {
        return nodo.tipo == objetivo;
    }

    public Vector<Arco> sucesor(Arco actual) {
        Vector<Arco> h = new Vector<>();
        for (int i = 0; i < 4; i++) {
            if (actual.getNodo().nodos[i] != null && !actual.visitados.contains(actual.getNodo().nodos[i])) {
                int peso = 1 + (i - actual.direccion + 4) % 4;
                //int peso = 1 + (i - actual.direccion + 4)%4 +   100*( (actual.seguridad - energiaAvanzar) > 0 || objetivo==Nodo.RECURSO ? 0:1); 
                //System.out.println("peso: " + peso);
                h.add(new Arco(actual.getNodo().nodos[i], actual, peso, this.calcularHeuristica(actual.getNodo().nodos[i]), i, 0, energiaMaxima,energiaAvanzar));
            }
        }
        return h;
    }

    public int calcularPeso(Nodo nodo) {
        return 1;
    }

    public int calcularHeuristica(Nodo nodo) {
        Integer abjX = objetivos.ceilingKey(nodo.coordX);
        Integer arrX = objetivos.floorKey(nodo.coordX);
        int D1 = Integer.MAX_VALUE,D2 = Integer.MAX_VALUE;
        
        if (abjX != null && objetivos.containsKey(abjX)) {
            Integer a = objetivos.get(abjX).ceilingKey(nodo.coordY);
            Integer b = objetivos.get(abjX).floorKey(nodo.coordY);
            
            int y = 0;
            int x = abjX;
            if (a != null && b != null) {
                y = Math.abs(nodo.coordY - a) < Math.abs(nodo.coordY - b) ? a : b;
            } else if (a != null || b != null) {
                y = a != null ? a : b;
            }
            //System.out.println("HEURISTICA" + (Math.abs(x - nodo.coordX) + Math.abs(y - nodo.coordY)));
            D1 = Math.abs(x - nodo.coordX) + Math.abs(y - nodo.coordY);
        }       
        
        if (arrX != null && objetivos.containsKey(arrX)) {
            Integer a = objetivos.get(arrX).ceilingKey(nodo.coordY);
            Integer b = objetivos.get(arrX).floorKey(nodo.coordY);
            
            int y = 0;
            int x = arrX;
            if (a != null && b != null) {
                y = (Math.abs(nodo.coordY - a) < Math.abs(nodo.coordY - b)) ? a : b;
            } else if (a != null || b != null) {
                y = a != null ? a : b;
            }
            //System.out.println("HEURISTICA" + (Math.abs(x - nodo.coordX) + Math.abs(y - nodo.coordY)));
            D2 = Math.abs(x - nodo.coordX) + Math.abs(y - nodo.coordY);
        }        

        return Math.min(D1, D2);
    }

    private Vector<Integer> camino(Arco actual, int direccion) {
        Arco arco = actual;
        Vector<Integer> h = new Vector<>();
        //System.out.println("DIRECCION " );// debug
        while (arco.getAntecesor() != null) {
            //System.out.print(arco.direccion +" , ");// debug
            h.add(arco.direccion);
            arco = arco.getAntecesor();
        }
        for (int i = h.size() - 1; i >= 0; i--) {
            int aux = h.get(i);
            h.set(i, (h.get(i) - direccion + 4) % 4);
            direccion = aux;
        }
        //System.out.println("");// debug
        return h;
    }

}
