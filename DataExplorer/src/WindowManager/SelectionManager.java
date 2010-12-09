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
	
	public Vector<Container> containers;
	
	private Container currentContainer;
	

	static SelectionManager selectionManager;
	
	public static SelectionManager getSelectionManager() {
		if (selectionManager == null) selectionManager = new SelectionManager();
		return selectionManager;
	}
	
	public static void close() {
		selectionManager = null;
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
			((Container) containers.elementAt(i)).updateSelection();
		}

		String s = "";

		int selectedGenes = 0;
		int anzahlGenes = 0;
		if (DataManager.getDataManager().Cases != null) {
			for (int i = 0; i < DataManager.getDataManager().Cases.size(); i++) {
				if (DataManager.getDataManager().Cases.elementAt(i) != null
						&& DataManager.getDataManager().Cases.elementAt(i).isSelected())
					selectedGenes++;
				if (DataManager.getDataManager().Cases.elementAt(i) != null)
					anzahlGenes++;
			}

			s += "Genes: " + selectedGenes + "/" + anzahlGenes + " ("
					+ (double) (selectedGenes * 10000 / anzahlGenes) / 100
					+ "%)";

		}

		int selectedExps = 0;
		if (DataManager.getDataManager().Variables != null) {
			int anzahlExps = DataManager.getDataManager().Variables.size();
			for (int i = 0; i < DataManager.getDataManager().Variables.size(); i++) {
				if (DataManager.getDataManager().Variables.elementAt(i).isSelected())
					selectedExps++;

			}
			if (anzahlExps == 0) anzahlExps++;
			s += "   Samples: " + selectedExps + "/" + anzahlExps + " ("
					+ (double) (selectedExps * 10000 / anzahlExps) / 100 + "%)";

		}

		int selectedCGHs = 0;
		String clones = null;

		IData idata = IData.getIData();
		
		
		idata.MainPanel.remove(idata.infoPanel);
		idata.MainPanel.add(idata.infoPanel, BorderLayout.SOUTH);
		idata.infoPanel.removeAll();
		JLabel infoLabel = new JLabel(s);
		// infoLabel.setFont(new Font("Calibri",Font.PLAIN,12));

		
		idata.add(infoLabel);

		idata.update(idata.getGraphics());
		idata.repaint();
		idata.paint(idata.infoPanel.getGraphics());
		//idata.updateUI();
		
		

	}
	
	
	
    public void deleteAllContainers() {
    	containers = new Vector();
    	currentContainer = null;
    }
    
    
	
	public Container getCurrentContainer() {
		if (currentContainer == null) {
		    currentContainer = new Container();
		    containers = new Vector();
		    containers.add(currentContainer);    
		}
		return currentContainer;
	}
	
	
	
}
