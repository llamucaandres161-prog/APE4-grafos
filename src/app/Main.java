package app;

import negocio.Grafo;
import java.util.*;

public class Main {

    static void intentar(Grafo g, String a, String b) {
        System.out.print("agregarAmistad(" + a + ", " + b + ") -> ");
        try {
            g.agregarAmistad(a, b);
            System.out.println("OK");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Grafo g = new Grafo();

        // Vértices: usuarios de la red social
        g.agregarUsuario("P01", "Lucas");
        g.agregarUsuario("P02", "Sofía");
        g.agregarUsuario("P03", "Mateo");
        g.agregarUsuario("P04", "Valeria");
        g.agregarUsuario("P05", "Daniel");
        g.agregarUsuario("P06", "Camila");

        // Aristas: amistades
        g.agregarAmistad("P01", "P02");
        g.agregarAmistad("P02", "P03");
        g.agregarAmistad("P03", "P01");
        g.agregarAmistad("P01", "P04");
        g.agregarAmistad("P04", "P05");

        // 1) V, E y G = (V, E)
        System.out.println("=== 1) Conjuntos V y E ===");
        System.out.println("V = " + g.vertices());
        StringBuilder e = new StringBuilder();
        for (String[] ar : g.aristas()) e.append("(").append(ar[0]).append(", ").append(ar[1]).append("), ");
        System.out.println("E = {" + e.substring(0, e.length() - 2) + "}");
        System.out.println("|V| = " + g.numVertices() + ", |E| = " + g.numAristas());

        // 3) Grados
        System.out.println("\n=== 3) Grado de cada usuario ===");
        for (String u : g.vertices())
            System.out.println(g.nombre(u) + " (" + u + "): grado " + g.grado(u));
        System.out.println("Usuario(s) aislado(s): " + g.usuariosAislados());
        String top = g.usuarioMayorGrado();
        System.out.println("Mayor grado: " + g.nombre(top) + " (" + top + ") con " + g.grado(top));
        System.out.println("Suma de grados = " + g.sumaGrados() + " | 2 x |E| = " + (2 * g.numAristas())
                + " -> " + (g.sumaGrados() == 2 * g.numAristas() ? "SE CUMPLE" : "NO SE CUMPLE"));

        // 4) Ciclo y conectividad
        System.out.println("\n=== 4) Ciclo y conectividad ===");
        System.out.println("Ciclo encontrado: " + g.encontrarCiclo());
        System.out.println("Componentes conexas: " + g.componentesConexas());
        System.out.println("¿Todos conectados? " + (g.esConexo() ? "Sí" : "No"));

        // 5) Consultas
        System.out.println("\n=== 5) Consultas ===");
        System.out.println("existeRelacion(P02, P03) = " + g.existeRelacion("P02", "P03"));
        System.out.println("existeRelacion(P01, P02) = " + g.existeRelacion("P01", "P02"));
        System.out.println("existeRelacion(P02, P05) = " + g.existeRelacion("P02", "P05"));
        System.out.println("existeRelacion(P01, P05) = " + g.existeRelacion("P01", "P05"));
        System.out.println("vecinos(P04) = " + g.vecinos("P04"));
        System.out.println("grado(P04)   = " + g.grado("P04"));
        System.out.println("vecinos(P01) = " + g.vecinos("P01"));
        System.out.println("grado(P01)   = " + g.grado("P01"));

        // 6) Relaciones que deben rechazarse
        System.out.println("\n=== 6) Relaciones rechazadas ===");
        intentar(g, "P01", "P03");   // duplicada
        intentar(g, "P03", "P01");   // duplicada (orden invertido)
        intentar(g, "P01", "P01");   // lazo
        intentar(g, "P01", "P99");   // usuario inexistente
        System.out.println("Aristas tras los rechazos: " + g.numAristas() + " (el grafo no cambió)");

        // Modo interactivo opcional: java -cp out app.Main -i
        if (args.length > 0 && args[0].equals("-i")) menu(g);
    }

    static void menu(Grafo g) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- MENÚ ---\n1. Agregar usuario\n2. Agregar amistad\n3. ¿Existe relación?"
                    + "\n4. Vecinos\n5. Grado\n0. Salir");
            System.out.print("Opción: ");
            String op = sc.nextLine().trim();
            try {
                switch (op) {
                    case "1": System.out.print("ID: "); String id = sc.nextLine().trim();
                              System.out.print("Nombre: "); g.agregarUsuario(id, sc.nextLine().trim());
                              System.out.println("Usuario agregado."); break;
                    case "2": System.out.print("ID A: "); String a = sc.nextLine().trim();
                              System.out.print("ID B: "); g.agregarAmistad(a, sc.nextLine().trim());
                              System.out.println("Amistad registrada."); break;
                    case "3": System.out.print("ID A: "); String x = sc.nextLine().trim();
                              System.out.print("ID B: "); System.out.println(g.existeRelacion(x, sc.nextLine().trim())); break;
                    case "4": System.out.print("ID: "); System.out.println(g.vecinos(sc.nextLine().trim())); break;
                    case "5": System.out.print("ID: "); System.out.println(g.grado(sc.nextLine().trim())); break;
                    case "0": return;
                    default: System.out.println("Opción inválida.");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
