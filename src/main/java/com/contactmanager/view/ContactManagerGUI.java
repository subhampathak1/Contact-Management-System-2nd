package com.contactmanager.view;

import com.contactmanager.controller.ContactDAO;
import com.contactmanager.model.Contact;
import com.contactmanager.util.ContactManagerException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class ContactManagerGUI extends JFrame {
    private final ContactDAO contactDAO;
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private JTextField firstNameField, lastNameField, phoneField, emailField;
    private JTextArea addressArea;
    private JTextField searchField;
    
    public ContactManagerGUI() {
        contactDAO = new ContactDAO();
        initializeUI();
        loadContacts();
    }
    
    private void initializeUI() {
        setTitle("Contact Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Create main panels
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create form panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.NORTH);
        
        // Create table
        createTable();
        JScrollPane scrollPane = new JScrollPane(contactTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Initialize text fields
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        addressArea = new JTextArea(3, 20);
        searchField = new JTextField(20);
        
        // Add components to panel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        panel.add(firstNameField, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 3;
        panel.add(lastNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        panel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        panel.add(new JScrollPane(addressArea), gbc);
        
        return panel;
    }
    
    private void createTable() {
        String[] columnNames = {"ID", "First Name", "Last Name", "Phone", "Email", "Address"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        contactTable = new JTable(tableModel);
        contactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        contactTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = contactTable.getSelectedRow();
                if (selectedRow != -1) {
                    loadContactToForm(selectedRow);
                }
            }
        });
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");
        JButton searchButton = new JButton("Search");
        
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        
        // Add action listeners
        addButton.addActionListener(e -> addContact());
        updateButton.addActionListener(e -> updateContact());
        deleteButton.addActionListener(e -> deleteContact());
        clearButton.addActionListener(e -> clearForm());
        searchButton.addActionListener(e -> searchContacts());
        
        return panel;
    }
    
    private void loadContacts() {
        try {
            List<Contact> contacts = contactDAO.getAllContacts();
            updateTableModel(contacts);
        } catch (ContactManagerException e) {
            showError("Error loading contacts: " + e.getMessage());
        }
    }
    
    private void updateTableModel(List<Contact> contacts) {
        tableModel.setRowCount(0);
        for (Contact contact : contacts) {
            Object[] row = {
                contact.getId(),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getPhoneNumber(),
                contact.getEmail(),
                contact.getAddress()
            };
            tableModel.addRow(row);
        }
    }
    
    private void loadContactToForm(int row) {
        firstNameField.setText((String) tableModel.getValueAt(row, 1));
        lastNameField.setText((String) tableModel.getValueAt(row, 2));
        phoneField.setText((String) tableModel.getValueAt(row, 3));
        emailField.setText((String) tableModel.getValueAt(row, 4));
        addressArea.setText((String) tableModel.getValueAt(row, 5));
    }
    
    private void addContact() {
        if (!validateForm()) return;
        
        Contact contact = new Contact(
            firstNameField.getText(),
            lastNameField.getText(),
            phoneField.getText(),
            emailField.getText(),
            addressArea.getText()
        );
        
        try {
            contactDAO.addContact(contact);
            loadContacts();
            clearForm();
            JOptionPane.showMessageDialog(this, "Contact added successfully!");
        } catch (ContactManagerException e) {
            showError("Error adding contact: " + e.getMessage());
        }
    }
    
    private void updateContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a contact to update");
            return;
        }
        
        if (!validateForm()) return;
        
        Contact contact = new Contact(
            firstNameField.getText(),
            lastNameField.getText(),
            phoneField.getText(),
            emailField.getText(),
            addressArea.getText()
        );
        contact.setId((Integer) tableModel.getValueAt(selectedRow, 0));
        
        try {
            contactDAO.updateContact(contact);
            loadContacts();
            JOptionPane.showMessageDialog(this, "Contact updated successfully!");
        } catch (ContactManagerException e) {
            showError("Error updating contact: " + e.getMessage());
        }
    }
    
    private void deleteContact() {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a contact to delete");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this contact?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (Integer) tableModel.getValueAt(selectedRow, 0);
                contactDAO.deleteContact(id);
                loadContacts();
                clearForm();
                JOptionPane.showMessageDialog(this, "Contact deleted successfully!");
            } catch (ContactManagerException e) {
                showError("Error deleting contact: " + e.getMessage());
            }
        }
    }
    
    private void searchContacts() {
        String searchTerm = searchField.getText().trim();
        try {
            List<Contact> contacts = contactDAO.searchContacts(searchTerm);
            updateTableModel(contacts);
        } catch (ContactManagerException e) {
            showError("Error searching contacts: " + e.getMessage());
        }
    }
    
    private void clearForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressArea.setText("");
        contactTable.clearSelection();
    }
    
    private boolean validateForm() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressArea.getText().trim();

        if (firstName.isEmpty()) {
            showError("First Name cannot be empty.");
            return false;
        }
        if (lastName.isEmpty()) {
            showError("Last Name cannot be empty.");
            return false;
        }
        if (phone.isEmpty()) {
            showError("Phone Number cannot be empty.");
            return false;
        }

        // Basic phone number validation (e.g., digits only, optional country code)
        if (!Pattern.matches("^[+]?[0-9\\s-]{7,20}$", phone)) {
            showError("Invalid Phone Number format. Please use only digits, spaces, hyphens, and an optional '+' at the start.");
            return false;
        }

        // Basic email validation (regex for common email format)
        if (!email.isEmpty() && !Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", email)) {
            showError("Invalid Email format.");
            return false;
        }

        // Address can be empty, but if not empty, should be reasonable length
        if (address.length() > 255) {
            showError("Address is too long (max 255 characters).");
            return false;
        }

        return true;
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new ContactManagerGUI().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}