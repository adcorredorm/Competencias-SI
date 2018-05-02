package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.FutureUN;

public class Node {
	int x;
	int y; 
	public Node(int x , int y) {
		this.x = x; 
		this.y = y; 
	}
	@Override
	public int hashCode() {
		final long prime = (long)( 1e9 )+7L;
		long result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return Long.hashCode( result );
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Node [x=" + x + ", y=" + y + "]";
	}
}
