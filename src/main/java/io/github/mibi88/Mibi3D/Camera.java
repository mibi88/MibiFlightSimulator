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
public class Camera {
    public float x, y, z;
    public float rx, ry, rz;
    
    /**
     * Create a new camera that the 3D scene will be rendered from
     * 
     * @param x
     * @param y
     * @param z
     * @param rx The rotation on the X axis of the camera
     * @param ry The rotation on the Y axis of the camera
     * @param rz The rotation on the Z axis of the camera
     */
    public Camera(float x, float y, float z, float rx, float ry, float rz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
    }
    
    /**
     * Move the camera in the 3D scene
     * 
     * @param x
     * @param y
     * @param z
     */
    public void move(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }
    
    /**
     * Rotate the camera
     * 
     * @param rx The rotation on the X axis
     * @param ry The rotation on the Y axis
     * @param rz The rotation on the Z axis
     */
    public void rotate(float rx, float ry, float rz) {
        this.rx += rx;
        this.ry += ry;
        this.rz += rz;
    }
}
