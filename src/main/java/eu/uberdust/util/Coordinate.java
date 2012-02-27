/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *                             Research Academic Computer Technology Institute (RACTI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.uberdust.util;

import java.io.Serializable;

/**
 * Holds all information about a coordinate for a Node. Can also be used for GPS positioning.
 *
 * @author Malte Legenhausen
 */
public final class Coordinate implements Serializable {
    /**
     * Serial Version Unique ID.
     */
    private static final long serialVersionUID = 4172459795364749105L;

    /**
     * X cartesian axis.
     */
    private Double xPos;

    /**
     * Y cartesian axis.
     */
    private Double yPos;

    /**
     * Z cartesian axis.
     */
    private Double zPos;

    /**
     * Phi angle.
     */
    private Double phi;

    /**
     * Theta angle.
     */
    private Double theta;

    /**
     * Constructor.
     */
    public Coordinate() {
        // empty constructor
    }

    /**
     * Constructor.
     */
    public Coordinate(final Double xPos, final Double yPos, final Double zPos, final Double phi, final Double theta) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.phi = phi;
        this.theta = theta;
    }

    /**
     * Constructor.
     */
    public Coordinate(final Double xPos, final Double yPos, final Double zPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.phi = 0.0;
        this.theta = 0.0;
    }

    /**
     * Returns x.
     *
     * @return x
     */
    public Double getX() {
        return xPos;
    }

    /**
     * Sets xPos.
     *
     * @param xPos xPos
     */
    public void setX(final Double xPos) {
        this.xPos = xPos;
    }

    /**
     * Returns y.
     *
     * @return y.
     */
    public Double getY() {
        return yPos;
    }

    /**
     * Sets yPos.
     *
     * @param yPos yPos.
     */
    public void setY(final Double yPos) {
        this.yPos = yPos;
    }

    /**
     * Returns z.
     *
     * @return z.
     */
    public Double getZ() {
        return zPos;
    }

    /**
     * Sets zPos.
     *
     * @param zPos zPos.
     */
    public void setZ(final Double zPos) {
        this.zPos = zPos;
    }

    /**
     * Returns phi.
     *
     * @return phi.
     */
    public Double getPhi() {
        return phi;
    }

    /**
     * Sets phi.
     *
     * @param phi phi.
     */
    public void setPhi(final Double phi) {
        this.phi = phi;
    }

    /**
     * Returns theta.
     *
     * @return theta.
     */
    public Double getTheta() {
        return theta;
    }

    /**
     * Sets theta.
     *
     * @param theta theta.
     */
    public void setTheta(final Double theta) {
        this.theta = theta;
    }

    /**
     * Returns string representation of coordinate.
     *
     * @return string representation of coordinate.
     */
    public String toString() {
        return "x=" + xPos + ", y=" + yPos + ", z=" + zPos + ((phi == null) ? ("") : (", phi=" + phi))
                + ((theta == null) ? ("") : (", theta=" + theta));
    }

    /**
     * WGS84 constant.
     */
    private static final double WGS84_CONST = 298.257222101;

    /**
     * WGS84_ALPHA constant.
     */
    private static final double WGS84_ALPHA = 1.0 / WGS84_CONST;

    /**
     * WGS84_A constant.
     */
    private static final double WGS84_A = 6378137.0;

    /**
     * WGS84_B constant.
     */
    private static final double WGS84_B = WGS84_A * (1.0 - WGS84_ALPHA);

    /**
     * WGS84_C constant.
     */
    private static final double WGS84_C = WGS84_A * WGS84_A / WGS84_B;

    /**
     * Rotate coordinates using phi angle.
     *
     * @param coordinate , coordinate instance.
     * @param phi        , phi angle.
     * @return rotated coordinates
     */
    public static Coordinate rotate(final Coordinate coordinate, final Double phi) {
        final Double rad = Math.toRadians(phi);
        final Double cos = Math.cos(rad);
        final Double sin = Math.sin(rad);
        final Double xPos = coordinate.getX() * cos - coordinate.getY() * sin;
        final Double yPos = coordinate.getY() * cos + coordinate.getX() * sin;
        return new Coordinate(xPos, yPos, coordinate.getZ(), coordinate.getPhi(), coordinate.getTheta());
    }

    /**
     * Absolute coordinates based on an origin coordinate.
     *
     * @param origin     , origin coordinate.
     * @param coordinate , relative coordinate.
     * @return absolute cooordinates.
     */
    public static Coordinate absolute(final Coordinate origin, final Coordinate coordinate) {
        final Double yPos = coordinate.getY() + origin.getY();
        final Double xPos = coordinate.getX() + origin.getX();
        return new Coordinate(xPos, yPos, origin.getZ(), origin.getPhi(), origin.getTheta());
    }

    /**
     * Transforms a coordinate from a xyz coordinate system in an equivalent geographical coordinate
     * with latitude, longitude and height information.
     * The units that is used for the xyz system are meters.
     *
     * @param coordinate The xyz-coordinate that has to be converted.
     * @return The geographic coordinate.
     */
    public static Coordinate xyz2blh(final Coordinate coordinate) {
        final double xPos = coordinate.getX();
        final double yPos = coordinate.getY();
        final double zPos = coordinate.getZ();

        final double roh = 180.0 / Math.PI;

        final double e0Param = (WGS84_A * WGS84_A) - (WGS84_B * WGS84_B);
        final double e1Param = Math.sqrt(e0Param / (WGS84_A * WGS84_A));
        final double e2Param = Math.sqrt(e0Param / (WGS84_B * WGS84_B));

        final double sqrt = Math.sqrt((xPos * xPos) + (yPos * yPos));

        final double theta = Math.atan((zPos * WGS84_A) / (sqrt * WGS84_B));

        final double lParam = Math.atan(yPos / xPos) * roh;
        final double bParam = Math.atan((zPos + (e2Param * e2Param * WGS84_B * Math.pow(Math.sin(theta), 3)))
                / (sqrt - (e1Param * e1Param * WGS84_A * Math.pow(Math.cos(theta), 3))));

        final double eta2 = e2Param * e2Param * Math.pow(Math.cos(bParam), 2);
        final double sqrt1 = Math.sqrt(1.0 + eta2);
        final double nParam = WGS84_C / sqrt1;

        final double hParam = (sqrt / Math.cos(bParam)) - nParam;
        return new Coordinate(bParam * roh, lParam, hParam, coordinate.getPhi(), coordinate.getTheta());
    }

    /**
     * Transforms a coordinate from the geographic coordinate system to an equivalent xyz coordinate system.
     * The units that is used for the xyz system are meters.
     *
     * @param coordinate The geographic coordinate that has to be converted.
     * @return The converted xyz coordinate.
     */
    public static Coordinate blh2xyz(final Coordinate coordinate) {
        final double roh = Math.PI / 180.0;

        final double sqrt = Math.sqrt(((WGS84_A * WGS84_A) - (WGS84_B * WGS84_B)) / (WGS84_B * WGS84_B));

        final double bParam = coordinate.getX() * roh;
        final double lParam = coordinate.getY() * roh;

        final double eta2 = sqrt * sqrt * Math.pow(Math.cos(bParam), 2);
        final double sqrt1 = Math.sqrt(1.0 + eta2);
        final double nParam = WGS84_C / sqrt1;

        final double hParam = coordinate.getZ();
        final double xParam = (nParam + hParam) * Math.cos(bParam) * Math.cos(lParam);
        final double yParam = (nParam + hParam) * Math.cos(bParam) * Math.sin(lParam);
        final double zParam = (Math.pow(WGS84_B / WGS84_A, 2) * nParam + hParam) * Math.sin(bParam);
        return new Coordinate(xParam, yParam, zParam, coordinate.getPhi(), coordinate.getTheta());
    }
}
