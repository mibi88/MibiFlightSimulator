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
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

/**
 * A class that is used to render the 3D scene
 * 
 * @author mibi88
 */
public class Renderer {
    private final float FOV = 70f;
    private final float NEAR_PLANE = 0.1f, FAR_PLANE = 1000f;
    
    Matrix4f projection_matrix;
    
    /**
     * Initializes the renderer by creating the projection matrix used when
     * rendering the 3D scene.
     * 
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
     * Set the light used when rendering the 3D scene
     * 
     * @param light_position_location The location of the uniform variable that
     * will contain the position of the light
     * @param light_color_location The location of the uniform variable that
     * will contain the color of the light
     * @param light The Light object
     * @param shaders The Shaders object to load the light to
     */
    public void load_light(int light_position_location,
            int light_color_location, Light light, Shaders shaders) {
        shaders.load_in_uniform_var(light_position_location,
                new Vector3f(light.x, light.y, light.z));
        shaders.load_in_uniform_var(light_color_location,
                new Vector3f(light.r, light.g, light.b));
    }
    
    /**
     * Load the shine and reflectivity values used to render a model
     * 
     * @param shine_damper_location The location of the shine damper uniform
     * variable
     * @param reflectivity_location The location of the reflectivity uniform
     * variable
     * @param shine_damper The shine damper value
     * @param reflectivity The reflectivity value
     * @param shaders The Shaders to load the variables to
     */
    public void load_shine_and_reflectivity(int shine_damper_location,
            int reflectivity_location,
            float shine_damper, float reflectivity, Shaders shaders) {
        shaders.load_in_uniform_var(shine_damper_location,
                shine_damper);
        shaders.load_in_uniform_var(reflectivity_location,
                reflectivity);
    }
    
    /**
     * Load up data about the fog in the 3D scene
     * 
     * @param gradient The gradient of the fog
     * @param density The density of the fog
     * @param fog If fog should be enabled or not
     * @param gradient_location The location of the gradient uniform variable
     * @param density_location The location of the density uniform variable
     * @param fog_location The location of the boolean to enable or disable fog
     * @param shaders The Shaders object to load the data to
     */
    public void load_fog(float gradient, float density, boolean fog,
            int gradient_location, int density_location, int fog_location,
            Shaders shaders) {
        shaders.load_in_uniform_var(gradient_location,
                gradient);
        shaders.load_in_uniform_var(density_location,
                density);
        shaders.load_in_uniform_var(fog_location,
                fog);
    }
    
    /**
     * Call this method before rendering to prepare the Renderer.
     * 
     * This method loads the view matrix that adapts the positions of the
     * vertices to the position of the camera, and clears the glfw window.
     * 
     * @param window The window to use the renderer on.
     * @param r The red component of the sky color
     * @param g The green component of the sky color
     * @param b The blue component of the sky color
     */
    public void init(Window window, float r, float g, float b) {
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        GL30.glClearColor(r, g, b, 1f);
    }
    
    /**
     *
     * @param camera The camera object to create the view matrix from.
     * @param ambient_lighting The amount of ambient lighting, a float between 0
     * and 1
     * @param ambient_lighting_location The location of the ambient lighting
     * uniform variable
     * @param view_matrix_location The location of the uniform variable that
     * will contain the view matrix.
     * @param shaders The shader object that will be used to load the view
     * matrix as a uniform variable to the shaders with the
     * Shaders.get_uniform_location method.
     */
    public void load_scene_settings(Camera camera, float ambient_lighting,
            int ambient_lighting_location, int view_matrix_location,
            Shaders shaders) {
        Matrix4f view_matrix = Maths.create_view_matrix(camera);
        shaders.load_in_uniform_var(view_matrix_location,
                view_matrix);
        shaders.load_in_uniform_var(ambient_lighting_location,
                ambient_lighting);
    }
    
    /**
     * Start drawing entities of a model
     * 
     * @param model The model that will be used to draw the next entities
     * @param shine_damper_location The location of the shine damper uniform
     * variable
     * @param reflectivity_location The location of the reflectivity uniform
     * variable
     * @param shaders The Shaders object to use
     */
    public void start_using_model(TexturedModel model,
            int shine_damper_location, int reflectivity_location,
            Shaders shaders) {
        GL30.glBindVertexArray(model.get_vao());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glEnableVertexAttribArray(2);
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, model.texture_id);
    }
    
    /**
     * Stop using a model to render entities
     * 
     * @param model The model that was used to render the entities
     */
    public void stop_using_model(TexturedModel model) {
        model.unbind_texture();
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glDisableVertexAttribArray(2);
    }
    
    /**
     * Start drawing entities of an image
     * 
     * @param image The image that will be used to draw the next entities
     * @param shaders The Shaders object to use
     */
    public void start_using_image(Image image, Shaders shaders) {
        GL30.glBindVertexArray(image.get_vao());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, image.texture_id);
        GL30.glDisable(GL30.GL_DEPTH_TEST);
        GL30.glEnable(GL30.GL_BLEND);
    }
    
    /**
     * Stop using an image to render entities
     * 
     * @param image The image that was used to render the entities
     */
    public void stop_using_image(Image image) {
        image.unbind_texture();
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glEnable(GL30.GL_DEPTH_TEST);
        GL30.glDisable(GL30.GL_BLEND);
    }
    
    /**
     * Render a 3D model in the 3D scene
     * 
     * @param entity The entity to draw on screen.
     */
    public void render_entity(ModelEntity entity) {
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
        
        if(model.texture_atlas_size > 0) {
            entity.shaders.load_in_uniform_var(
                    entity.texture_x_location, 
                    entity.texture_x
            );
            entity.shaders.load_in_uniform_var(
                    entity.texture_y_location, 
                    entity.texture_y
            );
            entity.shaders.load_in_uniform_var(
                    entity.cell_size_location, 
                    entity.cell_size
            );
        }
        
        GL30.glDrawElements(GL30.GL_TRIANGLES,
                model.get_vertices_amount(), GL30.GL_UNSIGNED_INT,
                0);
    }
    
    /**
     * Render an image on the screen
     * 
     * @param window
     * @param entity The entity to draw on screen.
     */
    public void render_entity(Window window, ImageEntity entity) {
        Image image = entity.image;
        
        System.out.println(window.max_width);
        System.out.println(window.max_height);
        
        Matrix4f transformation_matrix = Maths.create_transformation_matrix(
                new Vector2f(entity.x*window.max_width,
                        entity.y*window.max_height),
                entity.rot,
                entity.x_scale,
                entity.y_scale
        );
        
        entity.shaders.load_in_uniform_var(
                entity.transformation_matrix_location,
                transformation_matrix
        );
        
        if(image.texture_atlas_size > 0) {
            entity.shaders.load_in_uniform_var(
                    entity.texture_x_location, 
                    entity.texture_x
            );
            entity.shaders.load_in_uniform_var(
                    entity.texture_y_location, 
                    entity.texture_y
            );
            entity.shaders.load_in_uniform_var(
                    entity.cell_size_location, 
                    entity.cell_size
            );
        }
        
        GL30.glDrawElements(GL30.GL_TRIANGLES,
                6, GL30.GL_UNSIGNED_INT,
                0);
    }
}
