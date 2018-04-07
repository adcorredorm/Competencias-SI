package unalcol.agents.examples.labyrinth.multeseo.eater.agente;

import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.AStar;
import unalcol.agents.examples.labyrinth.multeseo.eater.agente.busqueda.Accion;
import unalcol.agents.simulate.util.SimpleLanguage;

import java.util.*;


public class ThunderCats implements AgentProgram {

    private class DirectedPath{
        int[] path;
        int counter;

        int getAction(){
            if(counter < path.length) return path[counter++];
            return -1; //TODO: Ojo con esto
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

    private boolean[] actualPercept;
    private Vector<Coordinate> aux;
    private LabyrinthStack stack;
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
        stack = new LabyrinthStack();
        explorationPath = new DirectedPath(new int[0]);
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
        AStar<Coordinate> busqueda = new AStar<>(new Successors(graph, direction), goal, 100 , new Manhattan(goal));
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
    /****/

    @Override
    public Action compute(Percept p) {

        if((Boolean)p.getAttribute(this.language.getPercept(goal))){
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
            return new Action(language.getAction(1));
        }
        /* Esto podria no ser necesario */

        actualPercept[wf] = (Boolean)p.getAttribute(this.language.getPercept(wf));
        actualPercept[wr] = (Boolean)p.getAttribute(this.language.getPercept(wr));
        actualPercept[wb] = (Boolean)p.getAttribute(this.language.getPercept(wb));
        actualPercept[wl] = (Boolean)p.getAttribute(this.language.getPercept(wl));

        //actualPercept[goal] = (Boolean)p.getAttribute(this.language.getPercept(goal));
        actualPercept[fail] = (Boolean)p.getAttribute(this.language.getPercept(fail));

        actualPercept[af] = (Boolean)p.getAttribute(this.language.getPercept(af));
        actualPercept[ar] = (Boolean)p.getAttribute(this.language.getPercept(ar));
        actualPercept[ab] = (Boolean)p.getAttribute(this.language.getPercept(ab));
        actualPercept[al] = (Boolean)p.getAttribute(this.language.getPercept(al));

        actualPercept[res] = (Boolean)p.getAttribute(this.language.getPercept(res));
        /* -- */

        if(actualPercept[res]){//TODO: Mover al final
            food.add(coordinate);
            if(energylv != (int) p.getAttribute(ENERGY_LEVEL)) comands.add(0, language.getAction(4));
        }
        energylv = (int) p.getAttribute(ENERGY_LEVEL);

        ///****/if(actualPercept[fail]) comands.clear();

        if(!graph.containsKey(coordinate.toString())){
            aux = new Vector<>(4);
            /* Creacion del grafo:
             * Como la percepcion es relativa a la direccion del agente, pero el grafo
             * se construye con respecto a las coordenadas, es necesario realizar el ajuste.
             * El +4 es para corregir la operacion modulo cuando es negativa
             */
            for(int i = LEFT; i >= FRONT; i--){
                if(!actualPercept[i]){
                    switch (direction){
                        case FRONT:
                            switch (i){
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
                            break;
                        case RIGHT:
                            switch (i){
                                case LEFT:
                                    aux.add(new Coordinate(coordinate.x(), coordinate.y() + 1));
                                    break;
                                case FRONT:
                                    aux.add(new Coordinate(coordinate.x() + 1, coordinate.y()));
                                    break;
                                case RIGHT:
                                    aux.add(new Coordinate(coordinate.x(), coordinate.y() - 1));
                                    break;
                                case BACK:
                                    aux.add(new Coordinate(coordinate.x() - 1, coordinate.y()));
                                    break;
                            }
                            break;
                        case BACK:
                            switch (i){
                                case BACK:
                                    aux.add(new Coordinate(coordinate.x(), coordinate.y() + 1));
                                    break;
                                case LEFT:
                                    aux.add(new Coordinate(coordinate.x() + 1, coordinate.y()));
                                    break;
                                case FRONT:
                                    aux.add(new Coordinate(coordinate.x(), coordinate.y() - 1));
                                    break;
                                case RIGHT:
                                    aux.add(new Coordinate(coordinate.x() - 1, coordinate.y()));
                                    break;
                            }
                            break;
                        case LEFT:
                            switch (i){
                                case RIGHT:
                                    aux.add(new Coordinate(coordinate.x(), coordinate.y() + 1));
                                    break;
                                case BACK:
                                    aux.add(new Coordinate(coordinate.x() + 1, coordinate.y()));
                                    break;
                                case LEFT:
                                    aux.add(new Coordinate(coordinate.x(), coordinate.y() - 1));
                                    break;
                                case FRONT:
                                    aux.add(new Coordinate(coordinate.x() - 1, coordinate.y()));
                                    break;
                            }
                            break;
                    }
                }
            }
            /*
            if(!actualPercept[(wl - direction + 4)%4]) aux.add(new Coordinate(coordinate.x() - 1, coordinate.y()));
            if(!actualPercept[(wb - direction + 4)%4]) aux.add(new Coordinate(coordinate.x(), coordinate.y() - 1));
            if(!actualPercept[(wr - direction + 4)%4]) aux.add(new Coordinate(coordinate.x() + 1, coordinate.y()));
            if(!actualPercept[(wf - direction + 4)%4]) aux.add(new Coordinate(coordinate.x(), coordinate.y() + 1));
            */

            for(Coordinate c: aux){
                if(!graph.containsKey(c.toString())) stack.add(c); //TODO: Simplificar ese super-case
            }
            graph.put(coordinate.toString(), aux);
        }

        if(energylv < 0 && !food.isEmpty()){ //Disabled
            if(foodPath == null || !foodPath.hasNext()) foodPath = new DirectedPath(goTo(food));
            else moveAbsolute(foodPath.getAction()); //TODO: cuando va a buscar comida necesita reordenar la pila
        }

        if(comands.isEmpty()){
            if(!explorationPath.hasNext()){
                while(graph.containsKey(stack.peek().toString())) stack.pop(); //Da errores cuando va a lugares ya visitados
                Coordinate c = stack.peek();
                int[] w = goTo(c);
                if(w.length > 4){
                    stack.reorder(new Manhattan(new MultiTarget(coordinate)));
                    if(!c.equals(stack.peek()))w = goTo(stack.peek()); //en caso de que no se halla modificado, no es necesario hacer denuevo la busqueda
                }
                explorationPath.reset(w);
                stack.pop();//TODO: working in...
            }
            moveAbsolute(explorationPath.getAction());
        }


        /*-------------------------------------*/
        String action = comands.get(0);

        if(action.equals("rotate")){
            direction = (direction + 1)%4;
        }else if(action.equals("advance")){
            if(actualPercept[af]) return new Action(language.getAction(0)); //TODO: Ojo si se muere un agente al frente mio
            //System.out.println("(" + coordinate.x() + "," + coordinate.y() + ")");
            switch (direction){
                case FRONT: coordinate.yUp(); break;
                case BACK: coordinate.yDown(); break;
                case RIGHT: coordinate.xUp(); break;
                case LEFT: coordinate.xDown(); break;
            }
        }
        comands.remove(0);
        return new Action(action);
    }

    @Override
    public void init() {
        // ?? Donde se usa init??
        this.comands = new Vector<>();
        this.direction = FRONT;
    }
}
