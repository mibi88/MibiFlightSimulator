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

#define RADIUS 2
#define KERNEL_W int(RADIUS*2+1)
#define KERNEL_H KERNEL_W
#define OFFSET 1/200
#define KERNEL_SUM 0.9991245656040866
#define MIN_COLOR 0.7

in vec2 pass_texture_coords;

out vec4 out_color;

uniform sampler2D texture_sampler;

void main(void) {
    float kernel[KERNEL_W*KERNEL_H] = float[](
        1, 2, 4, 2, 1,
        2, 4, 6, 4, 2,
        4, 6, 8, 6, 4,
        2, 4, 6, 4, 2,
        1, 2, 4, 2, 1
    );
    
    vec3 color = vec3(0.0, 0.0, 0.0);
    for(int y=0;y<KERNEL_H;y++){
        for(int x=0;x<KERNEL_W;x++){
            float ix = float(x-(KERNEL_W/2))*OFFSET;
            float iy = float(y-(KERNEL_H/2))*OFFSET;
            color += max(vec3(texture(texture_sampler, pass_texture_coords+vec2(ix, iy)))-MIN_COLOR, 0.0)*kernel[y*KERNEL_W+x];
        }
    }
    color /= KERNEL_SUM;
    vec3 base_color = vec3(texture(texture_sampler, pass_texture_coords));
    out_color = vec4(color+base_color, 1.0);
}
