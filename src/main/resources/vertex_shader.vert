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
#version 400 core

in vec3 position;
in vec2 texture_coords;
in vec3 normal;

out vec2 pass_texture_coords;

out vec3 normal_vector;
out vec3 to_light_vector;
out vec3 to_camera_vector;

out float visibility;

uniform mat4 transformation_matrix;
uniform mat4 projection_matrix;
uniform mat4 view_matrix;

uniform vec3 light_position;

uniform float fog_gradient;
uniform float fog_density;

uniform int fog;

uniform float texture_x;
uniform float texture_y;

uniform float cell_size;

void main(void) {
    vec4 world_position = transformation_matrix * vec4(position, 1.0);
    
    vec4 position_relative_to_camera = view_matrix * world_position;
    gl_Position = projection_matrix * position_relative_to_camera;
    pass_texture_coords = texture_coords;
    
    if(cell_size < 1.0) {
        pass_texture_coords *= cell_size;
        pass_texture_coords.x += texture_x;
        pass_texture_coords.y += texture_y;
    }
    
    normal_vector = (transformation_matrix * vec4(normal, 0.0)).xyz;
    to_light_vector = light_position - world_position.xyz;
    
    to_camera_vector = (inverse(view_matrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz -
            world_position.xyz;
    
    if(fog == 1) {
        float distance_from_camera = length(position_relative_to_camera.xyz);
        visibility = clamp(
                exp(-pow(distance_from_camera*fog_density, fog_gradient)),
                0.0, 1.0);
    }
}
