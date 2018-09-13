package com.innoveworkshop.monicom;

import gnu.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A serial communication handler to easily interface the UI code with the
 * underlying communication code.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
public class CommsHandler {
    private String port;
    private int baud;
    private int parity;
    private int data_bits;
    private int stop_bits;
    private boolean connected;

    /**
     * Creates a new CommsHandler object with the default configuration.
     * 
     * Defaults:
     *   - Baud Rate: 9600
     *   - Parity: None
     *   - Data Bits: 8
     *   - Stop Bits: 1
     */
    public CommsHandler() {
        this.port = null;
        this.baud = 9600;
        this.parity = SerialPort.PARITY_NONE;
        this.data_bits = SerialPort.DATABITS_8;
        this.stop_bits = SerialPort.STOPBITS_1;
        this.connected = false;
    }
    
    /**
     * Creates a configuration string like "COM1 9600 8N1" for display.
     * 
     * @return Serial port configuration string.
     */
    public String getConfigurationString() {
        String str = "";
        // TODO: Create a configuration string like "COM1 9600 8N1" for the window title.
        return str;
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
     * Sets the serial baud rate.
     * 
     * @param baud_rate Baud rate.
     * @return True if the baud rate value is valid.
     */
    public boolean setBaudRate(int baud_rate) {
        if (baud_rate > 0) {
            this.baud = baud_rate;
            return true;
        }
        
        return false;
    }
    
    /**
     * Gets the current baud rate.
     * 
     * @return Current baud rate.
     */
    public int getBaudRate() {
        return this.baud;
    }
    
    /**
     * Sets the parity bit configuration.
     * 
     * @param parity Parity type as a single character: (N, O, E, M, S).
     * @return True if it is a valid type of parity.
     */
    public boolean setParity(char parity) {
        switch (parity) {
            case 'N':
                this.parity = SerialPort.PARITY_NONE;
                return true;
            case 'O':
                this.parity = SerialPort.PARITY_ODD;
                return true;
            case 'E':
                this.parity = SerialPort.PARITY_EVEN;
                return true;
            case 'M':
                this.parity = SerialPort.PARITY_MARK;
                return true;
            case 'S':
                this.parity = SerialPort.PARITY_SPACE;
                return true;
        }
        
        return false;
    }
    
    /**
     * Gets the parity bit configuration.
     * 
     * @return Parity type as a single character: (N, O, E, M, S). '-' in case of error.
     */
    public char getParity() {
        switch (this.parity) {
            case SerialPort.PARITY_NONE:
                return 'N';
            case SerialPort.PARITY_ODD:
                return 'O';
            case SerialPort.PARITY_EVEN:
                return 'E';
            case SerialPort.PARITY_MARK:
                return 'M';
            case SerialPort.PARITY_SPACE:
                return 'S';
        }
        
        return '-';
    }
    
    /**
     * Sets the amount of data bits in the serial transmission.
     * 
     * @param bits Amount of data bits.
     * @return True if it is a valid number of bits.
     */
    public boolean setDataBits(int bits) {
        switch (bits) {
            case 5:
                this.data_bits = SerialPort.DATABITS_5;
                return true;
            case 6:
                this.data_bits = SerialPort.DATABITS_6;
                return true;
            case 7:
                this.data_bits = SerialPort.DATABITS_7;
                return true;
            case 8:
                this.data_bits = SerialPort.DATABITS_8;
                return true;
        }
        
        return false;
    }
    
    /**
     * Gets the amount of data bits.
     * 
     * @return Number of data bits. 0 if error.
     */
    public int getDataBits() {
        switch (this.data_bits) {
            case SerialPort.DATABITS_5:
                return 5;
            case SerialPort.DATABITS_6:
                return 6;
            case SerialPort.DATABITS_7:
                return 7;
            case SerialPort.DATABITS_8:
                return 8;
        }
        
        return 0;
    }
    
    /**
     * Sets the number of stop bits in the serial communication.
     * 
     * @param bits Number of stop bits.
     * @return True if the number of stop bits is valid.
     */
    public boolean setStopBits(float bits) {
        int ibits = (int)bits;
        if (bits == 1.5) {
            ibits = 15;
        }
        
        switch (ibits) {
            case 1:
                this.stop_bits = SerialPort.STOPBITS_1;
                return true;
            case 15:
                this.stop_bits = SerialPort.STOPBITS_1_5;
                return true;
            case 2:
                this.stop_bits = SerialPort.STOPBITS_2;
                return true;
        }
        
        return false;
    }
    
    /**
     * Gets the number of stop bits.
     * 
     * @return Number of stop bits. 0 if error.
     */
    public float getStopBits() {
        switch (this.stop_bits) {
            case SerialPort.STOPBITS_1:
                return 1.0f;
            case SerialPort.STOPBITS_1_5:
                return 1.5f;
            case SerialPort.STOPBITS_2:
                return 2.0f;
        }
        
        return 0.0f;
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
