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
    
    /**
     * Initializes the renderer by creating the projection matrix used when
     * rendering the 3D scene.
     * @param window
     */
    public Renderer(Window window) {
        projection_matrix = Maths.create_projection_matrix(
                FOV,
                NEAR_PLANE, FAR_PLANE,
                window
        );
    }
    
    /**
     * Load the projection matrix to a uniform variable to be accessed by the
     * shader.
     * 
     * This method needs to be called after loading the shaders.
     * 
     * @param projection_matrix_location The location of the uniform variable.
     * Get it by using the Shaders.get_uniform_location method.
     * @param shaders The shaders to load the matrix as a uniform variable in.
     */
    public void load_projection_matrix(int projection_matrix_location,
            Shaders shaders) {
        shaders.load_in_uniform_var(
                projection_matrix_location, 
                projection_matrix
        );
    }
    
    /**
     * Call this method before rendering to prepare the Renderer.
     * 
     * This method loads the view matrix that adapts the positions of the
     * vertices to the position of the camera, and clears the glfw window.
     * 
     * @param window The window to use the renderer on.
     * @param view_matrix_location The location of the uniform variable that
     * will contain the view matrix.
     * @param camera The camera object to create the view matrix from.
     * @param shaders The shader object that will be used to load the view
     * matrix as a uniform variable to the shaders with the
     * Shaders.get_uniform_location method.
     */
    public void init(Window window, int view_matrix_location, Camera camera,
            Shaders shaders) {
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        GL30.glClearColor(1, 1, 1, 1);
        Matrix4f view_matrix = Maths.create_view_matrix(camera);
        shaders.load_in_uniform_var(view_matrix_location,
                view_matrix);
        
    }
    
    /**
     * Render a 3D model in the 3D scene
     * 
     * @param entity The entity to draw on screen.
     */
    public void render_entity(Entity entity) {
        TexturedModel model = entity.model;
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
        GL30.glEnableVertexAttribArray(1);
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        model.bind_texture();
        GL30.glDrawElements(GL30.GL_TRIANGLES,
                model.get_vertices_amount(), GL30.GL_UNSIGNED_INT,
                0);
        model.unbind_texture();
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
    }
}
