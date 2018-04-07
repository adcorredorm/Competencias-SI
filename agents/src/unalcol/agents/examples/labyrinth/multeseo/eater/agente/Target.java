package unalcol.agents.examples.labyrinth.multeseo.eater.agente;

import unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.Goal;

import java.util.Vector;

public class Target implements Goal<Coordinate> {

    private Vector<Coordinate> target;

    Target(Coordinate target){
        this.target = new Vector<>(1);
        this.target.add(target);
    }

    @Override
    public boolean isGoal(Coordinate estado) {
        return new MultiTarget(target).isGoal(estado);
    }
}
