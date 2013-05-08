package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import app.model.User;

public abstract class AbstractStartupPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/* Login panel components */
	protected JLabel titleLabel;
	private JPanel loginInfoPanel;
	private JLabel userNameLabel;
	protected JTextField userNameField;
	private JLabel passLabel;
	protected JTextField passField;
	private JLabel typeLabel;
	protected JComboBox<String> typeComboBox;
	protected JPanel bottomPanel;
	protected JButton buttomButton;
	
	protected final ProductsView pv;

	public AbstractStartupPanel(ProductsView pv) {
		this.pv = pv;
		initComponents();
	}

	abstract protected void addButtomButton();
	
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
		buttomButton = new JButton();

		setBorder(new javax.swing.border.BevelBorder(100));

		setLayout(new GridBagLayout());
		((GridBagLayout)getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)getLayout()).rowHeights = new int[] {39, 0, 0, 0};
		((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

		add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
				new Insets(0, 0, 5, 0), 0, 0));

		createLoginInfoPanel();
		addButtomButton();
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

}
