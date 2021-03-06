package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

/**
 * @authors Andreea Hodea, Liviu Chircu
 */
public class LoginPanel extends AbstractStartupPanel {

	private static final long serialVersionUID = 1L;

	public LoginPanel(ProductsView pv) {
		super(pv);
		titleLabel.setText("Login page");
	}

	@Override
	protected void addButtomButton() {
		bottomPanel.setLayout(new GridBagLayout());
		((GridBagLayout)bottomPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 38, 0, 0, 0};
		((GridBagLayout)bottomPanel.getLayout()).rowHeights = new int[] {0, 0, 0};
		((GridBagLayout)bottomPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
		((GridBagLayout)bottomPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

		//---- buttomButton ----
		buttomButton.setText("Login");
		buttomButton.addActionListener(new ActionListener() {
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
			}
		});
		bottomPanel.add(buttomButton, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 5), 0, 0));

		final JLabel registerLink = new JLabel("Register");
		registerLink.setForeground(Color.blue);
		registerLink.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				pv.getContentPane().removeAll();
				pv.addRegisterPanel();
				pv.getContentPane().repaint();
			}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {
				setCursor (new Cursor (Cursor.DEFAULT_CURSOR));
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setCursor (new Cursor (Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
		});
		bottomPanel.add(registerLink, new GridBagConstraints(10, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 10), 0, 0));
		add(bottomPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
	}
}
