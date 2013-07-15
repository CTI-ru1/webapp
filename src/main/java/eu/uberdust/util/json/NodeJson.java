package eu.uberdust.util.json;

/**
 * POJO class for holding parameters for JSON representation of a node.
 */
public final class NodeJson {

    /**
     * Node's id.
     */
    private transient String nodeId;

    /**
     * Constructor.
     */
    public NodeJson() {
        // empty constructor
    }

    /**
     * Constructor.
     *
     * @param nodeId node's id.
     */
    public NodeJson(final String nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * Returns node's id.
     *
     * @return node's id.
     */
    public String getId() {
        return nodeId;
    }

    /**
     * Sets node's nodeId.
     *
     * @param nodeId node's nodeId
     */
    public void setId(final String nodeId) {
        this.nodeId = nodeId;
    }
}
