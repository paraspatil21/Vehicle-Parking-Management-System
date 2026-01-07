package com.vpms;

import com.vpms.view.LoginFrame;
import javax.swing.SwingUtilities;

/**
 * Main entry point for the Vehicle Parking Management System.
 * Suitable for B.Sc. student project level.
 */
public class Main {
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
