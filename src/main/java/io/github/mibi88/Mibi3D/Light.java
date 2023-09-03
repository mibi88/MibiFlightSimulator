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

/**
 *
 * @author mibi88
 */
public class Light {
    public float x, y, z;
    public float r, g, b;

    /**
     * Create a new light
     * 
     * @param x
     * @param y
     * @param z
     * @param r The red component of the color of the light, a float between 0
     * and 1
     * @param g The green component of the color of the light, a float between 0
     * and 1
     * @param b The blue component of the color of the light, a float between 0
     * and 1
     */
    public Light(float x, float y, float z, float r, float g, float b) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
