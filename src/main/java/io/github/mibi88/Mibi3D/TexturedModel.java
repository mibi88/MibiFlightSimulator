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
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

/**
 *
 * @author mibi88
 */
public class TexturedModel extends Model {
    public int texture_id;
    public final static int FILTER_NEAREST = GL30.GL_NEAREST;
    public final static int FILTER_LINEAR = GL30.GL_LINEAR;
    public final static int FILTER_MIPMAP_NEAREST =
            GL30.GL_NEAREST_MIPMAP_NEAREST;
    public final static int FILTER_MIPMAP_LINEAR = GL30.GL_LINEAR_MIPMAP_LINEAR;
    
    public final static int WRAP_REPEAT = GL30.GL_REPEAT;
    public final static int WRAP_MIRRORED_REPEAT = GL30.GL_MIRRORED_REPEAT;
    public final static int WRAP_CLAMP_TO_EDGE = GL30.GL_CLAMP_TO_EDGE;
    public final static int WRAP_CLAMP_TO_BORDER = GL30.GL_CLAMP_TO_BORDER;
    
    private final ArrayList<Integer> texture_list;
    
    public TexturedModel(float[] vertices, int[] indices,
            float[] texture_coords, String texture_file, int texture_filter,
            int texture_wrap) throws Exception {
        super(vertices, indices, texture_coords);
        texture_list = new ArrayList<Integer>();
        texture_id = load_texture(texture_file, texture_filter,
                texture_wrap);
    }
    
    private int load_texture(String file_name, int filter, int wrap)
            throws Exception {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(
                file_name
        );
        byte[] image_data_bytes = stream.readAllBytes();
        ByteBuffer image_data = BufferUtils.createByteBuffer(
                image_data_bytes.length
        );
        image_data.put(image_data_bytes);
        image_data.flip();
        
        int id = GL30.glGenTextures();
        
        // Bind the texture
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, id);
        
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D,
                GL30.GL_TEXTURE_MIN_FILTER, filter);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D,
                GL30.GL_TEXTURE_MAG_FILTER, filter);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D,
                GL30.GL_TEXTURE_WRAP_S, wrap);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D,
                GL30.GL_TEXTURE_WRAP_T, wrap);
        
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        
        //STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = STBImage.stbi_load_from_memory(image_data,
                width, height, channels, 4);
        
        System.out.printf("Image properties: w=%d, h=%d, channels=%d\n",
                width.get(0), height.get(0),
                channels.get(0));
        
        if(image == null) {
            throw new Exception("Failed to load image!");
        }
        
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0,
                GL30.GL_RGBA, width.get(0),
                height.get(0), 0, GL30.GL_RGBA,
                GL30.GL_UNSIGNED_BYTE, image);
        GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
        
        STBImage.stbi_image_free(image);
        
        texture_list.add(id);
        
        // Unbind the texture
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
        
        return id;
    }
    
    public void bind_texture() {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture_id);
    }
    
    public void unbind_texture() {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
    }
    
    @Override
    public void free() {
        super.free();
        for(int texture:texture_list) {
            GL30.glDeleteTextures(texture);
        }
    }
}
