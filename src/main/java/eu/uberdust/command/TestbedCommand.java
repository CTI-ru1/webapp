package eu.uberdust.command;

/**
 * POJO class for holding parameters for testbed related command.
 */
public class TestbedCommand {

    /**
     * Identity of the network.
     */
    private String testbedId = null;

    /**
     * the Format.
     */
    private String format = null;

    /**
     * Returns testbed id.
     *
     * @return testbed id
     */
    public final String getTestbedId() {
        return testbedId;
    }

    /**
     * Sets testbedID.
     *
     * @param testbedId a testbed id.
     */
    public final void setTestbedId(final String testbedId) {
        this.testbedId = testbedId;
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
}
