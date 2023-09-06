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
public class ImageEntity {
    public float x, y, rot, x_scale, y_scale;
    public Image image;
    public Shaders shaders;
    
    public int transformation_matrix_location;
    public final int texture_x_location;
    public final int texture_y_location;
    public final int cell_size_location;
    
    public final float texture_x;
    public final float texture_y;
    
    public final float cell_size;
    
    public ImageEntity(Image image, float x, float y, float rot, float x_scale,
            float y_scale, Shaders shaders, int transformation_matrix_location,
            int texture_num, int texture_x_location, int texture_y_location,
            int cell_size_location) {
        this.x = x;
        this.y = y;
        this.rot = rot;
        
        float width = (float)image.size[0], height = (float)image.size[1];
        
        this.x_scale = x_scale;
        this.y_scale = y_scale;
        if(image.size[0] > image.size[1]) {
            this.y_scale *= height/width;
        } else {
            this.x_scale *= width/height;
        }
        
        this.image = image;
        this.shaders = shaders;
        
        this.transformation_matrix_location = transformation_matrix_location;
        this.texture_x_location = texture_x_location;
        this.texture_y_location = texture_y_location;
        this.cell_size_location = cell_size_location;
        if(image.texture_atlas_size > 1) {
            float cell_size = 1f/(float)image.texture_atlas_size;
            int texture_x = texture_num%image.texture_atlas_size;
            this.texture_x = (float)texture_x*cell_size;
            int texture_y = texture_num/image.texture_atlas_size;
            this.texture_y = (float)texture_y*cell_size;
            this.cell_size = cell_size;
        } else {
            this.texture_x = 0f;
            this.texture_y = 0f;
            this.cell_size = 1f;
        }
    }
}
