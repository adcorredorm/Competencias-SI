package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats;

import java.util.HashSet;
import java.util.Vector;

public class LabyrinthStack extends Vector<Coordinate> {

    /*
     * Creo que esta clase ya no sirve para nada, pero la dejo aca por si acaso :P
     */

    private HashSet<Coordinate> set;
    private Coordinate aux;

    public LabyrinthStack(){
        this.set = new HashSet<>();
    }

    public Coordinate pop(){
        aux = this.get(size()-1);
        this.remove(size()-1);
        return aux;
    }

    @Override
    public synchronized boolean add(Coordinate obj) {
        if(!set.contains(obj)){
            set.add(obj);
            return super.add(obj);
        }
        return false;
    }

    public Coordinate peek(){
        return this.get(size()-1);
    }


}
