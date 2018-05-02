package unalcol.agents.examples.labyrinth.multeseo.eater.SIS20181.Retoricos;

public class CostoUniforme extends Busqueda{
    @Override
    public Coleccion coleccion() {
        return new ColeccionColaPrioridad();
    }

}
