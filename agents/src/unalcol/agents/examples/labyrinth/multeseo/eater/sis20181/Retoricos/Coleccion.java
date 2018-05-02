package unalcol.agents.examples.labyrinth.multeseo.eater.SIS20181.Retoricos;


public interface Coleccion {
    public void adicionar(Arco a);
    public boolean esvacia();
    public Arco obtener();
    public void remover();
    public int tam();
}
