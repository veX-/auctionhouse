package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import app.ProductListModel;

/**
 * 
 * @authors Liviu Chircu, Andreea Hodea, 2012-2013
 * 
 */
public class ProductsView extends JFrame {

	private static final long serialVersionUID = 1L;
	private final String TITLE = "Auction House";
	private final String HELLO_MSG = "Hi, there! Your product list is:";
	private final GUIMediator med;
	private JScrollPane prodPane;
	private JLabel username;
	private JPanel wrapper;
	private JButton btnLogout;
	private JTable table;
	final LoginPanel loginPanel;
	final RegisterUserPanel registerPanel;

	public ProductsView(GUIMediator med) {
		this.setLocationRelativeTo(null);
		this.med = med;
		med.registerProductsView(this);

		setTitle(TITLE);
		registerPanel = new RegisterUserPanel(this);
		loginPanel = new LoginPanel(this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addLoginPanel();

		this.setVisible(true);
	}

	public void buildMainWindow() {
		wrapper = new JPanel(new GridBagLayout());
		wrapper.setBackground(Color.WHITE);
		this.getContentPane().add(wrapper);

		createExtremityPane(0, 1);
		createLogoutPanel(0, 0);
		createTableDescription(1, 1);
		createProductPane(1, 2);
		createExtremityPane(2, 1);
	}

	private void addLoginPanel() {
		this.getContentPane().add(loginPanel);
		this.setSize(400, 200);
		setLocationRelativeTo(null);
	}

	public void addRegisterPanel() {
		this.getContentPane().add(registerPanel);
		this.setSize(0, 0); // don't know why but this makes it work
		this.setSize(400, 200);
		setLocationRelativeTo(null);
	}

	private void createExtremityPane(int x, int y) {
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridx = x;
		gbc_panel.gridy = y;
		gbc_panel.gridheight = 2;
		panel.setBackground(Color.WHITE);
		wrapper.add(panel, gbc_panel);
	}

	private void createLogoutPanel(int x, int y) {
		final JPanel logoutPanel = new JPanel();

		logoutPanel.add((username = new JLabel("Username")));
		logoutPanel.setBackground(Color.LIGHT_GRAY);

		btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});
		logoutPanel.add(btnLogout);

		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.EAST;
		gbc_panel.gridx = x;
		gbc_panel.gridy = y;
		gbc_panel.gridwidth = 4;
		gbc_panel.gridheight = 1;
		wrapper.add(logoutPanel, gbc_panel);
	}

	private void createTableDescription(int x, int y) {
		JLabel lblHello = new JLabel(HELLO_MSG);
		GridBagConstraints gbc_panel1 = new GridBagConstraints();
		gbc_panel1.anchor = GridBagConstraints.WEST;
		gbc_panel1.gridx = x;
		gbc_panel1.gridy = y;
		wrapper.add(lblHello, gbc_panel1);
	}

	private void createProductPane(int x, int y) {
		TableModel model = med.getProducts();
		table = new JTable(model);

		table.addMouseListener(new ClickListener(med));

		for (int i = ProductListModel.STATUS_COL, n = model.getColumnCount(); i < n; i++) {
			if (i != ProductListModel.PROGRESS_COL)
				table.getColumnModel().getColumn(i).setCellRenderer(new JScrollTableRenderer());
			else
				table.getColumnModel().getColumn(ProductListModel.PROGRESS_COL)
				.setCellRenderer(new JProgressTableRenderer());
		}
		table.getTableHeader().setReorderingAllowed(false);

		prodPane = new JScrollPane(table);
		prodPane.setBorder(BorderFactory.createLineBorder(Color.black));
		prodPane.setAlignmentX(10);
		GridBagConstraints gbc_panel2 = new GridBagConstraints();
		gbc_panel2.gridx = x;
		gbc_panel2.gridy = y;
		wrapper.add(prodPane, gbc_panel2);
	}

	private void updateRowHeights() {

		for (int row = 0; row < table.getRowCount(); row++) {
			int column = ProductListModel.LIST_COL;
			int rowHeight = table.getRowHeight();
			Component comp = table.prepareRenderer(
					table.getCellRenderer(row, column), row, column);
			rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);

			table.setRowHeight(row, rowHeight);
		}
	}

	/**
	 * This method should be called whenever the status of a product has changed
	 * (due to a click on a cell or due to a message on the network).
	 */
	@Override
	public void repaint() {
		updateRowHeights();
		prodPane.repaint();
	}

	public void register(String username, String password, String type) {
		if (!med.register(username, password, type)) {
			JOptionPane.showMessageDialog(getParent(), "Register failed", 
				    "Login info error",
				    JOptionPane.PLAIN_MESSAGE);
			return;
		}
		this.remove(registerPanel);
		addLoginPanel();
		this.getContentPane().repaint();
	}

	public void logIn(String username, String password, String type) {
		if (med.logIn(username, password, type)) {
			this.getContentPane().remove(loginPanel);
			this.buildMainWindow();
			this.username.setText(username);
			this.pack();
			setLocationRelativeTo(null);
			this.getContentPane().repaint();
			
			med.postGUIInit();
		}
		else {
			JOptionPane.showMessageDialog(getParent(), "Login failed", 
				    "Login info error",
				    JOptionPane.PLAIN_MESSAGE);
		}
	}

	public void logout() {
		if (med.logOut()) {
			this.remove(wrapper);
			addLoginPanel();
			med.resetStatus();
			this.getContentPane().repaint();
		}
	}

}

class JScrollTableRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;
	JList<String> pane;

	public JScrollTableRenderer() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		pane = (JList<String>) value;

		return pane;

	}
}

class JProgressTableRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;
	JProgressBar bar;

	public JProgressTableRenderer() {
		super();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		bar = (JProgressBar) value;

		return bar;

	}
}