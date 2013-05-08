package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class RegisterUserPanel extends AbstractStartupPanel {
	private static final long serialVersionUID = 1L;

	public RegisterUserPanel(ProductsView pv) {
		super(pv);
		titleLabel.setText("Register page");
	}

	@Override
	protected void addButtomButton() {
		bottomPanel.setLayout(new GridBagLayout());
		((GridBagLayout)bottomPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 38, 0, 0, 0};
		((GridBagLayout)bottomPanel.getLayout()).rowHeights = new int[] {0, 0, 0};
		((GridBagLayout)bottomPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
		((GridBagLayout)bottomPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

		//---- buttomButton ----
		buttomButton.setText("Register");
		buttomButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = userNameField.getText();
				if (username.isEmpty()) {
					JOptionPane.showMessageDialog(getParent(), "Username cannot be null", 
						    "Register info error",
						    JOptionPane.PLAIN_MESSAGE);	
					return;
				}
				String password = passField.getText();
				String type = (String)typeComboBox.getSelectedItem();
				pv.register(username, password, type);
			}
		});
		bottomPanel.add(buttomButton, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 5), 0, 0));

		add(bottomPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		
	}

}
