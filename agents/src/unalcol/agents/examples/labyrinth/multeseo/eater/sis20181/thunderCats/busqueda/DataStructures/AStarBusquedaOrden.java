package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.DataStructures;

import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.Arco;
import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.Heuristica;

import java.util.ArrayList;

public class AStarBusquedaOrden<T> implements ColeccionBusqueda<T> {

    private Heuristica<T> heuristica;
    private ArrayList<Arco<T>> lista;

    //Igual creo que no se va a a usar.... pero por si acaso aca queda xD

    public AStarBusquedaOrden(int capacidad, Heuristica<T> h){
        heuristica = h;
        lista = new ArrayList<>(capacidad);
    }


    @Override
    public void adicionar(Arco<T> a) {
        if(esvacia()){
            lista.add(a);
        }else{
            int index = 0;
            double valor = heuristica.estimar(a.getEstado());
            while(index < lista.size() && valor < heuristica.estimar(lista.get(index).getEstado())){ //TODO: Esto es muy costoso, optimizar
                index++;
            }
            lista.add(index, a);
        }
    }

    @Override
    public boolean esvacia() {
        return lista.isEmpty();
    }

    @Override
    public Arco<T> obtener() {
        return lista.get(lista.size()-1);
    }

    @Override
    public void remover() {
        lista.remove(lista.size()-1);
    }

    @Override
    public int size() {
        return lista.size();
    }
}
