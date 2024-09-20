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
 * A class to manage a window with an OpenGL context
 * @author mibi88
 */
public class Window {
    public long window_id;
    
    protected float max_width, max_height;
    
    protected boolean wireframe;
    
    Framebuffer framebuffer = null;
    
    /**
     * Create a new window
     * 
     * @param width The width of the window
     * @param height The height of the window
     * @param title The title of the window
     * @param multisample The amount of multisampling. 0 to disable it
     * @throws Exception
     */
    public Window(int width, int height, String title, int multisample)
            throws Exception {
        
        if(!glfwInit()) {
            throw new Exception("Unable to initialize GLFW!");
        }
        
        glfwSetErrorCallback(
                GLFWErrorCallback.createPrint(System.err)
        );
        
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
        
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL30.GL_TRUE);
        
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        
        if(multisample > 1) glfwWindowHint(GLFW_SAMPLES, multisample);
                
        window_id = glfwCreateWindow(width, height, title,
                NULL, NULL);
        if(window_id == NULL) {
            throw new Exception("Failed to create window");
        }
        
        glfwSetInputMode(window_id, GLFW_CURSOR,
                GLFW_CURSOR_HIDDEN);
        
        glfwMakeContextCurrent(window_id);
        GL.createCapabilities();
        
        GLUtil.setupDebugMessageCallback();
        
        String opengl_version = GL30.glGetString(GL30.GL_VERSION);
        String window_title = String.format("%s (OpenGL %s)", title,
                opengl_version);
        
        glfwSetWindowTitle(window_id , window_title);
        
        glfwSwapInterval(1); // V-sync enabled.
        glfwShowWindow(window_id);
        
        GL30.glEnable(GL30.GL_DEPTH_TEST);
        GL30.glEnable(GL30.GL_CULL_FACE);
        GL30.glCullFace(GL30.GL_BACK);
        if(multisample > 1) GL30.glEnable(GL30.GL_MULTISAMPLE);
        
        GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_FILL);
        update_gl_viewport();
        
        wireframe = false;
    }
    
    public void toggle_wireframe() {
        if(wireframe) {
            GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_FILL);
            GL30.glEnable(GL30.GL_CULL_FACE);
        } else {
            GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_LINE);
            GL30.glDisable(GL30.GL_CULL_FACE);
        }
        wireframe = !wireframe;
    }
    
    /**
     * Update the content of the window
     */
    public void update() {
        // TODO: update GL viewport only after resize
        update_gl_viewport();
        GLFW.glfwSwapBuffers(window_id);
    }
    
    /**
     * Update the OpenGL viewport to fill the window
     */
    public void update_gl_viewport() {
        int[] window_size = get_window_size();
        int max_size = window_size[0] > window_size[1] ?
                window_size[0] : window_size[1];
        
        // I center the viewport and set it to the width or the height depending
        // on which one is the biggest
        GL30.glViewport(0, 0, window_size[0], window_size[1]);
        
        max_width = (float)window_size[0]/(float)max_size;
        max_height = (float)window_size[1]/(float)max_size;
        
        if(framebuffer != null){
            framebuffer.update_textures(this);
        }
    }
    
    /**
     * Poll events to get for example keyboard inputs later on
     */
    public void poll_events() {
        glfwPollEvents();
    }
    
    /**
     * Check if the user tried to close the window
     * 
     * @return A boolean that is true if the user tried to close the window
     */
    public boolean quit_asked() {
        return glfwWindowShouldClose(window_id);
    }
    
    /**
     * Delete everything that's not needed anymore
     */
    public void destroy() {
        glfwFreeCallbacks(window_id);
        glfwDestroyWindow(window_id);
        
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    
    /**
     * Get the size of the window
     * 
     * @return An array of integers that contains at an index of 0 the width and
     * at an index of 1 the height of the window
     */
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
    
    public void set_framebuffer(Framebuffer framebuffer) {
        this.framebuffer = framebuffer;
    }
}
