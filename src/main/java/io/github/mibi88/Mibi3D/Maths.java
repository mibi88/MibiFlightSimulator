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

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Some methods to create matrices
 * 
 * @author mibi88
 */
public class Maths {

    /**
     * Create a transformation matrix
     * 
     * @param translation
     * @param rx The rotation on the X axis
     * @param ry The rotation on the Y axis
     * @param rz The rotation on the Z axis
     * @param scale The scale
     * @return The transformation matrix, a Matrix4f
     */
    public static Matrix4f create_transformation_matrix(Vector3f translation,
            float rx, float ry, float rz, float scale) {
        Matrix4f matrix = new Matrix4f()
                .translate(translation)
                .rotate((float)Math.toRadians(rx),
                        new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(ry),
                        new Vector3f(0, 1, 0))
                .rotate((float)Math.toRadians(rz),
                        new Vector3f(0, 0, 1))
                .scale(scale);
        return matrix;
    }
    
    /**
     * Create a transformation matrix
     * 
     * @param translation
     * @param rot The rotation of the image
     * @param scale_x The scale on the X axis
     * @param scale_y The scale on the Y axis
     * @return The transformation matrix, a Matrix4f
     */
    public static Matrix4f create_transformation_matrix(Vector2f translation,
            float rot, float scale_x, float scale_y) {
        Matrix4f matrix = new Matrix4f()
                .translate(new Vector3f(translation.x, translation.y, 0f))
                .rotate((float)Math.toRadians(rot),
                        new Vector3f(0, 0, 1))
                .scaleXY(scale_x, scale_y);
        return matrix;
    }
    
    /**
     * Create a perspective projection matrix
     * 
     * @param fov The field of view
     * @param near_plane The distance of the near plane
     * @param far_plane The distance of the far plane
     * @param window The Window object that will be used to draw the scene
     * @return The projection matrix, a Matrix4f
     */
    public static Matrix4f create_projection_matrix(float fov, float near_plane,
            float far_plane, Window window) {
        int[] window_size = window.get_window_size();
        float aspect_ratio = (float)window_size[0]/(float)window_size[1];
        
        Matrix4f projection_matrix = new Matrix4f()
                .perspective((float)Math.toRadians(fov),
                        aspect_ratio, near_plane,
                        far_plane);
        
        return projection_matrix;
    }
    
    /**
     * Create a view matrix from a Camera object
     * 
     * @param camera The Camera object
     * @return The view matrix, a Matrix4f
     */
    public static Matrix4f create_view_matrix(Camera camera) {
        Vector3f translation = new Vector3f(-camera.x, -camera.y, -camera.z);
        Matrix4f matrix = new Matrix4f()
                .rotate((float)Math.toRadians(camera.rx),
                        new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(camera.ry),
                        new Vector3f(0, 1, 0))
                .rotate((float)Math.toRadians(camera.rz),
                        new Vector3f(0, 0, 1))
                .translate(translation);
        return matrix;
    }
}
