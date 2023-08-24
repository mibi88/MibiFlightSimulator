/*
 * Copyright (C) 2023 mibi88
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package io.github.mibi88.Mibi3D;

import java.nio.IntBuffer;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 *
 * @author mibi88
 */
public class Window {
    public long window_id;
    public Window(int width, int height, String title) throws Exception {
        
        if(!glfwInit()) {
            throw new Exception("Unable to initialize GLFW!");
        }
        
        glfwSetErrorCallback(
                GLFWErrorCallback.createPrint(System.err)
        );
        
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL30.GL_TRUE);
        
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        
        window_id = glfwCreateWindow(width, height, title,
                NULL, NULL);
        if(window_id == NULL) {
            throw new Exception("Failed to create window");
        }
        
        glfwSetInputMode(window_id, GLFW_CURSOR,
                GLFW_CURSOR_HIDDEN);
        
        glfwMakeContextCurrent(window_id);
        GL.createCapabilities();
        
        String opengl_version = GL30.glGetString(GL30.GL_VERSION);
        String window_title = String.format("%s (OpenGL %s)", title,
                opengl_version);
        
        glfwSetWindowTitle(window_id , window_title);
        
        glfwSwapInterval(1);
        glfwShowWindow(window_id);
    }
    
    public void update() {
        GLFW.glfwSwapBuffers(window_id);
    }
    
    public void poll_events() {
        glfwPollEvents();
    }
    
    public boolean quit_asked() {
        return glfwWindowShouldClose(window_id);
    }
    
    public void destroy() {
        glfwFreeCallbacks(window_id);
        glfwDestroyWindow(window_id);
        
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    
    public int[] get_window_size() {
        int[] size = new int[2];
        IntBuffer width_buffer = BufferUtils.createIntBuffer(1);
        IntBuffer height_buffer = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(
                window_id,
                width_buffer,
                height_buffer
        );
        size[0] = width_buffer.get(0);
        size[1] = height_buffer.get(0);
        return size;
    }
}
