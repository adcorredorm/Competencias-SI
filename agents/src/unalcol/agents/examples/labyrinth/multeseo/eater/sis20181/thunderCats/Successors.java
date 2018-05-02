package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats;

import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.Accion;
import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.Arco;
import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.Sucesor;

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
        int last, cost;
        try{
            last = estado.getPath().lastElement().getCode();
        }catch (Exception| Error ex){
            last = -1;
        }
        if(graph.containsKey(state.toString())){
            for(Coordinate vecino : graph.get(state.toString())){
                path = estado.getPath();
                // Con el costo asi le da mas prioridad a mantener la direccion (no siempre)
                if(vecino.y() > state.y()) {
                    cost = (direction == ThunderCats.FRONT)? 1 : (ThunderCats.FRONT - direction + 4)%4;
                    action = new Accion(ThunderCats.FRONT, cost);//(direction + ThunderCats.FRONT)%4);
                }
                else if(vecino.x() > state.x()) {
                    cost = (direction == ThunderCats.RIGHT)? 1 : (ThunderCats.RIGHT - direction + 4)%4;
                    action = new Accion(ThunderCats.RIGHT, cost);//(direction + ThunderCats.RIGHT)%4);
                }
                else if(vecino.y() < state.y()) {
                    cost = (direction == ThunderCats.BACK)? 1 : (ThunderCats.BACK - direction + 4)%4;
                    action = new Accion(ThunderCats.BACK, cost);//(direction + ThunderCats.BACK)%4);
                }
                else if(vecino.x() < state.x()) {
                    cost = (direction == ThunderCats.LEFT)? 1 : (ThunderCats.LEFT - direction + 4)%4;
                    action = new Accion(ThunderCats.LEFT, cost);//(direction + ThunderCats.LEFT)%4);
                } else action = new Accion(-1, 0);

                if(last != (action.getCode()+2)%4 && action.getCode() != -1) { //Accion inversa
                    path.addElement(action);
                    successors.add(new Arco<>(vecino, path, estado.costoTotal() + action.cost()));
                }
            }
        }
        return successors;
    }
}
