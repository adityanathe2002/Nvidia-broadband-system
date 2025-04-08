package nvidia.in;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class ConnectionJDBC {
	private static String sqlURL= "jdbc:mysql://localhost:3306/linkdein_db";
	private static String sqlUsername="root";
	private static String sqlPassword="root";
    public static Connection connection;
    public  PreparedStatement pst;

    // Static block for initializing database connection once
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(sqlURL, sqlUsername, sqlPassword);
        } catch (Exception e) {
            e.printStackTrace(); // Use a logger in production
        }
    }

    
     ConnectionJDBC() {
    	 
     }

    // Method to insert user data into the database
    public  boolean insertUserToDatabase(String username, String mobileNumber , String city) {
        String query = "INSERT INTO sign_in (username, mobileNumber, city) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, mobileNumber);
            stmt.setString(3, city);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Use a logger in production
            return false;
        }
    } 
    public  boolean insertAdminToDatabase(String adminId ,String username, String mobileNumber, String emailId , String password) {
        String query = "INSERT INTO admin (adminID, userName, mobileNumber ,emailID ,password) VALUES (?, ?, ? , ?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
        	stmt.setString(1, adminId);
            stmt.setString(2, username);
            stmt.setString(3, mobileNumber);
            stmt.setString(4, emailId);
            stmt.setString(5, password);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Use a logger in production
            return false;
        }
    }
    
    public boolean isAdminPresent(String mobileNumber) {
        String query = "SELECT COUNT(*) FROM admin WHERE mobileNumber = ?";
        
        try (Connection con = DriverManager.getConnection(sqlURL, sqlUsername, sqlPassword);
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, mobileNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;  // If count > 0, user exists
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Use logger in production
        }
        
        return false;  // Default to false if query fails
    }
    public boolean adminLogin(String username, String password) {
        boolean isAdmin = false;
        String query = "SELECT * FROM admin WHERE userName = ? AND password = ?";
        
        try (Connection con = ConnectionJDBC.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setString(1, username);
            pst.setString(2, password);
            
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                isAdmin = true;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return isAdmin;
    }

    
    public boolean isUserPresent(String mobileNumber) {
        String query = "SELECT COUNT(*) FROM sign_in WHERE mobileNumber = ?";
        
        try (Connection con = DriverManager.getConnection(sqlURL, sqlUsername, sqlPassword);
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, mobileNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;  // If count > 0, user exists
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Use logger in production
        }
        
        return false;  // Default to false if query fails
    }
    

    public void prepareStatement(String query) throws SQLException {
        if (connection != null) {
            pst = connection.prepareStatement(query);
        } else {
            System.out.println("Connection not established.");
        }
    }
    
    public boolean isAccountNoPresent(String accountNo) {
        String query = "SELECT COUNT(*) FROM broadband_plans WHERE accountNumber = ?";
        
        try (Connection con = DriverManager.getConnection(sqlURL, sqlUsername, sqlPassword);
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, accountNo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;  // If count > 0, accountNo exists
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with proper logging in production
        }
        
        return false;  // Default to false if query fails
    }

    public boolean isUserPresentIInBroadbandTable(String mobileNumber) {
        String query = "SELECT COUNT(*) FROM broadband_plans WHERE mobileNumber = ?";
        
        try (Connection con = DriverManager.getConnection(sqlURL, sqlUsername, sqlPassword);
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, mobileNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;  // If count > 0, user exists
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Use logger in production
        }
        
        return false;  // Default to false if query fails
    }
    
 // Method to check if an active account exists for a given phone number
    public Long getActiveAccountNoByPhoneNumber(String mobileNumber) {
        String query = "SELECT accountNo FROM broadband_plans WHERE mobileNumber = ?";
        
        try (Connection con = DriverManager.getConnection(sqlURL, sqlUsername, sqlPassword);
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, mobileNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getLong("accountNo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
 // Method to update broadband plan details by mobile number
    public void updatePlanByMobileNumber(String mobileNumber, String planType, double planPrice, String planData, String speed, String planDuration, long accountNo) {
        String query = "UPDATE broadband_plans SET planType = ?, planPrice = ?, planData = ?, speed = ?, planDuration = ?, accountNo = ? WHERE mobileNumber = ?";

        try (Connection con = DriverManager.getConnection(sqlURL, sqlUsername, sqlPassword);
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, planType);
            stmt.setDouble(2, planPrice);
            stmt.setString(3, planData);
            stmt.setString(4, speed);
            stmt.setString(5, planDuration);
            stmt.setLong(6, accountNo);
            stmt.setString(7, mobileNumber);

            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Plan updated successfully for mobile number: " + mobileNumber);
            } else {
                JOptionPane.showMessageDialog(null, "No plan found for mobile number: " + mobileNumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void closeConnection() {
    	try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public static Connection getConnection() {
		 try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            connection = DriverManager.getConnection(sqlURL, sqlUsername, sqlPassword);
	        } catch (Exception e) {
	            e.printStackTrace(); // Use a logger in production
	        }
		 return connection;
	}



}