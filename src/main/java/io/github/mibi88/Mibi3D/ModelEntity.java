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
 * An entity of a model
 * 
 * @author mibi88
 */
public abstract class ModelEntity {
    public float x, y, z;
    public float rx, ry, rz;
    public float scale;
    public Shaders shaders;
    public int transformation_matrix_location;
    
    /**
     * Create a new entity of a 3D object
     * 
     * @param x
     * @param y
     * @param z
     * @param rx The rotation of the entity on the X axis
     * @param ry The rotation of the entity on the Y axis
     * @param rz The rotation of the entity on the Z axis
     * @param scale The scale of the entity
     * @param shaders The shaders that will be used to render this entity
     * @param transformation_matrix_location The location to load the
     * transformation matrix to
     */
    public ModelEntity(float x, float y, float z, float rx,
            float ry, float rz, float scale, Shaders shaders,
            int transformation_matrix_location) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rx = rx%360;
        this.ry = ry%360;
        this.rz = rz%360;
        this.scale = scale;
        this.shaders = shaders;
        this.transformation_matrix_location = transformation_matrix_location;
    }
    
    /**
     * Move the entity
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
     * Rotate the entity
     * 
     * @param rx
     * @param ry
     * @param rz
     */
    public void rotate(float rx, float ry, float rz) {
        this.rx = (this.rx+rx)%360;
        this.ry = (this.ry+ry)%360;
        this.rz = (this.rz+rz)%360;
    }
}
