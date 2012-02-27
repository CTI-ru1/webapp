package eu.uberdust.util;

/**
 * POJO class for holding parameters for JSON representation of a capability.
 */
public class CapabilityJson {

    private String capabilityName;

    /**
     * Constructor.
     */
    public CapabilityJson() {
        // empty constructor.
    }

    /**
     * Constructor.
     * @param capabilityName capability name.
     */
    public CapabilityJson(final String capabilityName) {
        this.capabilityName = capabilityName;
    }

    /**
     * Returns capability name.
     * @return capability name.
     */
    public final String getCapabilityName() {
        return capabilityName;
    }

    /**
     * Sets capability name
     * @param capabilityName capability name.
     */
    public final void setCapabilityName(final String capabilityName) {
        this.capabilityName = capabilityName;
    }
}
