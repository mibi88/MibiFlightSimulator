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

import java.util.ArrayList;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author mibi88
 */
public class Image extends VAO {
    private final ArrayList<Integer> texture_list;
    protected int texture_id, texture_atlas_size;
    
    public int[] size;
    
    public Image(String texture_file, int texture_filter,
            int texture_wrap, int texture_atlas_size) throws Exception {
        this.texture_atlas_size = texture_atlas_size;
        
        texture_list = new ArrayList<>();
        Texture texture = new Texture();
        texture_id = texture.load_texture(texture_file,
                texture_filter, texture_wrap, 0,
                texture_list);
        size = texture.get_size();
        
        load_quad();
        unbind_vao();
    }
    
    private void load_quad() {
        float[] vertices = {
            0f, 0f, // v0
            0f, -1f, // v1
            1f, -1f, // v2
            1f, 0f // v3
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
        
        load_vertices(vertices);
        load_indices(indices);
        load_texture_coords(texture_coords);
    }
    
    private void load_vertices(float[] vertices) {
        // Create the VBO that will contain the vertices
        create_vbo(GL30.GL_ARRAY_BUFFER);
        
        // Put the data in the VBO
        load_in_vbo(0, GL30.GL_ARRAY_BUFFER, 2,
                vertices, true);
        
        // Unbind the VBO
        unbind_vbo(GL30.GL_ARRAY_BUFFER);
    }
    
    private void load_indices(int[] indices) {
        create_vbo(GL30.GL_ELEMENT_ARRAY_BUFFER);
        
        // Put the data in the VBO
        load_in_vbo(0, GL30.GL_ELEMENT_ARRAY_BUFFER,
                2, indices, false);
    }
    
    private void load_texture_coords(float[] texture_coords) {
        // Create the VBO that will contain the vertices
        create_vbo(GL30.GL_ARRAY_BUFFER);
        
        // Put the data in the VBO
        load_in_vbo(1, GL30.GL_ARRAY_BUFFER, 2,
                texture_coords, true);
        
        // Unbind the VBO
        unbind_vbo(GL30.GL_ARRAY_BUFFER);
    }
    
    /**
     * Start using the texture of this image
     */
    public void bind_texture() {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture_id);
    }
    
    /**
     * Stop using the texture of this image
     */
    public void unbind_texture() {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
    }
}
