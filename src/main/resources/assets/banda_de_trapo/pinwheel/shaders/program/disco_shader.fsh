#version 150

uniform float GameTime;
uniform float u_CenterX;
uniform float u_CenterZ;
uniform int u_Mode;

in vec4 v_Color;
in vec2 v_WorldPos;

out vec4 fragColor;

const float PI = 3.14159265359;

// ==========================================
// UTILIDADES DE COLOR
// ==========================================
vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

// ==========================================
// MODO 1 — CIRCLES
// Anillos más pequeños y detallados con borde suavizado
// ==========================================
vec3 mode1_Circles(float dist, float time) {
    float ring = fract(dist * 0.13 - time * 0.5);
    float hue = fract(dist * 0.04 - time * 0.08);

    float inBand = step(ring, 0.60);
    float edgeFade = sin(ring / 0.60 * PI);
    float bright = inBand * (0.6 + 0.4 * edgeFade);

    return hsv2rgb(vec3(hue, 1.0, bright));
}

// ==========================================
// MODO 2 — LAVA (sin cambios)
// ==========================================
vec3 mode2_Lava(vec2 wpos, float time) {
    float n = sin(wpos.x * 0.30 + time * 0.9) * cos(wpos.y * 0.28 + time * 0.7)
    + sin((wpos.x + wpos.y) * 0.18 - time * 0.5)
    + cos(wpos.x * 0.12 - wpos.y * 0.15 + time * 0.4);
    float hue    = clamp(n * 0.04 + 0.04, 0.0, 0.11);
    float bright = clamp(n * 0.35 + 0.65, 0.3, 1.0);
    float sat    = clamp(1.0 - n * 0.05, 0.85, 1.0);
    return hsv2rgb(vec3(hue, sat, bright));
}

// ==========================================
// MODO 3 — SPIRAL
// Espiral con bandas negras duras como imagen de referencia
// ==========================================
vec3 mode3_Spiral(float dist, float angle, float time) {
    float arms = 4.0;
    float normalizedAngle = angle / (2.0 * PI) + 0.5;

    float spiralParam = fract(normalizedAngle * arms + dist * 0.10 - time * 0.25);

    // Cada brazo tiene su color: hue del ÁNGULO, rota lento con el tiempo
    float hue = fract(normalizedAngle + time * 0.04);

    float armWidth = max(0.3, 1.8 - dist * 0.035);
    // Difuminado suave dentro del brazo
    float bright = pow(sin(spiralParam * PI), armWidth) * 0.95;

    return hsv2rgb(vec3(hue, 0.95, bright));
}
// ==========================================
// MODO 4 — DIAGONAL STRIPES
// ==========================================
vec3 mode4_DiagonalStripes(vec2 wpos, float time) {
    float pos = (wpos.x + wpos.y) * 0.55 - time * 3.5;
    float stripe = mod(pos, 14.0);

    if (stripe < 0.5 || stripe > 8.5) {
        return vec3(0.0);
    }

    float bandPos = (stripe - 0.5) / 8.0;
    float fadeSin = sin(bandPos * PI);

    float hue = fract((wpos.x + wpos.y) * 0.055 - time * 0.10);
    float brightness = 0.3 + 0.7 * fadeSin;

    return hsv2rgb(vec3(hue, 1.0, brightness));
}

// ==========================================
// MODO 5 — RAINBOW (sin negro)
// ==========================================
vec3 mode5_Rainbow(vec2 wpos, float time) {
    float wave = sin(wpos.x * 0.22 + time * 0.5) + cos(wpos.y * 0.22 + time * 0.4);
    float hue  = fract(wave * 0.18 + (wpos.x + wpos.y) * 0.04 - time * 0.12);
    return hsv2rgb(vec3(hue, 1.0, 1.0));
}

// ==========================================
// MODO 6 — WAVES (rainbow difuminado al negro)
// ==========================================
vec3 mode6_Waves(vec2 wpos, float time) {
    float wave6a = sin(wpos.x * 0.20 + time * 0.45);
    float wave6b = cos(wpos.y * 0.18 + time * 0.38);
    float waveSum = wave6a + wave6b;
    float hue = fract(waveSum * 0.18 + (wpos.x + wpos.y) * 0.04 - time * 0.12);
    float brightWave = sin(wpos.x * 0.15 + wpos.y * 0.10 + time * 0.30) * 0.4
    + cos(wpos.x * 0.08 - wpos.y * 0.14 + time * 0.25) * 0.3
    + 0.5;
    float brightness = clamp(brightWave, 0.0, 1.0);
    brightness = brightness * brightness;
    return hsv2rgb(vec3(hue, 1.0, brightness));
}

// ==========================================
// MODO 7 — SUNRAYS (rayos dorados del centro)
// ==========================================
vec3 mode7_Sunrays(vec2 wpos, float dist, float angle, float time) {
    // Rayos que salen del centro basados en el ángulo
    float rays = 12.0;
    float ray = sin(angle * rays + time * 0.5) * 0.5 + 0.5;
    // Más intenso cerca del centro, se apaga lejos
    float fade = 1.0 / (1.0 + dist * 0.08);
    float intensity = pow(ray, 3.0) * fade;
    // Paleta: negro → naranja → amarillo dorado → blanco en el pico
    vec3 dark   = vec3(0.0, 0.0, 0.0);
    vec3 orange = vec3(1.0, 0.35, 0.0);
    vec3 gold   = vec3(1.0, 0.85, 0.1);
    vec3 white  = vec3(1.0, 0.98, 0.85);
    vec3 col = mix(dark, orange, smoothstep(0.0, 0.35, intensity));
    col = mix(col, gold,  smoothstep(0.35, 0.7, intensity));
    col = mix(col, white, smoothstep(0.7,  1.0, intensity));
    return col;
}

// ==========================================
// MODO 8 — PULSE (latido radial cálido)
// ==========================================
vec3 mode8_Pulse(vec2 wpos, float dist, float time) {
    // Anillos que se expanden desde el centro como ondas de sonido
    float wave1 = sin(dist * 0.4 - time * 2.0) * 0.5 + 0.5;
    float wave2 = sin(dist * 0.4 - time * 2.0 + 2.1) * 0.5 + 0.5;
    float wave3 = sin(dist * 0.4 - time * 2.0 + 4.2) * 0.5 + 0.5;
    // RGB independientes para colores cálidos que pulsan
    float r = pow(wave1, 2.5);
    float g = pow(wave2, 4.0) * 0.4;
    float b = pow(wave3, 2.5) * 0.6;
    // Fade con la distancia
    float fade = clamp(1.0 - dist * 0.05, 0.0, 1.0);
    return vec3(r, g, b) * fade;
}

// ==========================================
// MODO 9 — STARS (estrellas que titilan)
// ==========================================
vec3 mode9_Stars(vec2 wpos, float time) {
    // Hash para posición pseudo-aleatoria de cada tile
    vec2 cell = floor(wpos);
    float hash = fract(sin(dot(cell, vec2(127.1, 311.7))) * 43758.5453);
    float hash2 = fract(sin(dot(cell, vec2(269.5, 183.3))) * 12345.6789);
    float hash3 = fract(sin(dot(cell, vec2(419.2, 371.9))) * 99999.1234);

    // Cada estrella titila a su propia frecuencia
    float twinkle = sin(time * (0.3 + hash * 0.8) + hash2 * 6.28) * 0.5 + 0.5;
    twinkle = pow(twinkle, 3.0);

    // Solo algunas celdas son estrellas (30% de tiles)
    float isStar = step(0.70, hash);
    float brightness = twinkle * isStar;

    // Color: mayoría blanco-azulado, algunas doradas, pocas rojizas
    vec3 blue  = vec3(0.7, 0.85, 1.0);
    vec3 gold  = vec3(1.0, 0.9, 0.4);
    vec3 red   = vec3(1.0, 0.4, 0.2);
    vec3 starColor = mix(blue, gold, step(0.85, hash));
    starColor = mix(starColor, red, step(0.95, hash));

    // Fondo negro con pequeño ruido azul oscuro
    vec3 bg = vec3(0.01, 0.01, 0.04) * (0.5 + 0.5 * hash3);

    return bg + starColor * brightness;
}