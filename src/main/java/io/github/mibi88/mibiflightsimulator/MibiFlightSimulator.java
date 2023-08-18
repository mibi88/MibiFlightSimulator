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
import org.joml.Matrix4f;
import org.joml.Vector3f;

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
                -0.5f, 0.5f, 0f,//v0
                -0.5f, -0.5f, 0f,//v1
                0.5f, -0.5f, 0f,//v2
                0.5f, 0.5f, 0f,//v3
            };

            int[] indices = {
                0,1,3,//top left triangle (v0, v1, v3)
                3,1,2//bottom right triangle (v3, v1, v2)
            };
            
            Shaders shaders = new Shaders(
                    "vertex_shader.vert",
                    "fragment_shader.frag"
            );
            shaders.bind_attribute(0, "position");
            
            shaders.finish_init();
            
            shaders.start();
            
            int transformation_matrix_location = shaders.get_uniform_location(
                    "transformation_matrix");
            
            int projection_matrix_location = shaders.get_uniform_location(
                    "projection_matrix");
            
            Model model = new Model(vertices, indices);
            Entity entity = new Entity(model, 0f, 0f, -1f, 0f, 0f,
                    0f, 1f, shaders, transformation_matrix_location);
            
            Renderer renderer = new Renderer(window);
            renderer.load_projection_matrix(projection_matrix_location,
                    shaders);
            
            while(!window.quit_asked()) {
                renderer.init(window);
                
                entity.rotate(0, 1, 0);
                
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
