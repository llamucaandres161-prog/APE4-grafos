package negocio;

import modelo.Usuario;
import java.util.*;

/**
 * APE 4 - Fase 1: Reto de grafos y vértices (Estructura de Datos, UTA - FISEI).
 * Red social modelada como grafo simple, no dirigido y no ponderado,
 * implementado con lista de adyacencia.
 */
public class Grafo {

    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();        // id -> nombre
    private final Map<String, Set<String>> adyacencia = new LinkedHashMap<>(); // id -> vecinos
    private int numAristas = 0;

    // ---------- Operaciones de modificación ----------

    public void agregarUsuario(String id, String nombre) {
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Error: el identificador del usuario no puede estar vacío.");
        if (usuarios.containsKey(id))
            throw new IllegalArgumentException("Error: el usuario " + id + " ya existe.");
        usuarios.put(id, new Usuario(id, nombre));
        adyacencia.put(id, new LinkedHashSet<>());
    }

    public void agregarAmistad(String a, String b) {
        if (!usuarios.containsKey(a) || !usuarios.containsKey(b)) {
            String falta = !usuarios.containsKey(a) ? a : b;
            throw new IllegalArgumentException("Error: el usuario " + falta + " no existe en la red.");
        }
        if (a.equals(b))
            throw new IllegalArgumentException("Error: un usuario no puede establecer una amistad consigo mismo.");
        if (adyacencia.get(a).contains(b))
            throw new IllegalArgumentException("Solicitud rechazada: Estos usuarios ya están registrados como amigos.");
        adyacencia.get(a).add(b);
        adyacencia.get(b).add(a);   // no dirigido: la amistad es mutua
        numAristas++;
    }

    // ---------- Consultas ----------

    public boolean existeRelacion(String a, String b) {
        return adyacencia.containsKey(a) && adyacencia.get(a).contains(b);
    }

    public Set<String> vecinos(String id) {
        verificarUsuario(id);
        return Collections.unmodifiableSet(adyacencia.get(id));
    }

    public int grado(String id) {
        verificarUsuario(id);
        return adyacencia.get(id).size();
    }

    public Set<String> vertices() { return Collections.unmodifiableSet(usuarios.keySet()); }

    public String nombre(String id) { verificarUsuario(id); return usuarios.get(id).getNombre(); }

    public int numVertices() { return usuarios.size(); }

    public int numAristas() { return numAristas; }

    /** Lista de aristas sin repetir (a, b) con a antes que b en el orden de inserción. */
    public List<String[]> aristas() {
        List<String[]> lista = new ArrayList<>();
        Set<String> visitados = new HashSet<>();
        for (String u : usuarios.keySet()) {
            for (String v : adyacencia.get(u))
                if (!visitados.contains(v)) lista.add(new String[]{u, v});
            visitados.add(u);
        }
        return lista;
    }

    public int sumaGrados() {
        int suma = 0;
        for (String u : usuarios.keySet()) suma += grado(u);
        return suma;
    }

    public List<String> usuariosAislados() {
        List<String> r = new ArrayList<>();
        for (String u : usuarios.keySet()) if (grado(u) == 0) r.add(u);
        return r;
    }

    public String usuarioMayorGrado() {
        String mejor = null;
        for (String u : usuarios.keySet()) if (mejor == null || grado(u) > grado(mejor)) mejor = u;
        return mejor;
    }

    /** Busca un ciclo (DFS). Devuelve la lista de vértices cerrando en el inicial, o lista vacía. */
    public List<String> encontrarCiclo() {
        Set<String> visitados = new HashSet<>();
        for (String inicio : usuarios.keySet()) {
            if (visitados.contains(inicio)) continue;
            Deque<String> camino = new ArrayDeque<>();
            List<String> ciclo = dfsCiclo(inicio, null, visitados, camino);
            if (!ciclo.isEmpty()) return ciclo;
        }
        return Collections.emptyList();
    }

    private List<String> dfsCiclo(String u, String padre, Set<String> visitados, Deque<String> camino) {
        visitados.add(u);
        camino.addLast(u);
        for (String v : adyacencia.get(u)) {
            if (v.equals(padre)) continue;
            if (camino.contains(v)) {                       // arista de retorno => ciclo
                List<String> ciclo = new ArrayList<>();
                boolean copiar = false;
                for (String x : camino) {
                    if (x.equals(v)) copiar = true;
                    if (copiar) ciclo.add(x);
                }
                ciclo.add(v);
                return ciclo;
            }
            if (!visitados.contains(v)) {
                List<String> r = dfsCiclo(v, u, visitados, camino);
                if (!r.isEmpty()) return r;
            }
        }
        camino.removeLast();
        return Collections.emptyList();
    }

    /** Componentes conexas (BFS). */
    public List<Set<String>> componentesConexas() {
        List<Set<String>> comps = new ArrayList<>();
        Set<String> visitados = new HashSet<>();
        for (String inicio : usuarios.keySet()) {
            if (visitados.contains(inicio)) continue;
            Set<String> comp = new LinkedHashSet<>();
            Queue<String> cola = new ArrayDeque<>();
            cola.add(inicio);
            visitados.add(inicio);
            while (!cola.isEmpty()) {
                String u = cola.poll();
                comp.add(u);
                for (String v : adyacencia.get(u))
                    if (visitados.add(v)) cola.add(v);
            }
            comps.add(comp);
        }
        return comps;
    }

    public boolean esConexo() { return componentesConexas().size() <= 1; }

    private void verificarUsuario(String id) {
        if (!usuarios.containsKey(id))
            throw new IllegalArgumentException("Error: el usuario " + id + " no existe en la red.");
    }
}
