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

import io.github.mibi88.Mibi3D.Engine;
import io.github.mibi88.Mibi3D.Texture;
import io.github.mibi88.Mibi3D.TexturedModel;
import io.github.mibi88.Mibi3D.TexturedModelEntity;
import java.util.Random;
import org.joml.Interpolationf;
import org.joml.Vector3f;
import org.lwjgl.stb.STBPerlin;

/**
 *
 * @author mibi88
 */
public class Terrain {
    private float[] vertices;
    private float[] heights;
    public float step;
    public int w, h;
    
    public TexturedModelEntity entities[];
    
    public float[] generate_normals(float x, float y, float z,
            int seed, int w, int h) {
        float height_left = get_height(x-1, z, seed, w, h);
        float height_right = get_height(x+1, z, seed, w, h);
        float height_up = get_height(x, z-1, seed, w, h);
        float height_down = get_height(x, z+1, seed, w, h);
        Vector3f normal = new Vector3f(height_left-height_right, 64f,
                height_down-height_up);
        normal.normalize();
        return new float[]{normal.x, normal.y, normal.z};
    }
    public float get_height(float x, float y, int seed, int w, int h) {
        return STBPerlin.stb_perlin_noise3_seed(x*0.1f,
                y*0.1f,
                0,
                0,
                0,
                0,
                seed)*64f;
    }
    
    public void print_array(float[] array, String end) {
        for(int i=0;i<array.length;i++) {
            if(i == array.length-1) {
                System.out.printf("%f", array[i]);
            } else {
                System.out.printf("%f, ", array[i]);
            }
        }
        System.out.print(end);
    }
    public void print_array(int[] array, String end) {
        for(int i=0;i<array.length;i++) {
            if(i == array.length-1) {
                System.out.printf("%d", array[i]);
            } else {
                System.out.printf("%d, ", array[i]);
            }
        }
        System.out.print(end);
    }
    
    public TexturedModel generate_terrain(int w, int h, float step,
            String texture_file, int seed, int min_lamp_spacing,
            int max_lamp_spacing, int min_tree_spacing, int max_tree_spacing,
            TexturedModel street_lamp, TexturedModel tree, Engine engine)
            throws Exception {
        w++;
        h++;
        this.w = w;
        this.h = h;
        this.step = step;
        vertices = new float[w*h*3];
        heights = new float[w*h];
        int lamp_x_amount = (w*(int)step/max_lamp_spacing);
        int lamp_y_amount = (h*(int)step/max_lamp_spacing);
        int tree_x_amount = (w*(int)step/max_tree_spacing);
        int tree_y_amount = (h*(int)step/max_tree_spacing);
        entities = new TexturedModelEntity[lamp_x_amount*lamp_y_amount +
                tree_x_amount*tree_y_amount];
        Random random = new Random(seed);
        float[] texture_coords = new float[w*h*2];
        float[] normals = new float[w*h*3];
        int[] indices = new int[(w-1)*(h-1)*6];
        for(int y=0;y<h;y++) {
            for(int x=0;x<w;x++) {
                vertices[(y*w+x)*3] = -x*step;
                vertices[(y*w+x)*3+1] = get_height(x, y, seed, w, h);
                heights[y*w+x] = vertices[(y*w+x)*3+1];
                vertices[(y*w+x)*3+2] = -y*step;
                float[] normal = generate_normals(
                        vertices[(y*w+x)*3],
                        vertices[(y*w+x)*3+1],
                        vertices[(y*w+x)*3+2],
                        seed,
                        w, h
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
        
        float x = 0f, y = 0f;
        int pointer = 0;
        for(int int_y=0;int_y<lamp_y_amount;int_y++) {
            for(int int_x=0;int_x<lamp_x_amount;int_x++) {
                x = min_lamp_spacing + random.nextFloat() *
                        (max_lamp_spacing-min_lamp_spacing) +
                        int_x*max_lamp_spacing;
                y = min_lamp_spacing + random.nextFloat() *
                        (max_lamp_spacing-min_lamp_spacing) +
                        int_y*max_lamp_spacing;
                entities[pointer++] = engine.create_entity(street_lamp,
                        x, get_height_at_pos(x, y), -y,
                        0f, 0f, 0f, 1f, 0);
            }
        }
        for(int int_y=0;int_y<tree_y_amount;int_y++) {
            for(int int_x=0;int_x<tree_x_amount;int_x++) {
                x = min_tree_spacing + random.nextFloat() *
                        (max_tree_spacing-min_tree_spacing) +
                        int_x*max_tree_spacing;
                y = min_tree_spacing + random.nextFloat() *
                        (max_tree_spacing-min_tree_spacing) +
                        int_y*max_tree_spacing;
                //
                entities[pointer++] = engine.create_entity(tree,
                        x, get_height_at_pos(x, y), -y,
                        0f, 0f, 0f, 1f, 0);
            }
        }
        
        return new TexturedModel(vertices, indices, normals, texture_coords,
                texture_file, Texture.FILTER_MIPMAP_LINEAR,
                Texture.WRAP_REPEAT, 4,
                1);
    }
    
    public float get_height_at_pos(float x, float y) {
        x = (w*step)-x;
        int int_x = (int)(x/step), int_y = (int)(y/step);
        int max_pos = (int_y+1)*w+(int_x+1);
        int min_pos = (int_y-1)*w+(int_x-1);
        if(min_pos < 0 || max_pos >= heights.length) {
            System.out.println("WTF! It's out of bounds!");
            return 0f;
        }
        float x_pos = (x%step)/step;
        float y_pos = (y%step)/step;
        float height;
        if(x_pos <= 1f-y_pos) {
            height = Interpolationf.interpolateTriangle(
                    0f, 0f, heights[int_y*w+int_x],
                    1f, 0f, heights[int_y*w+int_x+1],
                    0f, 1f, heights[(int_y+1)*w+int_x],
                    x_pos, y_pos
            );
        } else {
            height = Interpolationf.interpolateTriangle(
                    1f, 0f, heights[int_y*w+int_x+1],
                    1f, 1f, heights[(int_y+1)*w+int_x+1],
                    0f, 1f, heights[(int_y+1)*w+int_x],
                    x_pos, y_pos
            );
        }
        return height;
    }
}
