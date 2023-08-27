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

import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;

/**
 *
 * @author mibi88
 */
public class Model {
    protected final int vao, vertices_amount;
    protected ArrayList<Integer> vbo_list;
    public Model(float[] vertices, int[] indices, float[] normals,
            float[] texture_coords) {
        // Initialize the VBO ArrayList
        vbo_list = new ArrayList<Integer>();
        // Create the VAO
        vertices_amount = indices.length;
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        
        // Load the vertices into a VBO
        load_vertices(vertices);
        
        // Load the indices into a VBO
        load_indices(indices);
        
        // Load the texture coordinates into a VBO
        load_texture_coords(texture_coords);
        
        // Load the normals into a VBO
        load_normals(normals);
        
        // Unbind the VAO
        GL30.glBindVertexArray(0);
    }
    
    private void load_vertices(float[] vertices) {
        // Create the VBO that will contain the vertices
        int vbo = create_vbo(GL30.GL_ARRAY_BUFFER);
        
        // Put the data in the VBO
        load_in_vbo(vbo, 0, GL30.GL_ARRAY_BUFFER, 3,
                vertices, true);
        
        // Unbind the VBO
        unbind_vbo(GL30.GL_ARRAY_BUFFER);
    }
    
    private void load_texture_coords(float[] texture_coords) {
        // Create the VBO that will contain the vertices
        int vbo = create_vbo(GL30.GL_ARRAY_BUFFER);
        
        // Put the data in the VBO
        load_in_vbo(vbo, 1, GL30.GL_ARRAY_BUFFER, 2,
                texture_coords, true);
        
        // Unbind the VBO
        unbind_vbo(GL30.GL_ARRAY_BUFFER);
    }
    
    private void load_normals(float[] normals) {
        // Create the VBO that will contain the vertices
        int vbo = create_vbo(GL30.GL_ARRAY_BUFFER);
        
        // Put the data in the VBO
        load_in_vbo(vbo, 2, GL30.GL_ARRAY_BUFFER, 3,
                normals, true);
        
        // Unbind the VBO
        unbind_vbo(GL30.GL_ARRAY_BUFFER);
    }
    
    private void load_indices(int[] indices) {
        int vbo = create_vbo(GL30.GL_ELEMENT_ARRAY_BUFFER);
        
        // Put the data in the VBO
        load_in_vbo(vbo, 0, GL30.GL_ELEMENT_ARRAY_BUFFER,
                3, indices, false);
    }
    
    private int create_vbo(int type) {
        int vbo = GL30.glGenBuffers();
        vbo_list.add(vbo);
        GL30.glBindBuffer(
                type,
                vbo
        );
        return vbo;
    }
    
    private FloatBuffer convert_to_buffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(
                data.length
        );
        buffer.put(data);
        buffer.flip();
        
        return buffer;
    }
    
    private IntBuffer convert_to_buffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(
                data.length
        );
        buffer.put(data);
        buffer.flip();
        
        return buffer;
    }
    
    private void load_in_vbo(int vbo_n, int pos, int type, int coord_size,
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
    
    private void load_in_vbo(int vbo_n, int pos, int type, int coord_size,
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
    
    private void unbind_vbo(int type) {
        GL30.glBindBuffer(
                type, 0
        );
    }
    
    public void free() {
        GL30.glDeleteVertexArrays(vao);
        for(int vbo:vbo_list) {
            GL30.glDeleteBuffers(vbo);
        }
    }
    
    public int get_vao() {
        return vao;
    }
    
    public int get_vbo(int vbo_n) throws IndexOutOfBoundsException {
        return vbo_list.get(vbo_n);
    }
    
    public int get_vertices_amount() {
        return vertices_amount;
    }
}
