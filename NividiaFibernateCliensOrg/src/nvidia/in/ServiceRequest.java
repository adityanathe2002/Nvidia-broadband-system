package nvidia.in;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class ServiceRequest extends JFrame{
	private JTextField requestIdField, userField;
	private JComboBox<String> typeComboBox;
	private JSpinner dateSpinner;
	private JButton submitButton, clearButton, backButton;
	private Color primaryColor = new Color(25, 118, 210);
	private Color accentColor = new Color(230, 74, 25); 
	public ServiceRequest() {
		super("Nvidia Broadband - Service Request");
		setupFrame();
		initializeComponents();
		addComponents();
		setupListeners();
		setVisible(true);
	}
	private void setupFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1920 , 1080 );
		setLocationRelativeTo(null);
		setContentPane(createMainPanel());
	}
	private JPanel createMainPanel() {
		JPanel mainPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				GradientPaint gradient = new GradientPaint(
						0, 0, new Color(41, 128, 185),
						0, getHeight(), new Color(44, 62, 80)
						);
				((Graphics2D) g).setPaint(gradient);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		mainPanel.setLayout(new BorderLayout());
		return mainPanel;
	}
	public int getRequestID() {
		int max = 100000;
		int min = 1;
		return (int)((Math.random() * max - min) + 1);  
	}
	private void initializeComponents() {
		requestIdField = createStyledTextField();
		requestIdField.setEditable(false);
		requestIdField.setText(String.valueOf(getRequestID())); // Set initial request ID

		userField = createStyledTextField();

		typeComboBox = createStyledComboBox(new String[]{
				"Connection Issue",
				"Speed Problem",
				"Bill Related",
				"Technical Support",
				"Plan Upgrade",
				"General Inquiry"
		});
		SpinnerDateModel dateModel = new SpinnerDateModel();
		dateSpinner = new JSpinner(dateModel);
		dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
		styleComponent(dateSpinner);
		submitButton = createStyledButton("Submit Request");
		clearButton = createStyledButton("Clear Form");
		backButton = createStyledButton("Go Back");
	}
	private JTextField createStyledTextField() {
		JTextField field = new JTextField(20);
		styleComponent(field);
		return field;
	}
	private JComboBox<String> createStyledComboBox(String[] items) {
		JComboBox<String> comboBox = new JComboBox<String>(items);
		styleComponent(comboBox);
		return comboBox;
	}
	private void styleComponent(JComponent component) {
		component.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		component.setBackground(Color.WHITE);
		component.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(200, 200, 200)),
				BorderFactory.createEmptyBorder(0, 10, 5, 10)
				));
	}
	private JButton createStyledButton(String text) {
		JButton button = new JButton(text);
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setBackground(new Color(41, 128, 185));
		button.setForeground(Color.WHITE);
		button.setOpaque(true);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setPreferredSize(new Dimension(150, 40));
		button.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				button.setBackground(new Color(52, 152, 219));
			}
			public void mouseExited(MouseEvent e) {
				button.setBackground(new Color(41, 128, 185));
			}
		});
		return button;
	}
	private void addComponents() {
		JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
		contentPanel.setOpaque(false);
		contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
		
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setOpaque(false);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);
		
		addFormRow(formPanel, "Request ID:", requestIdField, gbc, 0);
		addFormRow(formPanel, "User:", userField, gbc, 1);
		addFormRow(formPanel, "Request Type:", typeComboBox, gbc, 2);
		addFormRow(formPanel, "Date:", dateSpinner, gbc, 3);
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		
		buttonPanel.setOpaque(false);
		buttonPanel.add(submitButton);
		buttonPanel.add(clearButton);
		buttonPanel.add(backButton);
		contentPanel.add(createHeaderPanel(), BorderLayout.NORTH);
		contentPanel.add(formPanel, BorderLayout.CENTER);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);
		add(contentPanel);
	}
	private JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setOpaque(false);

		JLabel titleLabel = new JLabel("Service Request Form");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

		headerPanel.add(titleLabel, BorderLayout.CENTER);
		return headerPanel;
	}
	private void addFormRow(JPanel panel, String labelText, JComponent component, GridBagConstraints gbc, int row) {
		JLabel label = new JLabel(labelText);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		label.setForeground(Color.WHITE);
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.weightx = 0.3;
		panel.add(label, gbc);
		gbc.gridx = 1;
		gbc.weightx = 0.7;
		panel.add(component, gbc);
	}
	private void setupListeners() {
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 handleSubmit();
			}
		});
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  handleClear();
			}
		});
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 handleBack();
			}
		});
	}
		  private void handleSubmit() {
		      try {
		          Connection conn = ConnectionJDBC.getConnection();
		          String query = "INSERT INTO servicerequest (requestId, user, type, date) VALUES (?, ?, ?, ?)";
		          PreparedStatement pstmt = conn.prepareStatement(query);
	 
		          pstmt.setInt(1, Integer.parseInt(requestIdField.getText()));
		          pstmt.setString(2, userField.getText());
		          pstmt.setString(3, (String) typeComboBox.getSelectedItem());
		          pstmt.setDate(4, new java.sql.Date(((Date) dateSpinner.getValue()).getTime()));
	 
		          pstmt.executeUpdate();
		          JOptionPane.showMessageDialog(this, "Service request submitted successfully!");
		          handleClear();
	 
		      } catch (SQLException ex) {
		          JOptionPane.showMessageDialog(this,
		              "Error submitting request: " + ex.getMessage(),
		              "Error",
		              JOptionPane.ERROR_MESSAGE);
		          ex.printStackTrace();
		      }
		  }
		  private void handleClear() {
		      requestIdField.setText(String.valueOf(getRequestID())); // Generate new request ID
		      userField.setText("");
		      typeComboBox.setSelectedIndex(0);
		      dateSpinner.setValue(new Date());
		  }
		  private void handleBack() {
		      dispose();
		  }
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new ServiceRequest();
	}
}