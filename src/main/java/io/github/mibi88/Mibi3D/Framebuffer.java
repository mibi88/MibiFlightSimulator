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

import java.nio.ByteBuffer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL30;

/**
 * A class to handle framebuffers
 * 
 * @author mibi88
 */
public class Framebuffer extends VAO {
    protected int frame_buffer;
    protected int color_texture_id, depth_texture_id;
    protected int width, height;
    
    public Framebuffer(Window window)
            throws Exception {
        create_frame_buffer(window);
        
        // For rendering with shaders
        load_quad();
        unbind_vao();
    }
    
    private void create_frame_buffer(Window window) throws Exception {
        frame_buffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,
                frame_buffer);
        GL30.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
        create_textures(window);
    }
    
    public void bind_frame_buffer() throws Exception {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,
                frame_buffer);
        GL30.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
        if(GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) !=
                GL30.GL_FRAMEBUFFER_COMPLETE){
            throw new Exception("Failed to bind the framebuffer!");
        }
    }
    
    public void unbind_frame_buffer() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }
    
    private void create_textures(Window window) {
        color_texture_id = GL30.glGenTextures();
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, color_texture_id);
        
        int[] window_size = window.get_window_size();
        width = window_size[0];
        height = window_size[1];
        
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0,
                GL30.GL_RGB, width, height,
                0, GL30.GL_RGB, GL30.GL_UNSIGNED_BYTE,
                (ByteBuffer)null);
        
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D,
                GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D,
                GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
        
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER,
                GL30.GL_COLOR_ATTACHMENT0, GL30.GL_TEXTURE_2D,
                color_texture_id, 0);
        
        depth_texture_id = GL30.glGenTextures();
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, depth_texture_id);
        
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0,
                GL30.GL_DEPTH24_STENCIL8, width,
                height, 0, GL30.GL_DEPTH_STENCIL,
                GL30.GL_UNSIGNED_INT_24_8, (ByteBuffer)null);
        
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D,
                GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D,
                GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
        
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER,
                GL30.GL_DEPTH_STENCIL_ATTACHMENT,
                GL30.GL_TEXTURE_2D, depth_texture_id, 0);
    }
    public void update_textures(Window window) {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, color_texture_id);
        
        int[] window_size = window.get_window_size();
        width = window_size[0];
        height = window_size[1];
        
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0,
                GL30.GL_RGB, width, height,
                0, GL30.GL_RGB, GL30.GL_UNSIGNED_BYTE,
                (ByteBuffer)null);
        
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D,
                GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D,
                GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
        
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER,
                GL30.GL_COLOR_ATTACHMENT0, GL30.GL_TEXTURE_2D,
                color_texture_id, 0);
        
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, depth_texture_id);
        
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0,
                GL30.GL_DEPTH24_STENCIL8, width,
                height, 0, GL30.GL_DEPTH_STENCIL,
                GL30.GL_UNSIGNED_INT_24_8, (ByteBuffer)null);
        
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D,
                GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D,
                GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
        
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER,
                GL30.GL_DEPTH_STENCIL_ATTACHMENT,
                GL30.GL_TEXTURE_2D, depth_texture_id, 0);
    }
    
    public void render() {
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER,
                frame_buffer);
        GL30.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
        GL30.glBlitFramebuffer(0, 0, width, height, 0,
                0, width, height, GL30.GL_COLOR_BUFFER_BIT,
                GL30.GL_NEAREST);
    }
    public void render_with_shaders(Shaders shaders) {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        // Start using this texture and VBOs
        GL30.glBindVertexArray(this.get_vao());
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, color_texture_id);
        GL30.glDisable(GL30.GL_DEPTH_TEST);
        
        // Render the framebuffer
        GL30.glDrawElements(GL30.GL_TRIANGLES,
                6, GL30.GL_UNSIGNED_INT,
                0);
        
        // Stop using this texture and VBOs
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glEnable(GL30.GL_DEPTH_TEST);
    }
    
    // For rendering with shaders
    private void load_quad() {
        float[] vertices = {
            -1f, 1f, // v0
            -1f, -1f, // v1
            1f, -1f, // v2
            1f, 1f // v3
        };

        int[] indices = {
            0, 1, 3, // Top left triangle (v0, v1, v3)
            3, 1, 2 // Bottom right triangle (v3, v1, v2)
        };

        float[] texture_coords = {
            0f, 1f, // v0
            0f, 0f, // v1
            1f, 0f, // v2
            1f, 1f // v3
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
    
    public void free() {
        GL30.glDeleteFramebuffers(frame_buffer);
    }
}
