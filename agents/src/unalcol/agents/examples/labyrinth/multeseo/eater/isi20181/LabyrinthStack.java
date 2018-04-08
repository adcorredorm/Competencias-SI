package unalcol.agents.examples.labyrinth.multeseo.eater.isi20181;

import unalcol.agents.examples.labyrinth.multeseo.eater.isi20181.busqueda.Heuristica;
import java.util.Vector;

public class LabyrinthStack extends Vector<Coordinate> {

    /*
     * Creo que esta clase ya no sirve para nada, pero la dejo aca por si acaso :P
     */

    protected Heuristica<Coordinate> heuristica;
    private Coordinate aux;

    public Coordinate pop(){
        aux = this.get(size()-1);
        this.remove(size()-1);
        return aux;
    }

    public Coordinate peek(){
        return this.get(size()-1);
    }

    public void reorder(Heuristica<Coordinate> heuristica){
        this.heuristica = heuristica;
        for(int i = 0; i < size(); i++){
            System.out.print(heuristica.estimar(get(i)) + " ");
        }
        System.out.println();

        quicksort(0, size() - 1);

        for(int i = 0; i < size(); i++){
            System.out.print(heuristica.estimar(get(i)) + " ");
        }
        System.out.println('\n');
    }

    private void quicksort(int start, int end){
        int i = start, j = end;
        double pivot = heuristica.estimar(this.get(start + (end - start)/2));
        while(i <= j){
            while(heuristica.estimar(this.get(i)) > pivot) i++;
            while(heuristica.estimar(this.get(j)) < pivot) j--;
            if(i <= j){
                aux = this.get(i);
                this.set(i, this.get(j));
                this.set(j, aux);
                i++;
                j--;
            }
        }

        if(start < j) quicksort(start, j);
        if(i < end) quicksort(i, end);

    }


}
