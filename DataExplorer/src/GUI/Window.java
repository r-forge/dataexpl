package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Window implements MouseListener,MouseMotionListener{
      public int x=20,y=50, width=300,height= 200;
	  public String name;
	  boolean isMouseIn = false;
	 Graphics graphics;
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
		  if (g == null) return;
		  g.setColor(Color.WHITE);
		  g.fillRect(x,y,width,height);
		  graphics = g;
		  g.setColor(Color.BLACK);
		  g.drawString(name, width/2-30, 0);
		  
		  
		  if (isMouseIn) {
			  g.drawRect(x+1,y+1,width-2,height-2);
		  }
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
		
		
		if (isMouseIn(e)) {
			isMouseIn =  true;
			if (e.getClickCount() == 1 ) {
				
				zX = x-e.getX();
				zY = y-e.getY();
				
				if (zX == 0 || zY == 0 || zX == -1 || zY == -1 ||zX == 1 || zY == 1 ) {
					container.setCursor(new Cursor(Cursor.HAND_CURSOR ) );
					ziehen = true;
				}
			}
		}
		
	}


	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		container.setCursor(new Cursor(Cursor.DEFAULT_CURSOR ) );
		ziehen = false;
	}


	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		isMouseIn =  isMouseIn(e);
		
		if (ziehen) {
			
		    isMouseIn = true;
			x = e.getX() + zX;
			y = e.getY() + zY;
		}
		
		
		repaint();
		container.repaint();
		
		
		
	}


	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		isMouseIn =  isMouseIn(e);
		
		
		
		zX = x-e.getX();
		zY = y-e.getY();
		
		if (isMouseIn && (zX == 0 || zY == 0 || zX == -1 || zY == -1 ||zX == 1 || zY == 1 )) {
			container.setCursor(new Cursor(Cursor.HAND_CURSOR ) );
			ziehen = true;
		}
		else {
			container.setCursor(new Cursor(Cursor.DEFAULT_CURSOR ) );
			ziehen = false;
		}
		
		
		
	
		
		
		repaint();
		container.repaint();
		
	}
	 
	
	public void repaint() {
		paint(graphics);
	}
	
	boolean isMouseIn(MouseEvent e) {
		if (x<=e.getX() && x+width>=e.getX() && y+height>=e.getY() && y<=e.getY()) return true;
		else return false;
	}
	
	
	
	
	  
	  
}
