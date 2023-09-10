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
#define MAX_LIGHTS 16

in vec2 pass_texture_coords;

in vec3 normal_vector;
in vec3 to_light_vector[MAX_LIGHTS];
in vec3 to_camera_vector;

in float visibility;

out vec4 out_color;

uniform sampler2D texture_sampler;
uniform vec3 light_color[MAX_LIGHTS];

uniform vec3 attenuation[MAX_LIGHTS];

uniform float shine_damper;
uniform float reflectivity;
uniform float ambient_lighting;

uniform vec3 sky_color;

uniform int fog;

void main(void) {
    // Diffuse lighting
    vec3 unit_normal = normalize(normal_vector);
    vec3 unit_to_camera = normalize(to_camera_vector);
    
    vec3 total_diffuse_lighting = vec3(0.0);
    vec3 total_specular_lighting = vec3(0.0);
    
    for(int i=0;i<MAX_LIGHTS;i++) {
        float distance_to_light = length(to_light_vector[i]);
        
        float attenuation_factor = attenuation[i].x +
                attenuation[i].y * distance_to_light +
                attenuation[i].z * distance_to_light * distance_to_light;
        
        vec3 unit_to_light = normalize(to_light_vector[i]);

        float normal_dot_to_light = dot(unit_normal, unit_to_light);

        float brightness = max(normal_dot_to_light, 0.0);

        total_diffuse_lighting += (brightness * light_color[i]) /
                attenuation_factor;

        // Specular lighting
        vec3 light_direction = -unit_to_light;
        vec3 reflected_light_direction = reflect(light_direction, unit_normal);

        float specular_factor = dot(reflected_light_direction, unit_to_camera);
        specular_factor = max(specular_factor, 0.0);

        float damped_factor = pow(specular_factor, shine_damper);

        total_specular_lighting += (damped_factor * reflectivity *
                light_color[i]) / attenuation_factor;
    }
    total_diffuse_lighting = max(total_diffuse_lighting, ambient_lighting);
    
    out_color = vec4(total_diffuse_lighting, 1.0) *
            texture(texture_sampler, pass_texture_coords) +
            vec4(total_specular_lighting, 1.0);
    if(fog == 1) {
        out_color = mix(out_color, vec4(sky_color, 1.0), 1-visibility);
    }
}
