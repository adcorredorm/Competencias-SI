package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.FutureUN;

import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.types.collection.vector.*;
import unalcol.agents.Action;


public abstract class SimpleTeseoAgentProgram implements AgentProgram {
	protected SimpleLanguage language;
	protected Vector<String> cmd = new Vector<String>();

	public SimpleTeseoAgentProgram() {
	}

	public SimpleTeseoAgentProgram(SimpleLanguage _language) {
		language = _language;
	}

	public void setLanguage(SimpleLanguage _language) {
		language = _language;
	}

	public void init() {
		cmd.clear();
	}

	public abstract int accion(boolean PF, boolean PD, boolean PA, boolean PI, boolean MT, boolean FAIL, boolean AF,
			boolean AD, boolean AA, boolean AI, boolean RE, int eLevel);

	/**
	 * execute
	 *
	 * @param perception
	 *            Perception
	 * @return Action[]
	 */
	public Action compute(Percept p) {
		if (cmd.size() == 0) {

			boolean PF = ((Boolean) p.getAttribute(language.getPercept(language.getPerceptIndex("front")))).booleanValue();
			boolean PD = ((Boolean) p.getAttribute(language.getPercept(1))).booleanValue();
			boolean PA = ((Boolean) p.getAttribute(language.getPercept(2))).booleanValue();
			boolean PI = ((Boolean) p.getAttribute(language.getPercept(3))).booleanValue();
			boolean MT = ((Boolean) p.getAttribute(language.getPercept(4))).booleanValue();
			boolean FAIL = ((Boolean) p.getAttribute(language.getPercept(5))).booleanValue();
			boolean AF = ((Boolean) p.getAttribute(language.getPercept(6))).booleanValue();
			boolean AD = ((Boolean) p.getAttribute(language.getPercept(7))).booleanValue();
			boolean AA = ((Boolean) p.getAttribute(language.getPercept(8))).booleanValue();
			boolean AI = ((Boolean) p.getAttribute(language.getPercept(9))).booleanValue();
			boolean RE = ((Boolean) p.getAttribute(language.getPercept(language.getPerceptIndex("resource")))).booleanValue();
			int eLevel = ((Integer) p.getAttribute(language.getPercept(language.getPerceptIndex("energy_level")))).intValue();

			int d = accion(PF, PD, PA, PI, MT, FAIL, AF, AD, AA, AI, RE, eLevel);
			if (0 <= d && d < 4) {
				for (int i = 1; i <= d; i++) {
					cmd.add(language.getAction(3)); // rotate
				}
				cmd.add(language.getAction(2)); // advance
			} else if ( d == 5 )
			{
				cmd.add(language.getAction(4)); // eat
			}
			else {
				cmd.add(language.getAction(0)); // noop
			}
		}
		String x = cmd.get(0);
		cmd.remove(0);
		return new Action(x);
	}

	/**
	 * goalAchieved
	 *
	 * @param perception
	 *            Perception
	 * @return boolean
	 */
	public boolean goalAchieved(Percept p) {
		return (((Boolean) p.getAttribute(language.getPercept(4))).booleanValue());
	}
	
}
