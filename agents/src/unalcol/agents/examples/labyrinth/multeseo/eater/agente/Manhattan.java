package unalcol.agents.examples.labyrinth.multeseo.eater.agente;

import unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.Heuristica;

public class Manhattan implements Heuristica<Coordinate> {

    private MultiTarget target;
    public Manhattan(MultiTarget target){
        this.target = target;
    }

    @Override
    public double estimar(Coordinate estado) {
        double min = 15 * 15; //TODO: Revisar
        int x = estado.x();
        int y = estado.y();
        for(Coordinate c : target.set){
            min = Math.min(min, Math.abs(c.x() - x) + Math.abs(c.y() - y));
        }
        return min;
    }
}
