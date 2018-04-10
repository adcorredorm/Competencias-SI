package unalcol.agents.examples.labyrinth.multeseo.eater.isi20181.thunderCats;

import unalcol.agents.examples.labyrinth.multeseo.eater.isi20181.thunderCats.busqueda.Goal;

import java.util.Collection;
import java.util.Vector;

public class MultiTarget implements Goal<Coordinate> {

    Collection<Coordinate> set;

    public MultiTarget(Collection<Coordinate> target){
        this.set = target;
    }

    public MultiTarget(Coordinate target){
        this.set = new Vector<>(1);
        this.set.add(target);
    }

    @Override
    public boolean isGoal(Coordinate estado) {
        return set.contains(estado);
    }
}
