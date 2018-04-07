package unalcol.agents.examples.labyrinth.multeseo.eater.agente;

import unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.Accion;
import unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.Arco;
import unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.Sucesor;

import java.util.HashMap;
import java.util.Vector;


public class Successors implements Sucesor<Coordinate> {

    private HashMap<String, Vector<Coordinate>> graph;
    private int direction;

    public Successors(HashMap<String, Vector<Coordinate>> graph, int direction){
        this.graph = graph;
        this.direction = direction;
    }

    @Override
    public Vector<Arco<Coordinate>> obtener(Arco<Coordinate> estado) {
        Vector<Arco<Coordinate>> successors = new Vector<>();
        Vector<Accion> path;
        Accion action;
        Coordinate state = estado.getEstado();
        int last;
        try{
            last = estado.getPath().lastElement().getCode();
        }catch (Exception ex){
            last = -1; //TODO: Ojo con esto
        }

        if(graph.containsKey(state.toString())){
            for(Coordinate vecino : graph.get(state.toString())){
                path = estado.getPath();
                if(vecino.y() > state.y())
                    action = new Accion(ThunderCats.FRONT, (direction + ThunderCats.FRONT)%4);
                else if(vecino.x() > state.x())
                    action = new Accion(ThunderCats.RIGHT, (direction + ThunderCats.RIGHT)%4);
                else if(vecino.y() < state.y())
                    action = new Accion(ThunderCats.BACK, (direction + ThunderCats.BACK)%4);
                else //if(vecino[0] < state.x())
                    action = new Accion(ThunderCats.LEFT, (direction + ThunderCats.LEFT)%4);
                if(last != (action.getCode()+2)%4) {
                    path.addElement(action);
                    successors.add(new Arco<>(vecino, path, estado.costoTotal() + action.cost()));
                }
            }
        }
        return successors;
    }
}
