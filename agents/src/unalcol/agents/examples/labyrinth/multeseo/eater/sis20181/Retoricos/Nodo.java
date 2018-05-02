
package unalcol.agents.examples.labyrinth.multeseo.eater.SIS20181.Retoricos;


import java.util.Hashtable;
import java.util.TreeMap;

public class Nodo {
    public static final int NOVISITADO = 0;
    public static final int VISITADO = 1;    
    public static final int RECURSO = 2;
    
    public static final int[][] MOVIMIENTO = {{-1,0},{0,1},{1,0},{0,-1}};
    
    Nodo[] nodos;
    int tipo;
    int coordX;
    int coordY;
    
    public Nodo(int tipo, int coordX, int coordY, Nodo vecino,int direccion){
        this.nodos = new Nodo[4];
        this.nodos[(direccion + 2)%4 ] = vecino;
        this.tipo = tipo;
        this.coordX = coordX;
        this.coordY = coordY;
    }
    
    void pared(int lado,int direccion, Boolean p,Hashtable<String,Nodo> mapeados,TreeMap<Integer, TreeMap<Integer, Nodo> > novisitados ) {
        if(!p && nodos[(lado+direccion)%4] == null){
            int[] coords = {coordX + MOVIMIENTO[(lado+direccion)%4][0] ,coordY + MOVIMIENTO[(lado+direccion)%4][1] };
            if( !mapeados.containsKey(""+coords[0]+","+coords[1])){                
                nodos[(lado+direccion)%4] = new Nodo(0, coords[0]
                        , coords[1], this, (lado+direccion)%4 );
                mapeados.put(""+coords[0]+","+coords[1] , nodos[(lado+direccion)%4]);

                if (novisitados.containsKey(coords[0])) {                    
                    novisitados.get(coords[0]).put(coords[1], nodos[(lado+direccion)%4]);
                }else{
                    novisitados.put(coords[0], new TreeMap<>());
                    novisitados.get(coords[0]).put(coords[1], nodos[(lado+direccion)%4]);
                }                
                
            }else{                
                nodos[(lado+direccion)%4] = mapeados.get(""+coords[0]+","+coords[1]);
            }            
        }
    }
    
    @Override
    public boolean equals(Object obj) {                
        return ((Nodo)obj).coordX == coordX && ((Nodo)obj).coordY == coordY ;        
    }
    
}
