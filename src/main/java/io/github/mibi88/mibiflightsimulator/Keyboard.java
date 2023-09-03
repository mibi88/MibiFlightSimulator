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
package io.github.mibi88.mibiflightsimulator;

import io.github.mibi88.Mibi3D.Window;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

/**
 *
 * @author mibi88
 */
public class Keyboard {
    public boolean draw_plane = false;
    public boolean fog = true;
    
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
            if(scancode == 41 && action == GLFW.GLFW_RELEASE) {
                fog = !fog;
            }
        });
    }
    
    public boolean keydown(int key) {
        return keys[key];
    }
}
