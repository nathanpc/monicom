package com.innoveworkshop.monicom;

import gnu.io.*;
import java.awt.Toolkit;
import java.io.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * The beautiful main window of the monicom application.
 * 
 * @author Nathan Campos <nathan@innoveworkshop.com>
 */
public class MainWindow extends JFrame {
    private CommsHandler serial;
    private SerialReader comm_reader;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        // Initialize the serial communications object.
        this.serial = new CommsHandler();
        this.comm_reader = new SerialReader(this);
        
        // Build the UI and populate some menus.
        initComponents();
        enableInput(false);
        populateSerialPortsMenu();
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
        
        // Close any serial connections when the window is closed.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                mnuQuitActionPerformed(null);
            }
        });
        
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
        
        initActionComboItems(grpStopBits, new Runnable () {
            @Override
            public void run() {
                float bits = Float.valueOf(getSelectedMenuComboText(grpStopBits));
                
                if (serial.setStopBits(bits)) {
                    Debug.println("STOPBITS_SELECTED", String.valueOf(bits));
                }
            }
        });
    }
    
    /**
     * Sends text from the input field to the serial port.
     */
    private void sendText() {
        String str = txtInput.getText();
        // TODO: Build a history for recalling later.
        
        // Append the newline.
        if (chkCRLF.isSelected()) {
            str += "\r\n";
        } else {
            str += "\n";
        }
        
        // Ecco The Dolphin mode.
        if (chkEcho.isSelected()) {
            txtMonitor.append(str);
        }
        
        // Send and clear the input field.
        serial.sendString(str);
        txtInput.setText("");
    }
    
    /**
     * Populates the serial ports menu.
     */
    private void populateSerialPortsMenu() {
        // Grab the selected port, get new list of serial ports and clear the menu.
        String sel_port = getSelectedMenuComboText(grpPorts);
        List<String> ports = this.serial.getPorts();
        mnuPort.removeAll();
        
        // Populate the menu with the available ports.
        for (String port : ports) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(port, false);
            grpPorts.add(item);
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    if (serial.setPort(port)) {
                        Debug.println("PORT_SELECTED", port);
                    } else {
                        showErrorDialog("PORT_SELECTED_ERROR", "Serial Port not Available",
                                "Serial port '" + port + "' not available.");
                    }
                }
            });
            
            // Check if this was the previously selected port.
            if (port.equals(sel_port)) {
                item.setSelected(true);
                serial.setPort(port);
            }
            
            mnuPort.add(item);
        }
        
        // Add a separator if there are any ports available.
        if (ports.size() > 0) {
            mnuPort.addSeparator();
        }
        
        // Custom port option.
        JRadioButtonMenuItem item = new JRadioButtonMenuItem("Custom", false);
        grpPorts.add(item);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                String cport = JOptionPane.showInputDialog(null, "Serial Port",
                        "Custom Serial Port", JOptionPane.QUESTION_MESSAGE);
                
                if (cport != null) {
                    if (serial.setPort(cport)) {
                        Debug.println("PORT_SELECTED", "Custom: " + cport);
                    } else {
                        showErrorDialog("PORT_SELECTED_ERROR", "Serial Port not Available",
                                "Serial port '" +
                                cport + "' not available.\n\nCheck if the RxTx " + 
                                "binaries are in your Java library path.\nIf You're " +
                                "using Linux try this: https://stackoverflow.com/a/35931352/126353");
                    }
                }
            }
        });
        
        // Check if this was the previously selected port.
        if ("Custom".equals(sel_port)) {
            item.setSelected(true);
            item.doClick();
        }
        
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
     * Select a menu ComboBox item based in its text.
     * 
     * @param group ComboBox button group.
     * @param value 
     */
    private void selectMenuComboItem(ButtonGroup group, String value) {
        // Loop through each element in the button group.
        for (Enumeration<AbstractButton> items = group.getElements(); items.hasMoreElements();) {
            AbstractButton item = items.nextElement();
            
            // Check if this was the previously selected item.
            if (value.equals(item.getText())) {
                item.setSelected(true);
                item.doClick();
            }
        }
    }
    
    /**
     * Enables the user input field and send button.
     * 
     * @param enable Enable everything.
     */
    private void enableInput(boolean enable) {
        txtInput.setEnabled(enable);
        btSend.setEnabled(enable);
    }
    
    /**
     * Shows an error dialog and write a debug message.
     * 
     * @param tag Debug tag.
     * @param title Error dialog title.
     * @param message Error message.
     */
    private void showErrorDialog(String tag, String title, String message) {
        Debug.println(tag, message);
        JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.ERROR_MESSAGE);
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
     * Handles incoming data from the serial port.
     */
    public static class SerialReader implements SerialPortEventListener {
        private MainWindow mw;
        private InputStream in;

        public SerialReader(MainWindow mw) {
            this.mw = mw;
        }
        
        public void setInputStream(InputStream in) {
            this.in = in;
        }

        @Override
        public void serialEvent(SerialPortEvent evt) {
            try {
                int data;
                
                while ((data = in.read()) > -1) {
                    mw.txtMonitor.append(String.valueOf((char)data));
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
                System.exit(-1);
            }
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
        grpPorts = new javax.swing.ButtonGroup();
        dlgFile = new javax.swing.JFileChooser();
        pnlMain = new javax.swing.JPanel();
        sclMonitor = new javax.swing.JScrollPane();
        txtMonitor = new javax.swing.JTextArea();
        txtInput = new javax.swing.JTextField();
        btSend = new javax.swing.JButton();
        chkCRLF = new javax.swing.JCheckBox();
        chkEcho = new javax.swing.JCheckBox();
        mnuMain = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mnuConnect = new javax.swing.JMenuItem();
        mnuDisconnect = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
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
        mnuAbout = new javax.swing.JMenuItem();

        dlgFile.setDialogTitle("");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("monicom");
        setMinimumSize(new java.awt.Dimension(300, 300));
        setName("frmMain"); // NOI18N

        pnlMain.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        txtMonitor.setEditable(false);
        txtMonitor.setColumns(20);
        txtMonitor.setRows(5);
        sclMonitor.setViewportView(txtMonitor);

        txtInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInputActionPerformed(evt);
            }
        });

        btSend.setText("  Send  ");
        btSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSendActionPerformed(evt);
            }
        });

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

        mnuConnect.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mnuConnect.setText("Connect");
        mnuConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConnectActionPerformed(evt);
            }
        });
        mnuFile.add(mnuConnect);

        mnuDisconnect.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mnuDisconnect.setText("Disconnect");
        mnuDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDisconnectActionPerformed(evt);
            }
        });
        mnuFile.add(mnuDisconnect);
        mnuFile.add(jSeparator6);

        mnuNewSession.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mnuNewSession.setText("New Session");
        mnuNewSession.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNewSessionActionPerformed(evt);
            }
        });
        mnuFile.add(mnuNewSession);

        mnuSaveOutput.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mnuSaveOutput.setText("Save Output");
        mnuSaveOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSaveOutputActionPerformed(evt);
            }
        });
        mnuFile.add(mnuSaveOutput);
        mnuFile.add(jSeparator4);

        mnuImportSetup.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        mnuImportSetup.setText("Import Port Setup");
        mnuImportSetup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuImportSetupActionPerformed(evt);
            }
        });
        mnuFile.add(mnuImportSetup);

        mnuExportSetup.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        mnuExportSetup.setText("Export Port Setup");
        mnuExportSetup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExportSetupActionPerformed(evt);
            }
        });
        mnuFile.add(mnuExportSetup);
        mnuFile.add(jSeparator5);

        mnuQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        mnuQuit.setText("Quit");
        mnuQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuQuitActionPerformed(evt);
            }
        });
        mnuFile.add(mnuQuit);

        mnuMain.add(mnuFile);

        mnuEdit.setText("Edit");

        mnuFind.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        mnuFind.setText("Find...");
        mnuEdit.add(mnuFind);
        mnuEdit.add(jSeparator1);

        mnuCut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        mnuCut.setText("Cut");
        mnuCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCutActionPerformed(evt);
            }
        });
        mnuEdit.add(mnuCut);

        mnuCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        mnuCopy.setText("Copy");
        mnuCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCopyActionPerformed(evt);
            }
        });
        mnuEdit.add(mnuCopy);
        mnuEdit.add(jSeparator2);

        mnuSelectAll.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        mnuSelectAll.setText("Select All");
        mnuSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSelectAllActionPerformed(evt);
            }
        });
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

        mnuAbout.setText("About");
        mnuAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAboutActionPerformed(evt);
            }
        });
        mnuHelp.add(mnuAbout);

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
        populateSerialPortsMenu();
    }//GEN-LAST:event_mnuSetupMenuSelected

    private void mnuConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConnectActionPerformed
        if (serial.open("monicom", 2000, comm_reader)) {
            enableInput(true);
        } else {
            showErrorDialog("CONNECT_ERROR", "Connection Error",
                    "Unable to open serial port " + serial.getPort());
        }
    }//GEN-LAST:event_mnuConnectActionPerformed

    private void mnuDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDisconnectActionPerformed
        serial.close();
        enableInput(false);
    }//GEN-LAST:event_mnuDisconnectActionPerformed

    private void mnuQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuQuitActionPerformed
        serial.close();
        System.exit(0);
    }//GEN-LAST:event_mnuQuitActionPerformed

    private void btSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSendActionPerformed
        sendText();
    }//GEN-LAST:event_btSendActionPerformed

    private void mnuCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCutActionPerformed
        txtMonitor.cut();
    }//GEN-LAST:event_mnuCutActionPerformed

    private void mnuCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCopyActionPerformed
        txtMonitor.copy();
    }//GEN-LAST:event_mnuCopyActionPerformed

    private void mnuSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSelectAllActionPerformed
        txtMonitor.selectAll();
    }//GEN-LAST:event_mnuSelectAllActionPerformed

    private void mnuNewSessionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNewSessionActionPerformed
        // Reset everything.
        serial.close();
        enableInput(false);
        
        this.serial = new CommsHandler();
        this.comm_reader = new SerialReader(this);
        
        txtMonitor.setText("");
        txtInput.setText("");
        
        populateSerialPortsMenu();
    }//GEN-LAST:event_mnuNewSessionActionPerformed

    private void txtInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInputActionPerformed
        sendText();
    }//GEN-LAST:event_txtInputActionPerformed

    private void mnuSaveOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSaveOutputActionPerformed
        // Setup the dialog.
        dlgFile.setDialogType(JFileChooser.SAVE_DIALOG);
        dlgFile.setDialogTitle("Save monitor output to file");
        
        // Open the dialog and get the file.
        if (dlgFile.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File output = dlgFile.getSelectedFile();
            Debug.println("SAVE_OUTPUT", output.toString());
            
            try {
                // Create the file if it doesn't exist already.
                output.createNewFile();
                
                // Write to the file.
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
                    writer.write(txtMonitor.getText());
                }
            } catch (IOException ex) {
                showErrorDialog("SAVE_ERROR", "Save Error", "Unable to write to " +
                        output.toString());
            }
        }
    }//GEN-LAST:event_mnuSaveOutputActionPerformed

    private void mnuExportSetupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExportSetupActionPerformed
        Properties setup = new Properties();
        
        // Get the state of the check boxes.
        setup.setProperty("echo", String.valueOf(chkEcho.isSelected()));
        setup.setProperty("crlf", String.valueOf(chkCRLF.isSelected()));
        
        // Get the selected port settings.
        setup.setProperty("port", getSelectedMenuComboText(grpPorts));
        setup.setProperty("baud_rate", getSelectedMenuComboText(grpBaudRate));
        setup.setProperty("parity", getSelectedMenuComboText(grpParity));
        setup.setProperty("data_bits", getSelectedMenuComboText(grpDataBits));
        setup.setProperty("stop_bits", getSelectedMenuComboText(grpStopBits));
        
        // Setup the dialog.
        dlgFile.setDialogType(JFileChooser.SAVE_DIALOG);
        dlgFile.setDialogTitle("Export Setup");
        
        // Open the dialog and get the file.
        if (dlgFile.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File output = dlgFile.getSelectedFile();
            Debug.println("SAVE_SETUP", output.toString());
            
            try {
                // Create the file if it doesn't exist already and write to the file.
                output.createNewFile();
                setup.store(new FileWriter(output), "monicom setup");
            } catch (IOException ex) {
                showErrorDialog("SAVE_ERROR", "Export Setup Error",
                        "Unable to write to " + output.toString());
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_mnuExportSetupActionPerformed

    private void mnuImportSetupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuImportSetupActionPerformed
        // Setup the dialog.
        dlgFile.setDialogType(JFileChooser.SAVE_DIALOG);
        dlgFile.setDialogTitle("Import Setup");
        
        // Open the dialog and get the file.
        if (dlgFile.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File input = dlgFile.getSelectedFile();
            Debug.println("OPEN_SETUP", input.toString());
            
            try {
                // Load the properties file.
                Properties setup = new Properties();
                setup.load(new FileInputStream(input));
                
                // Set the checkboxes.
                chkEcho.setSelected(Boolean.valueOf(setup.getProperty("echo")));
                chkCRLF.setSelected(Boolean.valueOf(setup.getProperty("crlf")));
                
                // Select the setup options.
                selectMenuComboItem(grpPorts, setup.getProperty("port"));
                selectMenuComboItem(grpBaudRate, setup.getProperty("baud_rate"));
                selectMenuComboItem(grpParity, setup.getProperty("parity"));
                selectMenuComboItem(grpDataBits, setup.getProperty("data_bits"));
                selectMenuComboItem(grpStopBits, setup.getProperty("stop_bits"));
            } catch (IOException ex) {
                showErrorDialog("OPEN_ERROR", "Import Setup Error",
                        "Unable to parse the selected file. Are you sure this is the right one?");
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_mnuImportSetupActionPerformed

    private void mnuAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAboutActionPerformed
        AboutWindow about = new AboutWindow();
        about.setVisible(true);
    }//GEN-LAST:event_mnuAboutActionPerformed

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
    private javax.swing.JFileChooser dlgFile;
    private javax.swing.ButtonGroup grpBaudRate;
    private javax.swing.ButtonGroup grpDataBits;
    private javax.swing.ButtonGroup grpParity;
    private javax.swing.ButtonGroup grpPorts;
    private javax.swing.ButtonGroup grpStopBits;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JMenuItem mnuAbout;
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
    private javax.swing.JMenuItem mnuConnect;
    private javax.swing.JMenuItem mnuCopy;
    private javax.swing.JMenuItem mnuCut;
    private javax.swing.JMenu mnuDataBits;
    private javax.swing.JRadioButtonMenuItem mnuDataBits5;
    private javax.swing.JRadioButtonMenuItem mnuDataBits6;
    private javax.swing.JRadioButtonMenuItem mnuDataBits7;
    private javax.swing.JRadioButtonMenuItem mnuDataBits8;
    private javax.swing.JMenuItem mnuDisconnect;
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
