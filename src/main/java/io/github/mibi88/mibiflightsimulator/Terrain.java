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
package io.github.mibi88.mibiflightsimulator;

import io.github.mibi88.Mibi3D.TexturedModel;
import org.joml.Vector3f;
import org.lwjgl.stb.STBPerlin;

/**
 *
 * @author mibi88
 */
public class Terrain {
    public static float[] generate_normals(float x, float y, float z,
            int seed) {
        float height_left = get_height(x-1, y, seed);
        float height_right = get_height(x+1, y, seed);
        float height_up = get_height(x, y-1, seed);
        float height_down = get_height(x, y+1, seed);
        Vector3f normal = new Vector3f(height_left-height_right, 2f,
                height_down-height_up);
        normal.normalize();
        //return new float[]{normal.x, normal.y, normal.z};
        return new float[]{0f, 1f, 0f};
    }
    public static float get_height(float x, float y, int seed) {
        return STBPerlin.stb_perlin_noise3_seed(x*0.1f,
                y*0.1f,
                0,
                0,
                0,
                0,
                seed)*64;
    }
    
    public static void print_array(float[] array, String end) {
        for(int i=0;i<array.length;i++) {
            if(i == array.length-1) {
                System.out.printf("%f", array[i]);
            } else {
                System.out.printf("%f, ", array[i]);
            }
        }
        System.out.print(end);
    }
    public static void print_array(int[] array, String end) {
        for(int i=0;i<array.length;i++) {
            if(i == array.length-1) {
                System.out.printf("%d", array[i]);
            } else {
                System.out.printf("%d, ", array[i]);
            }
        }
        System.out.print(end);
    }
    
    public static TexturedModel generate_terrain(int w, int h, float step,
            String texture_file, int seed) throws Exception {
        w++;
        h++;
        float[] vertices = new float[w*h*3];
        float[] texture_coords = new float[w*h*2];
        float[] normals = new float[w*h*3];
        int[] indices = new int[(w-1)*(h-1)*6];
        for(int y=0;y<h;y++) {
            for(int x=0;x<w;x++) {
                vertices[(y*w+x)*3] = -x*step;
                vertices[(y*w+x)*3+1] = get_height(x, y, seed);
                vertices[(y*w+x)*3+2] = -y*step;
                float[] normal = generate_normals(
                        vertices[y*w+x],
                        vertices[y*w+x+1],
                        vertices[y*w+x+2],
                        seed
                );
                normals[(y*w+x)*3] = normal[0];
                normals[(y*w+x)*3+1] = normal[1];
                normals[(y*w+x)*3+2] = normal[2];
                texture_coords[(y*w+x)*2] = (float)x;
                texture_coords[(y*w+x)*2+1] = (float)y;
            }
        }
        
        int indices_pos = 0;
        for(int y=1;y<h;y++) {
            for(int x=1;x<w;x++) {
                int indice = y*h+x;
                indices[indices_pos++] = indice-h-1;
                indices[indices_pos++] = indice-1;
                indices[indices_pos++] = indice-h;
                
                indices[indices_pos++] = indice-h;
                indices[indices_pos++] = indice-1;
                indices[indices_pos++] = indice;
            }
        }
        // print_array(vertices, "\n");
        // print_array(texture_coords, "\n");
        // print_array(normals, "\n");
        // print_array(indices, "\n");
        return new TexturedModel(vertices, indices, normals, texture_coords,
                texture_file, TexturedModel.FILTER_MIPMAP_LINEAR,
                TexturedModel.WRAP_REPEAT, 4);
    }
}
