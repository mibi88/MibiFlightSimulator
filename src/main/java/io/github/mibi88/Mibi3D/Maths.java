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

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author mibi88
 */
public class Maths {
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
    
    public static Matrix4f create_projection_matrix(float fov, float near_plane,
            float far_plane, Window window) {
        int[] window_size = window.get_window_size();
        
        float aspect_ratio = window_size[0]/window_size[1];
        float y_scale = (1f/(float)Math.tan(
                (float)Math.toRadians(fov/2f)
        ))*aspect_ratio;
        float x_scale = y_scale/aspect_ratio;
        float frustum_length = far_plane-near_plane;
        
        Matrix4f projection_matrix = new Matrix4f();
        projection_matrix.set(0, 0, x_scale);
        projection_matrix.set(1, 1, y_scale);
        projection_matrix.set(2, 2,
                -((far_plane+near_plane)/frustum_length)
        );
        projection_matrix.set(2, 3, -1f);
        projection_matrix.set(3, 2,
                -((2*near_plane*far_plane)/frustum_length)
        );
        projection_matrix.set(3, 3, 0f);
        
        return projection_matrix;
    }
    
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
