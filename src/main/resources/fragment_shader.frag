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

in vec2 pass_texture_coords;

in vec3 normal_vector;
in vec3 to_light_vector;
in vec3 to_camera_vector;

in float visibility;

out vec4 out_color;

uniform sampler2D texture_sampler;
uniform vec3 light_color;

uniform float shine_damper;
uniform float reflectivity;
uniform float ambient_lighting;

uniform vec3 sky_color;

void main(void) {
    // Diffuse lighting
    vec3 unit_normal = normalize(normal_vector);
    vec3 unit_to_light = normalize(to_light_vector);
    
    float normal_dot_to_light = dot(unit_normal, unit_to_light);
    
    float brightness = max(normal_dot_to_light, ambient_lighting);
    
    vec3 diffuse = brightness * light_color;
    
    // Specular lighting
    vec3 unit_to_camera = normalize(to_camera_vector);
    vec3 light_direction = -unit_to_light;
    vec3 reflected_light_direction = reflect(light_direction, unit_normal);
    
    float specular_factor = dot(reflected_light_direction, unit_to_camera);
    specular_factor = max(specular_factor, 0.0);
    
    float damped_factor = pow(specular_factor, shine_damper);
    
    vec3 specular_lighting = damped_factor * reflectivity * light_color;
    
    out_color = vec4(diffuse, 1.0) *
            texture(texture_sampler, pass_texture_coords) +
            vec4(specular_lighting, 1.0);
    out_color = mix(out_color, vec4(sky_color, 1.0), 1-visibility);
}
