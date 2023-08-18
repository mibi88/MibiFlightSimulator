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

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author mibi88
 */
public class Renderer {
    private final float FOV = 70f;
    private final float NEAR_PLANE = 0.1f, FAR_PLANE = 200f;
    
    Matrix4f projection_matrix;
    
    public Renderer(Window window) {
        projection_matrix = Maths.create_projection_matrix(
                FOV,
                NEAR_PLANE, FAR_PLANE,
                window
        );
    }
    
    public void load_projection_matrix(int projection_matrix_location,
            Shaders shaders) {
        shaders.load_in_uniform_var(
                projection_matrix_location, 
                projection_matrix
        );
    }
    
    public void init(Window window, int view_matrix_location, Camera camera,
            Shaders shaders) {
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        GL30.glClearColor(1, 1, 1, 1);
        Matrix4f view_matrix = Maths.create_view_matrix(camera);
        shaders.load_in_uniform_var(view_matrix_location,
                view_matrix);
        
    }
    
    public void render_entity(Entity entity) {
        Model model = entity.model;
        Matrix4f transformation_matrix = Maths.create_transformation_matrix(
                new Vector3f(entity.x, entity.y, entity.z),
                entity.rx,
                entity.ry,
                entity.rz,
                entity.scale
        );
        
        entity.shaders.load_in_uniform_var(
                entity.transformation_matrix_location, 
                transformation_matrix
        );
        
        GL30.glBindVertexArray(model.get_vao());
        GL30.glEnableVertexAttribArray(0);
        GL30.glDrawElements(GL30.GL_TRIANGLES,
                model.get_vertices_amount(), GL30.GL_UNSIGNED_INT,
                0);
    }
}
