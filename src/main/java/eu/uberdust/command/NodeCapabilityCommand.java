package eu.uberdust.command;

/**
 * POJO class for holding parameters for a node-capability related command.
 */
public class NodeCapabilityCommand extends TestbedCommand {

    /**
     * Node ID.
     */
    private String nodeId = null;

    /**
     * the Format.
     */
    private String format = null;

    /**
     * Capability ID.
     */
    private String capabilityId = null;

    /**
     * returned Reading's limit for this node/capability.
     */
    private String readingsLimit = null;

    /**
     *
     */
    private String readingsFrom = null;

    /**
     *
     */
    private String readingsTo = null;


    /**
     * Get the ID of the Node or Link.
     *
     * @return the Node ID.
     */
    public final String getNodeId() {
        return nodeId;
    }

    /**
     * Sets the ID of the Node Or Link.
     *
     * @param nodeId the ID of the node.
     */
    public final void setNodeId(final String nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * Get the Format requested.
     *
     * @return the Format.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Set the Format requested.
     *
     * @param format the Format of request.
     */
    public void setFormat(final String format) {
        this.format = format;
    }


    /**
     * Returns the Id of capability.
     *
     * @return the Id of capability.
     */
    public final String getCapabilityId() {
        return capabilityId;
    }

    /**
     * Set the Id of capability.
     *
     * @param capabilityId the ID of capability.
     */
    public final void setCapabilityId(final String capabilityId) {
        this.capabilityId = capabilityId;
    }

    /**
     * Returns the readingsLimit of readings.
     *
     * @return the readingsLimit of readings.
     */
    public final String getReadingsLimit() {
        return readingsLimit;
    }

    /**
     * Sets the readingsLimit.
     *
     * @param readingsLimit the readingsLimit of readings
     */
    public final void setReadingsLimit(final String readingsLimit) {
        this.readingsLimit = readingsLimit;
    }

    public String getReadingsFrom() {
        return readingsFrom;
    }

    public void setReadingsFrom(String readingsFrom) {
        this.readingsFrom = readingsFrom;
    }

    public String getReadingsTo() {
        return readingsTo;
    }

    public void setReadingsTo(String readingsTo) {
        this.readingsTo = readingsTo;
    }
}
