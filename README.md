# 🦜 Little Birdie — Fabric 1.21.1

**Little Birdie** es un mod de Minecraft para Fabric que añade un minijuego inspirado en el juego "La banda de Trapo" de los SCG4 De Eufonia Studios. **Periquito** ejecuta una secuencia de movimientos que los jugadores deben memorizar y repetir antes de que se acabe el tiempo. El jugador que falle pierde una vida; si pierde las 5, muere en el juego.

---

## Requisitos

| Requisito | Versión |
|---|---|
| Minecraft | 1.21.1 |
| Fabric Loader | ≥ 0.16.10 |
| Fabric API | 0.116.12+1.21.1 |
| GeckoLib | 4.7.6 |
| Veil (Fabric) | 1.0.0.242 |
| Java | 21 |

> **Importante:** Veil y GeckoLib deben estar instalados como mods separados. El mod no funcionará sin ellos.

---

## Instalación

1. Descarga el `.jar` del mod desde la página de releases o desde la pagina del mod en Modrinth.
2. Colócalo en la carpeta `mods/` de tu instancia de Minecraft.
3. Descarga e instala las dependencias:
   - [Fabric API](https://modrinth.com/mod/fabric-api)
   - [GeckoLib](https://modrinth.com/mod/geckolib)
   - [Veil](https://modrinth.com/mod/veil)
4. Lanza el juego.

---

## El minijuego

### Cómo funciona

El minijuego es competitivo y multijugador. Un jugador con op lo inicia con el comando `/periquito iniciar`. Todos los jugadores seleccionados reciben la pantalla del juego simultáneamente y deben:

1. **Escuchar** la canción de introducción (~32 segundos).
2. **Memorizar** la secuencia de colores que el Periquito ejecuta mediante emotes y sonidos.
3. **Repetir** la secuencia correcta haciendo clic en los botones de colores dentro de **5 segundos por cada paso**.
4. **Sobrevivir** hasta completar todas las rondas configuradas.

Si un jugador falla un color o se le acaba el tiempo, pierde una vida. Con 0 vidas, el servidor lo mata y lo elimina del juego. Los demás jugadores continúan.

Al completar todas las rondas, el juego termina y la pantalla se cierra para los supervivientes.

### Colores y movimientos

Hay 11 colores/botones disponibles, cada uno asociado a un emote del Periquito, un sonido y un nombre de movimiento:

---

## Comandos

Todos los comandos requieren op.

### `/periquito iniciar <jugadores> <rondas> <numero de cuadros a repetirse en las rondas>`

Inicia el minijuego para un grupo de jugadores.

| Parámetro | Tipo | Rango | Descripción |
|---|---|---|---|
| `jugadores` | selector | cualquiera | Jugadores que participarán (`@a`, `@p`, nombres, etc.) |
| `rondas` | entero | 1–10 | Número de rondas que deben superar para ganar |
| `cuadros` | entero | 1–7 | Cuántos colores tendrá la secuencia a memorizar por ronda |

**Ejemplo:**
```
/periquito iniciar @a 3 5
```
Inicia una partida para todos los jugadores, con 3 rondas y secuencias de 5 colores.

---

### `/periquito añadir [pos]`

Spawnea un Periquito en la posición donde el operador está mirando (raycast de 10 bloques), o en las coordenadas especificadas. El Periquito hereda la orientación del jugador que ejecuta el comando.

**Ejemplo:**
```
/periquito añadir
/periquito añadir 100 64 200
```

> Se recomienda colocar un solo Periquito por arena. El juego automáticamente busca el Periquito más cercano al jugador dentro de un radio de 256 bloques al inicio de la pantalla.

---

### `/periquito eliminar`

Elimina todos los Periquitos dentro de un radio de 100 bloques del ejecutor del comando.

---

### `/periquito piso <modo>`

Cambia el modo visual (shader) de todos los bloques de Piso de Discoteca contiguos que estén bajo el ejecutor del comando.

| Parámetro | Rango | Descripción |
|---|---|---|
| `modo` | 1–10 | Efecto visual del shader (ver tabla de modos) |

**Ejemplo:**
```
/periquito piso 3
```

---

### `/periquito recalcular`

Recalcula el centro geométrico de la pista de Piso de Discoteca debajo del ejecutor. Útil si se añaden o quitan bloques después de haberla construido. El centro afecta a los shaders que usan coordenadas radiales (Circles, Spiral, Sunrays, Pulse).

---

## Bloques

### Piso de Discoteca

El **Piso de Discoteca** (`banda_de_trapo:disco_floor`) es el bloque central de la arena del minijuego. Funciona como superficie sobre la que los jugadores se paran durante el juego.

**Obtención:** Disponible en el grupo de ítems del mod en el inventario creativo.

### Modos de shader

El piso tiene 10 modos visuales, todos animados en tiempo real usando shaders GLSL (a través de Veil) y su equivalente CPU para el servidor:

| Modo | Nombre | Descripción |
|---|---|---|
| 1 | Circles | Anillos concéntricos de colores que se expanden desde el centro con bordes suavizados |
| 2 | Lava | Flujo orgánico de tonos cálidos naranja-rojo similares a lava en movimiento |
| 3 | Spiral | Espiral de 4 brazos con colores que rotan lentamente, basada en ángulo y distancia al centro |
| 4 | Diagonal Stripes | Bandas diagonales de colores que se desplazan con huecos negros entre ellas |
| 5 | Rainbow | Arcoíris continuo y saturado sin negro, basado en ondas sinusoidales |
| 6 | Waves | Similar al Rainbow pero con atenuación hacia el negro creando efecto de profundidad |
| 7 | Sunrays | 12 rayos dorados que irradian desde el centro con gradiente negro → naranja → oro → blanco |
| 8 | Pulse | Anillos concéntricos que se expanden como ondas de sonido en tonos cálidos pulsantes |
| 9 | Stars | Campo de estrellas pseudo-aleatorio donde cada bloque titila a su propia frecuencia |
| 10 | Aurora | Flujo orgánico en tonos violeta-azul-índigo similar a una aurora boreal |

> El modo 0 existe internamente como "apagado" (color gris oscuro), pero no es accesible por comando.

---

## El Periquito

El **Periquito** (`banda_de_trapo:periquito`) Está construido con GeckoLib para animaciones 3D fluidas.

**Propiedades de la entidad:**
- No es empujable ni atacable.
- No desaparece nunca (sin despawn natural).

---

## Configuración para servidores

Para usar este mod en un servidor multijugador de forma correcta, sigue estos pasos:

### Setup recomendado de la arena

1. Construye una pista de **Piso de Discoteca** del tamaño que desees (recomendado mínimo 7×7).
2. Coloca un Periquito frente a la pista con `/periquito añadir`.
3. Asegúrate de que el Periquito esté visible desde la zona de juego (la cámara orbita a 7 bloques de distancia y 4 bloques de altura).
4. Configura el modo de la pista con `/periquito piso <modo>`.
5. Usa `/periquito recalcular` si modificas la pista después.

### Flujo de una partida

```
1. Operador reúne a los jugadores en la arena.
2. Operador ejecuta: /periquito iniciar @a <rondas> <cuadros>
3. La pantalla del minijuego aparece a todos los jugadores.
4. El juego avanza automáticamente.
5. Los jugadores eliminados (0 vidas) mueren en el servidor y son removidos.
6. Al terminar todas las rondas, los supervivientes ganan y la pantalla se cierra.
```
### Sistema de cámara orbital

`OrbitalCameraSystem` implementa una cámara en tercera persona completamente personalizada con dos modos:

- **Modo PERIQUITO:** La cámara se posiciona 7.1 bloques detrás y 4.2 bloques arriba del Periquito, siguiendo su orientación. Se usa durante la canción, la introducción y la fase de mostrar el patrón.
- **Modo PLAYER:** La cámara orbita alrededor del jugador a 3.5 bloques de distancia y 1.2 de altura. El jugador puede rotar la cámara arrastrando el botón derecho del ratón.

### Shader del Piso de Discoteca

El piso tiene una implementación dual:

- **GPU (GLSL vía Veil):** Shader `disco_shader.fsh`/`.vsh` que recibe `GameTime`, `u_CenterX`, `u_CenterZ` y `u_Mode` como uniforms. Usa coordenadas de mundo interpoladas para calcular distancia y ángulo al centro.
- **CPU (Java):** `DiscoFloorRenderer` replica el mismo cálculo matemático bloque por bloque usando `Color.HSBtoRGB()` para generar el color de cada quad individualmente. Todos los bloques renderizan a fullbright (luz 15728880) para garantizar visibilidad en cualquier condición de iluminación.

El renderer extiende la distancia de render a 256 bloques y marca `rendersOutsideBoundingBox = true` para que el piso sea visible incluso cuando el chunk está parcialmente fuera del frustum.

---

## Compilar desde el código fuente

### Prerequisitos

- Java 21 (JDK)
- Git

### Pasos

```bash
git clone https://github.com/nondieddd/Little-Birdie.git
cd Little-Birdie
./gradlew build
```

El `.jar` resultante estará en `build/libs/`.

### Ejecutar en desarrollo

```bash
./gradlew runClient   # Cliente de desarrollo
./gradlew runServer   # Servidor de desarrollo
```

---

## Dependencias

| Mod | Versión | Uso |
|---|---|---|
| [Fabric API](https://modrinth.com/mod/fabric-api) | 0.116.12+1.21.1 | Networking, eventos, registro de entidades y bloques |
| [GeckoLib](https://modrinth.com/mod/geckolib) | 4.7.6 | Animaciones 3D del Periquito (modelo, huesos, emotes) |
| [Veil](https://modrinth.com/mod/veil) | 1.0.0.242 | Sistema de shaders GLSL personalizados para el piso |

---
## Licencia

Ver [LICENSE.txt](LICENSE.txt).
