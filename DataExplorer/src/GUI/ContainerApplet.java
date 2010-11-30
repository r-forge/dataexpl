package GUI;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.util.Vector;

import javax.swing.JApplet;

import Data.DataLoader;
import Data.DataManager;
import Data.Variable;
import GUI.Barchart.Barchart;

public class ContainerApplet extends JApplet{
	
	Container container;
	
	
	
	
	public void init()  
	 { 
		System.out.println("init");
		
		//String file = getParameter("File");
	    DataManager dm = DataManager.getDataManager();

	    //dm.openFile("Cars2004.txt",null);
	    dateiLaden();
		
		
		if (container == null) {
    		container = new Container();  
    		container.applet = this;
    		
    		for (int i = 0; i < container.windows.size(); i++) {
    			Window w = container.windows.elementAt(i);
    			addMouseListener(w);
    			addMouseMotionListener(w);
    		}
    		
    	}
		
		
		
        for (int i = 0; i < dm.variables.size(); i++) {
		        Variable var = dm.variables.elementAt(i);	
		        if (var.type==Variable.String) {
		        	
		        Barchart bar = new Barchart(container, var.name, dm.Genes, var.getStringData());
		        container.windows.add(bar);
		        addMouseListener(bar);
    			addMouseMotionListener(bar);
    			bar.x = container.getWidth()*i/4;
    			bar.y = container.getHeight()*i/4;
	
		}

		}
		
		
		
	  
	 }
	
	
	 
	 
	 
	 
	
      public void paint( Graphics g ) 
	  { 
    	  g.setColor(Color.WHITE);
    	  g.fillRect(0,0,this.getWidth(), this.getHeight());
    	
	    if (container != null) container.paint(g);
	  }



	

      
   // Datei vom Server laden
  	public void dateiLaden() {
  	// Dateinamen aus der OberflŠche holen
  	java.lang.String strDatei = new String(getParameter("File"));

  	// Plattformspezifisches Line-Separator-Zeichen ermitteln
    //	java.lang.String strLs = System.getProperty("line.separator");

  	// Stream fŸr das Einlesen der Daten anlegen
  	java.io.InputStream file = null;

  	// Objekt fŸr die Server-URL
  	java.net.URL url = null;

  	// Streams šffnen und URL des Servers bestimmen
  	try {
    url = new java.net.URL(strDatei);
  	//	url = new java.net.URL(this.getDocumentBase(),strDatei); //$NON-NLS-1$
  	file = url.openStream();
  	} catch (java.net.MalformedURLException mue) {
  	System.out.println("Fehlerhafte URL-Adresse / Datei");
  	} catch( java.io.IOException e ) {
  	System.out.println("IOException beim Initialisieren der Verbindung zum Server");
  	}

  	// BufferedReader Ÿber den InputStream legen
  	java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(file));

  	DataLoader.loadGeneExpressions(DataManager.getDataManager(),br,null,null);
		
  	
  }
  	
  	
  	
  	


	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	} 

}
