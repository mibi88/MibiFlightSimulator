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

import org.joml.Vector3f;

/**
 * The class that combines all the components of the 3D engine
 * 
 * @author mibi88
 */
public class Engine {
    private final Window window;
    private final Camera camera;
    
    private final Shaders shaders_3D;
    private final Shaders shaders_2D;
    
    private final Renderer renderer;
    
    private TexturedModel used_model;
    private Image used_image;
    
    private Light light;
    
    int transformation_matrix_location;
    int projection_matrix_location;
    int view_matrix_location;
    
    int light_position_location;
    int light_color_location;
    
    int shine_damper_location;
    int reflectivity_location;
    int ambient_lighting_location;
    
    int fog_gradient_location;
    int fog_density_location;
    int fog_location;
    int sky_color_location;
    
    int texture_x_location;
    int texture_y_location;
    int cell_size_location;
    
    int transformation_matrix_location_2D;
    
    int texture_x_location_2D;
    int texture_y_location_2D;
    int cell_size_location_2D;
    
    float r;
    float g;
    float b;
    
    float ambient_lighting;
    
    float fog_gradient;
    float fog_density;
    
    boolean fog;
    
    /**
     * Initializes the 3D engine
     * 
     * @param r The sky color red component, a float between 0 and 1
     * @param g The sky color green component, a float between 0 and 1
     * @param b The sky color blue component, a float between 0 and 1
     * @param ambient_lighting The amount of ambient lighting, a float between
     * 0 and 1
     * @param fog_gradient The size of the gradient of the fog
     * @param fog_density The density of the fog
     * @param fog A boolean to enable or disable fog
     * @throws Exception
     */
    public Engine(float r, float g, float b, float ambient_lighting,
            float fog_gradient, float fog_density, boolean fog)
            throws Exception {
        this.r = r;
        this.g = g;
        this.b = b;
        
        this.ambient_lighting = ambient_lighting;
        
        this.fog_gradient = fog_gradient;
        this.fog_density = fog_density;
        
        this.fog = fog;
        
        window = new Window(640, 480, "MibiFlightSimulator",
                4);
            
        shaders_3D = new Shaders(
                "shaders/3D_vertex_shader.vert",
                "shaders/3D_fragment_shader.frag"
        );
        
        shaders_3D.bind_attribute(0, "position");
        shaders_3D.bind_attribute(1, "texture_coords");
        shaders_3D.bind_attribute(2, "normal");

        shaders_3D.finish_init();

        transformation_matrix_location = shaders_3D.get_uniform_location(
                "transformation_matrix");
        projection_matrix_location = shaders_3D.get_uniform_location(
                "projection_matrix");
        view_matrix_location = shaders_3D.get_uniform_location(
                "view_matrix");

        light_position_location = shaders_3D.get_uniform_location(
                "light_position");
        light_color_location = shaders_3D.get_uniform_location(
                "light_color");

        shine_damper_location = shaders_3D.get_uniform_location(
                "shine_damper");
        reflectivity_location = shaders_3D.get_uniform_location(
                "reflectivity");
        ambient_lighting_location = shaders_3D.get_uniform_location(
                "ambient_lighting");
        
        fog_gradient_location = shaders_3D.get_uniform_location(
                "fog_gradient");
        fog_density_location = shaders_3D.get_uniform_location(
                "fog_density");
        fog_location = shaders_3D.get_uniform_location(
                "fog");
        sky_color_location = shaders_3D.get_uniform_location(
                "sky_color");
        
        texture_x_location = shaders_3D.get_uniform_location(
                "texture_x");
        texture_y_location = shaders_3D.get_uniform_location(
                "texture_y");
        cell_size_location = shaders_3D.get_uniform_location(
                "cell_size");
        
        shaders_2D = new Shaders(
                "shaders/2D_vertex_shader.vert",
                "shaders/2D_fragment_shader.frag"
        );
        
        shaders_2D.bind_attribute(0, "position");
        shaders_2D.bind_attribute(1, "texture_coords");
        
        shaders_2D.finish_init();
        
        transformation_matrix_location_2D = shaders_2D.get_uniform_location(
                "transformation_matrix");
        
        texture_x_location_2D = shaders_2D.get_uniform_location(
                "texture_x");
        texture_y_location_2D = shaders_2D.get_uniform_location(
                "texture_y");
        cell_size_location_2D = shaders_2D.get_uniform_location(
                "cell_size");
        
        renderer = new Renderer(window);

        camera = new Camera(0f, 0f, 0f, 0f,  0f, 0f);
    }
    
    /**
     * Get the Camera object used by the engine
     * 
     * @return The Camera object
     */
    public Camera get_camera() {
        return camera;
    }
    
    /**
     * Get the Window object used by the engine
     * 
     * @return The Window object
     */
    public Window get_window() {
        return window;
    }
    
    /**
     * Set the position of the camera
     * 
     * @param x
     * @param y
     * @param z
     * @param rx
     * @param ry
     * @param rz
     */
    public void set_camera_pos(float x, float y, float z, float rx, float ry,
            float rz) {
        camera.x = x;
        camera.y = y;
        camera.z = z;
        
        camera.rx = rx;
        camera.ry = ry;
        camera.rz = rz;
    }
    
    /**
     * Enable or disable fog
     * 
     * @param fog Sets if fog should be enabled or not
     */
    public void set_fog(boolean fog) {
        this.fog = fog;
    }
    
    /**
     * Set the light to use to render the scene
     * 
     * @param light The light to use
     */
    public void set_light(Light light) {
        this.light = light;
    }
    
    /**
     * Create a new entity from a TexturedModel
     * 
     * @param model The model to create an entity from
     * @param x
     * @param y
     * @param z
     * @param rx The rotation on the X axis of the entity
     * @param ry The rotation on the Y axis of the entity
     * @param rz The rotation on the Z axis of the entity
     * @param scale The scale of the entity
     * @param texture_num The number of the texture in the texture atlas to use
     * @return A new Entity object
     */
    public ModelEntity create_entity(TexturedModel model, float x, float y, float z,
            float rx, float ry, float rz, float scale, int texture_num) {
        return new ModelEntity(model, x, y, z, rx, ry, rz, scale,
                shaders_3D, transformation_matrix_location, texture_num,
                texture_x_location, texture_y_location, cell_size_location);
    }
    
    /**
     * Create a new entity from an Image
     * 
     * @param image The image to create an entity from
     * @param x
     * @param y
     * @param rot The rotation of the entity
     * @param x_scale The scale on the X axis of the entity
     * @param y_scale The scale on the Y axis of the entity
     * @param texture_num The number of the texture in the texture atlas to use
     * @return A new Entity object
     */
    public ImageEntity create_entity(Image image, float x, float y, float rot,
            float x_scale, float y_scale, int texture_num) {
        return new ImageEntity(image, x, y, rot, x_scale, y_scale,
                shaders_2D,
                transformation_matrix_location_2D,
                texture_num,
                texture_x_location_2D,
                texture_y_location_2D,
                cell_size_location_2D
        );
    }
    
    /**
     * Prepare the engine to render a frame
     */
    public void init() {
        renderer.init(window, r, g, b);
    }
    
    /**
     * Prepare a model to be rendered
     * 
     * @param model
     */
    public void start_using_model(TexturedModel model) {
        renderer.start_using_model(model, shine_damper_location,
                reflectivity_location, shaders_3D);
        used_model = model;
        shaders_3D.start();
        
        renderer.load_shine_and_reflectivity(shine_damper_location,
                reflectivity_location,
                model.shine_damper,
                model.reflectivity,
                shaders_3D
        );
        shaders_3D.load_in_uniform_var(sky_color_location,
                new Vector3f(r, g, b));
        renderer.load_scene_settings(camera, ambient_lighting,
                ambient_lighting_location, view_matrix_location,
                shaders_3D);
        renderer.load_light(light_position_location,
                light_color_location, light, shaders_3D);
        renderer.load_fog(fog_gradient, fog_density, fog,
                fog_gradient_location,
                fog_density_location, fog_location,
                shaders_3D);
        renderer.load_projection_matrix(projection_matrix_location,
                shaders_3D);
    }
    
    /**
     * Prepare an image to be rendered
     * 
     * @param image
     */
    public void start_using_image(Image image) {
        renderer.start_using_image(image, shaders_2D);
        used_image = image;
        shaders_2D.start();
    }
    
    /**
     * Stop rendering this model
     */
    public void stop_using_model() {
        renderer.stop_using_model(used_model);
    }
    
    /**
     * Stop rendering this image
     */
    public void stop_using_image() {
        renderer.stop_using_image(used_image);
    }
    
    /**
     * Render an entity
     * 
     * @param entity The entity to render
     */
    public void render_entity(ModelEntity entity) {
        renderer.render_entity(entity);
    }
    
    /**
     * Render an entity
     * 
     * @param entity The entity to render
     */
    public void render_entity(ImageEntity entity) {
        renderer.render_entity(window, entity);
    }
    
    /**
     * Free memory etc. after using the engine
     */
    public void destroy() {
        window.destroy();
        
        shaders_3D.free();
        
        shaders_2D.free();
    }
}
