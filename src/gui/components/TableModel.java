package gui.components;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import engine.components.ServerTable;

public class TableModel extends JTable implements MouseListener {
	private ArrayList<ServerTable> table_list;
	private DefaultTableModel table_Model;
	private static final long serialVersionUID = 1L;
	public TableModel(){
		table_Model = new DefaultTableModel();
		table_list = new ArrayList<ServerTable>();
		addMouseListener(this);
		setModel(table_Model);
	}
	
	public DefaultTableModel getTable_Model() {
		return table_Model;
	}
	
	public void clear() {
		table_list.clear();
		table_Model.setRowCount(0);
	}
	public void addColumn(String data[]){
		for(String element:data )
			table_Model.addColumn(element);
	}
	public void addRow(ServerTable data) {
		table_list.add(data);
		table_Model.addRow(new Object[]{data.getName()});
	}
	
	public ArrayList<ServerTable> getList() {
		return table_list;
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex) {
	  return false;
	}
	
	public void mouseReleased(MouseEvent e) {
		int r = rowAtPoint(e.getPoint());
		if ( r >= 0 && r < getRowCount()) 
            setRowSelectionInterval(r, r);
        else 
            clearSelection();
        if (getSelectedRow() < 0)
            return;
        if (e.isPopupTrigger() && e.getComponent() instanceof JTable ) {
            JPopupMenu popup = new JPopupMenu();
            popup.add(new JMenuItem("User Name"));
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
			
	}
	
	public void mouseClicked(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent arg0) {}
	
	public void mouseExited(MouseEvent arg0) {}
	
	public void mousePressed(MouseEvent arg0) {}
	
}
