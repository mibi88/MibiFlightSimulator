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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.stream.Collectors;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

/**
 *
 * @author mibi88
 */
public class Shaders {
    private int program_id, vertex_shader_id, fragment_shader_id;
    
    private FloatBuffer matrix4f;
    
    public Shaders(String vertex_shader_resource,
            String fragment_shader_resource) throws Exception {
        matrix4f = BufferUtils.createFloatBuffer(16);
        
        vertex_shader_id = load_shader_file(vertex_shader_resource,
                GL30.GL_VERTEX_SHADER);
        fragment_shader_id = load_shader_file(fragment_shader_resource,
                GL30.GL_FRAGMENT_SHADER);
        
        program_id = GL30.glCreateProgram();
        GL30.glAttachShader(program_id, vertex_shader_id);
        GL30.glAttachShader(program_id, fragment_shader_id);
    }
    
    public void finish_init() {
        GL30.glLinkProgram(program_id);
        GL30.glValidateProgram(program_id);
    }
    
    public int get_uniform_location(String name) {
        return GL30.glGetUniformLocation(program_id, name);
    }
    
    public void load_in_uniform_var(int location, float item) {
        GL30.glUniform1f(location, item);
    }
    
    public void load_in_uniform_var(int location, Vector3f item) {
        GL30.glUniform3f(location, item.x, item.y, item.z);
    }
    
    public void load_in_uniform_var(int location, Vector4f item) {
        GL30.glUniform4f(location, item.x, item.y, item.z, item.w);
    }
    
    public void load_in_uniform_var(int location, boolean item) {
        GL30.glUniform1i(location, item ? 1 : 0);
    }
    
    public void load_in_uniform_var(int location, int item) {
        GL30.glUniform1i(location, item);
    }
    
    public void load_in_uniform_var(int location, Matrix4f item) {
        item.get(matrix4f);
        GL30.glUniformMatrix4fv(location, false, matrix4f);
    }
    
    private int load_shader_file(String file_name, int shader_type)
            throws Exception {
        InputStream file = getClass().getClassLoader().getResourceAsStream(
                file_name
        );
        String content = new BufferedReader(
                new InputStreamReader(file)
        ).lines().collect(Collectors.joining("\n"));
        
        int shader_id = GL30.glCreateShader(shader_type);
        GL30.glShaderSource(shader_id, content);
        GL30.glCompileShader(shader_id);
        
        // Check if the shader was compiled
        int compile_status = GL30.glGetShaderi(shader_id,
                GL30.GL_COMPILE_STATUS);
        if(compile_status == GL30.GL_FALSE) {
            String logs = GL30.glGetShaderInfoLog(shader_id);
            String message = String.format(
                    "Could not compile shader!\nLogs:\n%s",
                    logs
            );
            throw new Exception(message);
        }
        
        return shader_id;
    }
    
    public void bind_attribute(int vbo_n, String input_var_name) {
        GL30.glBindAttribLocation(program_id, vbo_n,
                input_var_name);
    }
    
    public void start() {
        GL30.glUseProgram(program_id);
    }
    
    public void stop() {
        GL30.glUseProgram(0);
    }
    
    public void free() {
        stop();
        
        GL30.glDetachShader(program_id, vertex_shader_id);
        GL30.glDetachShader(program_id, fragment_shader_id);
        
        GL30.glDeleteShader(vertex_shader_id);
        GL30.glDeleteShader(fragment_shader_id);
        
        GL30.glDeleteProgram(program_id);
    }
}
