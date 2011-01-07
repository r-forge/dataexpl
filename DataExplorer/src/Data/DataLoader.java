package Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import Settings.Settings;
import StringTools.*;
import Tools.Tools;

import java.awt.*;


public class DataLoader {
	
	
	

    
	
	public static void loadFile(BufferedReader bfr, String fileName,FileDialog fileDialog) {
		

        DataManager dataManager = DataManager.getDataManager();
        
		try {

			dataManager.Variables = new Vector();

			String line = bfr.readLine();

			Vector<String> data = new Vector();
			
			MyStringTokenizer stk = new MyStringTokenizer(line);
			int col = 0;
			while (stk.hasMoreTokens()) {
				dataManager.Variables.add(new Variable(dataManager,stk.nextToken(),Variable.Double, col));
				col++;
			}

			
			
			
			int length = 0;
			boolean format = false;

			while ((line = bfr.readLine()) != null) {

				data.add(line);
				if (line.equals("")) {
					format = true;
					line = bfr.readLine();
				}
				length++;
			}
			
		
			if (format) length--;

			
			
			if (bfr == null) { 
			     if (fileName == null) bfr = new BufferedReader(new FileReader(fileDialog.getDirectory()+ "/" + fileDialog.getFile()));
			     else bfr = new BufferedReader(new FileReader(new File(fileName)));
			}
			else {
				
			}
			
			
			
			line = bfr.readLine();

			for (int i = 0; i < dataManager.Variables.size(); i++) {
				Variable var = dataManager.Variables.elementAt(i);
				var.setColumn(new double[length]);
				var.isNotNA = new boolean[length];
				var.stringData = new String[length];
			}
			
			
			
			int len = 0;
			
			
		  for (int k = 0; k < data.size(); k++) {

			    line = data.elementAt(k);
			    if (line.equals("")) {
					break;
				}
				    
				MyStringTokenizer st = new MyStringTokenizer(line,col);
				String token;
		
				for (int i = 0; i < col; i++) {
					token = st.nextToken();

					if ((dataManager.Variables.elementAt(i)).type == Variable.Double) {
						try {
							(dataManager.Variables.elementAt(i)).setValue(len,new Double(token).doubleValue());

							(dataManager.Variables.elementAt(i)).isNotNA[len] = true;
							if ((dataManager.Variables.elementAt(i)).getValue(len) > (dataManager.Variables.elementAt(i)).max) {
								(dataManager.Variables.elementAt(i)).max = (dataManager.Variables.elementAt(i)).getValue(len);
							}
							if ((dataManager.Variables.elementAt(i)).getValue(len) < (dataManager.Variables.elementAt(i)).min) {
								(dataManager.Variables.elementAt(i)).min = (dataManager.Variables.elementAt(i)).getValue(len);
							}

							if ((dataManager.Variables.elementAt(i)).getValue(len) > dataManager.maxValue) {
								dataManager.maxValue = (dataManager.Variables.elementAt(i)).getValue(len);
							}
							if ((dataManager.Variables.elementAt(i)).getValue(len) < dataManager.minValue) {
								dataManager.minValue = (dataManager.Variables.elementAt(i)).getValue(len);
							}

						} catch (Exception e) {
							if (token.equals("NA") || token.equals("")) {
								(dataManager.Variables.elementAt(i)).isNotNA[len] = false;
								(dataManager.Variables.elementAt(i)).setValue(len,dataManager.NA);
							} else {
								(dataManager.Variables.elementAt(i)).stringData[len] = token;
								(dataManager.Variables.elementAt(i)).type = Variable.String;
								(dataManager.Variables.elementAt(i)).isDiscret = true;
							}
						}
					} else {
						(dataManager.Variables.elementAt(i)).stringData[len] = token;
					}
				}

				len++;
			}

		System.out.println("len" + len);

			//dataManager.selectedRows = new boolean[dataManager.RowCount];
			//dataManager.selectedVariables = new boolean[dataManager.variables.size() - 2];

			 dataManager.Cases = new Vector();
			 for (int i = 0; i < length; i++) {
				 dataManager.Cases.add(new Case(dataManager.Variables.elementAt(0).stringData [i],i,dataManager));
				 
			 }
			

			for (int i = 0; i < dataManager.Variables.size(); i++) {
				
				Variable var = dataManager.Variables.elementAt(i);	    
				var.calculateMean();
				var.ID = i;
				var.Buffer = new Vector();
				
				for (int j = 0; j < var.stringData.length; j++) {
					if (Tools.doesntContain(var.Buffer, var.stringData [j])) var.Buffer.add(var.stringData [j]);
				    if (var.Buffer.size()>Settings.DiscretLimit) {
				    	var.setDiscret(false);
				    	break;  
				    }
				   
				}
				
				var.Buffer = Tools.sortBuffer(var.Buffer);
				
			}
			
			

		} catch (IOException e) {
			System.out.println("Wrong file format  " + e);
		}
		
	}
	
	
	
	
	public static void loadFromStringArray(String [] dataSet) {
		
		DataManager dataManager = DataManager.getDataManager();
	        
		
		

			dataManager.Variables = new Vector();

			String line = dataSet [0];
			
			MyStringTokenizer stk = new MyStringTokenizer(line);
			int col = 0;
			while (stk.hasMoreTokens()) {
				dataManager.Variables.add(new Variable(dataManager,stk.nextToken(),Variable.Double, col));
				col++;
			}

		

			for (int i = 0; i < dataManager.Variables.size(); i++) {
				Variable var = dataManager.Variables.elementAt(i);
				var.setColumn(new double[dataSet.length-1]);
				var.isNotNA = new boolean[dataSet.length-1];
				var.stringData = new String[dataSet.length-1];
			}
			
			
			
			int len = 0;
			
			
		  for (int k = 1; k < dataSet.length; k++) {

			    line = dataSet [k];
				    
				MyStringTokenizer st = new MyStringTokenizer(line,col);
				String token;
		
				for (int i = 0; i < col; i++) {
					token = st.nextToken();

					if ((dataManager.Variables.elementAt(i)).type == Variable.Double) {
						try {
							(dataManager.Variables.elementAt(i)).setValue(len,new Double(token).doubleValue());

							(dataManager.Variables.elementAt(i)).isNotNA[len] = true;
							if ((dataManager.Variables.elementAt(i)).getValue(len) > (dataManager.Variables.elementAt(i)).max) {
								(dataManager.Variables.elementAt(i)).max = (dataManager.Variables.elementAt(i)).getValue(len);
							}
							if ((dataManager.Variables.elementAt(i)).getValue(len) < (dataManager.Variables.elementAt(i)).min) {
								(dataManager.Variables.elementAt(i)).min = (dataManager.Variables.elementAt(i)).getValue(len);
							}

							if ((dataManager.Variables.elementAt(i)).getValue(len) > dataManager.maxValue) {
								dataManager.maxValue = (dataManager.Variables.elementAt(i)).getValue(len);
							}
							if ((dataManager.Variables.elementAt(i)).getValue(len) < dataManager.minValue) {
								dataManager.minValue = (dataManager.Variables.elementAt(i)).getValue(len);
							}

						} catch (Exception e) {
							if (token.equals("NA") || token.equals("")) {
								(dataManager.Variables.elementAt(i)).isNotNA[len] = false;
								(dataManager.Variables.elementAt(i)).setValue(len,dataManager.NA);
							} else {
								(dataManager.Variables.elementAt(i)).stringData[len] = token;
								(dataManager.Variables.elementAt(i)).type = Variable.String;
								(dataManager.Variables.elementAt(i)).isDiscret = true;
							}
						}
					} else {
						(dataManager.Variables.elementAt(i)).stringData[len] = token;
					}
				}

				len++;
			}

		System.out.println("len" + len);

			

			 dataManager.Cases = new Vector();
			 for (int i = 0; i < dataSet.length-1; i++) {
				 dataManager.Cases.add(new Case(dataManager.Variables.elementAt(0).stringData [i],i,dataManager));
				 
			 }
			

			for (int i = 0; i < dataManager.Variables.size(); i++) {
				
				Variable var = dataManager.Variables.elementAt(i);	    
				var.calculateMean();
				var.ID = i;
				var.Buffer = new Vector();
				
				for (int j = 0; j < var.stringData.length; j++) {
					if (Tools.doesntContain(var.Buffer, var.stringData [j])) var.Buffer.add(var.stringData [j]);
				    if (var.Buffer.size()>Settings.DiscretLimit) {
				    	var.setDiscret(false);
				    	break;  
				    }
				   
				}
				
				var.Buffer = Tools.sortBuffer(var.Buffer);
				
			}
			
			

		
		
		
		
	} 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
