package unalcol.agents.examples.labyrinth.multeseo.eater;

import unalcol.agents.Agent;
import unalcol.agents.AgentProgram;
import unalcol.agents.examples.labyrinth.Labyrinth;
import unalcol.agents.examples.labyrinth.LabyrinthDrawer;
import unalcol.agents.examples.labyrinth.multeseo.eater.SIS20181.Retoricos.MiAgente;
import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.FutureUN.TeseoUNfuture;
import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.RexeSiLaberinto.MyAgent;
import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.UNfail.UNfailAgentProgram;
import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.mandingas.Mandingas_agent;
import unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats.ThunderCats;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.types.collection.vector.Vector;

public class MultiTeseoEaterMain {
    private static SimpleLanguage getLanguage(){
	    return  new SimpleLanguage( new String[]{"front", "right", "back", "left", "treasure", "fail",
	        "afront", "aright", "aback", "aleft", "resource", "resource-color", "resource-shape", "resource-size", "resource-weight"},
	                                   new String[]{"no_op", "die", "advance", "rotate", "eat"}
	                                   );
	  }

	  public static void main( String[] argv ){
	     AgentProgram[] teseo = new AgentProgram[12];
	    int futureun = 0;
	    int mandingas = 1;
	    int retoricos = 2;
	    int rexes = 3;
	    int thundercats = 4;
	    int unfail = 5;

	    teseo[futureun] = new TeseoUNfuture( getLanguage() );
	    teseo[mandingas] = new Mandingas_agent( getLanguage() );
	    teseo[retoricos] = new MiAgente( getLanguage() );
	    teseo[rexes] = new MyAgent( getLanguage() );
	    teseo[thundercats] = new ThunderCats( getLanguage() );
	    teseo[unfail] = new UNfailAgentProgram( );


	    LabyrinthDrawer.DRAW_AREA_SIZE = 1000;
	    LabyrinthDrawer.CELL_SIZE = 50;
	    Labyrinth.DEFAULT_SIZE = 15;
	    
	    Agent agent1 = new Agent(teseo[thundercats]);
	    Agent agent2 = new Agent(teseo[rexes]);
	    
	    //Agent agent3 = new Agent(p3);
	    Vector<Agent> agent = new Vector<Agent>();
	    agent.add(agent1);
	    //agent.add(agent2);
//	    Agent agent = new Agent( new RandomReflexTeseoAgentProgram( getLanguage() ) );
	    MultiTeseoEaterMainFrame frame = new MultiTeseoEaterMainFrame( agent, getLanguage() );
	    frame.setVisible(true); 
	  }
}
