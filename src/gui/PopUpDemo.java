package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import app.Command;
import app.ProductListModel;
import app.MakeOfferComm;

public class PopUpDemo extends JPopupMenu {

	private static final long serialVersionUID = -7403189443588372049L;

	public PopUpDemo(GUIMediator med, final int row, final int col,
			final int index) {

		Map<String, Command> items = null;

		if (col == ProductListModel.PROD_COL)
			items = med.getServiceMenuItems(row, null);
		if (col == ProductListModel.LIST_COL)
			items = med.getContextMenuItems(row, index);
		if (items != null) {
			for (final Map.Entry<String, Command> i : items.entrySet()) {
				final JMenuItem item = new JMenuItem(i.getKey());
				if (i.getValue() instanceof MakeOfferComm)
					item.addMouseListener(new MakeOfferListener(i.getValue(), row, index,
							getParent()));
				else
					item.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							i.getValue().execute(row, col, index);
						}
					});
				add(item);
			}
		}
	}
}
