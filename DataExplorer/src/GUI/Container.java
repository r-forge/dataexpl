package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JFrame;

import WindowManager.SelectionManager;

public class Container extends JFrame implements MouseMotionListener{
	
	public Vector<Window> windows = new Vector();
	
	public JApplet applet;
	
	public Container() {
	    super("Container");
		this.setBounds(400,0,800,600);
		this.addMouseMotionListener(this);
		windows = new Vector();
	}
	
	public void updateSelection() {
		
	}
	
	
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		
		for (int i = 0; i < windows.size(); i++) {
			Window window = windows.elementAt(i);
			window.paint(g);
		}
		
		if (applet != null) applet.repaint();
	}

	
	public void repaint() {
		super.repaint();
		for (int i = 0;  i < windows.size(); i++) {
			windows.elementAt(i).repaint();
		}
	}
	
	
	public void add(Window w, boolean reorder) {
		windows.add(w);
	   
	      
	        addMouseListener(w);
	        addMouseMotionListener(w);
	        
	   for (int i = 0; i< windows.size(); i++) {
		   int n = (int)Math.sqrt(windows.size())+1;
		   
		   w.x = this.getWidth()*i%n+25;
		   w.y = this.getHeight()*i/n+25;
		  
	        w.width = this.getWidth()/n/2;
	        w.height = this.getHeight()/n/2;    
	   }     
	    
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void mouseMoved(MouseEvent e) {
		
	}
	

}
