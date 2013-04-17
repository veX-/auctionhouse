package gui;

import java.util.Map;

import javax.swing.table.TableModel;

import app.Command;

public interface GUIMediator {
	/* Communication from an implementation of GUIMediator to Mediator */
	public boolean register(String username);

	public boolean logIn(String username, String password, String type);

	public boolean logOut();

	public void registerProductsView(ProductsView pView);
	
	public void postMainWindowInit();

	public Map<String, Command> getContextMenuItems(int row, int listIndex);

	public Map<String, Command> getServiceMenuItems(int row, String name);

	public TableModel getProducts();

	/* Communication from Mediator to an implementation of GUIMediator */
	public void repaint();
}
