package com.innoveworkshop.monicom;

import gnu.io.*;
import java.util.ArrayList;
import java.util.List;
import sun.security.ssl.Debug;

/**
 * A serial communication handler to easily interface the UI code with the
 * underlying communication code.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
public class CommsHandler {
    private String port;
    private boolean connected;

    /**
     * Creates a new CommsHandler object.
     */
    public CommsHandler() {
        this.port = null;
        this.connected = false;
    }
    
    /**
     * Lists the ports in the system.
     * 
     * @return List of ports.
     */
    public List<String> getPorts() {
        return getPortsList(CommPortIdentifier.PORT_SERIAL);
    }
    
    /**
     * Checks if the serial connection is open.
     * 
     * @return Connection status.
     */
    public boolean isConnected() {
        return this.connected;
    }
    
    /**
     * Sets the serial port to be used if you're not connected.
     * 
     * @param port Serial port.
     * @return True if the port was able to be set.
     */
    public boolean setPort(String port) {
        if (!this.connected) {
            this.port = port;
            return true;
        }
        
        return false;
    }
    
    /**
     * Gets the current serial port.
     * 
     * @return Current serial port.
     */
    public String getPort() {
        return this.port;
    }
    
    /**
     * Lists the ports in the system for a given type.
     * 
     * @param type CommPortIdentifier port type.
     * @return List of ports.
     */
    private static List<String> getPortsList(int type) {
        List<String> ports = new ArrayList();
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        
        // Loop through the communication ports.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            Debug.println(getPortTypeName(portIdentifier.getPortType()), portIdentifier.getName());
            
            // Append the port to the list if it is of the correct type.
            if (portIdentifier.getPortType() == type) {
                ports.add(portIdentifier.getName());
            }
        }
        
        return ports;
    }
    
    /**
     * Gets a port type string for debug purposes.
     * 
     * @param type CommPortIdentifier port type.
     * @return Port type string.
     */
    private static String getPortTypeName(int type) {
        switch (type) {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "Unknown";
        }
    }
}
