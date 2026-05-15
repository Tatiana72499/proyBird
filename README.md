# Flappy Bird OpenGL 3.3 con LWJGL

Proyecto Java Maven hecho con **LWJGL + GLFW + OpenGL 3.3 core profile**.
El juego base tipo Flappy Bird fue refactorizado para que quede ordenado, entendible y facil de defender en un parcial de Programacion Grafica.

## Objetivo del proyecto

Esta version busca cumplir tres cosas al mismo tiempo:

- que el juego siga funcionando de forma estable
- que el codigo sea facil de explicar clase por clase
- que sea simple de modificar en vivo durante una defensa oral

## Tecnologias usadas

- Java 17
- Maven
- LWJGL 3.3.3
- GLFW
- OpenGL 3.3 Core Profile

## Requisitos

- Windows
- Java 17 o superior
- Maven instalado

El `pom.xml` esta preparado con `natives-windows` y no usa `-XstartOnFirstThread`.

## Compilar

```bash
mvn clean compile
```

## Ejecutar

```bash
mvn exec:exec "-DmainClass=com.graphics.AppFlappyBird"
```

Tambien puedes correrlo en dos pasos:

```bash
mvn clean compile
mvn exec:exec "-DmainClass=com.graphics.AppFlappyBird"
```

## Controles

### Durante el juego

- `ESPACIO`: salto del Jugador 1
- `W` o `FLECHA ARRIBA`: salto del Jugador 2
- `ESC`: cerrar el juego

### Menus

- `ENTER`: abrir inicio, reiniciar o confirmar flujo principal
- `1`: elegir modo de 1 jugador
- `2`: elegir modo de 2 jugadores
- Click del mouse:
  - `START`: abrir seleccion de jugadores
  - boton izquierdo de `GAME OVER`: reiniciar
  - boton derecho de `GAME OVER`: volver a elegir modo

## Caracteristicas implementadas

- Pajaro compuesto por figuras geometricas:
  - cuerpo
  - vientre
  - ala
  - pico
  - cola
  - ojo y pupila
- Dos jugadores simultaneos en la misma ventana
- Puntaje independiente para cada jugador
- Tuberias compartidas
- Game over solo cuando ambos jugadores mueren
- Dificultad progresiva segun el mejor puntaje de la partida
- Fondo con cielo, nubes, montanas, arbustos y suelo
- Pantalla de inicio
- Selector de 1 o 2 jugadores
- Pantalla de game over
- Sonidos por estado y accion:
  - menu
  - partida
  - salto
  - punto
  - golpe
  - game over

## Estructura del proyecto

```text
src/main/java/com/graphics/
  AppFlappyBird.java

src/main/java/com/graphics/core/
  Game.java
  InputManager.java
  MusicPlayer.java
  Window.java

src/main/java/com/graphics/entities/
  Bird.java
  Pipe.java

src/main/java/com/graphics/render/
  BackgroundRenderer.java
  HudRenderer.java
  Renderer.java
  ShapeRenderer.java

src/main/java/com/graphics/systems/
  CollisionSystem.java
  DifficultySystem.java
  PipeSpawner.java

src/main/java/com/graphics/utils/
  Color.java
  Constants.java
  Vec2.java
```

## Explicacion breve de cada clase

### `AppFlappyBird`

Contiene solo el `main`. Su trabajo es crear `Game` y ejecutar `run()`.

### `Game`

Es la clase principal del juego. Maneja:

- inicializacion
- bucle principal
- lectura de input
- actualizacion de fisica
- puntajes
- colisiones
- cambio de estados
- render
- limpieza final

Estados usados:

- `START`
- `PLAYER_SELECT`
- `PLAYING`
- `GAME_OVER`

### `Window`

Encapsula GLFW y OpenGL:

- crea la ventana
- crea el contexto OpenGL
- activa blending
- procesa eventos
- cambia el titulo
- convierte la posicion del mouse a coordenadas del juego

### `InputManager`

Centraliza entrada de teclado y mouse.
Se encarga especialmente de detectar pulsaciones simples para que una tecla no ejecute muchas veces la misma accion en un solo toque.

### `MusicPlayer`

Genera sonidos en memoria sin depender de archivos de audio externos.
Tiene sonidos para:

- menu
- juego
- salto
- punto
- golpe
- game over

### `Bird`

Representa a un jugador.
Cada pajaro tiene:

- posicion
- velocidad vertical
- estado vivo o muerto
- color propio
- puntaje propio
- tecla de salto propia

La fisica usa:

- gravedad
- impulso de salto
- limite de velocidad de caida

### `Pipe`

Representa una tuberia compartida entre jugadores.
Se mueve hacia la izquierda y verifica:

- si ya salio de pantalla
- si colisiona con un pajaro
- si cada jugador ya la puntuo

### `PipeSpawner`

Genera nuevas tuberias con un temporizador.
La posicion vertical del hueco se elige aleatoriamente dentro de un rango controlado.

### `DifficultySystem`

Calcula la dificultad segun el mayor puntaje entre los jugadores.
Aumenta:

- velocidad de tuberias

Reduce:

- intervalo de aparicion

Todo con limites maximos y minimos para que siga siendo jugable.

### `CollisionSystem`

Revisa colisiones:

- techo
- suelo
- tuberias

Si un pajaro choca, solo ese jugador muere.

### `Renderer`

Coordina el orden de dibujo:

1. fondo
2. tuberias
3. pajaros
4. HUD o pantallas especiales

### `ShapeRenderer`

Es el renderer geometrico base.
Permite dibujar:

- rectangulos
- triangulos
- circulos
- lineas

Usa un shader basico, un VAO y un VBO para reutilizar la misma estructura OpenGL.

### `BackgroundRenderer`

Dibuja el escenario del juego:

- cielo
- sol
- nubes
- montanas
- arbustos
- suelo

### `HudRenderer`

Dibuja la interfaz:

- HUD superior
- puntajes
- velocidad
- pantalla de inicio
- seleccion de jugadores
- pantalla de game over

## Como funciona el juego

### 1. Bucle principal

Cada frame hace este orden:

1. leer eventos de GLFW
2. procesar input
3. actualizar logica del juego
4. renderizar
5. intercambiar buffers

### 2. Fisica del pajaro

La fisica es simple:

1. al saltar se asigna una velocidad vertical positiva
2. en cada frame la gravedad reduce esa velocidad
3. la posicion `y` se actualiza con esa velocidad
4. el angulo visual del pajaro depende de la velocidad vertical

### 3. Colisiones

Las colisiones se calculan con cajas simples:

- el pajaro usa una caja de colision
- las tuberias usan rectangulos para cuerpo y tapas
- si las cajas se solapan, hay choque

### 4. Dificultad

La dificultad toma el mayor puntaje entre los jugadores y con eso:

- sube la velocidad de las tuberias
- baja el tiempo entre tuberias
- calcula un nivel numerico

### 5. Render del pajaro

El pajaro no es una sola figura.
Se compone con varias primitivas:

- circulos para cuerpo y ojo
- triangulos para ala, pico y cola

Todas las piezas se calculan alrededor del centro para que al inclinarse se muevan juntas.

## Cambios principales respecto a la version base

- Se separo una clase grande en varias clases por responsabilidad
- Se agrego modo de 2 jugadores
- Se mejoro la interfaz visual
- Se agrego selector de jugadores
- Se agrego pantalla de game over
- Se agrego musica y efectos
- Se mejoro la deteccion de colisiones contra las tuberias
- Se dejaron constantes centralizadas para facilitar cambios rapidos

## Nota para la defensa

Si te preguntan por que esta implementacion es buena para un parcial, puedes responder:

- porque separa logica, render, entidades y utilidades
- porque evita soluciones demasiado avanzadas o dificiles de justificar
- porque toda la parte visual se construye con primitivas OpenGL faciles de mostrar
- porque los valores importantes estan en `Constants`
- porque el codigo ya tiene comentarios en las partes clave

## Verificacion

Compilacion validada con:

```bash
mvn clean compile
```
