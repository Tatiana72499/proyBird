# Flappy Bird OpenGL 3.3 - Primer Parcial

## Estudiante

- Tatiana

## Resumen

Este proyecto corresponde a la entrega del primer parcial de Programacion Grafica.  
Se desarrolló sobre una base inicial tipo Flappy Bird en Java con LWJGL y OpenGL 3.3 Core Profile, reorganizando el codigo en varias clases y agregando mejoras de jugabilidad, interfaz y sonido.

El objetivo principal de esta version es que el proyecto:

- compile y ejecute de forma estable
- cumpla los requerimientos del enunciado
- sea facil de explicar y modificar durante la defensa oral

## Tecnologias utilizadas

- Java 17
- Maven
- LWJGL 3.3.3
- GLFW
- OpenGL 3.3 Core Profile

## Requisitos

- Windows
- Java 17 o superior
- Maven instalado

El proyecto usa `natives-windows` en el `pom.xml` y no requiere `-XstartOnFirstThread`.

## Compilacion

```bash
mvn clean compile
```

## Ejecucion

```bash
mvn exec:exec "-DmainClass=com.graphics.AppFlappyBird"
```

En Windows tambien puedes usar los lanzadores incluidos:

- `run-flappy.bat`
- `run-flappy.ps1`

## Controles

### Juego

- `ESPACIO`: salto del Jugador 1
- `W` o `FLECHA ARRIBA`: salto del Jugador 2
- `ESC`: cerrar el juego

### Menus

- `ENTER`: abrir inicio o reiniciar
- `1`: elegir modo de 1 jugador
- `2`: elegir modo de 2 jugadores
- Click izquierdo:
  - `START`: abrir seleccion de jugadores
  - boton izquierdo en `GAME OVER`: reiniciar
  - boton derecho en `GAME OVER`: volver a elegir modo

## Requerimientos del enunciado y como se cumplen

### 1. Pajaro compuesto por figuras geometricas

El pajaro ya no es un rectangulo unico. Se dibuja con varias primitivas OpenGL:

- cuerpo principal
- vientre
- ala
- pico
- cola
- ojo y pupila

Ademas:

- el ala tiene una animacion simple
- el pajaro se inclina segun su velocidad vertical
- todas las piezas se calculan alrededor del centro para mantener una composicion coherente

### 2. Modo de dos jugadores simultaneos

El juego soporta dos jugadores en la misma ventana:

- Jugador 1: `ESPACIO`
- Jugador 2: `W` o `FLECHA ARRIBA`

Cada jugador tiene:

- su propio pajaro
- su propia posicion
- su propia velocidad vertical
- su propio estado vivo o muerto
- su propio puntaje
- su propio color

Las tuberias son compartidas y la partida termina solo cuando ambos jugadores mueren.

### 3. Incremento progresivo de dificultad

La dificultad aumenta segun el mejor puntaje de la partida:

- aumenta la velocidad de las tuberias
- disminuye el tiempo entre apariciones
- existe un limite maximo y minimo para mantener la jugabilidad

La velocidad actual se muestra en el HUD superior.

### 4. Mejora de la interfaz del juego

Se mejoró la presentacion con:

- cielo, sol, nubes, montañas, arbustos y suelo
- pantalla de inicio
- pantalla de seleccion de jugadores
- pantalla de game over
- HUD superior con puntajes y velocidad
- sonido para menu, partida, salto, punto, golpe y game over

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

## Explicacion breve de las clases principales

- `AppFlappyBird`: contiene solo el `main` y arranca el juego.
- `Game`: controla el ciclo principal, el estado del juego, el input, la actualizacion, el render y la limpieza.
- `Window`: crea la ventana GLFW, inicializa OpenGL y actualiza el titulo.
- `InputManager`: centraliza teclado y mouse.
- `MusicPlayer`: genera sonidos simples en memoria para no depender de archivos de audio externos.
- `Bird`: representa a cada jugador, con fisica, puntaje y render compuesto.
- `Pipe`: representa cada tuberia compartida.
- `PipeSpawner`: genera tuberias usando un temporizador.
- `DifficultySystem`: calcula velocidad e intervalo segun el puntaje.
- `CollisionSystem`: detecta choques con suelo, techo y tuberias.
- `Renderer`: define el orden de dibujo general.
- `ShapeRenderer`: dibuja rectangulos, triangulos, circulos y lineas con OpenGL.
- `BackgroundRenderer`: dibuja el escenario del fondo.
- `HudRenderer`: dibuja la interfaz, menus y pantalla final.

## Funcionamiento general del juego

### Bucle principal

En cada frame el juego realiza este orden:

1. leer eventos de la ventana
2. procesar input
3. actualizar la logica
4. renderizar la escena
5. intercambiar buffers

### Fisica del pajaro

La fisica se basa en:

- gravedad
- impulso al saltar
- velocidad maxima de caida

La posicion vertical del pajaro cambia segun su velocidad, y su inclinacion visual depende de ese movimiento.

### Colisiones

Las colisiones se calculan con figuras simples:

- el pajaro usa una caja de colision
- las tuberias usan el cuerpo y las tapas visibles

Esto hace que el choque coincida mejor con lo que se ve en pantalla.

### Dificultad

La dificultad usa el mejor puntaje entre los jugadores para:

- aumentar la velocidad
- reducir el intervalo entre tuberias
- calcular el nivel actual

### Render del pajaro

El pajaro se construye con primitivas geometricas:

- circulos para cuerpo y ojo
- triangulos para ala, pico y cola

Todas las piezas se dibujan respecto al centro del pajaro, de forma que la animacion y la rotacion se mantengan coherentes.

## Cambios respecto a la version base

- se separo la logica en clases por responsabilidad
- se reemplazo el pajaro rectangular por uno compuesto
- se agrego modo de dos jugadores
- se agrego dificultad progresiva
- se mejoro la interfaz visual
- se agregaron sonidos por estado y accion
- se agregaron pantallas de inicio y game over
- se mejoro la deteccion de colisiones

## Nota para la defensa oral

Este proyecto fue organizado para que sea facil de defender:

- los valores ajustables estan en `Constants`
- la logica del juego esta centralizada en `Game`
- cada entidad y sistema tiene una responsabilidad clara
- el render se basa en primitivas geometricas faciles de explicar
- los comentarios del codigo son simples y directos

## Verificacion

Comando de compilacion esperado:

```bash
mvn clean compile
```

Comando de ejecucion esperado:

```bash
mvn exec:exec "-DmainClass=com.graphics.AppFlappyBird"
```
