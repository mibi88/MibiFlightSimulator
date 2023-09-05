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

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author mibi88
 */
public class VAO {
    protected int vao, vertices_amount;
    protected ArrayList<Integer> vbo_list;
    
    public VAO() {
        // Initialize the VBO ArrayList
        vbo_list = new ArrayList<>();
        // Create the VAO
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
    }
    
    /**
     * Unbind the VAO
     */
    public void unbind_vao() {
        GL30.glBindVertexArray(0);
    }
    
    /**
     * Create a VBO
     * 
     * @param type The type of VBO
     */
    public int create_vbo(int type) {
        int vbo = GL30.glGenBuffers();
        vbo_list.add(vbo);
        GL30.glBindBuffer(
                type,
                vbo
        );
        return vbo;
    }
    
    /**
     * Convert an array to a FloatBuffer
     * 
     * @param data The data to convert
     */
    public FloatBuffer convert_to_buffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(
                data.length
        );
        buffer.put(data);
        buffer.flip();
        
        return buffer;
    }
    
    /**
     * Convert an array to an IntBuffer
     * 
     * @param data The data to convert
     */
    public IntBuffer convert_to_buffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(
                data.length
        );
        buffer.put(data);
        buffer.flip();
        
        return buffer;
    }
    
    /**
     * Store an array in a VBO
     * 
     * @param pos The index of the VBO if attrib_pointer is true
     * @param type The type of data that will be loaded
     * @param coord_size The size of the coordinates that will be loaded
     * @param data The data to store
     * @param attrib_pointer If glVertexAttribPointer should be called
     */
    public void load_in_vbo(int pos, int type, int coord_size,
            float[] data, boolean attrib_pointer) {
        FloatBuffer buffer = convert_to_buffer(data);
        
        GL30.glBufferData(
                type,
                buffer,
                GL30.GL_STATIC_DRAW
        );
        if(attrib_pointer) {
            GL30.glVertexAttribPointer(pos, coord_size,
                    GL30.GL_FLOAT, false, 0, 0);
        }
    }
    
    /**
     * Store an array in a VBO
     * 
     * @param pos The index of the VBO if attrib_pointer is true
     * @param type The type of data that will be loaded
     * @param coord_size The size of the coordinates that will be loaded
     * @param data The data to store
     * @param attrib_pointer If glVertexAttribPointer should be called
     */
    public void load_in_vbo(int pos, int type, int coord_size,
            int[] data, boolean attrib_pointer) {
        IntBuffer buffer = convert_to_buffer(data);
        
        GL30.glBufferData(
                type,
                buffer,
                GL30.GL_STATIC_DRAW
        );
        if(attrib_pointer) {
            GL30.glVertexAttribPointer(pos, coord_size,
                    GL30.GL_FLOAT, false, 0, 0);
        }
    }
    
    /**
     * Unbind the currently bound VBO
     * 
     * @param type The type of data stored in the VBO
     */
    public void unbind_vbo(int type) {
        GL30.glBindBuffer(
                type, 0
        );
    }
    
    /**
     * Delete the VAO and the VBOs
     */
    public void free() {
        GL30.glDeleteVertexArrays(vao);
        for(int vbo:vbo_list) {
            GL30.glDeleteBuffers(vbo);
        }
    }
    
    /**
     * Get the id of the VAO
     * 
     * @return The id of the VAO
     */
    public int get_vao() {
        return vao;
    }
    
    /**
     * Get a VBO
     * 
     * @param vbo_n The number of the VBO
     * @return The id of the VBO
     * @throws IndexOutOfBoundsException
     */
    public int get_vbo(int vbo_n) throws IndexOutOfBoundsException {
        return vbo_list.get(vbo_n);
    }
}
