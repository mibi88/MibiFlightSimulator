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
            Engine engine = new Engine(0.8f, 1f, 1f, 0.7f,
                        1.5f, 0.0025f, true);
            window = engine.get_window();
            
            TexturedModel plane = new TexturedModel(
                    "models/plane.obj",
                    0,
                    "models/plane.png",
                    Texture.FILTER_MIPMAP_LINEAR,
                    Texture.WRAP_REPEAT,
                    4f, 1
            );
            plane.shine_damper = 5f;
            plane.reflectivity = 1f;
            
            TexturedModel sun_model = new TexturedModel(
                    "models/sun.obj",
                    0,
                    "models/sun.png",
                    Texture.FILTER_MIPMAP_LINEAR,
                    Texture.WRAP_REPEAT,
                    4f, 1
            );
            sun_model.shine_damper = 10f;
            sun_model.reflectivity = 1f;
            
            TexturedModel terrain_model = Terrain.generate_terrain(
                    1024, 1024, 8f, "models/grass.png",
                    -77
            );
            
            TexturedModelEntity player = engine.create_entity(plane, 0f,
                    64f, 0f, 0f, 0f, 0f, 1f, 0);
            
            Camera camera = engine.get_camera();
            
            Plane movement = new Plane(camera);
            
            TexturedModelEntity terrain = engine.create_entity(
                    terrain_model, 1024f*8f/2f, 0f, 0f,
                    0f, 0f, 0f, 1f,
                    0
            );
            TexturedModelEntity sun = engine.create_entity(sun_model,
                    0f, 70f, 0f, 0f, 0f, 0f, 1f,
                    0);
            
            Image title_image = new Image("images/title.png",
                    Texture.FILTER_NEAREST,
                    Texture.WRAP_REPEAT, 1);
            
            ImageEntity title = engine.create_entity(title_image,
                    -1f, 1f,
                    0f,
                    2f, 2f,
                    0);
            
            engine.set_camera_pos(0f, 64f, 0f, 0f, 0f, 0f);
            
            Keyboard keyboard = new Keyboard(window);
            Light light = new Light(0f, 64f, 0f, 1f, 1f, 1f);
            
            engine.set_light(light);
            
            //engine.add_entity(player);
            engine.add_entity(sun);
            engine.add_entity(terrain);
            //engine.add_entity(title);
            
            while(!window.quit_asked()) {
                engine.init();
                
                engine.set_fog(keyboard.fog);
                
                if(keyboard.draw_plane) {
                    // TODO : Display plane if needed
                }
                
                engine.render_scene();
                
                window.update();
                
                window.poll_events();
                
                if(keyboard.keydown(113)) {
                    movement.rotate_left();
                }
                if(keyboard.keydown(114)) {
                    movement.rotate_right();
                }
                if(keyboard.keydown(111)) {
                    movement.rotate_up();
                }
                if(keyboard.keydown(116)) {
                    movement.rotate_down();
                }
                if(keyboard.keydown(112)) {
                    movement.speed_up();
                }
                if(keyboard.keydown(117)) {
                    movement.slow_down();
                }
                
                if(keyboard.keydown(38)) {
                    movement.rotate_fast_left();
                }
                if(keyboard.keydown(39)) {
                    movement.rotate_fast_right();
                }
                movement.move();
                
                player.x = camera.x;
                player.y = camera.y;
                player.z = camera.z;
                
                light.x = camera.x;
                light.z = camera.z;
                
                sun.x = camera.x;
                sun.y = camera.y+15f;
                sun.z = camera.z;

                player.rx = -camera.rx; // Angle of attack
                player.ry = -camera.ry;
                player.rz = camera.rz;
            }
            
            engine.destroy();
            plane.free();
            terrain_model.free();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
