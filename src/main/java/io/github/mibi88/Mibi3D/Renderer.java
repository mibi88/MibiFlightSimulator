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

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author mibi88
 */
public class Renderer {
    public void init(Window window) {
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        GL30.glClearColor(1, 1, 1, 1);
    }
    
    public void render_model(Model model) {
        GL30.glBindVertexArray(model.get_vao());
        GL30.glEnableVertexAttribArray(0);
        GL30.glDrawElements(GL30.GL_TRIANGLES,
                model.get_vertices_amount(), GL30.GL_UNSIGNED_INT,
                0);
    }
}
