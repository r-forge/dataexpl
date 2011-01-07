package Data;

import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import GUI.Container;
import GUI.Window;
import GUI.Barchart.Barchart;
import WindowManager.SelectionManager;
import DataSet.*;

public class SessionLoader {

	 DataManager dataManager;
	
	 public SessionLoader() {
		 dataManager = DataManager.getDataManager(); 
	 }
	
	

	public void saveData(JFrame frame)  {
		 FileDialog fileDialog = new FileDialog(frame, "Save Session", 1);

		 fileDialog.setVisible(true);
	       
		 DataManager dataManager = DataManager.getDataManager();
	        
	       
			try { 
		  
		    BufferedWriter bfr = null;
			bfr = new BufferedWriter(new FileWriter(fileDialog.getDirectory()+ "/" + fileDialog.getFile()));
			
			writeFile(bfr, false);
		    writeSelection(bfr,false);	
		    
		   
		    SelectionManager s = SelectionManager.getSelectionManager();
		    for (int i = 0; i < s.containers.size(); i++) {	   
		    writeContainer(bfr,s.containers.elementAt(i),false);
		    }
			
			
			
			bfr.close();
			
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			
	

	}
	
	
	
	
	

	public void openSession(String fileName,JFrame frame) {
		FileDialog fileDialog = null;
		
		if (fileName == null) {
			System.out.println("Open File");
			fileDialog = new FileDialog(frame, "Open File", 0);
			fileDialog.setVisible(true);
			DataManager.getDataManager().file = fileDialog.getDirectory() + "/" + fileDialog.getFile();

		}

		if (fileName != null || fileDialog.getFile() != null) {

			// MainPanel.removeAll();
			try {

				BufferedReader bfr = null;
				if (fileName == null) bfr = new BufferedReader(new FileReader(fileDialog.getDirectory() + "/" + fileDialog.getFile()));
				else bfr = new BufferedReader(new FileReader(new File(fileName)));
				
				loadSession(bfr, frame);


			} catch (Exception e) {
				System.out.println("Load Error " + e.getStackTrace());
				JOptionPane.showMessageDialog(frame,
					    "Wrong file format.",
					    "Load Error",
					    JOptionPane.ERROR_MESSAGE);
				
			}

		}

	}

	
	
	
	
	
	
	
	
	
	
	
	// Loads a session from file
	public void loadSession(BufferedReader bfr,JFrame frame) {
		

        DataManager dataManager = DataManager.getDataManager();
        
		try {
	
			// Load the file name
			String file = readFile(bfr);
			// Load the data
			if (file != null) DataLoader.loadFile(new BufferedReader(new FileReader(file)),file,null);
			
			// Load selected indexes
			Vector<Integer> selection = readSelection(bfr); 		
			
			// select cases
			for (int i = 0; i< selection.size(); i++) {
			     dataManager.Cases.elementAt(selection.elementAt(i)).select();
			}
			
			// read Containers
		
			while (true) {
				String line = bfr.readLine();
				while (line != null && !line.startsWith("<Container")) line=bfr.readLine();
				
				if (line !=null && line.startsWith("<Container")) readContainer(bfr,line);
				else break;
			}
			
			
			// show containers
			Vector<Container> conts = SelectionManager.getSelectionManager().containers;
			for (int i = 0;  i<conts.size(); i++) conts.elementAt(i).setVisible(true);
			
			
			

		} catch (Exception e) {
			System.out.println("Wrong file format  " + e.getStackTrace());
		}
		
		
	}
	
	
    public String readFile(BufferedReader bfr) throws IOException {
    	
    	String line = bfr.readLine();
		while (line.equals("")) line=bfr.readLine();
		
		
    	String file = null;
    	
    	
		if (line.startsWith("<File")) {
			file = bfr.readLine();
		    System.out.println(file);
		}
		bfr.readLine();
		return file;
	}
	
    public Vector<Integer> readSelection(BufferedReader bfr) throws IOException {
    	
    	
    	
		String line = bfr.readLine();
		while (line.equals("")) line=bfr.readLine();
		
		Vector<Integer> sel = new Vector();
		
		if (line.startsWith("<Selection")) {
			StringTokenizer str = new StringTokenizer(bfr.readLine(),"	");
			while (str.hasMoreTokens()) {
				sel.add(Integer.parseInt(str.nextToken()));
			}
		}
		bfr.readLine();
		
		return sel;
	}

    public void readContainer(BufferedReader bfr,String firstLine) throws IOException {
    	
    	System.out.println(">Read Container: " + firstLine);
		
		// Container Parameters are initialized with default values.
		int x=400,y=0,width=400,height=400;
		String name = "Container";
		
		
		StringTokenizer str = new StringTokenizer(firstLine,"	");
			
			while (str.hasMoreTokens()) {
				String token = str.nextToken();
				if (token.startsWith("x=")) x = Integer.parseInt(token.substring(2));
				if (token.startsWith("y=")) y = Integer.parseInt(token.substring(2));
				if (token.startsWith("width=")) width = Integer.parseInt(token.substring(6));
				if (token.startsWith("heigth=")) height = Integer.parseInt(token.substring(7));
			//	if (token.startsWith("name=")) name = token.substring(4);
			}
			
	
		
		Container cont  = new Container(name);
		
		cont.setBounds(x,y,width,height);
		
		String line = "";
		while (!(line = bfr.readLine()).startsWith("</Container")) {
			
		    if (line.startsWith("<Window")) readWindow(bfr,cont,line);
			
		}
		
		SelectionManager.getSelectionManager().addContainer(cont);
		
		
    }
    
	
    
    public void readWindow(BufferedReader bfr,Container cont,String firstLine) throws IOException {
    	
    	System.out.println(">Read Window: " + firstLine);
    	
    	
		// Container Parameters are initialized with default values.
		int x=400,y=0,width=400,height=400;
		String name = "";
		String type="";
		
		
	
			StringTokenizer str = new StringTokenizer(firstLine,"	");
			
			while (str.hasMoreTokens()) {
				String token = str.nextToken();
				if (token.startsWith("x=")) x = Integer.parseInt(token.substring(2));
				if (token.startsWith("y=")) y = Integer.parseInt(token.substring(2));
				if (token.startsWith("width=")) width = Integer.parseInt(token.substring(6));
				if (token.startsWith("heigth=")) height = Integer.parseInt(token.substring(7));
				if (token.startsWith("name=")) name = token.substring(5);
				if (token.startsWith("type=")) type = token.substring(5);
			//	if (token.startsWith("name=")) name = token.substring(4);
			}
			
			
		// Load Cases
			
		  Vector<Case> cases = dataManager.Cases;
			
		// Load Variables
			
		   Vector<Variable> vars = new Vector();
			
			String line = "";
			while (!(line = bfr.readLine()).startsWith("</Window")) {
				if (line.startsWith("<Variables")) {
					vars = readVariables(bfr,line);
				}
			}
			
		if (type.equals("Barchart")) {
			Barchart bar = new Barchart(cont,vars.firstElement(),cases);
			cont.add(bar,true);
			bar.x = x;
			bar.y = y;
			bar.width = width;
			bar.height = height;
			bar.name = name;
		}
    	
		
		
    }
    
    
    public Vector<Variable> readVariables(BufferedReader bfr, String firstLine) throws IOException {
    	  System.out.println(">Read Variables: " + firstLine);
    	
		   Vector<Variable> vars = new Vector();
		   String line = bfr.readLine();
		   System.out.println(line);	

			StringTokenizer str = new StringTokenizer(line,"	");
			while (str.hasMoreTokens()) {
				vars.add(dataManager.Variables.elementAt(Integer.parseInt(str.nextToken())));
			}
		   System.out.println(line);	
		   // skip the </Variables Line 	
		   bfr.readLine(); 
		   return vars;
    }
    
    
    
    
    public void writeFile(BufferedWriter bfr, boolean applet) throws IOException {
    	bfr.write("<File>\n"+dataManager.file+"\n</File>\n");
	}
	
	
	
	
	public void writeSelection(BufferedWriter bfr, boolean applet) throws IOException {
		if (applet) bfr.write("\"");		
		bfr.write("<Selection>");
		if (applet) bfr.write("\\n\"+");
		else bfr.write("\n");
		
		if (applet) bfr.write("\"");
		for (int i = 0; i < dataManager.Cases.size(); i++) {
			if (dataManager.Cases.elementAt(i).isSelected()) bfr.write(dataManager.Cases.elementAt(i).getID() + "	");
		}
		if (applet) bfr.write("\\n\"+");
		bfr.write("\n");

		bfr.write("\n");
		if (applet) bfr.write("\"");
		bfr.write("</Selection>");
		if (applet) bfr.write("\\n\"+");
		bfr.write("\n");

	}
		
  
    
    
    public void writeContainer(BufferedWriter bfr,Container c, boolean applet) throws IOException {
    	if (applet) bfr.write("\"");	
    	bfr.write("<Container	x="+c.getLocation().x+"	y="+c.getLocation().y+"	width=" +
		    		+c.getWidth()+"	heigth="+c.getHeight()+"	>");
    	if (applet) bfr.write("\\n\"+");
		bfr.write("\n");   
		    
		    
		    for (int i = 0; i< c.panel.windows.size(); i++) {
		    	Window w = c.panel.windows.elementAt(i);
		    	writeWindow(bfr,w,applet);
		    }  
		    
		    
		    bfr.write("\n");
			if (applet) bfr.write("\"");
			bfr.write("</Container>");
			if (applet) bfr.write("\\n\"");
			bfr.write("\n");;		
	}
    
    
    
    public void writeWindow(BufferedWriter bfr,Window w, boolean applet) throws IOException {
    	 if (applet) bfr.write("\"");
    	 bfr.write("<Window	name=" + w.name+  "	x="+w.x+"	y="+w.y+"	width=" +
	    		+w.width+"	heigth="+w.height+"	type="+w.getType()+"	>");
    	 if (applet) bfr.write("\\n\"+");
 		 bfr.write("\n");
	
	     writeVariables(bfr,w.getVariables(),applet);
	     writeCases(bfr,w.getCases(),applet);
	       
	     bfr.write("\n");
			if (applet) bfr.write("\"");
			bfr.write("</Window>");
			if (applet) bfr.write("\\n\"+");
			bfr.write("\n");;	
    
    }
    
    public void writeVariables(BufferedWriter bfr,Vector<Variable> vars, boolean applet) throws IOException {
    	  if (applet) bfr.write("\"");
    	  bfr.write("<Variables>");
    	  if (applet) bfr.write("\\n\"+");
  		  bfr.write("\n");
    	  
    	  
  		  if (applet) bfr.write("\"");
  		  for (int i = 0; i < vars.size(); i++) {
    		  bfr.write("	"+vars.elementAt(i).getID());
    	  }
  		  if (applet) bfr.write("\\n\"+");

    	  
  		  
    	    bfr.write("\n");
			if (applet) bfr.write("\"");
			bfr.write("</Variables>");
			if (applet) bfr.write("\\n\"+");
			bfr.write("\n");;
			
    }
    
    
    public void writeCases(BufferedWriter bfr,Vector<ISelectable> cases, boolean applet) throws IOException {
    	// bfr.write("<Cases>\n");
   	    // for (int i = 0; i < cases.size(); i++) {
   	    //	  bfr.write(cases.elementAt(i).getID()+"	");
   	    // }
   	    // bfr.write("\n</Cases>\n");
    }	 
    
    

   public void loadAppletSession() {
	   // Datensatz laden
	   DataLoader.loadFile(Data.data,null,null);
	   
	   // Session initialisieren
	   
	   try {
		   
	   
	    // Load selected indexes
		Vector<Integer> selection = readSelection(Data.bfr); 		
		
		// select cases
		for (int i = 0; i< selection.size(); i++) {
		     dataManager.Cases.elementAt(selection.elementAt(i)).select();
		}
		
		// read Containers
	
		while (true) {
			String line = Data.bfr.readLine();
			while (line != null && !line.startsWith("<Container")) line=Data.bfr.readLine();
			
			if (line !=null && line.startsWith("<Container")) readContainer(Data.bfr,line);
			else break;
		}
		
			
	   
	    }catch (Exception e ) {
	    	System.out.println(e.getStackTrace());
	    }
		
	   
   }
    
    
    public void saveSessionApplet(JFrame frame) {
    	FileDialog fileDialog = new FileDialog(frame, "Save Applet", 1);

		 fileDialog.setVisible(true);
	       
		 DataManager dataManager = DataManager.getDataManager();
	        
	       
			try { 
		  
		    BufferedWriter bfr =  new BufferedWriter(new FileWriter(fileDialog.getDirectory()+ "/" + "Data.java"));
			
			bfr.write("package DataSet;\n");

			bfr.write("import java.io.BufferedReader;\n");
			bfr.write("import java.io.StringReader;\n");

			bfr.write("public class Data {\n");
				
			bfr.write("public static BufferedReader bfr = new BufferedReader(new StringReader(\n");
						
			
			writeSelection(bfr,true);	
		    
		   
		    SelectionManager s = SelectionManager.getSelectionManager();	   
		    
		    writeContainer(bfr,s.getCurrentContainer(),true);
		   
		    bfr.write("));\n");

		    
			
		    bfr.write("public static BufferedReader data = new BufferedReader(new StringReader(\n");

		    writeFileInApplet(bfr);
		    
		    bfr.write("));}");
		    
		    
		    
		    
			
			bfr.close();
			
			        Runtime process = Runtime.getRuntime();
			        process.exec("javac " + fileDialog.getDirectory()+ "/" + "Data.java");
			       
			        
			       
		
					//Runtime.getRuntime().exec("cp " + fileDialog.getDirectory()+ "/" + fileDialog.getFile() + " "+ fileDialog.getDirectory()+ "/" + fileDialog.getFile()+"2");
					
			        process.exec("cp " + "DataExpl.jar" + " "+ fileDialog.getDirectory()+ "/" +fileDialog.getFile());
				    process.exec("mkdir "+fileDialog.getDirectory()+ "/DataSet");
				    Thread.sleep(100);

				    process.exec("mkdir "+fileDialog.getDirectory()+ "/DataSet/DataSet");

				    
				    Thread.sleep(2000);
				    
				    
				    process.exec("cp " + fileDialog.getDirectory()+ "/" + "Data.class"+ " "+  fileDialog.getDirectory()+ "/DataSet/DataSet/" + "Data.class");
				    
				    Thread.sleep(1000);

				    process.exec("rm " + fileDialog.getDirectory()+ "/" + "Data.java");
				    process.exec("rm " + fileDialog.getDirectory()+ "/" + "Data.class");		    
					
					process.exec("jar -vuf "+fileDialog.getDirectory()+ "/" +fileDialog.getFile()+" -C "+fileDialog.getDirectory()+ "/"+"DataSet/ /");
				    
					Thread.sleep(5000);
				    process.exec("rm -R "+fileDialog.getDirectory()+ "/DataSet");
					
			} 
			catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
    }
    
    
    
    public void writeFileInApplet(BufferedWriter bfr) throws IOException {
    	BufferedReader reader = new BufferedReader(new FileReader(dataManager.getDataManager().file));
    	String line;
    	while ((line = reader.readLine())!=null) {
    		bfr.write("\"");
    		bfr.write(line);
    		bfr.write("\\n\"+");
    	}
    	bfr.write("\"\"");
    	reader.close();
    }

	
	
}
