package unalcol.agents.examples.labyrinth.multeseo.eater.isi20181.thunderCats;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.examples.labyrinth.multeseo.eater.isi20181.thunderCats.busqueda.AStar;
import unalcol.agents.examples.labyrinth.multeseo.eater.isi20181.thunderCats.busqueda.Accion;
import unalcol.agents.simulate.util.SimpleLanguage;

import java.util.*;


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
    private int direction, energylv;
    private Coordinate coordinate;
    private HashSet<Coordinate> food;
    private HashMap<String, Vector<Coordinate>> graph;
    private String returnedAction;

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
        graph = new HashMap<>();
        stack = new HashSet<>();
        explorationPath = new DirectedPath(new int[0]);
        foodPath = new DirectedPath(new int[0]);
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
        Vector<Accion> V = busqueda.aplicar(coordinate).getPath();
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

    private int encuentros = 0;

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

        //actualPercept[goal] = (Boolean)p.getAttribute(this.language.getPercept(goal));
        //TODO: No se me ocurre que hacer con fail
        actualPercept[fail] = (Boolean)p.getAttribute(this.language.getPercept(fail));

        actualPercept[af] = (Boolean)p.getAttribute(this.language.getPercept(af));
        //TODO: No se me ocurre como usar la percepcion de otro agente salvo que me este bloqueando el paso
        /*actualPercept[ar] = (Boolean)p.getAttribute(this.language.getPercept(ar));
        actualPercept[ab] = (Boolean)p.getAttribute(this.language.getPercept(ab));
        actualPercept[al] = (Boolean)p.getAttribute(this.language.getPercept(al));*/

        actualPercept[res] = (Boolean)p.getAttribute(this.language.getPercept(res));

        if(actualPercept[fail]){
            init();
        }

        /* Se comprueba si se puede comer para siepre estar a full */

        if(actualPercept[res]){
            food.add(coordinate);
            if(energylv != (int) p.getAttribute(ENERGY_LEVEL)){
                energylv = (int) p.getAttribute(ENERGY_LEVEL);
                return new Action(language.getAction(4));
            }
        }
        energylv = (int) p.getAttribute(ENERGY_LEVEL);

        //if(actualPercept[fail]) comands.clear(); //TODO: No se si dejar esto xD

        /* Creacion del grafo */

        if(!graph.containsKey(coordinate.toString())){
            aux = new Vector<>(4);
            /* Como la percepcion es relativa a la direccion del agente, pero el grafo se construye con respecto a las coordenadas,
             *  es necesario revisar a que corresponde la percepcion con respecto al tablero. */
            for(int i = wl; i >= wf; i--){
                if(!actualPercept[i]){
                    switch ((direction + i)%4){
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

        if(energylv < 15 && !food.isEmpty()){
            if(!foodPath.hasNext()) foodPath = new DirectedPath(goTo(food));
            moveAbsolute(foodPath.getAction());
        }

        /* Si otro agente esta en frente de nosotros cuanndo queremos avanzar */

        if(actualPercept[af]){
            if(comands.isEmpty() && !explorationPath.hasNext()){
                stack.remove(coordinate); // No se si esta sea la mejor forma de de remover los visitados ...
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
                    comands.add(language.getAction(0));
                }
                graph.get(coordinate.toString()).add(c);
            }
        }

        /* SI no tiene nada mas que hacer explore xD */

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
        food = new HashSet<>();
        graph = new HashMap<>();
        stack = new HashSet<>();
        explorationPath = new DirectedPath(new int[0]);
        foodPath = new DirectedPath(new int[0]);
    }
}