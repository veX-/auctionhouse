package gui;

import java.util.Map;

import javax.swing.table.TableModel;

import app.Command;
import app.Mediator;

public class GUIMediatorImpl implements GUIMediator {

	private Mediator med;
	private ProductsView pView;

	public GUIMediatorImpl(Mediator med) {
		this.med = med;
	}

	@Override
	public boolean register(String username) {
		return med.register(username);
	}

	@Override
	public boolean logIn(String username, String password, String type) {
		return med.logIn(username, password, type);
	}

	@Override
	public boolean logOut() {
		return med.logOut();
	}

	@Override
	public void registerProductsView(ProductsView pView) {
		this.pView = pView;

	}

	@Override
	public Map<String, Command> getContextMenuItems(int row, int listIndex) {
		return med.getContextMenuItems(row, listIndex);
	}

	@Override
	public Map<String, Command> getServiceMenuItems(int row, String name) {
		return med.getServiceMenuItems(row, name);
	}

	@Override
	public void repaint() {
		if (this.pView != null)
			pView.repaint();
	}

	@Override
	public TableModel getProducts() {
		return med.getProducts();
	}
	
	@Override
	public void postMainWindowInit() {
		med.postMainWindowInit();
	}

}
