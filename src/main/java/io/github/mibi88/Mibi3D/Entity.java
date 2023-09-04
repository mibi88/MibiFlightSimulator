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
public class Entity {
    public TexturedModel model;
    public float x, y, z;
    public float rx, ry, rz;
    public float scale;
    public Shaders shaders;
    public int transformation_matrix_location;
    public final int texture_x_location;
    public final int texture_y_location;
    public final int cell_size_location;
    
    public final float texture_x;
    public final float texture_y;
    
    public final float cell_size;
    
    /**
     * Create a new entity of a TexturedModel
     * 
     * @param model The TexturedModel to create an entity from
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
     * @param texture_num The number of the texture
     * @param texture_x_location The location of the texture_x uniform variable
     * @param texture_y_location The location of the texture_y uniform variable
     */
    public Entity(TexturedModel model, float x, float y, float z, float rx,
            float ry, float rz, float scale, Shaders shaders,
            int transformation_matrix_location, int texture_num,
            int texture_x_location, int texture_y_location,
            int cell_size_location) {
        this.model = model;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rx = rx%360;
        this.ry = ry%360;
        this.rz = rz%360;
        this.scale = scale;
        this.shaders = shaders;
        this.transformation_matrix_location = transformation_matrix_location;
        this.texture_x_location = texture_x_location;
        this.texture_y_location = texture_y_location;
        this.cell_size_location = cell_size_location;
        if(model.texture_atlas_size > 1) {
            float cell_size = 1f/(float)model.texture_atlas_size;
            int texture_x = texture_num%model.texture_atlas_size;
            this.texture_x = (float)texture_x*cell_size;
            int texture_y = texture_num/model.texture_atlas_size;
            this.texture_y = (float)texture_y*cell_size;
            this.cell_size = cell_size;
        } else {
            this.texture_x = 0f;
            this.texture_y = 0f;
            this.cell_size = 1f;
        }
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
