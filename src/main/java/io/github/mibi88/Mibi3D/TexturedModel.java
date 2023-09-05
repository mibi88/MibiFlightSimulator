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

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

/**
 * A model with a texture
 * 
 * @author mibi88
 */
public class TexturedModel extends Model {
    public int texture_id;
    
    public final int texture_atlas_size;
    
    private final ArrayList<Integer> texture_list;
    
    /**
     * Create a new model with a texture
     * 
     * @param vertices The array of vertices of the model
     * @param indices The position of the vertex to use in the array of vertices
     * @param normals The normals for each vertex
     * @param texture_coords The array texture coordinates
     * @param texture_file The resource file of the texture
     * @param texture_filter The filter of the texture (final integers that
     * start with FILTER, in the Texture class)
     * @param texture_wrap The way to wrap the texture (final integers that
     * start with WRAP, in the Texture class)
     * @param anisotropy_amount The amount of anisotropy. 0 to disable it
     * @param texture_atlas_size The size of the texture atlas. Set it to 1 or
     * smaller to disable it
     * @throws Exception
     */
    public TexturedModel(float[] vertices, int[] indices, float[] normals,
            float[] texture_coords, String texture_file, int texture_filter,
            int texture_wrap, float anisotropy_amount, int texture_atlas_size)
            throws Exception {
        super(vertices, indices, normals, texture_coords);
        this.texture_atlas_size = texture_atlas_size;
        texture_list = new ArrayList<>();
        Texture texture = new Texture();
        texture_id = texture.load_texture(texture_file,
                texture_filter, texture_wrap, anisotropy_amount,
                texture_list);
    }
    
    /**
     * Create a new model with a texture
     * 
     * @param obj_file The path to the OBJ file in the resources
     * @param mesh_num The number of the mesh to load in the OBJ file
     * @param texture_file The path to the texture in the resources folder
     * @param texture_filter The filter of the texture (final integers that
     * start with FILTER, in the Texture class)
     * @param texture_wrap The way to wrap the texture (final integers that
     * start with WRAP, in the Texture class)
     * @param anisotropy_amount The amount of anisotropy. 0 to disable it
     * @param texture_atlas_size The size of the texture atlas. Set it to 1 or
     * smaller to disable it
     * @throws Exception
     */
    public TexturedModel(String obj_file, int mesh_num, String texture_file,
            int texture_filter, int texture_wrap, float anisotropy_amount,
            int texture_atlas_size) throws Exception {
        super(obj_file, 0);
        this.texture_atlas_size = texture_atlas_size;
        texture_list = new ArrayList<>();
        Texture texture = new Texture();
        texture_id = texture.load_texture(texture_file,
                texture_filter, texture_wrap, anisotropy_amount,
                texture_list);
    }
    
    /**
     * Start using the texture of this model
     */
    public void bind_texture() {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture_id);
    }
    
    /**
     * Stop using the texture of this model
     */
    public void unbind_texture() {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
    }
    
    /**
     * Delete everything that's not needed anymore
     */
    @Override
    public void free() {
        super.free();
        for(int texture:texture_list) {
            GL30.glDeleteTextures(texture);
        }
    }
}
