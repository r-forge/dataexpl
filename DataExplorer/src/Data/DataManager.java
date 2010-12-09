package Data;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.Panel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;
import DataTree.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;

import org.rosuda.REngine.Rserve.RConnection;

import Clustering.Clustering;
import DataTree.DataTreeNode;
import GUI.IData;
import GUI.IconsManager.IconsManager;
import Settings.Settings;
import Tools.Tools;
import WindowManager.ISelectable;




public class DataManager {
	
	public static DataManager dataManager;
	
	public Vector<Variable> Variables;

	public Vector<Case> Cases;
	
	public RConnection rConnection;

	public static double NA = 6.02E23;

	public double minValue, maxValue;


	
	
	public DataManager()
	{
		
		
		
	}	
	
	
	public static DataManager getDataManager() {
		if (dataManager == null) dataManager =  new DataManager();
		return dataManager;
	}
	
	
	public static void close() {
		if (dataManager == null) return;
		
		dataManager.Variables = new Vector();
		dataManager.Cases = new Vector();
		dataManager.minValue = 0;
		dataManager.maxValue = 0;
		
		dataManager = null;
	}
	
	
	
	
	
	
	public void openFile(String fileName,JFrame frame) {
		FileDialog fileDialog = null;
		
		if (fileName == null) {
			System.out.println("Open File");
			fileDialog = new FileDialog(frame, "Open File", 0);
			fileDialog.setVisible(true);
		}

		if (fileName != null || fileDialog.getFile() != null) {

			// MainPanel.removeAll();
			try {

				BufferedReader bfr = null;
				if (fileName == null) bfr = new BufferedReader(new FileReader(fileDialog.getDirectory() + "/" + fileDialog.getFile()));
				else bfr = new BufferedReader(new FileReader(new File(fileName)));
				
				DataLoader.loadFile(bfr, fileName, fileDialog);


			} catch (Exception e) {
				System.out.println("Load Error ");
				JOptionPane.showMessageDialog(frame,
					    "Wrong file format.",
					    "Load Error",
					    JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}

		}

	}

	
	
	
	
	
	
	
	
	
	
	/**
	 * Speichert die Selektion aus Genexpressiondaten
	 * */
	public void saveGeneExpressionData(Vector<Variable> Experiments, BufferedWriter bfr) {
		// Speichern Variablennamen
		try {
			
			
			
			
		for (int i = 0; i < Experiments.size(); i++) {
			bfr.write(Experiments.elementAt(i).name + "	");
		}
		bfr.write('\n');
		
		
		int a = 0;
		for (int j = 0; j< Cases.size(); j++) {
		if (Cases.elementAt(j).isSelected()) {
			
			a++;
			for (int i = 0; i < Experiments.size(); i++) {
				if (Experiments.elementAt(i).containsGeneProfiles()) bfr.write(Experiments.elementAt(i).getColumn() [j]+"	");
				else bfr.write(Experiments.elementAt(i).stringData [j]+"	");
			}
			
			
			
			
			
			
			
		bfr.write('\n');
		}
		
		}
		
	//	System.out.println(a);
		
		bfr.close();
		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public double[] getRowData(int row) {
		double[] rowData = new double[Variables.size()];
		for (int i = 0; i < rowData.length; i++) {
			rowData[i] = Variables.elementAt(i).getValue(row);
		}
		return rowData;
	}

	public boolean isRowSelected(int row) {
		return (Cases.elementAt(row).isSelected());
	
	}

	public int getIndexInArray(int index, int[] order) {
		for (int i = 0; i < order.length; i++) {
			if (order[i] == index)
				return i;
		}
		return -1;
	}

	public void selectRow(int row) {
		
		Cases.elementAt(row).select();
	
	}
	
	
	
	
	public void deleteSelection() {
		if (Variables != null) {
		for (int i = 0; i < this.Variables.size(); i++) {
			Variables.elementAt(i).unselect();
		}
		
		
		for (int i = 0; i < this.Cases.size(); i++) {
			Cases.elementAt(i).unselect();
		}
		
		}
		
	}
	
	

	public void selectGenesClones() {
		if (Variables != null) {
		
	
		for (int i = 0; i < this.Cases.size(); i++) {
			Cases.elementAt(i).select();
		}
		
		}	
		
		
	}
	
	
	
		
		
	
			
		
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public Vector<Variable> getVariables() {
		return Variables;
	}

	public void setVariables(Vector<Variable> variables) {
		this.Variables = variables;
	}

	

	
	public RConnection getRConnection() {
		return rConnection;
	}

	public void setRConnection(RConnection connection) {
		rConnection = connection;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public Vector<Variable> getExperiments() {
		return Variables;
	}

	public void setExperiments(Vector<Variable> experiments) {
		Variables = experiments;
	}

	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
