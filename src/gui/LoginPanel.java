package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.*;

import app.model.User;

/**
 * @authors Andreea Hodea, Liviu Chircu
 */
public class LoginPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/* Login panel components */
	private JLabel titleLabel;
	private JPanel loginInfoPanel;
	private JLabel userNameLabel;
	private JTextField userNameField;
	private JLabel passLabel;
	private JTextField passField;
	private JLabel typeLabel;
	private JComboBox<String> typeComboBox;
	private JPanel bottomPanel;
	private JButton loginButton;
	
	private final ProductsView pv;

	public LoginPanel(ProductsView pv) {
		this.pv = pv;
		initComponents();
	}

	private void initComponents() {
		titleLabel = new JLabel();
		loginInfoPanel = new JPanel();
		userNameLabel = new JLabel();
		userNameField = new JTextField();
		passLabel = new JLabel();
		passField = new JTextField();
		typeLabel = new JLabel();
		typeComboBox = new JComboBox<String>();
		bottomPanel = new JPanel();
		loginButton = new JButton();

		setBorder(new javax.swing.border.BevelBorder(100));

		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {39, 0, 0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

		titleLabel.setText("Login page");
		add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 5, 0), 0, 0));

		createLoginInfoPanel();
		addLoginButton();
	}

	private void createLoginInfoPanel() {
		//======== loginInfoPanel ========
		loginInfoPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		loginInfoPanel.setLayout(new GridBagLayout());
		((GridBagLayout)loginInfoPanel.getLayout()).columnWidths = new int[] {0, 0, 115};

		//---- userNameLabel ----
		userNameLabel.setText("User name:");
		loginInfoPanel.add(userNameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 5, 5), 0, 0));
		loginInfoPanel.add(userNameField, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 5, 0), 0, 0));

		//---- passLabel ----
		passLabel.setText("Password:");
		loginInfoPanel.add(passLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 5, 5), 0, 0));
		loginInfoPanel.add(passField, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 5, 0), 0, 0));

		//---- typeLabel ----
		typeLabel.setText("Type:");
		loginInfoPanel.add(typeLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 0, 5), 0, 0));
		loginInfoPanel.add(typeComboBox, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		//---- typeLabel ----
		String[] types = {User.sellerType, User.buyerType};
		typeComboBox.setModel(new DefaultComboBoxModel<String>(types));

		add(loginInfoPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 5, 0), 0, 0));

	}

	private void addLoginButton() {

		bottomPanel.setLayout(new GridBagLayout());
		((GridBagLayout)bottomPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 38, 0, 0, 0};
		((GridBagLayout)bottomPanel.getLayout()).rowHeights = new int[] {0, 0, 0};
		((GridBagLayout)bottomPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
		((GridBagLayout)bottomPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

		//---- loginButton ----
		loginButton.setText("Login");
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = userNameField.getText();
				if (username.isEmpty()) {
					JOptionPane.showMessageDialog(getParent(), "Username cannot be null", 
						    "Login info error",
						    JOptionPane.PLAIN_MESSAGE);	
					return;
				}
				String password = passField.getText();
				String type = (String)typeComboBox.getSelectedItem();
				pv.logIn(username, password, type);
				pv.postMainWindowInit();
			}
		});
		bottomPanel.add(loginButton, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 5), 0, 0));

		add(bottomPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

	}
}
