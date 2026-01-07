package com.vpms.view;

import com.vpms.dao.ResidentDAO;
import com.vpms.dao.SlotDAO;
import com.vpms.dao.ParkingEntryDAO;
import com.vpms.model.Resident;
import com.vpms.model.Slot;
import com.vpms.model.ParkingEntry;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private JTabbedPane tabbedPane;
    private JTable tblSlots, tblResidents, tblLogs;
    private DefaultTableModel modelSlots, modelResidents, modelLogs;
    private JTextField txtVisName, txtVehName, txtVehNum, txtContact, txtReason, txtResName,
            txtFlatFloor, txtSlotNum;
    private JSpinner spinArrTime, spinExpDep;
    private JComboBox<String> cbVehType, cbVisitType;
    private JButton btnSaveEntry;

    public AdminDashboard() {
        setTitle("Surya Industry - VPMS Admin");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Navigation Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(45, 52, 54));
        header.setPreferredSize(new Dimension(1000, 60));

        JLabel lblTitle = new JLabel("  SURYA INDUSTRY - VPMS ADMIN PANEL", JLabel.LEFT);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.add(lblTitle, BorderLayout.WEST);

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });
        header.add(btnLogout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();

        setupSlotsTab();
        setupResidentsTab();
        setupLogsTab();

        add(tabbedPane, BorderLayout.CENTER);

        refreshData();
    }

    private void setupSlotsTab() {
        JPanel pnlSlots = new JPanel(new BorderLayout());
        modelSlots = new DefaultTableModel(new String[] {
                "ID", "Slot Number", "Slot Type", "Status",
                "Vehicle Owner’s Name", "Vehicle Owner’s Vehicle Name", "Vehicle Owner’s Vehicle Number",
                "Vehicle Owner’s Vehicle Type", "Vehicle Owner’s Contact Number", "Vehicle Owner’s Arrival Time",
                "Vehicle Owner’s Expected Departure Time", "Vehicle Owner’s Visit Type",
                "Vehicle Owner’s Reason for Visit",
                "Vehicle Owner’s Resident Name", "Vehicle Owner’s Flat & Floor"
        }, 0);
        tblSlots = new JTable(modelSlots);
        tblSlots.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // Set some widths for readability
        for (int i = 0; i < tblSlots.getColumnCount(); i++) {
            tblSlots.getColumnModel().getColumn(i).setPreferredWidth(150);
        }

        // Form Panel (Drawer)
        JPanel pnlDrawer = new JPanel(new GridBagLayout());
        pnlDrawer.setBorder(BorderFactory.createTitledBorder("Quick Vehicle Entry"));
        pnlDrawer.setPreferredSize(new Dimension(380, 700));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        int rowIdx = 0;
        addFormField(pnlDrawer, "Visitor Name:", txtVisName = new JTextField(15), gbc, rowIdx++);
        addFormField(pnlDrawer, "Vehicle Name:", txtVehName = new JTextField(15), gbc, rowIdx++);
        addFormField(pnlDrawer, "Vehicle Number:", txtVehNum = new JTextField(15), gbc, rowIdx++);

        cbVehType = new JComboBox<>(new String[] { "2W", "4W", "EV", "SERVICE" });
        addFormField(pnlDrawer, "Vehicle Type:", cbVehType, gbc, rowIdx++);

        addFormField(pnlDrawer, "Contact Number:", txtContact = new JTextField(15), gbc, rowIdx++);

        // JSpinner for Time Picker (In-built Java function)
        spinArrTime = new JSpinner(new SpinnerDateModel());
        spinArrTime.setEditor(new JSpinner.DateEditor(spinArrTime, "yyyy-MM-dd HH:mm:ss"));
        addFormField(pnlDrawer, "Arrival Time:", spinArrTime, gbc, rowIdx++);

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.HOUR, 2);
        spinExpDep = new JSpinner(new SpinnerDateModel());
        spinExpDep.setEditor(new JSpinner.DateEditor(spinExpDep, "yyyy-MM-dd HH:mm:ss"));
        spinExpDep.setValue(cal.getTime());
        addFormField(pnlDrawer, "Expected Departure:", spinExpDep, gbc, rowIdx++);

        cbVisitType = new JComboBox<>(new String[] { "RESIDENT", "VISITOR", "SERVICE" });
        addFormField(pnlDrawer, "Visit Type:", cbVisitType, gbc, rowIdx++);

        addFormField(pnlDrawer, "Reason:", txtReason = new JTextField(15), gbc, rowIdx++);
        addFormField(pnlDrawer, "Resident Name:", txtResName = new JTextField(15), gbc, rowIdx++);
        addFormField(pnlDrawer, "Flat & Floor:", txtFlatFloor = new JTextField(15), gbc, rowIdx++);
        addFormField(pnlDrawer, "Slot Number:", txtSlotNum = new JTextField(15), gbc, rowIdx++);
        txtSlotNum.setEditable(false);

        btnSaveEntry = new JButton("Save Parking Entry");
        btnSaveEntry.setBackground(new Color(39, 174, 96));
        btnSaveEntry.setForeground(Color.WHITE);
        btnSaveEntry.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = rowIdx++;
        gbc.gridwidth = 2;
        pnlDrawer.add(btnSaveEntry, gbc);

        // Fill empty space
        gbc.weighty = 1.0;
        pnlDrawer.add(new JLabel(""), gbc);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tblSlots), pnlDrawer);
        split.setDividerLocation(600);
        pnlSlots.add(split, BorderLayout.CENTER);

        // Selection Listener
        tblSlots.getSelectionModel().addListSelectionListener(e -> {
            int r = tblSlots.getSelectedRow();
            if (r >= 0) {
                txtSlotNum.setText((String) modelSlots.getValueAt(r, 1));
            }
        });

        btnSaveEntry.addActionListener(e -> handleQuickEntry());

        tabbedPane.addTab("Parking Slots", pnlSlots);
    }

    private void addFormField(JPanel p, String label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        p.add(field, gbc);
    }

    private void handleQuickEntry() {
        int row = tblSlots.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a slot from the table!");
            return;
        }

        String status = (String) modelSlots.getValueAt(row, 3);
        if (!"AVAILABLE".equals(status)) {
            JOptionPane.showMessageDialog(this, "Slot is not available!");
            return;
        }

        ParkingEntry entry = new ParkingEntry();
        entry.setVisitorName(txtVisName.getText());
        entry.setVehicleName(txtVehName.getText());
        entry.setVehicleNumber(txtVehNum.getText());
        entry.setVehicleType((String) cbVehType.getSelectedItem());
        entry.setContactNumber(txtContact.getText());
        entry.setVisitType((String) cbVisitType.getSelectedItem());
        entry.setReason(txtReason.getText());
        entry.setResidentName(txtResName.getText());

        String ff = txtFlatFloor.getText();
        if (ff.contains("-")) {
            String[] parts = ff.split("-");
            entry.setFlatNumber(parts[0].trim());
            entry.setFloorNumber(parts[1].trim());
        } else {
            entry.setFlatNumber(ff);
        }

        entry.setSlotId((int) modelSlots.getValueAt(row, 0));

        entry.setArrivalTime(new java.sql.Timestamp(((java.util.Date) spinArrTime.getValue()).getTime()));
        entry.setExpectedDeparture(new java.sql.Timestamp(((java.util.Date) spinExpDep.getValue()).getTime()));

        if (new ParkingEntryDAO().saveEntry(entry)) {
            JOptionPane.showMessageDialog(this, "Parking entry saved successfully!");
            refreshData();
            clearQuickForm();
        }
    }

    private void clearQuickForm() {
        txtVisName.setText("");
        txtVehName.setText("");
        txtVehNum.setText("");
        txtContact.setText("");

        spinArrTime.setValue(new java.util.Date());
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.HOUR, 2);
        spinExpDep.setValue(cal.getTime());

        txtReason.setText("");
        txtResName.setText("");
        txtFlatFloor.setText("");
        txtSlotNum.setText("");
        tblSlots.clearSelection();
    }

    private void setupResidentsTab() {
        JPanel pnlResidents = new JPanel(new BorderLayout());
        modelResidents = new DefaultTableModel(
                new String[] { "ID", "Resident Name", "Flat", "Floor", "Contact", "Vehicle Name", "Vehicle Number",
                        "Type", "Slot" },
                0);
        tblResidents = new JTable(modelResidents);
        pnlResidents.add(new JScrollPane(tblResidents), BorderLayout.CENTER);

        JPanel pnlActions = new JPanel();
        JButton btnAdd = new JButton("Add Resident");
        JButton btnDelete = new JButton("Delete Resident");
        pnlActions.add(btnAdd);
        pnlActions.add(btnDelete);
        pnlResidents.add(pnlActions, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> handleAddResident());
        btnDelete.addActionListener(e -> handleDeleteResident());

        tabbedPane.addTab("Resident Management", pnlResidents);
    }

    private void setupLogsTab() {
        JPanel pnlLogs = new JPanel(new BorderLayout());
        modelLogs = new DefaultTableModel(new String[] {
                "Visitor Name", "Vehicle Name", "Vehicle Number", "Vehicle Type", "Contact Number",
                "Arrival Time", "Expected Departure Time", "Visit Type", "Reason", "Resident Name", "Flat & Floor",
                "Slot Number"
        }, 0);
        tblLogs = new JTable(modelLogs);
        pnlLogs.add(new JScrollPane(tblLogs), BorderLayout.CENTER);

        tabbedPane.addTab("Parking Records", pnlLogs);
    }

    private void refreshData() {
        refreshSlots();
        refreshResidents();
        refreshLogs();
    }

    private void refreshSlots() {
        modelSlots.setRowCount(0);
        List<Slot> slots = new SlotDAO().getAllSlots();
        for (Slot s : slots) {
            modelSlots.addRow(new Object[] {
                    s.getId(), s.getSlotNumber(), s.getType(), s.getStatus(),
                    s.getOwnerName(), s.getVehicleName(), s.getVehicleNumber(), s.getVehicleType(),
                    s.getContactNumber(), s.getArrivalTime(), s.getExpectedDeparture(),
                    s.getVisitType(), s.getReason(), s.getResidentName(), s.getFlatFloor()
            });
        }
    }

    private void refreshResidents() {
        modelResidents.setRowCount(0);
        List<Resident> residents = new ResidentDAO().getAllResidents();
        for (Resident r : residents) {
            modelResidents.addRow(new Object[] {
                    r.getId(),
                    r.getName(),
                    r.getFlatNumber(),
                    r.getFloorNumber(),
                    r.getContactNumber(),
                    r.getVehicleName(),
                    r.getVehicleNumber(),
                    r.getVehicleType(),
                    r.getSlotNumber()
            });
        }
    }

    private void refreshLogs() {
        modelLogs.setRowCount(0);
        List<ParkingEntry> entries = new ParkingEntryDAO().getAllEntries();
        for (ParkingEntry e : entries) {
            modelLogs.addRow(new Object[] {
                    e.getVisitorName(),
                    e.getVehicleName(),
                    e.getVehicleNumber(),
                    e.getVehicleType(),
                    e.getContactNumber(),
                    e.getArrivalTime(),
                    e.getExpectedDeparture(),
                    e.getVisitType(),
                    e.getReason(),
                    e.getResidentName(),
                    e.getFlatNumber() + " - " + e.getFloorNumber(),
                    e.getSlotNumber()
            });
        }
    }

    private void handleAddResident() {
        JTextField name = new JTextField();
        JTextField flat = new JTextField();
        JTextField floor = new JTextField();
        JTextField contact = new JTextField();
        JTextField vName = new JTextField();
        JTextField vNum = new JTextField();
        JComboBox<String> vType = new JComboBox<>(new String[] { "2W", "4W", "EV", "SERVICE" });

        Object[] message = {
                "Name:", name,
                "Flat:", flat,
                "Floor:", floor,
                "Contact:", contact,
                "Vehicle Name:", vName,
                "Vehicle Number:", vNum,
                "Vehicle Type:", vType
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add Resident", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Resident r = new Resident(0, name.getText(), flat.getText(), floor.getText(), contact.getText(),
                    vNum.getText(), vName.getText(), (String) vType.getSelectedItem());
            if (new ResidentDAO().addResident(r)) {
                refreshResidents();
            }
        }
    }

    private void handleDeleteResident() {
        int row = tblResidents.getSelectedRow();
        if (row >= 0) {
            int id = (int) modelResidents.getValueAt(row, 0);
            if (new ResidentDAO().deleteResident(id)) {
                refreshResidents();
            }
        }
    }
}
