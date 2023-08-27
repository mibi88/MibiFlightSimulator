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

out vec4 out_color;

uniform sampler2D texture_sampler;
uniform vec3 light_color;

void main(void) {
    vec3 unit_normal = normalize(normal_vector);
    vec3 unit_to_light = normalize(to_light_vector);
    
    float normal_dot_to_light = dot(unit_normal, unit_to_light);
    
    float brightness = max(normal_dot_to_light, 0.2);
    
    vec3 diffuse = brightness * light_color;
    
    out_color = vec4(diffuse, 1.0) *
            texture(texture_sampler, pass_texture_coords);
}
