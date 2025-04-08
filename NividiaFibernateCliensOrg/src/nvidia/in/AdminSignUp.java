package nvidia.in;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class AdminSignUp extends JFrame {
	private JTextField usernameTextField, mobileNumberTextField, emailIDTextField;
	private JPasswordField adminPasswordTextField;
	private JButton  signUpButton, goBackButton;
	private Color signUpColor = new Color(30, 144, 255);
	private Color signInColor = new Color(220, 20, 60);
	private JCheckBox termsBox;
	private JComboBox<String> cities;

	public AdminSignUp() {
		setUpFrame();
		intializeComponents();
		addComponents();
		uiManager();
	}

	// 1st step
	private void setUpFrame() {
		setSize(1920 , 1080 );
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(createBackgroundImage());

	}

	// 2nd step
	private JPanel createBackgroundImage() {
		return new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {

				super.paintComponent(g);
				ImageIcon icon = new ImageIcon(getClass().getResource("/icons/nvidiabackground.jpg"));
				Image image = icon.getImage();

				double panelHeight = getHeight();
				double panelWidth = getWidth();

				double imageHeight = image.getHeight(this);
				double imageWidth = image.getWidth(this);

				double scaled = Math.max(panelWidth / imageWidth, panelHeight / imageHeight);

				int scaledHeight = (int) (scaled * imageHeight);
				int scaledWidth = (int) (scaled * imageWidth);

				int x = ((int) (panelWidth - scaledWidth) / 2);
				int y = ((int) (panelHeight - scaledHeight) / 2);

				g.drawImage(image, x, y, scaledWidth, scaledHeight, this);

				g.setColor(new Color(0, 0, 0, 140));
				g.fillRect(0, 0, getWidth(), getHeight());

			}
		};
	}

	private void intializeComponents() {
		usernameTextField = createStyledTextField(20);
		mobileNumberTextField = createStyledTextField(20);
		emailIDTextField=createStyledTextField(20);
		adminPasswordTextField=createStyledPasswordField(20);
		signUpButton = createStyledButtons("sign-Up", signInColor);
		signUpButton.addActionListener(e->handleSignUp());
		goBackButton = createStyledButtons("Go-Back", signUpColor);
		goBackButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new AdminSignInPage();
				dispose();

			}
		});

		termsBox = new JCheckBox(" I Agree to terms & conditions ");
		createStyledCheckBox(termsBox);

	}

	// 3rd step
	private void addComponents() {

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setOpaque(false);
		// contentPanel.add(Box.createVerticalStrut(20));

		JLabel title = new JLabel("Admin Registration");
		title.setFont(new Font("Arial", Font.BOLD, 30));
		title.setForeground(Color.WHITE);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(Box.createVerticalGlue());
		contentPanel.add(title);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 50)));

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);
		formPanel.setAlignmentX(CENTER_ALIGNMENT);

		addRowForm("Admin Username", usernameTextField, formPanel);
		formPanel.add(Box.createVerticalStrut(50));
		addRowForm("Mobile-Number", mobileNumberTextField, formPanel);
		formPanel.add(Box.createVerticalStrut(50));
		addRowForm("Admin Email-ID", emailIDTextField, formPanel);
		formPanel.add(Box.createVerticalStrut(50));
		addRowForm("Admin Password", adminPasswordTextField, formPanel);
		formPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		formPanel.add(termsBox);

		contentPanel.add(formPanel);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
		buttonPanel.setOpaque(false);
		buttonPanel.add(signUpButton);
		buttonPanel.add(goBackButton);
		contentPanel.add(buttonPanel);

		contentPanel.add(Box.createVerticalGlue());

		add(contentPanel);
	}

	// 4th step
	private JTextField createStyledTextField(int cols) {
		JTextField textField = new JTextField(cols);
		textField.setBackground(new Color(255, 255, 255));
		textField.setForeground(new Color(33, 33, 33));
		textField.setFont(new Font("Arial", Font.PLAIN, 18));
		textField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));

		return textField;
	}

	private JPasswordField createStyledPasswordField(int cols) {
		JPasswordField textField = new JPasswordField(cols);
		textField.setBackground(new Color(255, 255, 255));
		textField.setForeground(new Color(33, 33, 33));
		textField.setFont(new Font("Arial", Font.PLAIN, 18));
		textField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));

		return textField;
	}

	// 5th step
	private void addRowForm(String labelText ,JComponent component , JPanel formPanel)
	{
		JPanel rowPanel = new JPanel();
		rowPanel.setLayout(new BoxLayout(rowPanel,BoxLayout.X_AXIS));
		rowPanel.setOpaque(false);

		JLabel label = new JLabel(labelText);
		label.setForeground(Color.white);
		label.setFont(new Font("Arial",Font.PLAIN,14));
		label.setPreferredSize(new Dimension(120,25));
		rowPanel.add(label);
		rowPanel.add(Box.createRigidArea(new Dimension(10,0))) ;
		rowPanel.add(component) ;

		formPanel.add(rowPanel) ;
		formPanel.add(Box.createRigidArea(new Dimension(0,10))) ;


	}


	// 6th step
	private JButton createStyledButtons(String text, Color background) {
		JButton button = new JButton(text);
		button.setBackground(background);
		button.setForeground(Color.black);
		button.setFont(new Font("Arial", Font.BOLD, 15));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2),
				BorderFactory.createEmptyBorder(12, 25, 12, 25)));

		button.setOpaque(true);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(background.brighter());
				button.setOpaque(true);
			}


			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(background);
				button.setOpaque(true);
			}
		});

		return button;
	}

	private void handleSignUp() {
		// Check if terms and conditions are accepted
		if (!termsBox.isSelected()) {
			showError("Agree to the terms & conditions first.");
			return;
		}

		// Retrieve input values
		String username = usernameTextField.getText().trim();
		String mobilenumber = mobileNumberTextField.getText().trim();
		String emailID = emailIDTextField.getText().trim();
		String password = new String(adminPasswordTextField.getPassword()).trim();



		// Validate empty fields
		if (username.isEmpty() && mobilenumber.isEmpty() && emailID.isEmpty() && password.isEmpty()) {
			showError("Fields cannot be empty.");
			return;
		}

		// Validate mobile number
		if (!isValidMobile(mobilenumber)) {
			showError("Invalid Mobile Number. Enter a 10-digit number.");
			return;
		}

		// Validate email ID
		if (!isValidEmail(emailID)) {
			showError("Invalid Email ID. Please enter a valid email address.");
			return;
		}
		if (isValidPass(password)) {
			showError("Invalid Password. or Enter 8 Char Password");
			return;
		}


		// Insert data into the database
		ConnectionJDBC con = new ConnectionJDBC();

		if (con.isAdminPresent(mobilenumber)) {
			showError("Mobile Number Is Already In Used");
			return;

		}
		boolean insertedOrNot=   con.insertAdminToDatabase(generateAdminId()  ,username, mobilenumber, emailID,password);
		if (insertedOrNot) {
			JOptionPane.showMessageDialog(this, "Sign-up successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
			dispose();
			new AdminSignInPage();
		}else {
			JOptionPane.showMessageDialog(this, "Sign-up Failed!", "Failed", JOptionPane.WARNING_MESSAGE);
		}

	}
	public  String generateAdminId() {
		Random random = new Random();
		int randomNumber = 10000 + random.nextInt(90000); // Generate a 5-digit random number
		return "ADMIN" + randomNumber;
	}
	private boolean isValidPass(String password)
	{
		return password.matches("\\d{6}") ;
	}

	// Method to validate email ID using regex
	private boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
		return email != null && email.matches(emailRegex);
	}


	private boolean isValidMobile(String password) {
		return password.matches("\\d{10}");
	}

	private void showError(String message) {

		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	// 7th step
	private void createStyledCheckBox(JCheckBox box) {
		box.setForeground(Color.WHITE);
		box.setFont(new Font("Arial", Font.PLAIN, 14));
		box.setOpaque(false);
		box.setFocusPainted(false);

	}

	// 8th
	private void createStyledComboBox(JComboBox<String> city) {

		city.setFont(new Font("Arial", Font.BOLD, 19));
		// city.setBackground(new Color(33,171,230));
		city.setBackground(new Color(255, 255, 255));
		city.setForeground(Color.black);

	}
	public void uiManager() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Apply system look and feel
		} catch (Exception e) {
			e.printStackTrace(); // Print exception stack trace if an error occurs
		}
	}

	public static void main(String[] args) {
		new AdminSignUp();
	}
}