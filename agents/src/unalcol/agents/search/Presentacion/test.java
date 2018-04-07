package unalcol.agents.search.Presentacion;

import unalcol.agents.search.classic.BreadthFirstSearch;
import unalcol.types.collection.vector.Vector;

public class test<T> extends BreadthFirstSearch<NodoPuzzle>{


    public test(int _max_depth) {
        super(_max_depth);
    }

    public static void main(String[] args) {
        int [][] M = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,0,15}};


        Vector V =
                new test(15).apply(new NodoPuzzle(M, 3, 2) , new Sucesores(), new Objetivo(), new Costo());

        for(Object o : V.toArray()){
            System.out.println(o.toString());
        }

    }
}
