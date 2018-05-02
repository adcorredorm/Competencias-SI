package unalcol.agents.examples.labyrinth.multeseo.eater.SIS20181.Retoricos;

import java.util.Hashtable;
import java.util.TreeMap;
import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.simulate.util.SimpleLanguage;
import java.util.Vector;
import unalcol.agents.examples.labyrinth.LabyrinthUtil;

public class MiAgente implements AgentProgram {

    protected SimpleLanguage language;
    protected Nodo actual;
    protected Vector<String> cmd = new Vector<>();
    protected Hashtable<String, Nodo> mapeados = new Hashtable<>();
    protected TreeMap<Integer, TreeMap<Integer, Nodo>> novisitados = new TreeMap<>();
    protected TreeMap<Integer, TreeMap<Integer, Nodo>> recursos = new TreeMap<>();

    protected boolean cambio;
    protected boolean comio;
    protected boolean energiaEncontrada;
    protected boolean energiaMaximaCalculada;
    protected boolean mequierollenar;
    protected int direccion;

    protected int energiaMaxima = 0;
    protected int energiaPorRecurso = 0;
    protected int energiaAvanzar = 1;

    protected int energia;
    protected int energiaAnterior;
    protected int seguridad;

    public MiAgente(SimpleLanguage _language) {
        cambio = true;
        comio = false;
        seguridad = -1;
        language = _language;
        energiaEncontrada = false;
        mequierollenar = true;
        this.actual = new Nodo(1, 0, 0, null, 0);
        mapeados.put("0,0", this.actual);
    }

    @Override
    public Action compute(Percept p) {
        boolean MT = ((Boolean) p.getAttribute(language.getPercept(4)));
        boolean FAIL = ((Boolean) p.getAttribute(language.getPercept(5)));
        energia = (int) (p.getAttribute(LabyrinthUtil.ENERGY));
        
        energiaMaxima = Math.max(energiaMaxima, energia);
        if (cambio) {
            energiaAvanzar = Math.max(energiaAvanzar, energiaAnterior - energia);
        }
        if (comio) {
            energiaPorRecurso = Math.max(energiaPorRecurso, energia - energiaAnterior);
        }

        if (!MT) {
            if ((boolean) p.getAttribute(language.getPercept(10))) {
                energiaEncontrada = true;
                actual.tipo = Nodo.RECURSO;               
                seguridad = Math.min(energia + energiaAvanzar,energiaMaxima) / 2 - (energiaAvanzar);
                if (recursos.containsKey(actual.coordX)) {
                    recursos.get(actual.coordX).put(actual.coordY, actual);
                } else if (!recursos.containsKey(actual.coordY)) {
                    recursos.put(actual.coordX, new TreeMap<>());
                    recursos.get(actual.coordX).put(actual.coordY, actual);
                }
            } else {
                actual.tipo = Nodo.VISITADO;
            }

            if (novisitados.containsKey(actual.coordX)) {
                if (novisitados.get(actual.coordX).containsKey(actual.coordY)) {
                    novisitados.get(actual.coordX).remove(actual.coordY);
                    if(novisitados.get(actual.coordX).size() == 0){
                        novisitados.remove(actual.coordX);
                    }
                }
            }

            for (int i = 0; i < 4 && cambio && cmd.isEmpty(); i++) {
                actual.pared(i, direccion, ((Boolean) p.getAttribute(language.getPercept(i))), mapeados, novisitados);
            }

            //for (int i = 0; i < 4; i++) {
            //    System.out.println("INDICE "+i+" "+(actual.nodos[i] == null));            
            //}
            cambio = false;
            
            if (cmd.isEmpty()) {
                if (seguridad == 0 && energiaEncontrada) {
                    cmd = toInstrucciones((new CostoUniforme()).aplicar(actual, recursos, direccion, seguridad, Nodo.RECURSO, energiaMaxima,energiaAvanzar)); // objetivo comida
                    mequierollenar = true;
                    //System.out.println("MI COMIDA ES " + energia + " Y SIENTO QUE DEBO COMER");
                } else {
                    if(seguridad < 0){
                        energiaEncontrada = false;
                    }
                    cmd = toInstrucciones((new CostoUniforme()).aplicar(actual, novisitados, direccion, seguridad, Nodo.NOVISITADO, energiaMaxima,energiaAvanzar)); // objetivo casilla desconocida
                    //System.out.println("MI COMIDA ES " + energia + " Y SIENTO QUE ESTOY SEGURO " + seguridad);
                }
            }

            comio = false;
            if (actual.tipo == Nodo.RECURSO && ( energiaMaxima - energia >= energiaPorRecurso || mequierollenar) ) {
                if(energiaAnterior == energia){
                    mequierollenar = false;
                }else{                    
                    cmd.add(0, "eat");
                    comio = true;
                }
                seguridad = Math.min(energia + energiaAvanzar,energiaMaxima) / 2 - (energiaAvanzar);
                energiaMaxima = Math.max(energia, energiaMaxima);
            }
            
            //System.out.println(cmd);
            String x = cmd.get(0);
            cmd.remove(0);

            //System.out.println("Energia = " + (int) (p.getAttribute(LabyrinthUtil.ENERGY)));
            if (x.equals("rotate")) {
                direccion = (direccion + 1) % 4;
                //System.out.println("DIR " + direccion);
            }
            if (x.equals("advance")) {
                boolean AF = ((Boolean) p.getAttribute(language.getPercept(6)));
                if (AF) {
                    cmd.add(0, language.getAction(2)); // Vuelve a poner la instruccion avanzar en la lista de comandos
                    return new Action(language.getAction(0)); // No Op
                }
                if (seguridad > 0){
                    if(seguridad - energiaAvanzar < energiaAvanzar){
                        seguridad = 0;                        
                    }else{
                        seguridad -= energiaAvanzar;
                    }
                }else{
                    seguridad -= energiaAvanzar;
                }
                     
                cambio = true;
                actual = actual.nodos[direccion];
            }
            
            //System.out.println("Energia Maxima: " + energiaMaxima);
            //System.out.println("Energia Comer: " + energiaPorRecurso);
            //System.out.println("Energia Avanzar: " + energiaAvanzar);
            energiaAnterior = energia;
            return new Action(x);
        }
        return new Action("no_op");
    }

    @Override
    public void init() {
        direccion = 0;
        this.actual = new Nodo(Nodo.VISITADO, 0, 0, null, 0);
    }

    private Vector<String> toInstrucciones(Vector<Integer> l) {
        Vector<String> lista = new Vector<>();
        for (int i = l.size() - 1; i >= 0; i--) {
            for (int j = 0; j < l.get(i); j++) {
                lista.add(language.getAction(3));
            }
            lista.add(language.getAction(2));
        }
        return lista;
    }

}
