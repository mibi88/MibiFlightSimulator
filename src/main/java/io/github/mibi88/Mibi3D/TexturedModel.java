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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author mibi88
 */
public class TexturedModel extends Model {
    public int texture_id;
    public final static int FILTER_NEAREST = GL30.GL_NEAREST;
    public final static int FILTER_LINEAR = GL30.GL_LINEAR;
    
    private ArrayList<Integer> texture_list;
    
    public TexturedModel(float[] vertices, int[] indices,
            float[] texture_coords, String texture_file, int texture_filter)
            throws Exception {
        super(vertices, indices, texture_coords);
        texture_list = new ArrayList<Integer>();
        texture_id = load_texture(texture_file, texture_filter);
    }
    
    public int load_texture(String file_name, int filter) throws Exception {
        int data[];
        InputStream stream = getClass().getClassLoader().getResourceAsStream(
                file_name
        );
        
        BufferedImage image = ImageIO.read(stream);
        
        int width = image.getWidth(), height = image.getHeight();
        int size = width*height;
        
        int[] pixels = new int[size];
        
        image.getRGB(0, 0, width, height, pixels,
                0, width);
        
        data = new int[size];
        
        for(int i=0;i<size;i++) {
            int r = (pixels[i]&0xff0000)>>16;
            int g = (pixels[i]&0xff00)>>8;
            int b = (pixels[i]&0xff);
            int a = (pixels[i]&0xff000000)>>24;
            
            data[i] = a<<24|b<<16|g<<8|r;
        }
        
        int id = GL30.glGenTextures();
        
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D,
                GL30.GL_TEXTURE_MIN_FILTER, filter);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D,
                GL30.GL_TEXTURE_MAG_FILTER, filter);
        
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length*4);
        buffer.put(data);
        buffer.flip();
        
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0,
                GL30.GL_RGBA, width, height, 0,
                GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, buffer);
        
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
        
        texture_list.add(id);
        
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
        GL30.glDeleteVertexArrays(super.vao);
        for(int vbo:super.vbo_list) {
            GL30.glDeleteBuffers(vbo);
        }
        for(int texture:texture_list) {
            GL30.glDeleteTextures(texture);
        }
    }
}
