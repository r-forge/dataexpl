package GUI;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.util.Vector;

import javax.swing.JApplet;

import WindowManager.SelectionManager;

import Data.DataLoader;
import Data.DataManager;
import Data.Variable;
import DataSet.Data;
import GUI.Barchart.Barchart;

public class TestApplet extends JApplet{
Container container;
	
	
	
	
	public void init()  
	 { 
		System.out.println("Init Applet......");		  
	    DataManager dm = DataManager.getDataManager();

        if (container!=null) container.panel.removeAllWindows();
        container = null;
        	
		
		DataLoader.loadFile(Data.data,null,null);
		SelectionManager.getSelectionManager().deleteAllContainers();
		container = SelectionManager.getSelectionManager().getCurrentContainer();
    	container.panel.applet = this;
    	addMouseListener(container.panel);
   		addMouseMotionListener(container.panel);
    	
		
        for (int i = 0; i < dm.Variables.size(); i++) {
		        Variable var = dm.Variables.elementAt(i);	
		        if (var.type==Variable.String) {
		        	
		        Barchart bar = new Barchart(container, var, dm.Cases);
		        container.add(bar,false);
		        addMouseListener(bar);
	   			addMouseMotionListener(bar);
    			
	
		}

		}
       
        
	 }
	
	
	
	 
	 
	 
	 
	
      public void paint( Graphics g ) 
	  { 
    	  g.setColor(Color.WHITE);
    	  g.fillRect(0,0,this.getWidth(), this.getHeight());
    	
	    if (container != null) container.paint(g);
	  }




	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	} 

}


