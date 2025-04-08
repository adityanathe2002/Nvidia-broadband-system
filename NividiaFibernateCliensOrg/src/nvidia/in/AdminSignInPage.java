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
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class AdminSignInPage extends JFrame{
	private JTextField userNameField , passwordField ;
	public  static String userNameOfAdmin;
	private Color userNameColor , passwordColor ;
	private JButton signInButton ,signUpButton , goBackButton ;
	private Color signInColor = new Color(30,144,255 ) ;
	private Color signUpColor = new Color(100, 200, 200);
	private Color goBackColor = new Color(220,20,60) ;
	private JCheckBox termsBox ;



	public AdminSignInPage()
	{
		setFrame();
		initializeComponents();
		addComponents();
		setUpListeners();
	}

	//1st step
	private void setFrame()
	{
		setSize(1920 , 1080 );
		setVisible(true) ;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(createBackgroundImage()) ;

	}

	//2nd step
	private JPanel createBackgroundImage () 
	{
		return new JPanel()
		{
			@Override
			protected void paintComponent(Graphics g) {

				super.paintComponent(g);
				ImageIcon icon = new ImageIcon(getClass().getResource("/icons/nvidia.jpg")) ;
				Image image = icon.getImage();

				double panelHeight = getHeight() ;
				double panelWidth = getWidth() ;

				double imageHeight = image.getHeight(this) ;
				double imageWidth = image.getWidth(this) ;

				double scaled = Math.max(panelWidth/imageWidth, panelHeight/imageHeight) ;

				int scaledHeight = (int)(scaled*imageHeight) ;
				int scaledWidth = (int)(scaled*imageWidth) ;



				int x =((int)(panelWidth - scaledWidth) / 2);
				int y = ((int)(panelHeight - scaledHeight) / 2);

				g.drawImage(image, x, y, scaledWidth, scaledHeight, this) ;

				g.setColor(new Color(0,0,0,100)) ;
				g.fillRect(0, 0, getWidth(), getHeight()) ;

			}
		};
	}

	//3rd step
	private void addComponents()
	{

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.Y_AXIS));
		contentPanel.setOpaque(false);
		contentPanel.add(Box.createVerticalStrut(260));

		JLabel title = new JLabel("Admin SignIn",SwingConstants.CENTER);
		title.setFont(new Font("Arial",Font.BOLD,24)) ;
		title.setForeground(Color.WHITE);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(Box.createVerticalGlue()) ;
		contentPanel.add(title);
		contentPanel.add(Box.createRigidArea(new Dimension(0,20))) ;

		JPanel formPanel = new JPanel() ;
		formPanel.setLayout(new BoxLayout(formPanel,BoxLayout.Y_AXIS));
		formPanel.setOpaque(false);
		formPanel.setAlignmentX(CENTER_ALIGNMENT);

		addRowForm("Username",userNameField,formPanel);
		addRowForm("Password",passwordField,formPanel) ;
		formPanel.add(Box.createRigidArea(new Dimension(0,10))) ;
		formPanel.add(termsBox) ;

		contentPanel.add(formPanel) ;
		contentPanel.add(Box.createRigidArea(new Dimension(0,20))) ;

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
		buttonPanel.setOpaque(false);
		buttonPanel.add(signInButton) ;
		buttonPanel.add(signUpButton);
		buttonPanel.add(goBackButton) ;
		contentPanel.add(buttonPanel) ;
		contentPanel.add(Box.createVerticalGlue()) ;

		add(contentPanel) ;

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()) ;
				} catch (ClassNotFoundException e) {

					e.printStackTrace();
				} catch (InstantiationException e) {

					e.printStackTrace();
				} catch (IllegalAccessException e) {

					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {

					e.printStackTrace();
				}
			}

		});


	}

	//4th step
	private JTextField createStyledTextFields(int cols)
	{
		JTextField textField = new JTextField(cols);
		textField.setBackground(new Color(255, 255, 255));
		textField.setForeground(new Color(33,33,33));
		textField.setFont(new Font("Arial",Font.PLAIN,14));

		textField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(200, 200, 200)),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)
				));


		return textField ;
	}

	private void initializeComponents()
	{
		userNameField = createStyledTextFields(20) ;
		passwordField = createStyledTextFields(20) ;
		signInButton = createStyledButtons("Sign-In",signInColor) ;
		signUpButton= createStyledButtons("Sign-Up", signUpColor);
		goBackButton = createStyledButtons("Go-Back",goBackColor) ;
		termsBox = new JCheckBox("Agree to terms & conditions ");
		createStyledCheckBox(termsBox); 
	}

	//5th step 
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

	//6th step
	private JButton createStyledButtons(String text ,Color background)
	{
		JButton button = new JButton(text) ;
		button.setBackground(background); 
		button.setForeground(Color.black);
		button.setFont(new Font("Arial",Font.BOLD,15));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.WHITE, 2),
				BorderFactory.createEmptyBorder(12, 25, 12, 25)
				));
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



		return button ;

	}

	//7th step
	private void  createStyledCheckBox(JCheckBox box)
	{
		box.setForeground(Color.WHITE);
		box.setFont(new Font("Arial",Font.PLAIN,14));
		box.setOpaque(false);
		box.setFocusPainted(false);


	}


	//9th step

	private void setUpListeners()
	{
		signInButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleSignIn();

			}});

		goBackButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleGoBack();

			}});

		signUpButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new AdminSignUp();
				dispose();

			}
		});
	}

	

	private void handleSignIn()
	{
		if(!termsBox.isSelected())
		{
			showError("Agree terms & conditions firstly");
			return ;
		}

		String username =userNameField.getText().trim() ;
		String password = passwordField.getText().trim() ;
		if (username.isEmpty() && password.isEmpty()) {
			showError("Username Or Password Is Empty.");
			return;
		}
		if (isValidPass(password)) {
			showError("Invalid Password. or Enter 6 Char Password");
			return;
		}
		
		ConnectionJDBC con = new ConnectionJDBC();
		
		if (con.adminLogin(username, password)) {
			JOptionPane.showMessageDialog(this, "Sign-in successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
			userNameOfAdmin =username;
			System.out.println(userNameOfAdmin);
			new AdminDashboard();
			dispose();
		}
		else {
			showError("Username or Password Is Wrong");
		}
	
		
		
		
	}
	
	private boolean isValidPass(String password)
	{
		return password.matches("\\d{6}") ;
	}
	

	private void handleGoBack()
	{

		JOptionPane.showMessageDialog(this, "Going back...", "Go Back", JOptionPane.INFORMATION_MESSAGE);
		new HomePage();
	}

	private void showError(String message) {

		JOptionPane.showMessageDialog(this, message ,"Error",JOptionPane.ERROR_MESSAGE);
	}

	public static void main(String[] args) {
		new AdminSignInPage();
	}
}