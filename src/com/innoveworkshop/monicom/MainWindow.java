package com.innoveworkshop.monicom;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * The beautiful main window of the monicom application.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
public class MainWindow extends javax.swing.JFrame {
    private CommsHandler serial;

    /**
     * Creates new form MainWindow
     */
    public MainWindow(){
        // Build the UI.
        initComponents();
        
        // Initialize the serial communications object.
        this.serial = new CommsHandler();
        
        // Initialize the event handler for the combo items in the setup menu.
        initActionComboItems(grpBaudRate, new Runnable() {
            @Override
            public void run() {
                int baud = Integer.parseInt(getSelectedMenuComboText(grpBaudRate));
                
                if (serial.setBaudRate(baud)) {
                    Debug.println("BAUD_SELECTED", Integer.toString(baud));
                }
            }
        });
        
        initActionComboItems(grpParity, new Runnable() {
            @Override
            public void run() {
                char parity = getSelectedMenuComboText(grpParity).charAt(0);
                
                if (serial.setParity(parity)) {
                    Debug.println("PARITY_SELECTED", String.valueOf(parity));
                }
            }
        });
        
        initActionComboItems(grpDataBits, new Runnable() {
            @Override
            public void run() {
                int bits = Integer.parseInt(getSelectedMenuComboText(grpDataBits));
                
                if (serial.setDataBits(bits)) {
                    Debug.println("DATABITS_SELECTED", Integer.toString(bits));
                }
            }
        });
        
        initActionComboItems(grpStopBits, new Runnable() {
            @Override
            public void run() {
                float bits = Float.valueOf(getSelectedMenuComboText(grpStopBits));
                
                if (serial.setStopBits(bits)) {
                    Debug.println("STOPBITS_SELECTED", String.valueOf(bits));
                }
            }
        });
        
        // Populate some menus.
        populateSerialPortsMenu();
    }
    
    /**
     * Populates the serial ports menu.
     */
    private void populateSerialPortsMenu() {
        List<String> ports = this.serial.getPorts();
        mnuPort.removeAll();
        
        // Populate the menu with the available ports.
        for (String port : ports) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(port, false);
            // TODO: Add to port menu button group.
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    if (serial.setPort(port)) {
                        Debug.println("PORT_SELECTED", port);
                    }
                }
            });
            
            mnuPort.add(item);
        }
        
        // Add a separator if there are any ports available.
        if (ports.size() > 0) {
            mnuPort.addSeparator();
        }
        
        // Custom port option.
        JRadioButtonMenuItem item = new JRadioButtonMenuItem("Custom", false);
        // TODO: Add to port menu button group.
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String cport = JOptionPane.showInputDialog(null, "Serial Port",
                        "Custom Serial Port", JOptionPane.QUESTION_MESSAGE);
                
                if (cport != null) {
                    if (serial.setPort(cport)) {
                        Debug.println("PORT_SELECTED", "Custom: " + cport);
                    }
                }
            }
        });
        
        mnuPort.add(item);
    }
    
    /**
     * Gets the selected menu combo item text.
     * 
     * @param group ComboBox button group.
     * @return Selected item text.
     */
    private String getSelectedMenuComboText(ButtonGroup group) {
        // Loop through each element in the button group.
        for (Enumeration<AbstractButton> items = group.getElements(); items.hasMoreElements();) {
            AbstractButton item = items.nextElement();
            
            // If it is selected then return its text.
            if (item.isSelected()) {
                return item.getText();
            }
        }
        
        return null;
    }
    
    /**
     * Sets the default action to be performed by a ComboBox item when its
     * selected status changes.
     * 
     * @param group ComboBox button group.
     * @param callback What action will be taken when the menu item is clicked.
     */
    private void initActionComboItems(ButtonGroup group, Runnable callback) {
        // Loop through each element in the button group.
        for (Enumeration<AbstractButton> items = group.getElements(); items.hasMoreElements();) {
            AbstractButton item = items.nextElement();

            // Add a action listener to each one.
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    callback.run();
                }
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grpParity = new javax.swing.ButtonGroup();
        grpStopBits = new javax.swing.ButtonGroup();
        grpDataBits = new javax.swing.ButtonGroup();
        grpBaudRate = new javax.swing.ButtonGroup();
        pnlMain = new javax.swing.JPanel();
        sclMonitor = new javax.swing.JScrollPane();
        txtMonitor = new javax.swing.JTextArea();
        txtInput = new javax.swing.JTextField();
        btSend = new javax.swing.JButton();
        chkCRLF = new javax.swing.JCheckBox();
        chkEcho = new javax.swing.JCheckBox();
        mnuMain = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mnuNewSession = new javax.swing.JMenuItem();
        mnuSaveOutput = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        mnuImportSetup = new javax.swing.JMenuItem();
        mnuExportSetup = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        mnuQuit = new javax.swing.JMenuItem();
        mnuEdit = new javax.swing.JMenu();
        mnuFind = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mnuCut = new javax.swing.JMenuItem();
        mnuCopy = new javax.swing.JMenuItem();
        mnuPaste = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        mnuSelectAll = new javax.swing.JMenuItem();
        mnuSetup = new javax.swing.JMenu();
        mnuPort = new javax.swing.JMenu();
        mnuBaudRate = new javax.swing.JMenu();
        mnuBaudRate75 = new javax.swing.JRadioButtonMenuItem();
        mnuBaudRate110 = new javax.swing.JRadioButtonMenuItem();
        mnuBaudRate300 = new javax.swing.JRadioButtonMenuItem();
        mnuBaudRate1200 = new javax.swing.JRadioButtonMenuItem();
        mnuBaudRate2400 = new javax.swing.JRadioButtonMenuItem();
        mnuBaudRate4800 = new javax.swing.JRadioButtonMenuItem();
        mnuBaudRate9600 = new javax.swing.JRadioButtonMenuItem();
        mnuBaudRate19200 = new javax.swing.JRadioButtonMenuItem();
        mnuBaudRate38400 = new javax.swing.JRadioButtonMenuItem();
        mnuBaudRate57600 = new javax.swing.JRadioButtonMenuItem();
        mnuBaudRate115200 = new javax.swing.JRadioButtonMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        mnuBaudRateCustom = new javax.swing.JRadioButtonMenuItem();
        mnuParity = new javax.swing.JMenu();
        mnuParityNone = new javax.swing.JRadioButtonMenuItem();
        mnuParityOdd = new javax.swing.JRadioButtonMenuItem();
        mnuParityEven = new javax.swing.JRadioButtonMenuItem();
        mnuParityMark = new javax.swing.JRadioButtonMenuItem();
        mnuParitySpace = new javax.swing.JRadioButtonMenuItem();
        mnuDataBits = new javax.swing.JMenu();
        mnuDataBits5 = new javax.swing.JRadioButtonMenuItem();
        mnuDataBits6 = new javax.swing.JRadioButtonMenuItem();
        mnuDataBits7 = new javax.swing.JRadioButtonMenuItem();
        mnuDataBits8 = new javax.swing.JRadioButtonMenuItem();
        mnuStopBits = new javax.swing.JMenu();
        mnuStopBits1 = new javax.swing.JRadioButtonMenuItem();
        mnuStopBits1_5 = new javax.swing.JRadioButtonMenuItem();
        mnuStopBits2 = new javax.swing.JRadioButtonMenuItem();
        mnuHelp = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("monicom");
        setMinimumSize(new java.awt.Dimension(300, 300));
        setName("frmMain"); // NOI18N

        pnlMain.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        txtMonitor.setColumns(20);
        txtMonitor.setRows(5);
        sclMonitor.setViewportView(txtMonitor);

        btSend.setText("  Send  ");

        chkCRLF.setSelected(true);
        chkCRLF.setText("CR+LF");

        chkEcho.setText("Echo");

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addComponent(txtInput)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkEcho)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkCRLF)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btSend))
            .addComponent(sclMonitor, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addComponent(sclMonitor, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInput)
                    .addComponent(btSend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkCRLF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chkEcho))))
        );

        mnuFile.setText("File");

        mnuNewSession.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mnuNewSession.setText("New Session");
        mnuFile.add(mnuNewSession);

        mnuSaveOutput.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mnuSaveOutput.setText("Save Output");
        mnuFile.add(mnuSaveOutput);
        mnuFile.add(jSeparator4);

        mnuImportSetup.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        mnuImportSetup.setText("Import Port Setup");
        mnuFile.add(mnuImportSetup);

        mnuExportSetup.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        mnuExportSetup.setText("Export Port Setup");
        mnuFile.add(mnuExportSetup);
        mnuFile.add(jSeparator5);

        mnuQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        mnuQuit.setText("Quit");
        mnuFile.add(mnuQuit);

        mnuMain.add(mnuFile);

        mnuEdit.setText("Edit");

        mnuFind.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        mnuFind.setText("Find...");
        mnuEdit.add(mnuFind);
        mnuEdit.add(jSeparator1);

        mnuCut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        mnuCut.setText("Cut");
        mnuEdit.add(mnuCut);

        mnuCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        mnuCopy.setText("Copy");
        mnuEdit.add(mnuCopy);

        mnuPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        mnuPaste.setText("Paste");
        mnuEdit.add(mnuPaste);
        mnuEdit.add(jSeparator2);

        mnuSelectAll.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        mnuSelectAll.setText("Select All");
        mnuEdit.add(mnuSelectAll);

        mnuMain.add(mnuEdit);

        mnuSetup.setText("Setup");
        mnuSetup.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                mnuSetupMenuSelected(evt);
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
        });

        mnuPort.setText("Port");
        mnuSetup.add(mnuPort);

        mnuBaudRate.setText("Baud Rate");

        grpBaudRate.add(mnuBaudRate75);
        mnuBaudRate75.setText("75");
        mnuBaudRate.add(mnuBaudRate75);

        grpBaudRate.add(mnuBaudRate110);
        mnuBaudRate110.setText("110");
        mnuBaudRate.add(mnuBaudRate110);

        grpBaudRate.add(mnuBaudRate300);
        mnuBaudRate300.setText("300");
        mnuBaudRate.add(mnuBaudRate300);

        grpBaudRate.add(mnuBaudRate1200);
        mnuBaudRate1200.setText("1200");
        mnuBaudRate.add(mnuBaudRate1200);

        grpBaudRate.add(mnuBaudRate2400);
        mnuBaudRate2400.setText("2400");
        mnuBaudRate.add(mnuBaudRate2400);

        grpBaudRate.add(mnuBaudRate4800);
        mnuBaudRate4800.setText("4800");
        mnuBaudRate.add(mnuBaudRate4800);

        grpBaudRate.add(mnuBaudRate9600);
        mnuBaudRate9600.setSelected(true);
        mnuBaudRate9600.setText("9600");
        mnuBaudRate.add(mnuBaudRate9600);

        grpBaudRate.add(mnuBaudRate19200);
        mnuBaudRate19200.setText("19200");
        mnuBaudRate.add(mnuBaudRate19200);

        grpBaudRate.add(mnuBaudRate38400);
        mnuBaudRate38400.setText("38400");
        mnuBaudRate.add(mnuBaudRate38400);

        grpBaudRate.add(mnuBaudRate57600);
        mnuBaudRate57600.setText("57600");
        mnuBaudRate.add(mnuBaudRate57600);

        grpBaudRate.add(mnuBaudRate115200);
        mnuBaudRate115200.setText("115200");
        mnuBaudRate.add(mnuBaudRate115200);
        mnuBaudRate.add(jSeparator3);

        grpBaudRate.add(mnuBaudRateCustom);
        mnuBaudRateCustom.setText("Custom");
        mnuBaudRate.add(mnuBaudRateCustom);

        mnuSetup.add(mnuBaudRate);

        mnuParity.setText("Parity");

        grpParity.add(mnuParityNone);
        mnuParityNone.setSelected(true);
        mnuParityNone.setText("None (N)");
        mnuParity.add(mnuParityNone);

        grpParity.add(mnuParityOdd);
        mnuParityOdd.setText("Odd (O)");
        mnuParity.add(mnuParityOdd);

        grpParity.add(mnuParityEven);
        mnuParityEven.setText("Even (E)");
        mnuParity.add(mnuParityEven);

        grpParity.add(mnuParityMark);
        mnuParityMark.setText("Mark (M)");
        mnuParity.add(mnuParityMark);

        grpParity.add(mnuParitySpace);
        mnuParitySpace.setText("Space (S)");
        mnuParity.add(mnuParitySpace);

        mnuSetup.add(mnuParity);

        mnuDataBits.setText("Data Bits");

        grpDataBits.add(mnuDataBits5);
        mnuDataBits5.setText("5");
        mnuDataBits.add(mnuDataBits5);

        grpDataBits.add(mnuDataBits6);
        mnuDataBits6.setText("6");
        mnuDataBits.add(mnuDataBits6);

        grpDataBits.add(mnuDataBits7);
        mnuDataBits7.setText("7");
        mnuDataBits.add(mnuDataBits7);

        grpDataBits.add(mnuDataBits8);
        mnuDataBits8.setSelected(true);
        mnuDataBits8.setText("8");
        mnuDataBits.add(mnuDataBits8);

        mnuSetup.add(mnuDataBits);

        mnuStopBits.setText("Stop Bits");

        grpStopBits.add(mnuStopBits1);
        mnuStopBits1.setSelected(true);
        mnuStopBits1.setText("1");
        mnuStopBits.add(mnuStopBits1);

        grpStopBits.add(mnuStopBits1_5);
        mnuStopBits1_5.setText("1.5");
        mnuStopBits.add(mnuStopBits1_5);

        grpStopBits.add(mnuStopBits2);
        mnuStopBits2.setText("2");
        mnuStopBits.add(mnuStopBits2);

        mnuSetup.add(mnuStopBits);

        mnuMain.add(mnuSetup);

        mnuHelp.setText("Help");
        mnuMain.add(mnuHelp);

        setJMenuBar(mnuMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuSetupMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_mnuSetupMenuSelected
        // TODO: Repopulate the ports menu, but remember the last one selected.
        Debug.println("DEBUG", "TODO Repopulate the Ports menu.");
    }//GEN-LAST:event_mnuSetupMenuSelected

    //<editor-fold defaultstate="collapsed" desc="Main Function">
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // Select the GTK+ theme on Linux or go with the system native in OS X and Windows.
        try {
            if (UIManager.getSystemLookAndFeelClassName().equals(UIManager.getCrossPlatformLookAndFeelClassName())) {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("GTK+".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } else {
                // Set the system default look and feel.
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }
    //</editor-fold>

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btSend;
    private javax.swing.JCheckBox chkCRLF;
    private javax.swing.JCheckBox chkEcho;
    private javax.swing.ButtonGroup grpBaudRate;
    private javax.swing.ButtonGroup grpDataBits;
    private javax.swing.ButtonGroup grpParity;
    private javax.swing.ButtonGroup grpStopBits;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JMenu mnuBaudRate;
    private javax.swing.JRadioButtonMenuItem mnuBaudRate110;
    private javax.swing.JRadioButtonMenuItem mnuBaudRate115200;
    private javax.swing.JRadioButtonMenuItem mnuBaudRate1200;
    private javax.swing.JRadioButtonMenuItem mnuBaudRate19200;
    private javax.swing.JRadioButtonMenuItem mnuBaudRate2400;
    private javax.swing.JRadioButtonMenuItem mnuBaudRate300;
    private javax.swing.JRadioButtonMenuItem mnuBaudRate38400;
    private javax.swing.JRadioButtonMenuItem mnuBaudRate4800;
    private javax.swing.JRadioButtonMenuItem mnuBaudRate57600;
    private javax.swing.JRadioButtonMenuItem mnuBaudRate75;
    private javax.swing.JRadioButtonMenuItem mnuBaudRate9600;
    private javax.swing.JRadioButtonMenuItem mnuBaudRateCustom;
    private javax.swing.JMenuItem mnuCopy;
    private javax.swing.JMenuItem mnuCut;
    private javax.swing.JMenu mnuDataBits;
    private javax.swing.JRadioButtonMenuItem mnuDataBits5;
    private javax.swing.JRadioButtonMenuItem mnuDataBits6;
    private javax.swing.JRadioButtonMenuItem mnuDataBits7;
    private javax.swing.JRadioButtonMenuItem mnuDataBits8;
    private javax.swing.JMenu mnuEdit;
    private javax.swing.JMenuItem mnuExportSetup;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenuItem mnuFind;
    private javax.swing.JMenu mnuHelp;
    private javax.swing.JMenuItem mnuImportSetup;
    private javax.swing.JMenuBar mnuMain;
    private javax.swing.JMenuItem mnuNewSession;
    private javax.swing.JMenu mnuParity;
    private javax.swing.JRadioButtonMenuItem mnuParityEven;
    private javax.swing.JRadioButtonMenuItem mnuParityMark;
    private javax.swing.JRadioButtonMenuItem mnuParityNone;
    private javax.swing.JRadioButtonMenuItem mnuParityOdd;
    private javax.swing.JRadioButtonMenuItem mnuParitySpace;
    private javax.swing.JMenuItem mnuPaste;
    private javax.swing.JMenu mnuPort;
    private javax.swing.JMenuItem mnuQuit;
    private javax.swing.JMenuItem mnuSaveOutput;
    private javax.swing.JMenuItem mnuSelectAll;
    private javax.swing.JMenu mnuSetup;
    private javax.swing.JMenu mnuStopBits;
    private javax.swing.JRadioButtonMenuItem mnuStopBits1;
    private javax.swing.JRadioButtonMenuItem mnuStopBits1_5;
    private javax.swing.JRadioButtonMenuItem mnuStopBits2;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JScrollPane sclMonitor;
    private javax.swing.JTextField txtInput;
    private javax.swing.JTextArea txtMonitor;
    // End of variables declaration//GEN-END:variables
}
