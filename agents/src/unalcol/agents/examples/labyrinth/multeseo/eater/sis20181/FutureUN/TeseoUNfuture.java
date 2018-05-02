package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.FutureUN;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import unalcol.agents.simulate.util.SimpleLanguage;

public class TeseoUNfuture extends SimpleTeseoAgentProgram{
	public TeseoUNfuture(SimpleLanguage _language) {
		super(_language); 
		initT();
	}
	Node act; 
	Node obje;
	HashMap<Node,Integer> maze ;
	HashMap<Node,Integer> path ;
	HashMap<Node,Boolean> food ;
	Stack<Integer> opti;
	int actx , acty; 
	int actdir; 
	int prevdir;
	int cur = 1 ; 
	int prevLevel;
	int currentLevel ;
	int maxLevel ;
	boolean lastWasMove ;
	int [][] direc = {{-1,-0} , {0,1} , {1,0} , {0,-1}};
	Random r ; 
	boolean wait = true; 
	public int mask( boolean f , boolean d , boolean a , boolean i) {
		int mask = 0; 
		if (f)	mask |= 0b1;
		if (d)	mask |= 0b10;
		if (a)	mask |= 0b100;
		if (i)	mask |= 0b1000;
		return mask;
	}
	int timeWaited;
	public void initT() {
		maze = new HashMap<Node, Integer>(); 
		path = new HashMap<Node,Integer>();
		food = new HashMap<Node,Boolean>();
		act = new Node(actx , acty);
		opti = new Stack<Integer>();
		actx = 0;
		acty = 0; 
		prevdir = actdir = 0 ;
		prevLevel = -1;
		currentLevel = -1;
		maxLevel = -1;
		lastWasMove = false;
		r = new Random();
		timeWaited = 0;
	}
	public int getabsPercep(int relatPercep , int dir) {
		int tmp ;
		int mask = relatPercep;
		for ( int i = 0 ; i < (4-dir) ; i ++) {
			if ( mask % 2 == 1 ) tmp = 1 << 3 ; 
			else tmp = 0 ; 
			mask = mask >> 1;
			mask = tmp | mask;
		}
		return mask;
	}
	public boolean isbitOn(int mask , int i ) {
		mask = mask >> i ; 
		return ((mask & 1) == 1); 
	}
	public int delLastBranch(int maskEnem) {
		//System.out.println("last branh");
		for(int i = 0 ; i < 4 ; i ++) {
			if(((maskEnem >> i) & 1) == 1)
				continue;
			Node tmp = new Node(act.x +direc[i][0] , act.y + direc[i][1]);
			if(path.containsKey(tmp) && path.get(tmp).equals(cur-1)){
				path.remove(act);
				cur -- ;
				return i;
			}	
		}
		return 6; 
	}
	public Node imagine()
	{
		Node acttmp = new Node(act.x,act.y);
		int mask = maze.get(acttmp);
		mask = generateValidMoves(acttmp,mask);
		int curTmp = cur;
		int c = 0;
	//	System.out.println(curTmp);
		while (Integer.bitCount(mask)==4)
		{
		//	System.out.println(acttmp);
			for ( int i = 0 ; i < 4 ; i ++) {
				Node tmp = new Node (acttmp.x + direc[i][0] , acttmp.y + direc[i][1]);
			//	System.out.println(tmp + "" + path.get(tmp));
				if ( path.containsKey(tmp) && path.get(tmp).equals(curTmp-1)) {
					int masktmp = maze.get(acttmp);
				//	System.out.println(masktmp);
					if(isbitOn(masktmp,i))
						continue;
				//	System.out.println("dsfd");
					curTmp --;
					acttmp.x = tmp.x;
					acttmp.y = tmp.y;
					break;
				}
			}
		//	System.out.println("at end" + acttmp);
			mask = generateValidMoves(acttmp,maze.get(acttmp));
		//	System.out.println(maze.get(acttmp)+ " " +mask);
		}
		cur = path.get(acttmp);
		return acttmp;
	}
	public void bfs(Node from , Node to)
	{
		//System.out.println("obj" + to);
		HashMap<Node, Integer> step = new HashMap<Node, Integer>();
		Queue<Node> q = new LinkedList<Node>();
		step.put(from, 0);
		q.add(from);
		while(!q.isEmpty())
		{		
			Node head = new Node(q.peek().x, q.peek().y);
		//	System.out.println("head" + head);
			boolean flag = false;
			q.remove();
			for ( int i = 0 ; i < 4 ; i ++) {
				Node tmp = new Node (head.x + direc[i][0] , head.y + direc[i][1]);
			//	System.out.println(path.get(tmp));
				int mask = maze.get(head);
				if(isbitOn(mask,i))
					continue;
				if(step.containsKey(tmp))
					continue;
				if ( maze.containsKey(tmp)) {
					q.add(tmp);
					step.put(tmp, step.get(head)+1);
					if(tmp.equals(to))
					{
						flag =true;
						break;
					}
				}
			}
			if(flag)
				break;
		}
	//	System.out.println("fssdf");
		Node npath = new Node(to.x,to.y);
		int c = step.get(npath);
	//	System.out.println("path");
		while ( c > 0 )
		{
		//	System.out.println(npath);
			for ( int i = 0 ; i < 4 ; i ++) {
				Node tmp = new Node (npath.x + direc[i][0] , npath.y + direc[i][1]);
				int mask = maze.get(npath);
				if(isbitOn(mask,i))
					continue;
				if ( step.containsKey(tmp) && step.get(tmp).equals(c-1)) {
				
					c--;
					npath.x = tmp.x;
					npath.y = tmp.y;
					opti.add((i+2)%4);
					break;
				}
			}
		}
		//System.out.println(npath);
		return ; 
	} 
	public int whereIsMyFood(Node actual)
	{
			//System.out.println("obj" + to);
			Node from = new Node (actual.x,actual.y);
			Node to = null;
			int ans = -1;
			HashMap<Node, Integer> step = new HashMap<Node, Integer>();
			Queue<Node> q = new LinkedList<Node>();
			step.put(from, 0);
			q.add(from);
			while(!q.isEmpty())
			{		
				Node head = new Node(q.peek().x, q.peek().y);
		//		System.out.println("head" + head);
				boolean flag = false;
				q.remove();
				for ( int i = 0 ; i < 4 ; i ++) {
					Node tmp = new Node (head.x + direc[i][0] , head.y + direc[i][1]);
		//			System.out.println(path.get(tmp));
					if(!maze.containsKey(head))
						continue;
					int mask = maze.get(head);
					if(isbitOn(mask,i))
						continue;
					if(step.containsKey(tmp))
						continue;
					if ( maze.containsKey(tmp)) {
						q.add(tmp);
						step.put(tmp, step.get(head)+1);
						if(food.get(tmp).equals(true))
						{
							to = tmp;
							ans = step.get(head)+1;
							flag =true;
							break;
						}
					}
				}
				if(flag)
					break;
			}
			if( to == null)
				return -1;
		//	System.out.println("fssdf");
			Node npath = new Node(to.x,to.y);
			int c = step.get(npath);
		//	System.out.println("path");
			while ( c > 0 )
			{
			//	System.out.println(npath);
				for ( int i = 0 ; i < 4 ; i ++) {
					Node tmp = new Node (npath.x + direc[i][0] , npath.y + direc[i][1]);
					int mask = maze.get(npath);
					if(isbitOn(mask,i))
						continue;
					if ( step.containsKey(tmp) && step.get(tmp).equals(c-1)) {
					
						c--;
						npath.x = tmp.x;
						npath.y = tmp.y;
				//		opti.add((i+2)%4);
						break;
					}
				}
			}
			//System.out.println(npath);
			return ans; 
		} 	
	public int calcNextMove( int maskWalls , int maskEnem) {
//		System.out.println("enter");
	
		if(!opti.isEmpty())
		{
		//	System.out.println("cola"
			//		+ "");
		//	System.out.println(opti.peek());
			if ( ((maskEnem >> opti.peek()) & 1) == 1)
				return 6;
			return opti.peek();
		}
		int MoveMask = generateValidMoves(act,maskWalls);
		int MoveMaskEnem = generateValidMoves(act,maskEnem) | MoveMask;
		//System.out.println("enem" + maskEnem);
		//System.out.println("move" + MoveMask);

		int bitsoff = 4 - Integer.bitCount(MoveMaskEnem);
		//System.out.println(MoveMaskEnem);
		if (bitsoff != 0) {
			cur++; 
			int d = r.nextInt(bitsoff);
			for ( int i = 0 ; i < 4 ; i ++) {
				if ( !isbitOn(MoveMaskEnem,i)) {
					if ( d == 0 ) {
							return i;
					}
					else d -= 1; 
				}
			}
		}
		else if(Integer.bitCount(maskEnem) == 1) {
			if(timeWaited < 3 )
			{
				System.out.println(" la bal");
				return 6;
			}
			return delLastBranch(maskEnem);
		}else if (Integer.bitCount(maskEnem) == 0){
			obje = imagine();
			bfs(act,obje);
			return opti.peek();
		}
		
		return 6;
	}
	public int generateValidMoves(Node node, int mask) {
		for ( int i = 0 ; i < 4 ; i ++) {
			Node tmp = new Node(node.x + direc[i][0], node.y + direc[i][1]);
			if ( path.containsKey(tmp)) { mask |= 1 << i;}
		
		}
		return mask;
	}

	@Override
	public int accion(boolean PF, boolean PD, boolean PA, boolean PI, boolean MT, boolean FAIL, boolean AF, boolean AD,
			boolean AA, boolean AI, boolean RE, int eLevel) {
		currentLevel = eLevel;
		;
		//System.out.println("opti size " + opti.size());
		int maskNode = mask(PF , PD , PA , PI);
		int maskEnem = mask(AF,AD,AA,AI);
		if(FAIL)
		{
			System.out.println("asdfasfsadgfsadf asdfg as " + opti.size());
			System.out.println("se estrello con una garbinba atravesaa"  + opti.size());
			actx = actx - direc[actdir][0];
			acty = acty - direc[actdir][1];
	        cur--;
		}
		else
		{
			//System.out.println("ant was good");
			if(opti.size() > 0 && lastWasMove)
				opti.pop();
		}
		act = new Node(actx , acty);
		if(RE)
		{ 
			food.put(act,true);
			//	System.out.println("here");
	//		maskNode |= 1 << 13;
			if(prevLevel == currentLevel)
				maxLevel = currentLevel;
			prevLevel = currentLevel;
		//	System.out.println("elevel" + eLevel);
			if(currentLevel != maxLevel)
			{
				lastWasMove = false;
				timeWaited = 0;
				return 5;
			}
			//System.out.println("elevel" + eLevel);
		}
		else
			food.put(act, false);
		if(MT)
		{
			//System.out.println("SAFD");
			return -1;
		}
	
		//System.out.println("mascara original " + maskNode + "en tal direccion " + actdir);
		maskNode = getabsPercep(maskNode, actdir);
		maskEnem = getabsPercep(maskEnem,actdir);
		//System.out.println("mascara convertida " + maskNode);
		
		maze.put(act,maskNode);
		System.out.println(whereIsMyFood(act));
		
		if( !path.containsKey(act))
		{
			//System.out.println("marcando" + act + "con " +  cur);
			path.put(act , cur);
		}
		 System.out.println("( " + act.x + "," + act.y + ") : " + path.get(act));
		//System.out.println("( " + act.x + "," + act.y + ") : " + path.get(act));   
	    
		int k= calcNextMove(maskNode,maskEnem);
		//System.out.println(k);
		if( k > 3  || k < 0) { lastWasMove = false;
			timeWaited ++;
		return k;}
        int next = (k - actdir + 4 )% 4; 
       
        prevdir = actdir;
        actdir = k; 
		actx = actx + direc[actdir][0];
		acty = acty + direc[actdir][1];
		lastWasMove = true;
		timeWaited = 0;
		return next;
	}

}