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
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;
import static org.lwjgl.assimp.Assimp.aiProcess_Triangulate;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.AIFace;

/**
 * A 3D model
 * 
 * @author mibi88
 */
public class Model extends VAO {
    public float shine_damper = 10f, reflectivity = 0f;
    
    /**
     * Load a model
     * 
     * @param vertices An array of vertices
     * @param indices The position of the vertex to use in the array of vertices
     * @param normals The normals of the vertices
     * @param texture_coords The texture coordinates for each vertex
     */
    public Model(float[] vertices, int[] indices, float[] normals,
            float[] texture_coords) {
        super();
        init(vertices, indices, normals, texture_coords);
    }
    
    /**
     * Load a model
     * 
     * @param obj_file The obj file to load
     * @param mesh_num The number of the mesh to load
     * @throws Exception
     */
    public Model(String obj_file, int mesh_num) throws Exception {
        super();
        InputStream stream = getClass().getClassLoader().getResourceAsStream(
                obj_file
        );
        byte[] file_bytes = stream.readAllBytes();
        ByteBuffer file_data = BufferUtils.createByteBuffer(
                file_bytes.length
        );
        file_data.put(file_bytes);
        file_data.flip();
        AIScene ai_scene = Assimp.aiImportFileFromMemory(file_data,
                aiProcess_Triangulate, "");
        if (ai_scene == null) {
            throw new Exception("Error when loading model!");
        }
        int mesh_amount = ai_scene.mNumMeshes();
        if(mesh_num < 0 || mesh_num >= mesh_amount) {
            throw new Exception("Bad mesh number!");
        }
        PointerBuffer ai_meshes = ai_scene.mMeshes();
        if(ai_meshes == null) {
            throw new Exception("ai_meshes is null");
        }
        AIMesh mesh = AIMesh.create(ai_meshes.get(mesh_num));
        
        ArrayList<Float> vertices_array = new ArrayList<>();
        ArrayList<Float> texture_coords_array = new ArrayList<>();
        ArrayList<Float> normals_array = new ArrayList<>();
        ArrayList<Integer> indices_array = new ArrayList<>();
        
        // Vertices
        AIVector3D.Buffer vertices_buffer = mesh.mVertices();
        
        for(int i=0;i<vertices_buffer.limit();i++) {
            AIVector3D vector = vertices_buffer.get(i);
            
            vertices_array.add(vector.x());
            vertices_array.add(vector.y());
            vertices_array.add(vector.z());
        }
        
        // Texture coordinates
        AIVector3D.Buffer texture_coords_buffer = mesh.mTextureCoords(0);
        if(texture_coords_buffer == null) {
            throw new Exception("Texture coordinates buffer is null");
        }
        
        for(int i=0;i<texture_coords_buffer.limit();i++) {
            AIVector3D vector = texture_coords_buffer.get(i);
            
            texture_coords_array.add(vector.x());
            texture_coords_array.add(1-vector.y());
        }
        
        // Normals
        AIVector3D.Buffer normals_buffer = mesh.mNormals();
        if(normals_buffer == null) {
            throw new Exception("Normals buffer is null");
        }
        
        for(int i=0;i<normals_buffer.limit();i++) {
            AIVector3D vector = normals_buffer.get(i);
            
            normals_array.add(vector.x());
            normals_array.add(vector.y());
            normals_array.add(vector.z());
        }
        // Indices
        int faces_amount = mesh.mNumFaces();
        AIFace.Buffer facesBuffer = mesh.mFaces();
        for(int i=0;i<faces_amount;i++) {
            AIFace face = facesBuffer.get(i);
            if(face.mNumIndices() != 3) {
                throw new Exception("Three indices required!");
            }
            IntBuffer indices = face.mIndices();
            indices_array.add(indices.get(0));
            indices_array.add(indices.get(1));
            indices_array.add(indices.get(2));
        }
        
        // Convert all ArrayLists to arrays
        float[] vertices = new float[vertices_array.size()];
        float[] texture_coords = new float[texture_coords_array.size()];
        float[] normals = new float[normals_array.size()];
        int[] indices = new int[indices_array.size()];
        
        for(int i=0;i<vertices.length;i++) {
            vertices[i] = vertices_array.get(i);
        }
        for(int i=0;i<texture_coords.length;i++) {
            texture_coords[i] = texture_coords_array.get(i);
        }
        for(int i=0;i<normals.length;i++) {
            normals[i] = normals_array.get(i);
        }
        for(int i=0;i<indices.length;i++) {
            indices[i] = indices_array.get(i);
        }
        init(vertices, indices, normals, texture_coords);
    }
    
    /**
     * Load the model from data in arrays
     * 
     * @param vertices The array of vertices
     * @param indices The array of indices of the vertices
     * @param normals The array of normals
     * @param texture_coords The array of texture coordinates
     */
    private void init(float[] vertices, int[] indices, float[] normals,
            float[] texture_coords) {
        vertices_amount = indices.length;
        
        // Load the vertices into a VBO
        load_vertices(vertices);
        
        // Load the indices into a VBO
        load_indices(indices);
        
        // Load the texture coordinates into a VBO
        load_texture_coords(texture_coords);
        
        // Load the normals into a VBO
        load_normals(normals);
        
        // Unbind the VAO
        unbind_vao();
    }
    
    /**
     * Load the vertices in a VBO
     * 
     * @param vertices The array of vertices
     */
    private void load_vertices(float[] vertices) {
        // Create the VBO that will contain the vertices
        create_vbo(GL30.GL_ARRAY_BUFFER);
        
        // Put the data in the VBO
        load_in_vbo(0, GL30.GL_ARRAY_BUFFER, 3,
                vertices, true);
        
        // Unbind the VBO
        unbind_vbo(GL30.GL_ARRAY_BUFFER);
    }
    
    /**
     * Load the texture coordinates in a VBO
     * 
     * @param texture_coords The array of vertices
     */
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
     * Load the normals in a VBO
     * 
     * @param normals The array of normals
     */
    private void load_normals(float[] normals) {
        // Create the VBO that will contain the vertices
        create_vbo(GL30.GL_ARRAY_BUFFER);
        
        // Put the data in the VBO
        load_in_vbo(2, GL30.GL_ARRAY_BUFFER, 3,
                normals, true);
        
        // Unbind the VBO
        unbind_vbo(GL30.GL_ARRAY_BUFFER);
    }
    
    /**
     * Load the vertex indices in a VBO
     * 
     * @param indices The array of indices
     */
    private void load_indices(int[] indices) {
        create_vbo(GL30.GL_ELEMENT_ARRAY_BUFFER);
        
        // Put the data in the VBO
        load_in_vbo(0, GL30.GL_ELEMENT_ARRAY_BUFFER,
                3, indices, false);
    }
    
    /**
     * Get the amount of vertices in this model
     * 
     * @return The amount of vertices
     */
    public int get_vertices_amount() {
        return vertices_amount;
    }
}
