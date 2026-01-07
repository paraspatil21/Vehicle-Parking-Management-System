package com.vpms.view;

import com.vpms.dao.ParkingEntryDAO;
import com.vpms.dao.ResidentDAO;
import com.vpms.dao.SlotDAO;
import com.vpms.model.ParkingEntry;
import com.vpms.model.Resident;
import com.vpms.model.Slot;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

public class SecurityDashboard extends JFrame {
    private JTable tblActive;
    private DefaultTableModel modelActive;
    private JTextField txtVehNum, txtVehName, txtVisName, txtContact, txtReason;
    private JComboBox<String> cbVehType, cbVisitType;
    private JComboBox<Slot> cbSlots;
    private JLabel lblResidentInfo;

    public SecurityDashboard() {
        setTitle("Surya Industry - VPMS Security");
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(52, 73, 94));
        header.setPreferredSize(new Dimension(1100, 60));
        JLabel lblTitle = new JLabel("  SURYA INDUSTRY - VPMS ENTRY/EXIT", JLabel.LEFT);
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

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createEntryPanel());
        splitPane.setRightComponent(createActiveListPanel());
        splitPane.setDividerLocation(400);

        add(splitPane, BorderLayout.CENTER);

        refreshActiveList();
    }

    private JPanel createEntryPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("New Vehicle Entry"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Vehicle Number:"), gbc);
        gbc.gridx = 1;
        txtVehNum = new JTextField(15);
        panel.add(txtVehNum, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Vehicle Name:"), gbc);
        gbc.gridx = 1;
        txtVehName = new JTextField(15);
        panel.add(txtVehName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Vehicle Type:"), gbc);
        gbc.gridx = 1;
        cbVehType = new JComboBox<>(new String[] { "2W", "4W", "EV", "SERVICE" });
        panel.add(cbVehType, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Visit Type:"), gbc);
        gbc.gridx = 1;
        cbVisitType = new JComboBox<>(new String[] { "RESIDENT", "VISITOR", "SERVICE" });
        panel.add(cbVisitType, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Visitor Name:"), gbc);
        gbc.gridx = 1;
        txtVisName = new JTextField(15);
        panel.add(txtVisName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1;
        txtContact = new JTextField(15);
        panel.add(txtContact, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Reason:"), gbc);
        gbc.gridx = 1;
        txtReason = new JTextField(15);
        panel.add(txtReason, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Available Slot:"), gbc);
        gbc.gridx = 1;
        cbSlots = new JComboBox<>();
        panel.add(cbSlots, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        lblResidentInfo = new JLabel("Resident Info: Not Found");
        lblResidentInfo.setForeground(Color.BLUE);
        panel.add(lblResidentInfo, gbc);

        gbc.gridy = 8;
        JButton btnCheck = new JButton("Check Availability & Resident");
        panel.add(btnCheck, gbc);

        gbc.gridy = 9;
        JButton btnSave = new JButton("Confirm & Save Entry");
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);
        panel.add(btnSave, gbc);

        btnCheck.addActionListener(e -> checkVehicleAndSlots());
        btnSave.addActionListener(e -> saveEntry());

        return panel;
    }

    private JPanel createActiveListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Active Parked Vehicles"));

        modelActive = new DefaultTableModel(
                new String[] { "Visitor Name", "Vehicle Name", "Vehicle Number", "Vehicle Type", "Contact Number",
                        "Arrival Time", "Expected Departure Time", "Visit Type", "Reason", "Resident Name",
                        "Flat & Floor", "Slot Number" },
                0);
        tblActive = new JTable(modelActive);

        // Highlight logic for delays
        tblActive.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                try {
                    Timestamp expected = (Timestamp) table.getValueAt(row, 6); // Adjusted to 6 (Expected Time)
                    if (expected != null && expected.before(new Timestamp(System.currentTimeMillis()))) {
                        c.setBackground(new Color(255, 200, 200)); // Red for delay
                    } else {
                        c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    }
                } catch (Exception e) {
                }
                return c;
            }
        });

        panel.add(new JScrollPane(tblActive), BorderLayout.CENTER);

        JButton btnExit = new JButton("Mark Exit");
        btnExit.addActionListener(e -> handleExit());
        panel.add(btnExit, BorderLayout.SOUTH);

        return panel;
    }

    private void checkVehicleAndSlots() {
        String vNum = txtVehNum.getText();
        String type = (String) cbVehType.getSelectedItem();

        // Check Resident
        Resident r = new ResidentDAO().getResidentByVehicle(vNum);
        cbSlots.removeAllItems();

        if (r != null) {
            lblResidentInfo.setText("Resident: " + r.getName() + " | Flat: " + r.getFlatNumber());
            cbVisitType.setSelectedItem("RESIDENT");
            txtVisName.setText(r.getName());
            txtVehName.setText(r.getVehicleName());
            txtContact.setText(r.getContactNumber());
            cbVehType.setSelectedItem(r.getVehicleType());

            // For residents, we might want to show their reserved slot or all available of
            // that type
            // Adding their specific slot if available
            Slot reserved = new SlotDAO().getSlotById(r.getSlotId());
            if (reserved != null && "AVAILABLE".equals(reserved.getStatus())) {
                cbSlots.addItem(reserved);
            }
        } else {
            lblResidentInfo.setText("Resident Info: Not Found (Visitor)");
            cbVisitType.setSelectedItem("VISITOR");
        }

        // Fill other Available Slots
        List<Slot> available = new SlotDAO().getAvailableSlotsByType(type);
        for (Slot s : available) {
            // Avoid duplicates if reserved was already added
            boolean exists = false;
            for (int i = 0; i < cbSlots.getItemCount(); i++) {
                if (cbSlots.getItemAt(i).getId() == s.getId()) {
                    exists = true;
                    break;
                }
            }
            if (!exists)
                cbSlots.addItem(s);
        }
    }

    private void saveEntry() {
        if (cbSlots.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "No slot selected!");
            return;
        }

        Slot s = (Slot) cbSlots.getSelectedItem();
        ParkingEntry entry = new ParkingEntry();
        entry.setVehicleNumber(txtVehNum.getText());
        entry.setVehicleName(txtVehName.getText());
        entry.setVehicleType((String) cbVehType.getSelectedItem());
        entry.setVisitType((String) cbVisitType.getSelectedItem());
        entry.setVisitorName(txtVisName.getText());
        entry.setContactNumber(txtContact.getText());
        entry.setReason(txtReason.getText());
        entry.setSlotId(s.getId());

        // Optional: Capture resident info if it was found
        Resident r = new ResidentDAO().getResidentByVehicle(txtVehNum.getText());
        if (r != null && "RESIDENT".equals(cbVisitType.getSelectedItem())) {
            entry.setResidentName(r.getName());
            entry.setFlatNumber(r.getFlatNumber());
            entry.setFloorNumber(r.getFloorNumber());
        }

        // Set expected departure (default +2 hours for visitors)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 2);
        entry.setExpectedDeparture(new Timestamp(cal.getTimeInMillis()));

        if (new ParkingEntryDAO().saveEntry(entry)) {
            JOptionPane.showMessageDialog(this, "Entry Saved!");
            refreshActiveList();
            clearForm();
        }
    }

    private void handleExit() {
        int row = tblActive.getSelectedRow();
        if (row >= 0) {
            int entryId = (int) modelActive.getValueAt(row, 0);
            String vNum = (String) modelActive.getValueAt(row, 1);

            // In a real app, find slotId, here we need it from the list or DAO
            // For simplicity, let's assume we fetch the entry again or keep it in a list
            List<ParkingEntry> active = new ParkingEntryDAO().getActiveEntries();
            ParkingEntry entry = active.get(row); // Simplified mapping

            long diff = System.currentTimeMillis() - entry.getArrivalTime().getTime();
            String duration = (diff / (1000 * 60)) + " mins";

            if (new ParkingEntryDAO().markExit(entryId, entry.getSlotId(), duration)) {
                JOptionPane.showMessageDialog(this, "Vehicle " + vNum + " Exited. Duration: " + duration);
                refreshActiveList();
            }
        }
    }

    private void refreshActiveList() {
        modelActive.setRowCount(0);
        List<ParkingEntry> active = new ParkingEntryDAO().getActiveEntries();
        for (ParkingEntry e : active) {
            modelActive.addRow(new Object[] {
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

    private void clearForm() {
        txtVehNum.setText("");
        txtVehName.setText("");
        txtVisName.setText("");
        txtContact.setText("");
        txtReason.setText("");
        cbSlots.removeAllItems();
        lblResidentInfo.setText("Resident Info: Not Found");
    }
}
