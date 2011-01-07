package WindowManager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import GUI.*;
import Data.*;

import Data.DataManager;

public class SelectionManager {
	
	static public Vector<Container> containers;
	
	static private Container currentContainer;
	

	static SelectionManager selectionManager;
	
	public static SelectionManager getSelectionManager() {
		if (selectionManager == null) selectionManager = new SelectionManager();
		return selectionManager;
	}
	
	public static void close() {
		if (selectionManager != null) {
		if (containers != null) {
			for (int i = 0; i< containers.size(); i++) {
				containers.elementAt(i).setVisible(false);
			}
		}	
		containers = null;
		currentContainer = null;
		selectionManager = null;
		}
	}
	
	
	
	
	
	public void deleteSelection() {
		DataManager dm = DataManager.getDataManager();
		for (int i = 0; i < dm.Cases.size(); i++) {
			dm.Cases.elementAt(i).unselect();
		}
		for (int i = 0; i < dm.Variables.size(); i++) {
			dm.Variables.elementAt(i).unselect();
		}
	}
	

	
	public void repaintWindows() {
		// dataManager.selectVariables();
        
		
		if (containers == null) return;
			
		for (int i = 0; i < this.containers.size(); i++) {
			((Container) containers.elementAt(i)).panel.updateSelection();
		}

		String s = "";

		

		int selectedCases = 0;
		
		if (DataManager.getDataManager().Cases != null) {
			int anzahlCases = DataManager.getDataManager().Cases.size();
			for (int i = 0; i < DataManager.getDataManager().Cases.size(); i++) {
				if (DataManager.getDataManager().Cases.elementAt(i).isSelected())
					selectedCases++;

			}
			if (anzahlCases == 0) anzahlCases++;
			s += "   Cases:  " + selectedCases + "/" + anzahlCases + " ("
					+ (double) (selectedCases * 10000 / anzahlCases) / 100 + "%)";

		}

		int selectedCGHs = 0;
		String clones = null;

		IData idata = IData.getIData();
		
		
		idata.getContentPane().remove(idata.infoPanel);
		idata.infoPanel.removeAll();
		idata.infoLabel.setText(s);
		idata.infoPanel.add(idata.infoLabel,BorderLayout.WEST);
			
		idata.getContentPane().add(idata.infoPanel, BorderLayout.SOUTH);
		
		
		
		// infoLabel.setFont(new Font("Calibri",Font.PLAIN,12));

		
		//idata.add(infoLabel);

		idata.update(idata.getGraphics());
		idata.repaint();
		idata.infoPanel.paint(idata.infoPanel.getGraphics());
	//	idata.setVisible(true);
		//idata.updateUI();
		
		

	}
	
	
	
    public void deleteAllContainers() {
    	containers = new Vector();
    	currentContainer = null;
    }
    
    
	
	public Container getCurrentContainer() {
		if (currentContainer == null) {
		    currentContainer = new Container("Container");
		    containers = new Vector();
		    containers.add(currentContainer);    
		}
		return currentContainer;
	}
	
	
	public void addContainer(Container cont) {
		if (containers == null) containers = new Vector();
		containers.add(cont);
		currentContainer = cont;
	}
	
	
}
