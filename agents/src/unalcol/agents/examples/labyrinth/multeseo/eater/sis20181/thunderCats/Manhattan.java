package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats;

import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.Heuristica;

public class Manhattan implements Heuristica<Coordinate> {

    private MultiTarget target;
    public Manhattan(MultiTarget target){
        this.target = target;
    }

    @Override
    public double estimar(Coordinate estado) {
        double max = 0;
        int x = estado.x();
        int y = estado.y();
        for(Coordinate c : target.set){
            max = Math.max(max, Math.abs(c.x() - x) + Math.abs(c.y() - y));
        }
        return max;
    }
}
