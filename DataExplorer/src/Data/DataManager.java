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
	
	public Vector<Variable> variables;

	public Vector<Case> Genes;
	
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
				
				DataLoader.loadGeneExpressions(this, bfr, fileName, fileDialog);


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
		for (int j = 0; j< Genes.size(); j++) {
		if (Genes.elementAt(j).isSelected()) {
			
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
		double[] rowData = new double[variables.size()];
		for (int i = 0; i < rowData.length; i++) {
			rowData[i] = variables.elementAt(i).getValue(row);
		}
		return rowData;
	}

	public boolean isRowSelected(int row) {
		return (Genes.elementAt(row).isSelected());
	
	}

	public int getIndexInArray(int index, int[] order) {
		for (int i = 0; i < order.length; i++) {
			if (order[i] == index)
				return i;
		}
		return -1;
	}

	public void selectRow(int row) {
		
		Genes.elementAt(row).select(true);
	
	}
	
	
	
	
	public void deleteSelection() {
		if (variables != null) {
		for (int i = 0; i < this.variables.size(); i++) {
			variables.elementAt(i).unselect(false);
		}
		
		
		for (int i = 0; i < this.Genes.size(); i++) {
			Genes.elementAt(i).unselect(false);
		}
		
		}
		
	}
	
	

	public void selectGenesClones() {
		if (variables != null) {
		
	
		for (int i = 0; i < this.Genes.size(); i++) {
			Genes.elementAt(i).select(false);
		}
		
		}	
		
		
	}
	
	
	
		
		
	
			
		
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public Vector<Variable> getVariables() {
		return variables;
	}

	public void setVariables(Vector<Variable> variables) {
		this.variables = variables;
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
		return variables;
	}

	public void setExperiments(Vector<Variable> experiments) {
		variables = experiments;
	}

	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
