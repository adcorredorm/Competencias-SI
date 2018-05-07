package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.AStar;
import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.Accion;
import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.busqueda.Arco;
import unalcol.agents.simulate.util.SimpleLanguage;

import java.util.*;

//TODO Rexes (Prioriza a la derecha)
//TODO ¿Vale la pena calcular el costo de avanzar y rotar?
//TODO quitar el TODO de rexes antes de la competencia

/* Cambios Hechos
* Modificacion de la Heuristica Manhattan (Manhattan)
* graph ahora es un linkedHashMap (Como no se cambia la estructura de datos de A* sigue sin tener un orden estricto, pero como se cambio la Heuristica, tiene mejores resultados) (Thundercats)
* Se agrega una probabilidad para que no reconozca un estado como estado objetivo (solo del 0.05%) (Multitarget)
* Se deja un costo uniforme para el desplazamiento hacia cualquier direccion (Succesors)
* Se guarda el maximo de energia (solo para calcular el nivel de hambre (< 40%))(Thundercats)
 */

public class ThunderCats implements AgentProgram {

    private class DirectedPath{
        int[] path;
        int counter;

        int getAction(){
            if(counter < path.length) return path[counter++];
            return -1;
        }

        boolean hasNext(){ return counter < path.length; }

        void reset(int[] path){
            this.path = path;
            counter = 0;
        }

        DirectedPath(int[] path){
            this.path = path;
            counter = 0;
        }
    }

    public final static int FRONT = 0;
    public final static int RIGHT = 1;
    public final static int BACK = 2;
    public final static int LEFT = 3;

    private final static int wf = 0;
    private final static int wr = 1;
    private final static int wb = 2;
    private final static int wl = 3;
    private final static int goal = 4;
    private final static int fail = 5;
    private final static int af = 6;
    private final static int ar = 7;
    private final static int ab = 8;
    private final static int al = 9;
    private final static int res = 10;
    private final static String ENERGY_LEVEL = "energy_level";


    private SimpleLanguage language;
    private Vector<String> comands;
    private int direction, energylv, maxenergy;
    private Coordinate coordinate;
    private HashSet<Coordinate> food;
    private HashSet<Coordinate> badfood;
    private HashSet<Coordinate> posiblefood;
    private LinkedHashMap<String, Vector<Coordinate>> graph;
    private String returnedAction;

    private boolean hungry;
    private Coordinate lastcoordinate;

    private boolean[] actualPercept;
    private Vector<Coordinate> aux;
    private HashSet<Coordinate> stack;
    private DirectedPath foodPath;
    private DirectedPath explorationPath;

    public ThunderCats(SimpleLanguage sl){
        this.language = sl;
        comands = new Vector<>();
        direction = FRONT;
        coordinate = new Coordinate(0, 0);
        actualPercept = new boolean[11];
        food = new HashSet<>();
        badfood = new HashSet<>();
        posiblefood = new HashSet<>();
        graph = new LinkedHashMap<>();
        stack = new HashSet<>();
        explorationPath = new DirectedPath(new int[0]);
        foodPath = new DirectedPath(new int[0]);
        maxenergy = 0;
        energylv = -1; //solo para tener unn valor inicial
    }

    private void moveAbsolute(int pos){
        if(pos == -1) return ;
        rotateAbsolute(pos);
        comands.add(language.getAction(2));
    }

    private void rotateAbsolute(int pos){
        if(pos != direction){
            for(int i = 0; i < (4- direction +pos)%4; i++) comands.add(language.getAction(3));
        }
        /*  Rotar no consume energia, solo tiempo   */
    }

    private void rotateRelative(int pos){
        rotateAbsolute((direction + pos)%4);
    }

    private void moveRelative(int pos){
        moveAbsolute((direction + pos)%4);
    }

    private int[] goTo(Collection<Coordinate> target){
        MultiTarget goal = new MultiTarget(target);
        AStar<Coordinate> busqueda = new AStar<>(new Successors(graph, direction), goal, new Manhattan(goal));
        Arco<Coordinate> arco = busqueda.aplicar(coordinate);
        if(arco == null){
            System.out.println("MRD");
        }
        Vector<Accion> V = arco.getPath();
        int[] actions = new int[V.size()];
        int i = 0;
        for(Accion a : V) actions[i++] = a.getCode();
        return actions;
    }

    private int[] goTo(Coordinate target){
        Vector<Coordinate> t = new Vector<>(1);
        t.add(target);
        return goTo(t);
    }

    private int encuentros = 0;
    private int pastenergy;
    @Override
    public Action compute(Percept p) {

        /* Comprobamos si ya llegamos a la meta */
        if((Boolean)p.getAttribute(this.language.getPercept(goal))){
            System.out.println("Thunder, thunder, THUNDERCATS!");
            return new Action(language.getAction(1));
        }

        /* Extraccion de la percepcion */

        actualPercept[wf] = (Boolean)p.getAttribute(this.language.getPercept(wf));
        actualPercept[wr] = (Boolean)p.getAttribute(this.language.getPercept(wr));
        actualPercept[wb] = (Boolean)p.getAttribute(this.language.getPercept(wb));
        actualPercept[wl] = (Boolean)p.getAttribute(this.language.getPercept(wl));

        actualPercept[fail] = (Boolean)p.getAttribute(this.language.getPercept(fail));

        actualPercept[af] = (Boolean)p.getAttribute(this.language.getPercept(af));
        //TODO: No se me ocurre como usar la percepcion de otro agente salvo que me este bloqueando el paso
        /*actualPercept[ar] = (Boolean)p.getAttribute(this.language.getPercept(ar));
        actualPercept[ab] = (Boolean)p.getAttribute(this.language.getPercept(ab));
        actualPercept[al] = (Boolean)p.getAttribute(this.language.getPercept(al));*/

        actualPercept[res] = (Boolean)p.getAttribute(this.language.getPercept(res));
        pastenergy = (energylv == -1)?(int) p.getAttribute(ENERGY_LEVEL):energylv;
        energylv = (int) p.getAttribute(ENERGY_LEVEL);

        if(energylv > maxenergy) maxenergy = energylv;
        hungry = maxenergy*0.5 > energylv;

        if(actualPercept[fail]){
            coordinate = lastcoordinate;
        }

        /* Comida */

        if(actualPercept[res]){

            if(!badfood.contains(coordinate) && !food.contains(coordinate)) posiblefood.add(coordinate.clone());

            //Si comi y me bajo la energia, la comida es mala
            if((energylv  < pastenergy - 1)){
                posiblefood.remove(coordinate);
                badfood.add(coordinate.clone());
            }
            //Si me subio la energia
            if(energylv > pastenergy){
                posiblefood.remove(coordinate);
                food.add(coordinate.clone());
            }

            if(food.contains(coordinate) && energylv <= maxenergy && pastenergy != energylv){
                return new Action(language.getAction(4));
            }

            //Si tengo mas del 50% de la energia me arriesgo a probar
            if(!hungry && posiblefood.contains(coordinate)){
                return new Action(language.getAction(4));
            }
        }

        /* Creacion del grafo */

        if(!graph.containsKey(coordinate.toString())){
            aux = new Vector<>(4);
            /* Como la percepcion es relativa a la direccion del agente, pero el grafo se construye con respecto a las coordenadas,
             *  es necesario revisar a que corresponde la percepcion con respecto al tablero. */

            //Literalmente le damos prioridad a girar a la derecha XD
            int[] insOrden = {wr,wf,wb,wl};
            for(int i = 0; i < 4; i++){
                if(!actualPercept[insOrden[i]]){
                    switch ((direction + insOrden[i])%4){
                        case FRONT:
                            aux.add(new Coordinate(coordinate.x(), coordinate.y() + 1));
                            break;
                        case RIGHT:
                            aux.add(new Coordinate(coordinate.x() + 1, coordinate.y()));
                            break;
                        case BACK:
                            aux.add(new Coordinate(coordinate.x(), coordinate.y() - 1));
                            break;
                        case LEFT:
                            aux.add(new Coordinate(coordinate.x() - 1, coordinate.y()));
                            break;
                    }
                }
            }
            /* esta es otra forma de connstruir el grafo, insertando las percepciones en distinto orden
            if(!actualPercept[(wl - direction + 4)%4]) aux.add(new Coordinate(coordinate.x() - 1, coordinate.y()));
            if(!actualPercept[(wb - direction + 4)%4]) aux.add(new Coordinate(coordinate.x(), coordinate.y() - 1));
            if(!actualPercept[(wr - direction + 4)%4]) aux.add(new Coordinate(coordinate.x() + 1, coordinate.y()));
            if(!actualPercept[(wf - direction + 4)%4]) aux.add(new Coordinate(coordinate.x(), coordinate.y() + 1));
            */
            for(Coordinate c: aux){
                if(!graph.containsKey(c.toString())) stack.add(c);
            }
            graph.put(coordinate.toString(), aux);
        }

        /*Busca la fuente de comida mas cercana cuando este en niveles criticos */

        if(hungry){
            if(!food.isEmpty()){ //Si tengo hambre y conozco comida buena
                if(!explorationPath.hasNext()) explorationPath.reset(goTo(food));
            }else if(!posiblefood.isEmpty()){//La desesperada xD
                if(!explorationPath.hasNext()) explorationPath.reset(goTo(posiblefood));
            }

            if(comands.isEmpty()) moveAbsolute(explorationPath.getAction());
        }

        /* Si otro agente esta en frente de nosotros cuando queremos avanzar */

        if(actualPercept[af]){
            if(comands.isEmpty() && !explorationPath.hasNext()){
                stack.remove(coordinate); // No se si esta sea la mejor forma de remover los visitados ...
                explorationPath.reset(goTo(stack));
            }
            moveAbsolute(explorationPath.getAction());

            if(comands.get(0).equals("advance")){
                if(encuentros < 4){
                    encuentros++;
                    return new Action(language.getAction(0));
                }
                encuentros = 0;
                Coordinate c;
                switch (direction){
                    case FRONT:
                        c = new Coordinate(coordinate.x(), coordinate.y() + 1);
                        break;
                    case RIGHT:
                        c = new Coordinate(coordinate.x() + 1, coordinate.y());
                        break;
                    case BACK:
                        c = new Coordinate(coordinate.x(), coordinate.y() - 1);
                        break;
                    case LEFT:
                        c = new Coordinate(coordinate.x() - 1, coordinate.y());
                        break;
                    default:
                        c = new Coordinate(0, 0);
                        break;
                }
                graph.get(coordinate.toString()).remove(c);
                comands.clear();
                try{
                    explorationPath.reset(goTo(stack));
                    moveAbsolute(explorationPath.getAction());
                }catch (Exception e){
                    //TODO: Si no se mueven y nosotros si podemos, ceder paso
                    comands.add(language.getAction(0));
                }
                graph.get(coordinate.toString()).add(c);
            }
        }

        /* Si no tiene nada mas que hacer explore xD */

        if(comands.isEmpty()){
            if(!explorationPath.hasNext()){
                stack.remove(coordinate); // No se si esta sea la mejor forma de de remover los visitados ...
                explorationPath.reset(goTo(stack));
            }
            moveAbsolute(explorationPath.getAction());
        }


        /* Ejecucion de la accion */
        returnedAction = comands.get(0);
        comands.remove(0);

        if(returnedAction.equals("rotate")){
            direction = (direction + 1)%4;
        }else if(returnedAction.equals("advance")){
            //System.out.println("(" + coordinate.x() + "," + coordinate.y() + ")");
            lastcoordinate = coordinate.clone();
            switch (direction){
                case FRONT: coordinate.yUp(); break;
                case BACK: coordinate.yDown(); break;
                case RIGHT: coordinate.xUp(); break;
                case LEFT: coordinate.xDown(); break;
            }
        }
        return new Action(returnedAction);
    }

    @Override
    public void init() {
        comands = new Vector<>();
        direction = FRONT;
        coordinate = new Coordinate(0, 0);
        posiblefood = new HashSet<>();
        graph = new LinkedHashMap<>();
        stack = new HashSet<>();
        explorationPath = new DirectedPath(new int[0]);
        foodPath = new DirectedPath(new int[0]);
        //Esto no se esta usando
        for(Coordinate c: food){
            c.x -= lastcoordinate.x();
            c.y -= lastcoordinate.y();
        }
        for(Coordinate c: badfood){
            c.x -= lastcoordinate.x();
            c.y -= lastcoordinate.y();
        }
    }

    /** Solo test **/
    private int N = 0;
    private int[] eater1 = {BACK,BACK,BACK,BACK,BACK,RIGHT,FRONT,FRONT,RIGHT,RIGHT,RIGHT,FRONT,FRONT,RIGHT,BACK,RIGHT,BACK,RIGHT,BACK,RIGHT,BACK,BACK,BACK,LEFT,LEFT,BACK};
    private int[] eater2 = {BACK,BACK,RIGHT,FRONT,RIGHT,RIGHT,FRONT,RIGHT,RIGHT,RIGHT,BACK,BACK,BACK,LEFT,BACK,BACK,LEFT,BACK,BACK,RIGHT,BACK,BACK,RIGHT,RIGHT,BACK,BACK,BACK,BACK,BACK,RIGHT,RIGHT,RIGHT,FRONT,RIGHT,RIGHT,RIGHT,RIGHT,FRONT,FRONT,FRONT,LEFT,FRONT,FRONT,FRONT,RIGHT,FRONT,FRONT,FRONT,FRONT,LEFT,FRONT,FRONT,LEFT,FRONT,LEFT,LEFT,LEFT,BACK,BACK,LEFT,BACK,BACK,RIGHT,RIGHT,RIGHT,BACK,BACK,LEFT,BACK};
    private DirectedPath ex1 = new DirectedPath(eater1);
    private DirectedPath ex2 = new DirectedPath(eater2);

    private boolean ida = true;
    private DirectedPath vuelta = null;

    private void directedtest(int option){
        if((option == 1  && ex1.counter < eater1.length ) || ((option == 2 && ex1.counter == eater2.length))){
            if(option == 1) moveAbsolute(ex1.getAction());
            else moveAbsolute(ex2.getAction());
        }else{
            if(vuelta == null) vuelta = new DirectedPath(goTo(new Coordinate(0, 0)));
            moveAbsolute(vuelta.getAction());
            if(vuelta.counter == vuelta.path.length){
                ex1.counter = 0;
                ex2.counter = 0;
                vuelta.counter = 0;
            }
        }
    }

    private void graphPrint(){
        for(int i = -14; i <= 14; i++){ // Grafo explorado al completo
            for(int j = 14; j >= -14; j--){
                if(graph.containsKey(i + "," + j)){
                    Vector<Coordinate> value = graph.get(i + "," + j);
                    System.out.print("{(" + i + "," + j + ") -> {");
                    for(int k = 0; k < value.size()-1; k++){
                        System.out.print("(" + value.get(k).toString() + "),");
                    }
                    System.out.println("(" + value.get(value.size()-1).toString() + ")}}");
                }
            }
        }
    }
    /****/
}
