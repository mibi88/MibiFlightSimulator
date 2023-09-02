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

import io.github.mibi88.Mibi3D.Entity;
import org.joml.Vector3f;

/**
 *
 * @author mibi88
 */
public class Plane {
    private Entity plane;
    float speed = 0f;
    float max_speed = 1f;
    float min_fly_speed = 0.5f;
    
    float x_rot_speed = 0.05f;
    float y_rot_speed = 0.05f;
    float z_rot_speed = 0.05f;
    
    float x_rot = 0f;
    float y_rot = 0f;
    float z_rot = 0f;
    
    float z_rot_mul = 1.5f;
    
    float acceleration = 0.1f;
    float slow_down = 0.1f;
    
    float fall_speed;
    
    boolean got_faster = false;
    
    boolean rot_x = false;
    boolean rot_y = false;
    boolean rot_z = false;
    
    public Plane(Entity plane) {
        this.plane = plane;
    }
    
    public void speed_up() {
        speed += acceleration;
        got_faster = true;
    }
    
    public void slow_down() {
        if(speed > min_fly_speed) speed -= slow_down;
        if(speed < min_fly_speed) speed = min_fly_speed;
    }
    
    public void rotate_left() {
        z_rot -= z_rot_speed;
        rot_z = true;
    }
    
    public void rotate_right() {
        z_rot += z_rot_speed;
        rot_z = true;
    }
    
    public void rotate_fast_left() {
        y_rot -= y_rot_speed;
        rot_y = true;
    }
    
    public void rotate_fast_right() {
        y_rot += y_rot_speed;
        rot_y = true;
    }
    
    public void rotate_up() {
        x_rot += x_rot_speed;
        rot_x = true;
    }
    
    public void rotate_down() {
        x_rot -= x_rot_speed;
        rot_x = true;
    }
    
    public void move() {
        if(!got_faster) slow_down();
        
        if(!rot_x) {
            if(x_rot < 0f) x_rot += x_rot_speed;
            if(x_rot > 0f) x_rot -= x_rot_speed;
            
            if(x_rot > -x_rot_speed && x_rot < x_rot_speed) x_rot = 0f;
        }
        if(!rot_y) {
            if(y_rot < 0f) y_rot += y_rot_speed;
            if(y_rot > 0f) y_rot -= y_rot_speed;
            
            if(x_rot > -x_rot_speed && x_rot < x_rot_speed) x_rot = 0f;
        }
        if(!rot_z) {
            if(z_rot < 0f) z_rot += z_rot_speed;
            if(z_rot > 0f) z_rot -= z_rot_speed;
            
            if(z_rot > -z_rot_speed && z_rot < x_rot_speed) z_rot = 0f;
        }
        
        plane.rx -= x_rot;
        plane.ry += z_rot*z_rot_mul;
        plane.ry += y_rot;
        plane.rz -= z_rot;
        
        Vector3f direction = new Vector3f(0f, 0f, -speed);
        direction.rotateX((float)Math.toRadians(-plane.rx));
        direction.rotateY((float)Math.toRadians(-plane.ry));
        direction.rotateY((float)Math.toRadians(plane.rz*z_rot_mul));
        plane.x += direction.x;
        plane.y += direction.y;
        plane.z += direction.z;
        
        got_faster = false;
        
        rot_x = false;
        rot_y = false;
        rot_z = false;
    }
}
