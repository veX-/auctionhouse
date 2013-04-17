package gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JTable;

import app.ProductListModel;

@SuppressWarnings("unchecked")
public class ClickListener extends MouseAdapter {
	private final GUIMediator med;
	private final int LIST_ELEM_SIZE = 18;

	public ClickListener(GUIMediator med) {
		this.med = med;
	}

	private void setSelection(MouseEvent e) {
		JTable table = (JTable) e.getSource();

		int r = table.rowAtPoint(e.getPoint());
		int c = table.columnAtPoint(e.getPoint());
		int index = -1;

		if (table.getColumnClass(c) == JList.class
				&& c == ProductListModel.LIST_COL) {
			/*
			 * Get selected element in list based on click location and start of
			 * list position.
			 */
			JList<String> users = (JList<String>) table.getValueAt(r, c);
			if (users.getModel().getSize() > 0) {
				Point tablep = table.getLocationOnScreen();
				int rowHeight = tablep.y;
				for (int i = 0; i < r; i++)
					rowHeight += table.getRowHeight(i);
				index = (e.getYOnScreen() - rowHeight) / LIST_ELEM_SIZE;
				users.clearSelection();
				users.setSelectedIndex(index);
			}
		}

		if (r >= 0 && r < table.getRowCount() && c >= 0
				&& c < table.getColumnCount()) {
			table.setRowSelectionInterval(r, r);
			table.setColumnSelectionInterval(c, c);
		} else {
			table.clearSelection();
		}
		if (e.isPopupTrigger()) {
			doPop(e.getComponent(), e.getX(), e.getY(), r, c, index);
		}
	}

	@Override
	/**
	 * On MacOS popup is triggered on mouse pressed.
	 */
	public void mousePressed(MouseEvent e) {
		setSelection(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setSelection(e);
	}

	/**
	 * 
	 * @param c
	 *            JTable containing clicked element.
	 * @param x
	 *            Click x coordinate.
	 * @param y
	 *            Click y coordinate.
	 * @param row
	 *            Row in table.
	 * @param col
	 *            Column in table.
	 * @param index
	 *            Index in list from table cell or -1 if the list wasn't the
	 *            clicked element.
	 */
	private void doPop(Component c, int x, int y, int row, int col, int index) {
		PopUpDemo menu = new PopUpDemo(med, row, col, index);

		menu.show(c, x, y);
	}
}
