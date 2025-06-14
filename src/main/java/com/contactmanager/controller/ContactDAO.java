package com.contactmanager.controller;

import com.contactmanager.model.Contact;
import com.contactmanager.util.DatabaseConnection;
import com.contactmanager.util.ContactManagerException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {
    
    public void addContact(Contact contact) throws ContactManagerException {
        if (contact == null) {
            throw new ContactManagerException("Contact cannot be null.");
        }
        if (contact.getFirstName() == null || contact.getFirstName().trim().isEmpty()) {
            throw new ContactManagerException("First name cannot be empty.");
        }
        if (contact.getLastName() == null || contact.getLastName().trim().isEmpty()) {
            throw new ContactManagerException("Last name cannot be empty.");
        }
        if (contact.getPhoneNumber() == null || contact.getPhoneNumber().trim().isEmpty()) {
            throw new ContactManagerException("Phone number cannot be empty.");
        }

        String sql = "INSERT INTO contacts (first_name, last_name, phone_number, email, address) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, contact.getFirstName());
            pstmt.setString(2, contact.getLastName());
            pstmt.setString(3, contact.getPhoneNumber());
            pstmt.setString(4, contact.getEmail());
            pstmt.setString(5, contact.getAddress());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contact.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new ContactManagerException("Error adding contact to the database: " + e.getMessage(), e);
        }
    }
    
    public void updateContact(Contact contact) throws ContactManagerException {
        if (contact == null) {
            throw new ContactManagerException("Contact cannot be null.");
        }
        if (contact.getId() == 0) {
            throw new ContactManagerException("Contact ID must be set for update.");
        }
        if (contact.getFirstName() == null || contact.getFirstName().trim().isEmpty()) {
            throw new ContactManagerException("First name cannot be empty.");
        }
        if (contact.getLastName() == null || contact.getLastName().trim().isEmpty()) {
            throw new ContactManagerException("Last name cannot be empty.");
        }
        if (contact.getPhoneNumber() == null || contact.getPhoneNumber().trim().isEmpty()) {
            throw new ContactManagerException("Phone number cannot be empty.");
        }

        String sql = "UPDATE contacts SET first_name=?, last_name=?, phone_number=?, email=?, address=? WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, contact.getFirstName());
            pstmt.setString(2, contact.getLastName());
            pstmt.setString(3, contact.getPhoneNumber());
            pstmt.setString(4, contact.getEmail());
            pstmt.setString(5, contact.getAddress());
            pstmt.setInt(6, contact.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ContactManagerException("Error updating contact in the database: " + e.getMessage(), e);
        }
    }
    
    public void deleteContact(int id) throws ContactManagerException {
        if (id <= 0) {
            throw new ContactManagerException("Invalid contact ID for deletion.");
        }

        String sql = "DELETE FROM contacts WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ContactManagerException("Error deleting contact from the database: " + e.getMessage(), e);
        }
    }
    
    public List<Contact> getAllContacts() throws ContactManagerException {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Contact contact = new Contact();
                contact.setId(rs.getInt("id"));
                contact.setFirstName(rs.getString("first_name"));
                contact.setLastName(rs.getString("last_name"));
                contact.setPhoneNumber(rs.getString("phone_number"));
                contact.setEmail(rs.getString("email"));
                contact.setAddress(rs.getString("address"));
                contacts.add(contact);
            }
        } catch (SQLException e) {
            throw new ContactManagerException("Error retrieving all contacts from the database: " + e.getMessage(), e);
        }
        
        return contacts;
    }
    
    public List<Contact> searchContacts(String searchTerm) throws ContactManagerException {
        List<Contact> contacts = new ArrayList<>();
        // Basic validation for search term
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new ContactManagerException("Search term cannot be empty.");
        }

        String sql = "SELECT * FROM contacts WHERE first_name LIKE ? OR last_name LIKE ? OR phone_number LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Contact contact = new Contact();
                    contact.setId(rs.getInt("id"));
                    contact.setFirstName(rs.getString("first_name"));
                    contact.setLastName(rs.getString("last_name"));
                    contact.setPhoneNumber(rs.getString("phone_number"));
                    contact.setEmail(rs.getString("email"));
                    contact.setAddress(rs.getString("address"));
                    contacts.add(contact);
                }
            }
        } catch (SQLException e) {
            throw new ContactManagerException("Error searching contacts in the database: " + e.getMessage(), e);
        }
        
        return contacts;
    }
}