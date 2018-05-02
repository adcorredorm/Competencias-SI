package unalcol.agents.examples.labyrinth.multeseo.eater.sis20181.thunderCats;

public class Coordinate{

    private int x, y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int x(){ return x; }
    public int y(){ return y; }

    public void xUp(){ x++; }
    public void xDown(){ x--; }
    public void yUp(){ y++; }
    public void yDown(){ y--; }

    @Override
    public String toString(){
        StringBuilder S = new StringBuilder();
        S.append(x);
        S.append(',');
        S.append(y);
        return S.toString();
    }

    @Override
    public Coordinate clone(){
        return new Coordinate(x, y);
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Coordinate){
            return ((Coordinate) obj).x == x && ((Coordinate) obj).y == y;
        }else return false;
    }

    @Override
    public int hashCode(){
        return Integer.getInteger( Integer.toString(x,2) + Integer.toString(y,2) ,2);
    }

}
