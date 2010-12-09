package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.ToolTipManager;

public class Window implements MouseListener,MouseMotionListener{
      public int x=20,y=50, width=300,height= 200;
	  public String name;
	  boolean isMouseIn = false;
	  public int border = 3;

	//  Image image;
      public Container container;
      boolean ziehen = false;
      int zX,zY;
      
      
      
      public Window() {
    	  
      }
      
	  
	  
	  public Window(String name,Container container) {
    	  this.name = name;
    	  this.container = container;
    	 
      }
	  
	  
	  public void paint(Graphics g) {
		 
		//  if (image == null) image = container.createImage(width,height);
		//  paintImage(image);
		  
		//  Graphics2D g2 = (Graphics2D) image.getGraphics();
			
		//  g.setRenderingHint( RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		    
		  g.setColor(Color.WHITE);
		  g.fillRect(x,y,width,height);
		  
		 // if (isMouseIn) paintBorder(g);
		  
		  
	  }
	  
	  
	  public boolean isWindowDragged(MouseEvent e) {
		  return false;
	  }
	  
	  
	  
	  public  void paintBorder(Graphics g2) {
		
		  g2.setColor(Color.GRAY);
		  g2.drawRect(0,0,width-1,height-1);
		  g2.fillRect(0,0,border+1,border+1);
		  g2.fillRect(width-border-1,height-border-1,border+1,border+1);
		  g2.fillRect(width-border-1,0,border+1,border+1);
		  g2.fillRect(0,height-border-1,border+1,border+1);

	  }
	  
	  
	  
	  

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		isMouseIn =  isMouseIn(e);
		
	}


	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		isMouseIn = isMouseIn(e);
		if (isMouseIn) {

			if (e.getClickCount() == 1 ) {
				
				zX = e.getX()-x;
				zY = e.getY()-y;
				
				if (isWindowDragged(e)) {

					container.setCursor(new Cursor(Cursor.HAND_CURSOR ) );
					ziehen = true;
				}
			}
		}
		

	}
	
	
	public Point getPoint(MouseEvent e) {
		return new Point(e.getX(),e.getY());
	}


	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		isMouseIn =  isMouseIn(e);
		container.setCursor(new Cursor(Cursor.DEFAULT_CURSOR ) );
		ziehen = false;
	}


	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		isMouseIn =  isMouseIn(e);
		
		if (ziehen) {
			
		    isMouseIn = true;
			x = e.getX() - zX;
			y = e.getY() - zY;
		}
		
		
		repaint();
		container.repaint();
		
		
		
	}
	
	public void addSelection(Point p1, Point p2) {}

	
	public void updateSelection() {}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		isMouseIn = isMouseIn(e);
		
		
		if (isMouseIn) {
		
		   zX = e.getX()-x;
		   zY = e.getY()-y;
		
		   if (isWindowDragged(e)) {
			    container.setCursor(new Cursor(Cursor.HAND_CURSOR ) );
           }
		   else {
		    	container.setCursor(new Cursor(Cursor.DEFAULT_CURSOR ) );		

		   }
	
		  // repaint();
		  // container.repaint();
		}
		
	}
	
	public boolean isResized(MouseEvent e) {
		return false;
	}
	
	
	public void repaint() {
		//if (image != null) paint((Graphics2D)image.getGraphics());
		if (container.getGraphics()!=null) paint(container.getGraphics());
	}
	 
	
	protected boolean isMouseIn(MouseEvent e) {
		if (x<=e.getX() && x+width>=e.getX() && y+height>=e.getY() && y<=e.getY()) return true;
		else return false;
	}
	
	
	
	
	  
	  
}
