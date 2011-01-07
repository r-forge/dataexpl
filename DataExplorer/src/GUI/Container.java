package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

import WindowManager.SelectionManager;

public class Container extends JFrame{
	
	public ContainerPanel panel;
	
	public Container(String name) {
	    super(name);
		this.setBounds(400,0,800,800);
		
		panel = new ContainerPanel(this);
		this.getContentPane().setLayout(new  BorderLayout());
		this.getContentPane().add(panel,BorderLayout.CENTER);
		
		
	}
	
	
	public void add(Window w, boolean reorder) {
		panel.add(w,reorder);
	}
	
	public Vector<Window> getWindowVector() {
		return panel.windows;
	}
	
	
	
public class ContainerPanel extends JPanel implements MouseMotionListener, MouseListener{
	
	public Vector<Window> windows = new Vector();
 	public JApplet applet;
	Point point1, point2;
	public Container container;

    public ContainerPanel(Container cont) {
    	container = cont;
    	this.addMouseMotionListener(this);
		this.addMouseListener(this);
		windows = new Vector();	
		

		ToolTipManager.sharedInstance().registerComponent(this);
		ToolTipManager.sharedInstance().setInitialDelay(500);
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		ToolTipManager.sharedInstance().setReshowDelay(500);
		
    }

	
	
	public void removeAllWindows() {
		windows = new Vector();
		
	}
	
	public void updateSelection() {
		for (int i = 0; i < windows.size(); i++) {
			windows.elementAt(i).updateSelection();
		}
		repaint();
	}
	
	
	public void paint(Graphics g) {
		
		if (g == null) return;
		
		g.setColor(Color.WHITE);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		
		for (int i = 0; i < windows.size(); i++) {
			Window window = windows.elementAt(i);
			window.paint(g);
		}
		
		
		
	
		
		if (point1 != null && point2 != null) {
			g.setColor(Color.BLACK);
			g.drawRect(Math.min(point1.x, point2.x), Math.min(point1.y,
					point2.y), Math.abs(point2.x - point1.x), Math.abs(point2.y
					- point1.y));
		}
		
		if (applet != null) applet.repaint();
		
		
		
	}

	
	public void repaint() {
		super.repaint();
		if (windows == null) return; 
		for (int i = 0;  i < windows.size(); i++) {
			windows.elementAt(i).repaint();
		}
		if (applet != null) applet.repaint();

	}
	
	
	public void add(Window w, boolean reorder) {
	    	windows.add(w);
	        addMouseListener(w);
	        addMouseMotionListener(w);
	        
	        
	        for (int i = 0; i< windows.size(); i++) {
		        int n = (int)Math.sqrt(windows.size());
		        if (n*n<windows.size()) n++;
		        
		        w.x = (container.getWidth()/n)*(i%n)+50;
		        w.y = (container.getHeight()/n)*(i/n)+50;
		  
	            w.width = container.getWidth()/n/2;
	            w.height = container.getHeight()/n/2;    
	       }     
	    
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	    point2 = e.getPoint();

	}


	public void mouseMoved(MouseEvent e) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR ) );		

	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		boolean windowDragged = false;
		
		for (int i = 0; i< windows.size(); i++) {
			   windowDragged =  windows.elementAt(i).isWindowDragged(e) || windows.elementAt(i).isResized(e) || windowDragged;
		}
		
		if (!windowDragged) point1 = e.getPoint();

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
        point2 = e.getPoint();
		
		if (!(e.getButton() == MouseEvent.BUTTON3 || e.isControlDown())) {

			if (point1 != null && point2 != null) {
				if (!e.isShiftDown()) SelectionManager.getSelectionManager().deleteSelection();
				for (int i = 0; i< windows.size(); i++) {
				   windows.elementAt(i).addSelection(point1, point2);
				}
			}

			point1 = null;
			point2 = null;
			
			
			SelectionManager.getSelectionManager().repaintWindows();
		}
		
	}
	
	
	public String getToolTipText(MouseEvent e) {
		
		for (int i = 0; i< windows.size(); i++) {
			  if (windows.elementAt(i).isMouseIn(e)) return windows.elementAt(i).getToolTip(e);
		}
		return null;
	}
	

}
}
