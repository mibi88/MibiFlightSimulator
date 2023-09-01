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
            Engine engine = new Engine();
            window = engine.get_window();
            
            TexturedModel plane = new TexturedModel(
                    "models/plane.obj",
                    0,
                    "models/plane.png",
                    TexturedModel.FILTER_MIPMAP_LINEAR,
                    TexturedModel.WRAP_REPEAT,
                    4f
            );
            plane.shine_damper = 5f;
            plane.reflectivity = 1f;
            
            TexturedModel sun_model = new TexturedModel(
                    "models/sun.obj",
                    0,
                    "models/sun.png",
                    TexturedModel.FILTER_MIPMAP_LINEAR,
                    TexturedModel.WRAP_REPEAT,
                    4f
            );
            sun_model.shine_damper = 10f;
            sun_model.reflectivity = 1f;
            
            TexturedModel terrain_model = Terrain.generate_terrain(
                    1024, 1024, 8f, "models/grass.png",
                    -77
            );
            
            Entity player = engine.create_entity(plane, 0f, 64f, 0f,
                    0f, 0f, 0f, 1f);
            
            Entity terrain = engine.create_entity(terrain_model,
                    1024f*8f/2f, 0f, 0f, 0f, 0f, 0f, 1f);
            Entity sun = engine.create_entity(sun_model,
                    0f, 70f, 0f, 0f, 0f, 0f, 1f);
            
            engine.set_camera_pos(0f, 64f, 0f, 0f, 0f, 0f);
            
            Camera camera = engine.get_camera();
            
            Keyboard keyboard = new Keyboard(window);
            Light light = new Light(0f, 64f, 0f, 1f, 1f, 1f);
            
            while(!window.quit_asked()) {
                engine.init(0.8f, 1f, 1f, 0.7f,
                        1.5f, 0.0025f);
                
                engine.load_light(light);
                
                if(keyboard.draw_plane) {
                    engine.start_using_model(plane);
                    engine.render_entity(player);
                    engine.stop_using_model();
                }
                
                engine.start_using_model(sun_model);
                engine.render_entity(sun);
                engine.stop_using_model();
                
                engine.start_using_model(terrain_model);
                engine.render_entity(terrain);
                engine.stop_using_model();
                
                window.update();
                
                window.poll_events();
                
                /*if(keyboard.keydown(113)) {
                    player.rz--;
                }
                if(keyboard.keydown(114)) {
                    player.rz++;
                }
                if(keyboard.keydown(111)) {
                    player.rx--;
                }
                if(keyboard.keydown(116)) {
                    player.rx++;
                }*/
                
                if(keyboard.keydown(113)) {
                    player.x--;
                }
                if(keyboard.keydown(114)) {
                    player.x++;
                }
                if(keyboard.keydown(111)) {
                    player.z--;
                }
                if(keyboard.keydown(116)) {
                    player.z++;
                }
                
                camera.x = player.x;
                camera.y = player.y;
                camera.z = player.z;
                
                light.x = player.x;
                light.z = player.z;
                
                sun.x = player.x;
                sun.z = player.z;

                camera.rx = player.rx; // Angle of attack
                camera.ry = player.ry;
                camera.rz = player.rz;
            }
            
            engine.destroy();
            plane.free();
            terrain_model.free();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
