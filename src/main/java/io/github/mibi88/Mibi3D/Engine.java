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

/**
 *
 * @author mibi88
 */
public class Engine {
    private final Window window;
    private final Camera camera;
    private final Shaders shaders;
    private final Renderer renderer;
    
    private TexturedModel used_model;
    
    int transformation_matrix_location;
    int projection_matrix_location;
    int view_matrix_location;
    int light_position_location;
    int light_color_location;
    int shine_damper_location;
    int reflectivity_location;
    int ambient_lighting_location;
    
    public Engine() throws Exception {
        window = new Window(640, 480,
                    "MibiFlightSimulator", 4);
            
        shaders = new Shaders(
                "vertex_shader.vert",
                "fragment_shader.frag"
        );
        shaders.bind_attribute(0, "position");
        shaders.bind_attribute(1, "texture_coords");
        shaders.bind_attribute(2, "normal");

        shaders.finish_init();

        shaders.start();

        transformation_matrix_location = shaders.get_uniform_location(
                "transformation_matrix");
        projection_matrix_location = shaders.get_uniform_location(
                "projection_matrix");
        view_matrix_location = shaders.get_uniform_location(
                "view_matrix");

        light_position_location = shaders.get_uniform_location(
                "light_position");
        light_color_location = shaders.get_uniform_location(
                "light_color");

        shine_damper_location = shaders.get_uniform_location(
                "shine_damper");
        reflectivity_location = shaders.get_uniform_location(
                "reflectivity");
        ambient_lighting_location = shaders.get_uniform_location(
                "ambient_lighting");
        
        renderer = new Renderer(window);
        renderer.load_projection_matrix(projection_matrix_location, shaders);

        camera = new Camera(0f, 0f, 0f, 0f,  0f, 0f);
    }
    
    public Camera get_camera() {
        return camera;
    }
    
    public Window get_window() {
        return window;
    }
    
    public void set_camera_pos(float x, float y, float z, float rx, float ry,
            float rz) {
        camera.x = x;
        camera.y = y;
        camera.z = z;
        
        camera.rx = rx;
        camera.ry = ry;
        camera.rz = rz;
    }
    
    public void load_light(Light light) {
        renderer.load_light(light_position_location,
                light_color_location, light, shaders);
    }
    
    public Entity create_entity(TexturedModel model, float x, float y, float z,
            float rx, float ry, float rz, float scale) {
        return new Entity(model, x, y, z, rx, ry, rz, scale,
                shaders, transformation_matrix_location);
    }
    
    public void clear(float r, float g, float b, float ambient_lighting) {
        renderer.init(window, view_matrix_location, camera, shaders,
                r, g, b, ambient_lighting, ambient_lighting_location);
    }
    
    public void start_using_model(TexturedModel model) {
        renderer.start_using_model(model, shine_damper_location,
                reflectivity_location, shaders);
        used_model = model;
    }
    
    public void stop_using_model() {
        renderer.stop_using_model(used_model);
    }
    
    public void render_entity(Entity entity) {
        renderer.render_entity(entity);
    }
    
    public void destroy() {
        window.destroy();
        shaders.stop();
        shaders.free();
    }
}
