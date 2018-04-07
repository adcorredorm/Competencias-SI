package unalcol.agents.search.Presentacion;

public class NodoPuzzle {

    private int[][] puzzle;
    private int posx, posy;


    public int[][] getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(int[][] puzzle) {
        this.puzzle = puzzle;
    }

    public int[] getPos() {
        return new int[]{posx, posy};
    }

    public void setPos(int posx, int posy) {
        this.posx = posx;
        this.posy = posy;
    }

    public NodoPuzzle(int[][] M, int posx, int posy){
        puzzle = M;
        this.posx = posx;
        this.posy = posy;
    }

    public NodoPuzzle(){
        this(Objetivo.Default_puzzle, 3, 3);
    }

}
