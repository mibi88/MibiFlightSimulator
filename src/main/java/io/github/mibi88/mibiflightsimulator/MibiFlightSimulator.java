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

import io.github.mibi88.Mibi3D.*;

/**
 *
 * @author mibi88
 */
public class MibiFlightSimulator {
    static public Window window;

    public static void main(String[] args) {
        System.out.printf("Using LWJGL %s\n",
                org.lwjgl.Version.getVersion());
        try {
            window = new Window(640, 480,
                    "MibiFlightSimulator");
            
            float[] vertices = {
                -0.5f, 0.5f, 0f, // v0
                -0.5f, -0.5f, 0f, // v1
                0.5f, -0.5f, 0f, // v2
                0.5f, 0.5f, 0f, // v3
            };

            int[] indices = {
                0, 1, 3, // Top left triangle (v0, v1, v3)
                3, 1, 2 // Bottom right triangle (v3, v1, v2)
            };
            
            float[] texture_coords = {
                0f, 0f, // v0
                0f, 1f, // v1
                1f, 1f, // v2
                1f, 0f // v3
            };
            
            Shaders shaders = new Shaders(
                    "vertex_shader.vert",
                    "fragment_shader.frag"
            );
            shaders.bind_attribute(0, "position");
            shaders.bind_attribute(1, "texture_coords");
            
            shaders.finish_init();
            
            shaders.start();
            
            int transformation_matrix_location = shaders.get_uniform_location(
                    "transformation_matrix");
            
            int projection_matrix_location = shaders.get_uniform_location(
                    "projection_matrix");
            
            int view_matrix_location = shaders.get_uniform_location(
                    "view_matrix");
            
            int texture_sampler_location = shaders.get_uniform_location(
                    "texture_sampler"
            );
            
            TexturedModel model = new TexturedModel(vertices, indices,
                    texture_coords, "hello_world.png",
                    TexturedModel.FILTER_NEAREST,
                    TexturedModel.WRAP_REPEAT);
            
            shaders.load_in_uniform_var(texture_sampler_location,
                    model.texture_id);
            
            Entity entity = new Entity(model, 0f, 0f, -1f, 0f, 0f,
                    0f, 1f, shaders, transformation_matrix_location);
            
            Renderer renderer = new Renderer(window);
            renderer.load_projection_matrix(projection_matrix_location,
                    shaders);
            
            Camera camera = new Camera(0f, 0f, 0f, 0f,  0f, 0f);
            
            while(!window.quit_asked()) {
                renderer.init(window, view_matrix_location, camera, shaders);
                
                entity.rotate(0f, 1f, 0f);
                
                renderer.render_entity(entity);
                window.update();
                
                window.poll_events();
            }
            
            shaders.free();
            model.free();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
