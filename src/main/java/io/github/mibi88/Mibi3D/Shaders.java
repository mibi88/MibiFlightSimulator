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
 * A class that manages the vertex and fragment shaders
 * 
 * @author mibi88
 */
public class Shaders {
    private final int program_id, vertex_shader_id, fragment_shader_id;
    
    private final FloatBuffer matrix4f;
    
    /**
     * Load the vertex and the fragment shader
     * 
     * @param vertex_shader_resource The path to the vertex shader file in the
     * resources
     * @param fragment_shader_resource The path to the fragment shader file in
     * the resources
     * @throws Exception
     */
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
    
    /**
     * Finish loading the shaders after calling bind_attribute for each VBO
     */
    public void finish_init() {
        GL30.glLinkProgram(program_id);
        GL30.glValidateProgram(program_id);
    }
    
    /**
     * Get the location of a uniform variable
     * 
     * @param name The name of the uniform variable
     * @return The location of the uniform variable
     */
    public int get_uniform_location(String name) {
        return GL30.glGetUniformLocation(program_id, name);
    }
    
    /**
     * Load a value in an uniform variable
     * 
     * @param location The location of the uniform variable (get it using
     * get_uniform_location)
     * @param item The float to load to the uniform variable
     */
    public void load_in_uniform_var(int location, float item) {
        GL30.glUniform1f(location, item);
    }
    
    /**
     * Load a value in an uniform variable
     * 
     * @param location The location of the uniform variable (get it using
     * get_uniform_location)
     * @param item The Vector3f to load to the uniform variable
     */
    public void load_in_uniform_var(int location, Vector3f item) {
        GL30.glUniform3f(location, item.x, item.y, item.z);
    }
    
    /**
     * Load a value in an uniform variable
     * 
     * @param location The location of the uniform variable (get it using
     * get_uniform_location)
     * @param item The Vector4f to load to the uniform variable
     */
    public void load_in_uniform_var(int location, Vector4f item) {
        GL30.glUniform4f(location, item.x, item.y, item.z, item.w);
    }
    
    /**
     * Load a value in an uniform variable
     * 
     * @param location The location of the uniform variable (get it using
     * get_uniform_location)
     * @param item The boolean to load to the uniform variable
     */
    public void load_in_uniform_var(int location, boolean item) {
        GL30.glUniform1i(location, item ? 1 : 0);
    }
    
    /**
     * Load a value in an uniform variable
     * 
     * @param location The location of the uniform variable (get it using
     * get_uniform_location)
     * @param item The integer to load to the uniform variable
     */
    public void load_in_uniform_var(int location, int item) {
        GL30.glUniform1i(location, item);
    }
    
    /**
     * Load a value in an uniform variable
     * 
     * @param location The location of the uniform variable (get it using
     * get_uniform_location)
     * @param item The Matrix4f to load to the uniform variable
     */
    public void load_in_uniform_var(int location, Matrix4f item) {
        item.get(matrix4f);
        GL30.glUniformMatrix4fv(location, false, matrix4f);
    }
    
    /**
     * Load a shader file
     * 
     * @param file_name The shader file in the resources
     * @param shader_type The shader type (vertex shader, fragment shader etc.)
     * @return The id of the shader
     * @throws Exception 
     */
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
    
    /**
     * Bind a VBO to the shader program
     * 
     * @param vbo_n The index of the VBO
     * @param input_var_name The input variable that it will be bound to
     */
    public void bind_attribute(int vbo_n, String input_var_name) {
        GL30.glBindAttribLocation(program_id, vbo_n,
                input_var_name);
    }
    
    /**
     * Start using this shader program
     */
    public void start() {
        GL30.glUseProgram(program_id);
    }
    
    /**
     * Stop using this shader program
     */
    public void stop() {
        GL30.glUseProgram(0);
    }
    
    /**
     * Delete this shader program and the shaders
     */
    public void free() {
        stop();
        
        GL30.glDetachShader(program_id, vertex_shader_id);
        GL30.glDetachShader(program_id, fragment_shader_id);
        
        GL30.glDeleteShader(vertex_shader_id);
        GL30.glDeleteShader(fragment_shader_id);
        
        GL30.glDeleteProgram(program_id);
    }
}
