package unalcol.agents.search.Presentacion;

import unalcol.search.Goal;
import unalcol.sort.Order;
import unalcol.types.collection.Collection;

import java.util.Iterator;

public class Objetivo<T, Boolean> implements Goal<T, java.lang.Boolean> {


    public static final int[][] Default_puzzle = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,0}};

    @Override
    public Order<java.lang.Boolean> order() {
        return null;
    }

    @Override
    public java.lang.Boolean compute(T t) {
        NodoPuzzle nodo = (NodoPuzzle) t;

        int[][] M = nodo.getPuzzle();

        for(int i = 0; i < 4; i++){
            for(int j= 0; j < 4; j++){
                if(M[i][j] != Default_puzzle[i][j]) return false;
            }
        }

        return true;
    }

    @Override
    public boolean set(Object o, Object o2) {
        return false;
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
    public int size() {
        return 0;
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
