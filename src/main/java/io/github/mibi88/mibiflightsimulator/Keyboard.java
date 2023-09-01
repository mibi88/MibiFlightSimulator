/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.github.mibi88.mibiflightsimulator;

import io.github.mibi88.Mibi3D.Camera;
import io.github.mibi88.Mibi3D.Entity;
import io.github.mibi88.Mibi3D.Window;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

/**
 *
 * @author tomin
 */
public class Keyboard {
    public boolean draw_plane = false;
    
    protected static boolean[] keys;
    
    public Keyboard(Window window) {
        keys = new boolean[65536];
        glfwSetKeyCallback(window.window_id,
                (window_id, key, scancode, action, mods) -> {
            if(scancode < 0) return;
            keys[scancode] = action != GLFW.GLFW_RELEASE;
            
            if(scancode == 47 && action == GLFW.GLFW_RELEASE) {
                draw_plane = !draw_plane;
            }
        });
    }
    
    public boolean keydown(int key) {
        return keys[key];
    }
}
