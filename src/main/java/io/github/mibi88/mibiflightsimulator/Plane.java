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

import io.github.mibi88.Mibi3D.Camera;
import org.joml.Vector3f;

/**
 *
 * @author mibi88
 */
public class Plane {
    private Camera plane;
    float max_speed = 2f;
    float min_fly_speed = 0.5f;
    float speed = 0f;
    
    float rx_speed = 0.05f;
    float ry_speed = 0.05f;
    float rz_speed = 0.05f;
    
    float rx = 0f;
    float ry = 0f;
    float rz = 0f;
    
    float rz_mul = 3f;
    
    float acceleration = 0.005f;
    float slow_down = 0.005f;
    
    float fall_speed;
    
    boolean got_faster = false;
    
    boolean rot_x = false;
    boolean rot_y = false;
    boolean rot_z = false;
    
    public Plane(Camera plane) {
        this.plane = plane;
    }
    
    public void speed_up() {
        if(speed < max_speed) speed += acceleration;
        if(speed > max_speed) speed = max_speed;
        got_faster = true;
    }
    
    public void slow_down() {
        if(speed > min_fly_speed) speed -= slow_down;
        if(speed < min_fly_speed) speed = min_fly_speed;
    }
    
    public void rotate_left() {
        rz -= rz_speed;
        rot_z = true;
    }
    
    public void rotate_right() {
        rz += rz_speed;
        rot_z = true;
    }
    
    public void rotate_fast_left() {
        ry -= ry_speed;
        rot_y = true;
    }
    
    public void rotate_fast_right() {
        ry += ry_speed;
        rot_y = true;
    }
    
    public void rotate_up() {
        rx += rx_speed;
        rot_x = true;
    }
    
    public void rotate_down() {
        rx -= rx_speed;
        rot_x = true;
    }
    
    public void move() {
        if(!got_faster) slow_down();
        
        if(!rot_x) {
            if(rx < 0f) rx += rx_speed;
            if(rx > 0f) rx -= rx_speed;
            
            if(rx > -rx_speed && rx < rx_speed) rx = 0f;
        }
        if(!rot_y) {
            if(ry < 0f) ry += ry_speed;
            if(ry > 0f) ry -= ry_speed;
            
            if(rx > -rx_speed && rx < rx_speed) rx = 0f;
        }
        if(!rot_z) {
            if(rz < 0f) rz += rz_speed;
            if(rz > 0f) rz -= rz_speed;
            
            if(rz > -rz_speed && rz < rx_speed) rz = 0f;
        }
        
        plane.rx -= rx;
        plane.ry += rz*rz_mul;
        plane.ry += ry;
        plane.rz -= rz;
        
        Vector3f direction = new Vector3f(0f, 0f, -speed);
        direction.rotateX((float)Math.toRadians(-plane.rx));
        direction.rotateY((float)Math.toRadians(-plane.ry));
        direction.rotateY((float)Math.toRadians(plane.rz));
        plane.x += direction.x;
        plane.y += direction.y;
        plane.z += direction.z;
        
        got_faster = false;
        
        rot_x = false;
        rot_y = false;
        rot_z = false;
        
        plane.rx %= 360f;
        plane.ry %= 360f;
        plane.rz %= 360f;
        while(plane.rx < 0f) plane.rx = 360 - plane.rx;
        while(plane.ry < 0f) plane.ry = 360 - plane.ry;
        while(plane.rz < 0f) plane.rz = 360 - plane.rz;
        
        //System.out.println(speed);
    }
}
