package eu.uberdust.command;

/**
 * POJO class for holding parameters for testbed related command.
 */
public class TestbedInsertCommand {

    private String name;
    private String federated;
    private String zone;
    private String description;
    private String url;
    private float x;
    private float y;
    private float z;
    private float phi;
    private float theta;
    private String coordinate;
    private String prefix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFederated() {
        return federated;
    }

    public void setFederated(String federated) {
        this.federated = federated;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getPhi() {
        return phi;
    }

    public void setPhi(float phi) {
        this.phi = phi;
    }

    public float getTheta() {
        return theta;
    }

    public void setTheta(float theta) {
        this.theta = theta;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        return "TestbedInsertCommand{" +
                "name='" + name + '\'' +
                ", federated='" + federated + '\'' +
                ", zone='" + zone + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", phi=" + phi +
                ", theta=" + theta +
                ", coordinate='" + coordinate + '\'' +
                '}';
    }
}

