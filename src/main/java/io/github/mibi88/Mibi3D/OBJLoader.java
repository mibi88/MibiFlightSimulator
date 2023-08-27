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
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 *
 * @author mibi88
 */
public class OBJLoader {
    private static ArrayList<Float> vertices_array;
    private static ArrayList<Float> texture_coords_array;
    private static ArrayList<Float> normals_array;
    private static ArrayList<Integer> indices_array;

    private static String[] line;
    
    private static float[] vertices, texture_coords, normals;
    private static int[] indices;
    
    private static InputStream file;
    private static String texture_file_name;
    
    private static BufferedReader buffered_reader;
    
    private static int line_number;
    
    public OBJLoader(String model_file_name, String texture_file_name) {
        file = getClass().getClassLoader().getResourceAsStream(
                model_file_name
        );
        buffered_reader = new BufferedReader(
                new InputStreamReader(file)
        );
        
        this.texture_file_name = texture_file_name;
        
        line_number = 0;
    }
    
    public static void read_line() throws Exception {
        try {
            line = buffered_reader.readLine().split(" ");
        } catch(Exception exception) {
            line = null;
        }
        line_number++;
    }
    
    public static TexturedModel load_model(int texture_filter, int texture_wrap)
            throws Exception {
        
        try {
            vertices_array = new ArrayList<>();
            texture_coords_array = new ArrayList<>();
            normals_array = new ArrayList<>();
            indices_array = new ArrayList<>();

            line = null;

            do {
                read_line();
                switch(line[0]) {
                    case "v": // A vertex position
                        vertices_array.add(Float.valueOf(line[1]));
                        vertices_array.add(Float.valueOf(line[2]));
                        vertices_array.add(Float.valueOf(line[3]));
                        break;
                    case "vt": // Texture coordinates
                        texture_coords_array.add(Float.valueOf(line[1]));
                        texture_coords_array.add(Float.valueOf(line[2]));
                        break;
                    case "vn": // Normal vector
                        normals_array.add(Float.valueOf(line[1]));
                        normals_array.add(Float.valueOf(line[2]));
                        normals_array.add(Float.valueOf(line[3]));
                        break;
                }
            } while(!"f".equals(line[0]));
            
            texture_coords = new float[vertices_array.size()/3*2];
            normals = new float[vertices_array.size()];

            while(line != null) {
                if(!"f".equals(line[0])) {
                    read_line();
                    continue;
                }
                String[] vertex_1 = line[1].split("/");
                String[] vertex_2 = line[2].split("/");
                String[] vertex_3 = line[3].split("/");

                process_vertex(vertex_1);
                process_vertex(vertex_2);
                process_vertex(vertex_3);

                read_line();
            }

            buffered_reader.close();
            
            vertices = new float[vertices_array.size()];
            indices = new int[indices_array.size()];

            for(int i=0;i<vertices_array.size();i++) {
                vertices[i] = vertices_array.get(i);
            }

            for(int i=0;i<indices_array.size();i++) {
                indices[i] = indices_array.get(i);
            }
        } catch(Exception exception) {
            exception.printStackTrace();
            throw new Exception(String.format(
                    "Error when reading OBJ file line %d: %s",
                    line_number, exception.getMessage())
            );
        }
        
        return new TexturedModel(vertices, indices, normals, texture_coords,
                texture_file_name, texture_filter, texture_wrap);
    }
    
    private static void process_vertex(String[] vertex) {
        // Get the indice of the vertex
        int vertex_indice = parseInt(vertex[0])-1;
        indices_array.add(vertex_indice);
        
        // Get the texture coordinates
        int texture_coords_indice = parseInt(vertex[1])-1;
        texture_coords[vertex_indice*2] =
                texture_coords_array.get(texture_coords_indice*2);
        texture_coords[vertex_indice*2+1] =
                1-texture_coords_array.get(texture_coords_indice*2+1);
        
        // Get the normal vector
        int normal_indice = parseInt(vertex[2])-1;
        System.out.println(vertex_indice);
        normals[vertex_indice*3] = normals_array.get(normal_indice*3);
        normals[vertex_indice*3+1] = normals_array.get(normal_indice*3+1);
        normals[vertex_indice*3+2] = normals_array.get(normal_indice*3+2);
    }
}
