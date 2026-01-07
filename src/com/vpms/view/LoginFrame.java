package com.vpms.view;

import com.vpms.dao.UserDAO;
import com.vpms.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRole;
    private JButton btnLogin, btnForgotPassword;

    public LoginFrame() {
        setTitle("Surya Industry - VPMS Login");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("SURYA INDUSTRY - VPMS", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(40, 60, 100));
        add(lblTitle, BorderLayout.NORTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        txtUsername = new JTextField(15);
        panel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(15);
        panel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        cbRole = new JComboBox<>(new String[] { "ADMIN", "SECURITY" });
        panel.add(cbRole, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(60, 130, 200));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        panel.add(btnLogin, gbc);

        gbc.gridy = 4;
        btnForgotPassword = new JButton("Forgot Password?");
        btnForgotPassword.setBorderPainted(false);
        btnForgotPassword.setContentAreaFilled(false);
        btnForgotPassword.setForeground(Color.BLUE);
        panel.add(btnForgotPassword, gbc);

        add(panel, BorderLayout.CENTER);

        btnLogin.addActionListener(this::handleLogin);
        btnForgotPassword
                .addActionListener(e -> JOptionPane.showMessageDialog(this, "Please contact Admin to reset password."));
    }

    private void handleLogin(ActionEvent e) {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String selectedRole = (String) cbRole.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty!");
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.login(username, password);

        if (user != null && user.getRole().equals(selectedRole)) {
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + user.getUsername());
            this.dispose();
            if (user.getRole().equals("ADMIN")) {
                new AdminDashboard().setVisible(true);
            } else {
                new SecurityDashboard().setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials or role!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
