package nvidia.in;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfile extends JFrame {
    public UserProfile() {
        // Set up the frame
        setTitle("User Profile");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel with a vertical box layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Fetch user profile details
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/linkdein_db", "root", "root");
            
            // Query to join signup_table and broadband_plans
            String query = "SELECT s.username, s.mobileNumber, s.city, " +
                           "b.planType, b.planPrice, b.planData, " +
                           "b.speed, b.planDuration, b.accountNo " +
                           "FROM sign_in s " +
                           "LEFT JOIN broadband_plans b ON s.mobileNumber = b.mobileNumber " +
                           "WHERE s.mobileNumber = ?";
            
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, String.valueOf(UserDashboard.phoneNo));
            
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Personal Details Section
                JPanel personalDetailsPanel = createSectionPanel("Personal Details");
                addProfileField(personalDetailsPanel, "Name", rs.getString("username"));
                addProfileField(personalDetailsPanel, "Mobile Number", rs.getString("mobileNumber"));
                addProfileField(personalDetailsPanel, "City", rs.getString("city"));
                mainPanel.add(personalDetailsPanel);
                
                // Add some spacing
                mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

                // Plan Details Section
                JPanel planDetailsPanel = createSectionPanel("Plan Details");
                addProfileField(planDetailsPanel, "Plan Type", 
                    rs.getString("planType") != null ? rs.getString("planType") : "No Active Plan");
                addProfileField(planDetailsPanel, "Plan Price", 
                    rs.getString("planPrice") != null ? "₹ " + rs.getString("planPrice") : "N/A");
                addProfileField(planDetailsPanel, "Plan Data", 
                    rs.getString("planData") != null ? rs.getString("planData") : "N/A");
                addProfileField(planDetailsPanel, "Speed", 
                    rs.getString("speed") != null ? rs.getString("speed") : "N/A");
                addProfileField(planDetailsPanel, "Plan Duration", 
                    rs.getString("planDuration") != null ? rs.getString("planDuration") : "N/A");
                addProfileField(planDetailsPanel, "Account Number", 
                    rs.getString("accountNo") != null ? rs.getString("accountNo") : "N/A");
                mainPanel.add(planDetailsPanel);
            } else {
                // No user found
                JLabel noUserLabel = new JLabel("No user details found");
                noUserLabel.setFont(new Font("Arial", Font.BOLD, 16));
                noUserLabel.setForeground(Color.RED);
                mainPanel.add(noUserLabel);
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error fetching profile details: " + e.getMessage(), 
                "Profile Error", 
                JOptionPane.ERROR_MESSAGE);
        }

        // Scroll pane in case of many details
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane);

        // Make frame visible
        setVisible(true);
    }

    // Create a section panel with a title
    private JPanel createSectionPanel(String sectionTitle) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), 
            sectionTitle, 
            0, 
            0, 
            new Font("Arial", Font.BOLD, 16)
        ));
        return sectionPanel;
    }

    // Helper method to add profile fields
    private void addProfileField(JPanel panel, String label, String value) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelComponent = new JLabel(label + ": ");
        labelComponent.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel valueComponent = new JLabel(value != null ? value : "Not Available");
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        
        fieldPanel.add(labelComponent);
        fieldPanel.add(valueComponent);
        
        panel.add(fieldPanel);
    }
}