package unalcol.agents.search.Presentacion;

import unalcol.agents.Action;
import unalcol.agents.search.GraphSpace;
import unalcol.types.collection.Collection;
import unalcol.types.collection.vector.Vector;

import java.util.Iterator;

public class Sucesores<T> implements GraphSpace<T>{

    public static final Vector<Action> Puzzle_Action =
            new Vector(new Action[]
                    {new Action("UP"), new Action("DOWN"), new Action("LEFT"), new Action("RIGHT")}, 4);


    public void switchear(int[][] M, int x, int y, Action action){
        int aux = M[x][y];
        switch (action.getCode()){
            case "UP":
                M[x][y] = M[x][y-1];
                M[x][y-1] = aux;
            break;

            case "DOWN":
                M[x][y] = M[x][y+1];
                M[x][y+1] = aux;

                break;

            case "LEFT":
                M[x][y] = M[x-1][y];
                M[x-1][y] = aux;

                break;

            case "RIGHT":
                M[x][y] = M[x+1][y];
                M[x+1][y] = aux;

                break;
        }
    }

    @Override
    public T succesor(T state, Action action) {
        NodoPuzzle nodo = (NodoPuzzle) state;

        switch (action.getCode()){
            case "UP":
                if(nodo.getPos()[1] == 0){
                    switchear(nodo.getPuzzle(), nodo.getPos()[0], nodo.getPos()[1], new Action("DOWN"));
                }else{
                    switchear(nodo.getPuzzle(), nodo.getPos()[0], nodo.getPos()[1], action);
                }

                break;

            case "DOWN":
                if(nodo.getPos()[1] == 3){
                    switchear(nodo.getPuzzle(), nodo.getPos()[0], nodo.getPos()[1], new Action("UP"));
                }else{
                    switchear(nodo.getPuzzle(), nodo.getPos()[0], nodo.getPos()[1], action);
                }

                break;

            case "LEFT":
                if(nodo.getPos()[0] == 0){
                    switchear(nodo.getPuzzle(), nodo.getPos()[0], nodo.getPos()[1], new Action("RIGHT"));
                }else{
                    switchear(nodo.getPuzzle(), nodo.getPos()[0], nodo.getPos()[1], action);
                }

                break;
            case "RIGHT":
                if(nodo.getPos()[0] == 3){
                    switchear(nodo.getPuzzle(), nodo.getPos()[0], nodo.getPos()[1], new Action("LEFT"));
                }else{
                    switchear(nodo.getPuzzle(), nodo.getPos()[0], nodo.getPos()[1], action);
                }

                break;
        }

        return (T)nodo;
    }

    @Override
    public Vector<Action> succesor(T state) {
        return Puzzle_Action;
    }

    @Override
    public boolean feasible(T t) {
        return true;
    }

    /**********************************/

    @Override
    public double feasibility(T t) {
        return 0;
    }

    @Override
    public T repair(T t) {
        return null;
    }

    @Override
    public T pick() {
        return null;
    }

    @Override
    public boolean set(Object o, Object o2) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean add(Object o) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public Collection<Object> keys() {
        return null;
    }

    @Override
    public boolean valid(Object o) {
        return false;
    }

    @Override
    public Object get(Object o) {
        return null;
    }

    @Override
    public Iterator<Object> iterator() {
        return null;
    }
}
