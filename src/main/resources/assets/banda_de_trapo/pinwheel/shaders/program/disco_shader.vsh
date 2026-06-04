#version 150

in vec3 Position;
in vec4 Color;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform float u_BlockX;
uniform float u_BlockZ;

out vec4 v_Color;
out vec2 v_WorldPos;

void main() {
	gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
	v_Color = Color;
	// Coordenadas del mundo: posición base del bloque + offset local del vértice
	v_WorldPos = vec2(u_BlockX + Position.x, u_BlockZ + Position.z);
}