package nvidia.in;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Random;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminDashboard extends JFrame {

	private  static String userNameAdmin = AdminSignInPage.userNameOfAdmin; 
	private static String adminID,adminName , adminEmailId ;
	private JButton billGeneratorButton, accountDetailsButton, serviceRequestButton, profileButton;
	private JPanel rightPanel;
	private JTable serviceRequestTable;  // Declare JTable as a class variable


	public AdminDashboard() {
		super("Admin Dashboard");
		setupFrame();
		addComponent();
		setVisible(true);
		fetchAdminDetails();
	}

	private void fetchAdminDetails() {
		try {
			ConnectionJDBC con = new ConnectionJDBC();
			con.getConnection();
			String query = "SELECT adminID, userName, emailID FROM admin WHERE userName = ?";
			con.prepareStatement(query);
			con.pst.setString(1, userNameAdmin);
			ResultSet rs = con.pst.executeQuery();
			if (rs.next()) {
				adminID = rs.getString("adminID");
				adminName = rs.getString("userName");
				adminEmailId = rs.getString("emailID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addComponent() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		JPanel leftPanel = createAdminDashPanel();
		mainPanel.add(leftPanel, BorderLayout.WEST);

		rightPanel = createMainPanel();
		mainPanel.add(rightPanel, BorderLayout.CENTER);

		add(mainPanel);
	}

	private void setupFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(900,700);
		setLocationRelativeTo(null); // Center the window on screen
	}

	public void initializeComponent() {
		billGeneratorButton = createStyledButton("Bill Generator");
		billGeneratorButton.addActionListener(e -> showBillGeneratorPanel());

		accountDetailsButton = createStyledButton("Account Details");
		accountDetailsButton.addActionListener(e -> showAccountDetailsPanel());


		serviceRequestButton = createStyledButton("Service Request");
		serviceRequestButton.addActionListener(e -> serviceRequestsPanel());

		profileButton = createStyledButton("Profile");
		profileButton.addActionListener(e-> profileInfoAndUpdate());


	}




	public JPanel createAdminDashPanel() {
		JPanel adminPanel = new JPanel();
		adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS)); 
		adminPanel.setBackground(new Color(50, 50, 50));
		adminPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 20));

		initializeComponent(); 

		// Add buttons to the panel with spacing
		adminPanel.add(billGeneratorButton);
		adminPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		adminPanel.add(accountDetailsButton);
		adminPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		adminPanel.add(serviceRequestButton);
		adminPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		adminPanel.add(profileButton);

		return adminPanel;
	}

	private JButton createStyledButton(String text) {
		JButton button = new JButton(text);
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setBackground(new Color(70, 70, 70));
		button.setForeground(Color.WHITE);
		button.setOpaque(true);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		Dimension buttonSize = new Dimension(200, 40);
		button.setPreferredSize(buttonSize);
		button.setMaximumSize(buttonSize); 

		return button;
	}

	public JPanel createMainPanel() {
		JPanel adminMainPanel = new JPanel();
		adminMainPanel.setLayout(new BoxLayout(adminMainPanel, BoxLayout.Y_AXIS));
		adminMainPanel.setBackground(Color.white);
		adminMainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		return adminMainPanel;
	}

	private JPanel billGeneratorPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Labels and input fields
		String[] labels = {"Account ID:", "Plan Bill (Monthly)", "Due Fine ($):", "State Tax (%)", "Total Amount ($):"};
		JTextField[] fields = {
				new JTextField(), new JTextField("0.00"),
				new JTextField("0.00"), new JTextField("18.00"),
				new JTextField("0.00")
		};

		fields[4].setEditable(false); // Total Amount field is read-only

		for (int i = 0; i < labels.length; i++) {
			gbc.gridx = 0;
			gbc.gridy = i;
			panel.add(new JLabel(labels[i]), gbc);

			gbc.gridx = 1;
			panel.add(fields[i], gbc);
		}

		// Buttons
		JButton calculateButton = new JButton("Calculate Total");
		JButton generateBillButton = new JButton("Generate Bill");

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(calculateButton);
		buttonPanel.add(generateBillButton);

		gbc.gridx = 0;
		gbc.gridy = labels.length;
		gbc.gridwidth = 2;
		panel.add(buttonPanel, gbc);

		// Calculate Total Button Logic
		calculateButton.addActionListener(e -> {
			try {
				double planBill = Double.parseDouble(fields[1].getText());
				double dueFine = Double.parseDouble(fields[2].getText());
				double stateTax = Double.parseDouble(fields[3].getText());

				// Calculate total with state tax
				double totalAmount = planBill + dueFine + (planBill * stateTax / 100);
				fields[4].setText(String.format("%.2f", totalAmount));
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(panel, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Generate Bill Button Logic
		generateBillButton.addActionListener(e -> {
			try {
				int accountNo = Integer.parseInt(fields[0].getText());
				double planBill = Double.parseDouble(fields[1].getText());
				double dueFine = Double.parseDouble(fields[2].getText());
				double stateTax = Double.parseDouble(fields[3].getText());
				double totalAmount = Double.parseDouble(fields[4].getText());

				String transactionID = "TXN" + System.currentTimeMillis(); // Simple transaction ID generator
				String paymentType = "Credit Card"; // You can add a dropdown for dynamic selection
				String date = java.time.LocalDate.now().toString(); // Current date

				ConnectionJDBC con = new ConnectionJDBC();
				con.getConnection();

				String query = """
						    INSERT INTO bill (accountNo, billAmount, transactionID, paymentType, dueFine, gstTax, date)
						    VALUES (?, ?, ?, ?, ?, ?, ?)
						""";

				con.prepareStatement(query);
				con.pst.setInt(1, accountNo);
				con.pst.setDouble(2, planBill);
				con.pst.setString(3, transactionID);
				con.pst.setString(4, paymentType);
				con.pst.setDouble(5, dueFine);
				con.pst.setDouble(6, stateTax);
				con.pst.setString(7, date);

				int rowsInserted = con.pst.executeUpdate();

				if (rowsInserted > 0) {
					JOptionPane.showMessageDialog(panel, "Bill generated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
					for (JTextField field : fields) {
						field.setText("0.00"); // Reset fields
					}
					fields[0].setText(""); // Clear Account ID
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(panel, "Error generating bill: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		return panel;
	}


	private void showBillGeneratorPanel() {
		rightPanel.removeAll(); // Clear existing content
		rightPanel.add(billGeneratorPanel()); // Add bill generator form
		rightPanel.revalidate();
		rightPanel.repaint();
	}

	private void serviceRequestsPanel() {
		rightPanel.removeAll();
		JPanel panel = new JPanel(new BorderLayout());

		// Define column names for the table
		String[] columnNames = {"Request ID", "User", "Type", "Status", "Date"};
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		JTable table = new JTable(model);

		// Store the table instance for future reference
		this.serviceRequestTable = table;

		// Create status ComboBox for status updates
		JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Completed", "Resolved", "In Progress", "Pending", "Cancelled"});

		// Add ActionListener to update status when selected
		statusComboBox.addActionListener(e -> {
			try {
				String selectedStatus = (String) statusComboBox.getSelectedItem();  // Get updated value
				int selectedRequestId = getSelectedRequestId(); // Get request ID from selected row
				
				if (selectedRequestId != -1) {
					handleStatusCombobox(selectedStatus, selectedRequestId);
					JOptionPane.showMessageDialog(null, "Status Updated Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
				} 
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});

		// Set the ComboBox editor in the 4th column (Status column)
		table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(statusComboBox));

		// Fetch service requests from the database
		try (Connection conn = ConnectionJDBC.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM servicerequest")) {

			while (rs.next()) {
				model.addRow(new Object[]{
						rs.getInt("requestId"),
						rs.getString("user"),
						rs.getString("type"),
						rs.getString("status"),
						rs.getDate("date")
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Add table to scroll pane
		JScrollPane scrollPane = new JScrollPane(table);
		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(e -> serviceRequestsPanel());

		// Set BorderLayout for rightPanel
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(scrollPane, BorderLayout.CENTER);

		// Create a sub-panel with right-aligned FlowLayout
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(refreshButton);

		// Add button panel to the bottom of the rightPanel
		rightPanel.add(buttonPanel, BorderLayout.SOUTH);

		rightPanel.revalidate();
		rightPanel.repaint();
	}
	private int getSelectedRequestId() {
		int selectedRow = serviceRequestTable.getSelectedRow();
		if (selectedRow != -1) { // Check if a row is selected
			return (int) serviceRequestTable.getValueAt(selectedRow, 0); // Request ID from column 0
		}
		return -1; // No row selected
	}

	private void handleStatusCombobox(String status, int requestId) throws SQLException {
		Connection conn = ConnectionJDBC.getConnection();
		String query = "UPDATE servicerequest SET status = ? WHERE requestId = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);

		pstmt.setString(1, status);
		pstmt.setInt(2, requestId);

		try {
			int rowsUpdated = pstmt.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Status updated successfully.");
			} else {
				System.out.println("No matching request found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	private void showAccountDetailsPanel() {
		rightPanel.removeAll();

		String[] columns = {"Mobile Number", "User Name", "Plan Type", "Plan Price", "Account Number"};
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		JTable table = new JTable(model);

		try {
			ConnectionJDBC con = new ConnectionJDBC();
			con.getConnection();

			// Use JOIN to fetch userName along with broadband plan details
			String query = """
					SELECT bp.mobileNumber, si.userName, bp.planType, 
					       bp.planPrice, bp.accountNo
					FROM broadband_plans bp
					INNER JOIN sign_in si ON bp.mobileNumber = si.mobileNumber
					""";

			con.prepareStatement(query);
			ResultSet rs = con.pst.executeQuery();

			while (rs.next()) {
				model.addRow(new Object[]{
						rs.getString("mobileNumber"),
						rs.getString("userName"),
						rs.getString("planType"),
						rs.getDouble("planPrice"),
						rs.getString("accountNo")
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		JScrollPane scrollPane = new JScrollPane(table);
		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(e -> showAccountDetailsPanel());

		// Set BorderLayout for the main panel
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(scrollPane, BorderLayout.CENTER);

		// Create a sub-panel with right-aligned FlowLayout
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(refreshButton);

		// Add the button panel to the bottom of the rightPanel
		rightPanel.add(buttonPanel, BorderLayout.SOUTH);

		rightPanel.revalidate();
		rightPanel.repaint();
	}


	private void profileInfoAndUpdate() {
		rightPanel.removeAll();

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel adminIDLabel = new JLabel("Admin ID:");
		JTextField adminIDField = new JTextField(adminID); // Example ID, can be fetched dynamically
		adminIDField.setEditable(false);

		JLabel nameLabel = new JLabel("Name:");
		JTextField nameField = new JTextField(adminName);

		JLabel emailLabel = new JLabel("Email:");
		JTextField emailField = new JTextField(adminEmailId);

		JLabel roleLabel = new JLabel("Role:");
		JTextField roleField = new JTextField("System Administrator");
		roleField.setEditable(false);

		JButton updateButton = new JButton("Update Profile");

		// Adding components to the panel
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(adminIDLabel, gbc);
		gbc.gridx = 1;
		panel.add(adminIDField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(nameLabel, gbc);
		gbc.gridx = 1;
		panel.add(nameField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(emailLabel, gbc);
		gbc.gridx = 1;
		panel.add(emailField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(roleLabel, gbc);
		gbc.gridx = 1;
		panel.add(roleField, gbc);

		gbc.gridx = 1;
		gbc.gridy = 4;
		panel.add(updateButton, gbc);

		// Update Profile Button Action
		updateButton.addActionListener(e -> {
			String name = nameField.getText();
			String email = emailField.getText();

			// Validate input
			if (name.isEmpty() || email.isEmpty()) {
				JOptionPane.showMessageDialog(panel, "Name and Email cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Perform the update operation (Database logic here)
			try {
				ConnectionJDBC con = new ConnectionJDBC();
				con.getConnection();

				String query = """
						UPDATE admin
						SET userName = ?, emailId = ?
						WHERE adminID = ?
						""";

				con.prepareStatement(query);
				con.pst.setString(1, name);
				con.pst.setString(2, email);
				con.pst.setString(3, adminIDField.getText());

				int rowsUpdated = con.pst.executeUpdate();

				if (rowsUpdated > 0) {
					JOptionPane.showMessageDialog(panel, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(panel, "Failed to update profile.", "Error", JOptionPane.ERROR_MESSAGE);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(panel, "Error updating profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		rightPanel.add(panel);
		rightPanel.revalidate();
		rightPanel.repaint();
	}




	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new AdminDashboard()); 
	}
}