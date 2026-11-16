# APE 4 · Fase 1 — Red social como grafo (Java)

**Asignatura:** Estructura de Datos — Universidad Técnica de Ambato (FISEI), Ingeniería en Software, 3B
**Docente:** Ing. José Rubén Caiza Caizabuano, PhD.
**Autor:** Andres Llamuca

## Descripción

Modelado de una red social como un **grafo simple, no dirigido y no ponderado**, implementado con **lista de adyacencia** (`Map<String, Set<String>>`).

- **Vértices (V):** los usuarios P01 a P06 (Lucas, Sofía, Mateo, Valeria, Daniel, Camila).
- **Aristas (E):** las amistades `{P01,P02}, {P01,P03}, {P01,P04}, {P02,P03}, {P04,P05}`.

El programa resuelve las preguntas de la Fase 1: conjuntos V y E, grados (con la comprobación suma de grados = 2·|E|), ciclo, conectividad, consultas (`existeRelacion`, `vecinos`, `grado`) y rechazo de relaciones inválidas.

## Estructura

```
src/
├── modelo/
│   └── Usuario.java      # Vértice: id y nombre
├── negocio/
│   └── Grafo.java        # Lógica del grafo: usuarios, amistades, consultas, ciclo, conexidad
└── app/
    └── Main.java         # Programa principal (demo de la APE y menú interactivo)
```

## Requisitos

- JDK 11 o superior (probado con Java 21)

## Compilar y ejecutar

Desde la raíz del proyecto:

**Linux / macOS**
```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out app.Main
```

**Windows (PowerShell)**
```powershell
mkdir out
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out app.Main
```

Para el menú interactivo, agrega `-i`: `java -cp out app.Main -i`

> Si en consola salen mal las tildes: `java -Dfile.encoding=UTF-8 -cp out app.Main`

## Operaciones de `Grafo`

| Método | Descripción |
|---|---|
| `agregarUsuario(id, nombre)` | Agrega un vértice (rechaza id vacío o repetido) |
| `agregarAmistad(a, b)` | Agrega una arista; rechaza lazos, duplicados y usuarios inexistentes |
| `existeRelacion(a, b)` | `true` si a y b son amigos |
| `vecinos(id)` | Amigos directos del usuario |
| `grado(id)` | Número de amigos |
| `encontrarCiclo()` | Busca un ciclo con DFS |
| `componentesConexas()` / `esConexo()` | Conectividad con BFS |

## Resultado esperado

```
V = [P01, P02, P03, P04, P05, P06]
E = {(P01, P02), (P01, P03), (P01, P04), (P02, P03), (P04, P05)}
Suma de grados = 10 | 2 x |E| = 10 -> SE CUMPLE
Ciclo encontrado: [P01, P02, P03, P01]
¿Todos conectados? No
existeRelacion(P02, P03) = true
existeRelacion(P02, P05) = false
agregarAmistad(P01, P03) -> Solicitud rechazada: Estos usuarios ya están registrados como amigos.
agregarAmistad(P01, P01) -> Error: un usuario no puede establecer una amistad consigo mismo.
agregarAmistad(P01, P99) -> Error: el usuario P99 no existe en la red.
```
