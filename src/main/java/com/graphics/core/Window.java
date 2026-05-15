package com.graphics.core;

import com.graphics.utils.Constants;
import com.graphics.utils.Vec2;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

// Encapsula la creacion de la ventana GLFW y el contexto OpenGL.
public class Window {

    private long handle;
    private String baseTitle;

    public Window(String title) {
        this.baseTitle = title;
    }

    public void init() {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("No se pudo iniciar GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);

        handle = GLFW.glfwCreateWindow(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, baseTitle, 0, 0);
        if (handle == 0) {
            throw new IllegalStateException("No se pudo crear la ventana");
        }

        GLFW.glfwMakeContextCurrent(handle);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(handle);
        GL.createCapabilities();

        // Activamos blending para que los colores con alpha se mezclen bien.
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public long getHandle() {
        return handle;
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    public void requestClose() {
        GLFW.glfwSetWindowShouldClose(handle, true);
    }

    public void swapBuffers() {
        GLFW.glfwSwapBuffers(handle);
    }

    public void pollEvents() {
        GLFW.glfwPollEvents();
    }

    public String getBaseTitle() {
        return baseTitle;
    }

    // Convierte la posicion del mouse desde pixeles de ventana
    // a coordenadas normalizadas de OpenGL.
    public Vec2 getCursorNdc() {
        double[] mouseX = new double[1];
        double[] mouseY = new double[1];
        int[] width = new int[1];
        int[] height = new int[1];

        GLFW.glfwGetCursorPos(handle, mouseX, mouseY);
        GLFW.glfwGetWindowSize(handle, width, height);

        float ndcX = (float) ((mouseX[0] / Math.max(1, width[0])) * 2.0 - 1.0);
        float ndcY = (float) (1.0 - (mouseY[0] / Math.max(1, height[0])) * 2.0);
        return new Vec2(ndcX, ndcY);
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(handle, title);
    }

    public void cleanup() {
        GLFW.glfwDestroyWindow(handle);
        GLFW.glfwTerminate();
    }
}
