package com.graphics.render;

import com.graphics.utils.Color;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

// Encapsula el dibujo de figuras geometricas simples en OpenGL 3.3 core.
// Se usa un shader basico y se envian vertices ya transformados en CPU.
public class ShapeRenderer {

    private int program;
    private int vao;
    private int vbo;
    private int colorLocation;

    // Crea el VAO y VBO que se reutilizan para todas las figuras.
    public void init() {
        createProgram();
        vao = GL30.glGenVertexArrays();
        vbo = GL15.glGenBuffers();

        GL30.glBindVertexArray(vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, 4096L * Float.BYTES, GL15.GL_DYNAMIC_DRAW);
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 2 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    // Shader minimo para pintar geometria 2D con un color uniforme.
    private void createProgram() {
        String vertexSource = """
                #version 330 core
                layout (location = 0) in vec2 aPos;
                void main() {
                    gl_Position = vec4(aPos, 0.0, 1.0);
                }
                """;

        String fragmentSource = """
                #version 330 core
                uniform vec4 uColor;
                out vec4 fragColor;
                void main() {
                    fragColor = uColor;
                }
                """;

        int vertexShader = compileShader(GL20.GL_VERTEX_SHADER, vertexSource);
        int fragmentShader = compileShader(GL20.GL_FRAGMENT_SHADER, fragmentSource);

        program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertexShader);
        GL20.glAttachShader(program, fragmentShader);
        GL20.glLinkProgram(program);

        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            throw new IllegalStateException("Error al enlazar shader: " + GL20.glGetProgramInfoLog(program));
        }

        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);
        colorLocation = GL20.glGetUniformLocation(program, "uColor");
    }

    private int compileShader(int type, String source) {
        int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);
        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            throw new IllegalStateException("Error al compilar shader: " + GL20.glGetShaderInfoLog(shader));
        }
        return shader;
    }

    public void beginFrame() {
        GL20.glUseProgram(program);
        GL30.glBindVertexArray(vao);
    }

    public void endFrame() {
        GL30.glBindVertexArray(0);
        GL20.glUseProgram(0);
    }

    public void drawRect(float x, float y, float width, float height, Color color) {
        drawRect(x, y, width, height, 0.0f, color);
    }

    public void drawRect(float x, float y, float width, float height, float angle, Color color) {
        float hw = width * 0.5f;
        float hh = height * 0.5f;

        float[] corners = new float[] {
                -hw, -hh,
                 hw, -hh,
                 hw,  hh,
                -hw, -hh,
                 hw,  hh,
                -hw,  hh
        };

        float[] vertices = new float[corners.length];
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        for (int i = 0; i < corners.length; i += 2) {
            float rx = corners[i] * cos - corners[i + 1] * sin;
            float ry = corners[i] * sin + corners[i + 1] * cos;
            vertices[i] = x + rx;
            vertices[i + 1] = y + ry;
        }
        drawVertices(vertices, color);
    }

    public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Color color) {
        drawVertices(new float[] {x1, y1, x2, y2, x3, y3}, color);
    }

    // El circulo se aproxima con varios triangulos alrededor del centro.
    public void drawCircle(float centerX, float centerY, float radius, Color color, int segments) {
        int triangles = Math.max(segments, 12);
        float[] vertices = new float[triangles * 6];
        int index = 0;
        for (int i = 0; i < triangles; i++) {
            float angleA = (float) (Math.PI * 2.0 * i / triangles);
            float angleB = (float) (Math.PI * 2.0 * (i + 1) / triangles);

            vertices[index++] = centerX;
            vertices[index++] = centerY;
            vertices[index++] = centerX + (float) Math.cos(angleA) * radius;
            vertices[index++] = centerY + (float) Math.sin(angleA) * radius;
            vertices[index++] = centerX + (float) Math.cos(angleB) * radius;
            vertices[index++] = centerY + (float) Math.sin(angleB) * radius;
        }
        drawVertices(vertices, color);
    }

    // Una linea gruesa se dibuja como un rectangulo inclinado.
    public void drawLine(float x1, float y1, float x2, float y2, float thickness, Color color) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (length <= 0.0001f) {
            return;
        }

        float nx = -dy / length * thickness * 0.5f;
        float ny = dx / length * thickness * 0.5f;

        float[] vertices = new float[] {
                x1 + nx, y1 + ny,
                x2 + nx, y2 + ny,
                x2 - nx, y2 - ny,
                x1 + nx, y1 + ny,
                x2 - nx, y2 - ny,
                x1 - nx, y1 - ny
        };
        drawVertices(vertices, color);
    }

    // Sube vertices al VBO dinamico y ejecuta el draw call.
    private void drawVertices(float[] vertices, Color color) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices).flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
        GL20.glUniform4f(colorLocation, color.r, color.g, color.b, color.a);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.length / 2);
    }

    public void cleanup() {
        GL15.glDeleteBuffers(vbo);
        GL30.glDeleteVertexArrays(vao);
        GL20.glDeleteProgram(program);
    }
}
