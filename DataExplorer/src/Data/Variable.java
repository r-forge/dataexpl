package Data;
import java.awt.Color;
import java.util.StringTokenizer;
import java.util.Vector;

import Clustering.NodeCase;
import GUI.IPlot;

public class Variable implements ISelectable,NodeCase{
	
	public String name;

	public int type; // Double,String

	public boolean selected;

	public double max;

	public double min;

	public double mean;

	public static final int Double = 1, String = 2;

	// type double

	private double[] doubleData;

	//public boolean[] isSelected;

	public boolean[] isNotNA;

	// type String

	public String[] stringData;

	public int ID;
	
	DataManager dataManager;

	
	public boolean isDiscret = true;
	
	public Vector<String> Buffer;
	

	public boolean isGene() {
		return false;
	}
	
	
	public void setDiscret(boolean discret)
	{
	this.isDiscret = discret;	
	} 

	public Variable(DataManager dataManager,String name, int type, int ID) {
		this.name = name;
		this.type = type;
		this.ID = ID;
		this.dataManager = dataManager;
	}

	public void select() {
		this.selected = true;	
	}

	public boolean isSelected() {
		
		return selected;
		
	}

	public void unselect() {
		this.selected = false;
	}

	
	
	public double [] getColumn() { 
		double [] column = new double [this.doubleData.length];
		for (int i = 0; i < doubleData.length;i++) {
			if (doubleData [i] != dataManager.NA) column [i] = doubleData [i];
			
		}
		return column;
	}
	
	public double [] getColumn(int col) { 
	return null;
	}	
		
	
	public double [] getColumn(Vector<ISelectable> Genes) { 
		double [] column = new double [Genes.size()];
		for (int i = 0; i < column.length;i++) {
			if (doubleData [Genes.elementAt(i).getID()] != dataManager.NA) column [i] = doubleData [Genes.elementAt(i).getID()];
			
		}
		return column;
	}
	
	
	
	
	
	public double getValue(int i) { 
			if (doubleData [i] != dataManager.NA) return doubleData [i];
			return mean;
		
	}
	
	
	
	public double getRealValue(int i) { 
	      return doubleData [i];
	
}
	
	public void setValue(int i, double value) { 
		doubleData [i ] = value;
	
}
	
	
	public void setColumn(double [] column) {
		this.doubleData = column;
	}
	

	public void calculateMean() {
		mean = 0;
	
		int len = 0;
		for (int i = 0; i < this.doubleData.length; i++) {
			if (doubleData[i] != dataManager.NA) {
				mean += doubleData[i];
			    len ++;
			    
			}
		}
		mean /= len;

	}

	public boolean isVariable() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	public boolean containsGeneProfiles() {
		if (type == Variable.Double) return true;
		return false;
	}

	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}

	public java.lang.String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public double[] getRow(Vector<ISelectable> rows) {
		// TODO Auto-generated method stub
		return null;
	}

	public double getMax() {
		// TODO Auto-generated method stub
		
		
		return max;
	}

	public double getMin() {
		// TODO Auto-generated method stub
		return min;
	}

	
	public int getType() {
		// TODO Auto-generated method stub
		
		
		return type;
	}
	
	
	public String [] getStringData() {
		// TODO Auto-generated method stub
		return stringData;
	}

	public Vector<ISelectable> getVariables() {
		// TODO Auto-generated method stub
		
		Vector<ISelectable> v = new Vector();
		for (int i = 0; i < dataManager.Cases.size(); i++) {
			v.add(dataManager.Cases.elementAt(i));
		}
		return v;
	}

	public double[] getDoubleData() {
		// TODO Auto-generated method stub
		
		
		return doubleData;
	}

	public boolean isClone() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCGHVariable() {
		// TODO Auto-generated method stub
		return false;
	}

	

}
