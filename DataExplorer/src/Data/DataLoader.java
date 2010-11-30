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
	
	
	

    
	
	public static void loadGeneExpressions(DataManager dataManager,BufferedReader bfr, String fileName,FileDialog fileDialog) {
		


        
		try {

			dataManager.variables = new Vector();

			String line = bfr.readLine();

			Vector<String> data = new Vector();
			
			MyStringTokenizer stk = new MyStringTokenizer(line);
			int col = 0;
			while (stk.hasMoreTokens()) {
				dataManager.variables.add(new Variable(dataManager,stk.nextToken(),Variable.Double, col));
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

			for (int i = 0; i < dataManager.variables.size(); i++) {
				Variable var = dataManager.variables.elementAt(i);
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

					if ((dataManager.variables.elementAt(i)).type == Variable.Double) {
						try {
							(dataManager.variables.elementAt(i)).setValue(len,new Double(token).doubleValue());

							(dataManager.variables.elementAt(i)).isNotNA[len] = true;
							if ((dataManager.variables.elementAt(i)).getValue(len) > (dataManager.variables.elementAt(i)).max) {
								(dataManager.variables.elementAt(i)).max = (dataManager.variables.elementAt(i)).getValue(len);
							}
							if ((dataManager.variables.elementAt(i)).getValue(len) < (dataManager.variables.elementAt(i)).min) {
								(dataManager.variables.elementAt(i)).min = (dataManager.variables.elementAt(i)).getValue(len);
							}

							if ((dataManager.variables.elementAt(i)).getValue(len) > dataManager.maxValue) {
								dataManager.maxValue = (dataManager.variables.elementAt(i)).getValue(len);
							}
							if ((dataManager.variables.elementAt(i)).getValue(len) < dataManager.minValue) {
								dataManager.minValue = (dataManager.variables.elementAt(i)).getValue(len);
							}

						} catch (Exception e) {
							if (token.equals("NA") || token.equals("")) {
								(dataManager.variables.elementAt(i)).isNotNA[len] = false;
								(dataManager.variables.elementAt(i)).setValue(len,dataManager.NA);
							} else {
								(dataManager. variables.elementAt(i)).stringData[len] = token;
								(dataManager.variables.elementAt(i)).type = Variable.String;
								(dataManager.variables.elementAt(i)).isDiscret = true;
							}
						}
					} else {
						(dataManager.variables.elementAt(i)).stringData[len] = token;
					}
				}

				len++;
			}

		System.out.println("len" + len);

			//dataManager.selectedRows = new boolean[dataManager.RowCount];
			//dataManager.selectedVariables = new boolean[dataManager.variables.size() - 2];

			 dataManager.Genes = new Vector();
			 for (int i = 0; i < length; i++) {
				 dataManager.Genes.add(new Case(dataManager.variables.elementAt(0).stringData [i],i,dataManager));
				 
			 }
			

			for (int i = 0; i < dataManager.variables.size(); i++) {
				
				Variable var = dataManager.variables.elementAt(i);	    
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
	
	
	
	
	
	
	
	
	
	
	
	
}
