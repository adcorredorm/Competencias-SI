package unalcol.agents.examples.labyrinth.multeseo.eater;

public class Permutacion {
    public static int next(){  return (int)(Math.random()*6); }

    public static void main(String[] args) {
        int[] p = new int[6];
        for(int i = 0; i < 6; i++){
            p[i] = i;
        }
        for(int i = 0; i < 12; i++){
            int l= next();
            int j = next();
            int k=p[l];
            p[l] = p[j];
            p[j] = k;

        }
        for(int a : p) System.out.print(a + ",");
    }
}
