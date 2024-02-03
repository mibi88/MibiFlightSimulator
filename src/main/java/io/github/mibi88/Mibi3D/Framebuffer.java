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
import org.lwjgl.opengl.GL30;

/**
 * A class to handle framebuffers
 * 
 * @author mibi88
 */
public class Framebuffer {
    protected int frame_buffer;
    protected int color_texture_id, depth_texture_id;
    protected int width, height;
    
    public Framebuffer(Window window) {
        create_frame_buffer(window);
    }
    
    private void create_frame_buffer(Window window) {
        frame_buffer = GL30.glGenFramebuffers();
        bind_frame_buffer();
        create_textures(window);
    }
    
    public void bind_frame_buffer() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER,
                frame_buffer);
        GL30.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
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
    
    public void free() {
        GL30.glDeleteFramebuffers(frame_buffer);
    }
}
