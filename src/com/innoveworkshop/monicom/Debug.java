package com.innoveworkshop.monicom;

/**
 * A simple debug helper class.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
public class Debug {
    /**
     * Simply prints a debug message if the Constants.DEBUG flag is set.
     * 
     * @param tag Debug event tag.
     * @param message Debug event message.
     */
    public static void println(String tag, String message) {
        if (Constants.DEBUG) {
            System.out.println("[" + tag + "] " + message);
        }
    }
}
