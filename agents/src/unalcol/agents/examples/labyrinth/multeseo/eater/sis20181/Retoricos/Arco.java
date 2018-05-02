
package unalcol.agents.examples.labyrinth.multeseo.eater.SIS20181.Retoricos;

import java.util.Vector;

public class Arco {
    Nodo nodo;
    Arco antecesor;
    int direccion;
    int costo;
    int costoAcumulado;
    int costoEstimado;
    Vector<Nodo> visitados;
    int seguridad;    
    
    public Arco(Nodo _nodo, Arco _antecesor, int _costo, int _costoEstimado, int _direccion, int _seguridad, int energiaMaxima,int energiaAvanzar) {
        this.nodo = _nodo;
        this.antecesor = _antecesor;
        this.costo = _costo;
        this.costoEstimado = _costoEstimado;
        if(_antecesor != null){
            this.costoAcumulado = _costo + _antecesor.costoAcumulado;
            this.visitados = new Vector<Nodo>(_antecesor.visitados);
            this.visitados.add(_antecesor.nodo);
            if(this.nodo.tipo == Nodo.RECURSO){
                this.seguridad = energiaMaxima;
            }else{
                this.seguridad = antecesor.seguridad - energiaAvanzar;
            }
        }else{
            this.visitados = new Vector<Nodo>();
        }  
        this.direccion = _direccion;                
    }
   
    
    public Nodo getNodo() {
        return nodo;
    }

    public Arco getAntecesor() {
        return antecesor;
    }

    public int getCosto() {
        return costo;
    }
    
    public int getCostoAcumulado() {
        return costoAcumulado;
    }

    public int getCostoEstimado() {
        return costoEstimado;
    }

}
