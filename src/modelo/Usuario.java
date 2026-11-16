package modelo;

/** Vértice del grafo: un usuario de la red social. */
public class Usuario {

    private final String id;      // identificador, ej. "P01"
    private final String nombre;  // ej. "Lucas"

    public Usuario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() { return id; }

    public String getNombre() { return nombre; }

    @Override
    public String toString() { return nombre + " (" + id + ")"; }
}
