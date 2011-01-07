package GUI.Barchart;

import java.util.*;
import javax.swing.*;

import Tools.Tools;
import WindowManager.SelectionManager;

import java.awt.event.*;
import java.awt.*;

import Data.Case;
import Data.ISelectable;
import Data.Variable;
import GUI.IData;
import GUI.IPlot;

public class Barchart extends GUI.Window implements IPlot{

	JMenuItem item = new JMenuItem("");
	
	String[] data;
	
	Barchart barchart;

	Image image;

	int abstandLinks = 80;
	
	int abstandRechts = 20;

	int abstandOben = 32;
	
	int yName = 15;

	int BarHeigth = 16;
	
	int BarSpace = 12;

	int abstandString = 5;

	boolean firstPaint = true;
	
	boolean labelResizing = false;
	
	boolean barResizing = false;
	
    boolean heightResizing = false;
	
	boolean spaceResizing = false;
	
	Bar resizingBar;
	


	Color GRAY = new Color(192, 192, 192);

	float colorShift = 0;

	JPopupMenu menu;

	/*Bars to the categories of given data*/
	Vector<Bar> bars;
	
	
    public Barchart(GUI.Container container, Variable var, Vector cases) {
		
		this.name = var.name;
		this.container = container;
		this.Cases = cases;
		this.data = var.getStringData(Cases);
		Variables = new Vector();
		Variables.add(var);
		
		if (IData.getIData().windowMenu != null) IData.getIData().windowMenu.add(item);
		
		
	}
	

	
		
		
	

	public int indexOf(String s) {
		for (int i = 0; i < bars.size(); i++) {
			if (bars.elementAt(i).name.equals(s))
				return i;
		}
		return -1;
	}

	

	
	public void addSelection(Point point1, Point point2) {
        		
		
		boolean selected = false;	

		for (int i = 0; i < bars.size(); i++) {
		
			
			if (Tools.containsRectInRect(

			abstandLinks+x,

			abstandOben+y + i * (BarHeigth+BarSpace),

			abstandLinks+ x+(int) Math.round((width - abstandLinks - abstandRechts)
							* bars.elementAt(i).barRel),

			abstandOben + y+i * (BarSpace+ BarHeigth) + BarHeigth,

			point1.x, point1.y, point2.x, point2.y))
		
			{
				selected = bars.elementAt(i).select() || selected;
			}

	
		}		
		
		
		

	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		super.mouseReleased(e);
		
		
		if (labelResizing) {
			labelResizing = false;
			resizingBar = null;
			return;
		}
		if (barResizing) {
			barResizing = false;
			resizingBar = null;
			return;
		}
		if (heightResizing) {
			heightResizing = false;
			resizingBar = null;
			return;
		}

		if (spaceResizing) {
			spaceResizing = false;
			resizingBar = null;
			return;
		}


		
	}

	public void mouseClicked(MouseEvent e) {
		
		super.mouseClicked(e);
		if (!isMouseIn(e)) return;
	
		
		if (e.getButton() == MouseEvent.BUTTON3 || e.isControlDown()) {

			menu = new JPopupMenu();
			JMenuItem item;

			JMenu sortMenu = new JMenu("Sort by ...");

			item = new JMenuItem("Count");

			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sortByCount();
				}
			});
			sortMenu.add(item);

			item = new JMenuItem("Absolute selected");

			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sortByAbsolutSelected();
				}
			});
			sortMenu.add(item);

			item = new JMenuItem("Relative selected");

			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sortByRelativeSelected();
				}
			});
			sortMenu.add(item);

			item = new JMenuItem("Lexicographic");

			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sortByLexico();
				}
			});
			sortMenu.add(item);

			sortMenu.addSeparator();

			item = new JMenuItem("Reverse");

			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sortByReverse();
				}
			});
			sortMenu.add(item);

			menu.add(sortMenu);
			
			
			
			
			
			menu.addSeparator();
			
			
			 item = new JMenuItem("Print");
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// createCorrelationGenes();
						
						
						
						print();
						
					}
				});
				menu.add(item);

			

			menu.show(container, e.getX(), e.getY());

		}
		

	}

	public void mouseDragged(MouseEvent e) {
		super.mouseDragged(e);
		if (!isMouseIn(e)) return;
		
		
		
		if (labelResizing) {
			this.abstandLinks = e.getX()-x;
			updateSelection();
			return;
		}
		
		if (barResizing) {
			
				if (resizingBar != null) {
				Bar bar = getMaxBar();	
				this.abstandRechts = width - abstandLinks - (e.getX()-x- abstandLinks)*bar.cases.size()/resizingBar.cases.size();
			    updateSelection();
			}
			return;
		}
		if (heightResizing) {
			
				if (resizingBar != null && e.getY()-y>=abstandOben && bars.indexOf(resizingBar)==0 ) {
				this.BarHeigth = (e.getY() - abstandOben-y);
			    updateSelection();
			}
			return;
		}
		if (spaceResizing) {
			
				if (resizingBar != null && bars.indexOf(resizingBar)!= 0) {
				this.BarSpace =  (e.getY()-y - abstandOben)/(bars.indexOf(resizingBar))-BarHeigth;
				updateSelection();
			}
				
				
				if (resizingBar != null && bars.indexOf(resizingBar)== 0) {
					abstandOben =  e.getY()-y;
					updateSelection();
				}
				
				
			return;
		}
		
		
	

	}
	
    public int getMaxBarWidth() {
    	int max = 0;
    	for (int i = 0; i < bars.size(); i++) {
    		Bar bar = bars.elementAt(i);
    		if (bar.barWidth>max) max = bar.barWidth;
    		
    	}
    	
    	return max;
    }	
    
    
    
    
    public Bar getMaxBar() {
    	int max = 0;
    	int j = 0;
    	for (int i = 0; i < bars.size(); i++) {
    		Bar bar = bars.elementAt(i);
    		if (bar.barWidth>max){
    			max = bar.barWidth;
    			j = i;
    		}
    		
    	}
    	
    	return bars.elementAt(j);
    }	
    
    
    
    
    
    public String getType() {
    	return "Barchart";
    }
    
    
    
    
    public String getVaribles() {
    	return name;
    }
    
    
    
    
    public String getToolTip(MouseEvent e) {
    	if (!isMouseIn(e)) return null;
    	
    //	if (e.isControlDown()) 
    	{
			Bar bar =  this.getBar(e);

			if (bar != null) {
				String s = bar.name;
				int selectedCount = 0, gesamtExperInBalken = 0;

				for (int j = 0; j < this.data.length; j++) {
					if (this.data[j].equals(s)) {
						if (getVariable(j) != null
								&& getVariable(j).isSelected())
							selectedCount++;
						gesamtExperInBalken++;
					}
				}

				double koeff = (double) selectedCount / gesamtExperInBalken;

				return "<HTML><BODY BGCOLOR = 'WHITE'><FONT FACE = 'Verdana'><STRONG>"
						+ bar.name
						+ "<FONT FACE = 'Arial'><br>"
						+ "</STRONG>"
						+ (int) Math.round(bar.barAbs*koeff)
						+ "/"
						+ bar.barAbs + "	" + "(" + Tools.round100(koeff * 100) + "%)</HTML>";

			}
		}
		return null;
    	
    }
    
    
    
	
    
    public boolean isWindowDragged(MouseEvent e) {
    	
    	if (abstandLinks+x<= e.getX() && e.getX() <= abstandLinks+x + Tools.getStringSpace(name,container.getGraphics())+3*border && y <= e.getY() && e.getY() <= y+ yName) return true;
    	return false;
    	
    }
	
    
    
	

	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		if (!isMouseIn(e)) return;
		
		if (isLabelResizing(e)) {
			labelResizing = true;
			resizingBar = getBar(e);
			return;
		}
		if (isBarWidthResizing(e)) {
			barResizing = true;
			resizingBar = getBar(e);
			return;
		}
		if (isHeightResizing(e)) {
			heightResizing = true;
			resizingBar = getBar(e);
			return;
		}
		if (isSpaceResizing(e)) {
			spaceResizing = true;
			resizingBar = getBar(e);
			return;
		}
		
	}
	
	public boolean isResized(MouseEvent e) {
		if (isLabelResizing(e)||isBarWidthResizing(e)|| isHeightResizing(e)||isSpaceResizing(e)) return true;
		else return false;
	}

	
	
	
	public void keyPressed(KeyEvent e) {
	}

	
	
	
	public void keyTyped(KeyEvent e) {

	}

	
	
	
	public void keyReleased(KeyEvent e) {

	}

	
	
	public void calculateBalken() {

		bars = new Vector();

		Vector<String> barStrings = new Vector();

		for (int i = 0; i < data.length; i++) {
		
			boolean add = true;
			for (int j = 0; j < barStrings.size(); j++) {
				if (barStrings.elementAt(j).equals(data[i])) {
					add = false;
					bars.elementAt(j).cases.add(getVariable(i));
				}
			}
			if (add) {
				barStrings.add(data[i]);
				bars.add(new Bar(this, data [i]));
				bars.lastElement().cases.add(getVariable(i));

			}
		}



		for (int i = 0; i < bars.size(); i++) {
			Bar bar = bars.elementAt(i);
			bar.barRel = bar.cases.size();
			bar.barAbs = bar.cases.size();
			bars.elementAt(i).color = GRAY;
		}
		

		double max = 0;
		for (int i = 0; i < bars.size(); i++) {
			if (max < bars.elementAt(i).barRel) max = bars.elementAt(i).barRel;
		}

		for (int i = 0; i < bars.size(); i++) {
		
			bars.elementAt(i).barRel /= max;
		  
		}
		
		
        sortByLexico();
        updateSelection();
	}

	
	
	
	
	public void calculateAbstandLinks() {
		this.abstandLinks =  90;
		
		int length = 0;
		
		for (int i = 0; i < this.bars.size(); i++) {
			String s = this.bars.elementAt(i).name;
			int Width = 0;
		
			for (int j = 0; j < s.length(); j++)
			{
				if (container.getGraphics() != null) Width += container.getGraphics().getFontMetrics().charWidth(s.charAt(j));
			    else Width += 4; 
			}

			if (length < Width)
				length = Width;

		}

		this.abstandLinks = Math.min(length + 12, 80);
	}

	
	
	
	
	public void updateSelection() {

		if (bars == null) calculateBalken();
		for (int i = 0; i < bars.size(); i++) {

			Bar bar = bars.elementAt(i);
            bar.nameShort = Tools.cutLabels(bar.name, 
            		abstandLinks- abstandString - 3, container.getGraphics());
			int selectedCount = 0;

			
			for (int j = 0; j < bar.cases.size(); j++) {
				if (bar.cases.elementAt(j).isSelected()) selectedCount++;
			}

			bar.koeff = (double) selectedCount / bar.cases.size();
			bar.selectedCount = selectedCount;
			bar.barWidth = (int) Math.round((width - abstandLinks - abstandRechts)* bar.barRel);
			bar.barSelectedWidth = (int) Math.round((width - abstandLinks - abstandRechts)* bar.barRel * bar.koeff);

			if (   (width - abstandLinks - abstandRechts) * bar.barRel * bar.koeff > 0
				&& (width - abstandLinks - abstandRechts) * bar.barRel * bar.koeff < 1) 
				bar.barSelectedWidth = 1;

		}


	}
	
	
	
	
	
	
	

	public void sortByCount() {
		Vector<Bar> balkenTemp = new Vector();
		for (int i = 0; i < bars.size(); i++) {
			Bar bar = bars.elementAt(i);
			int j = 0;
			while (j < balkenTemp.size()
					&& bar.barAbs < balkenTemp.elementAt(j).barAbs) {
				j++;
			}
			balkenTemp.insertElementAt(bar, j);

		}

		this.bars = balkenTemp;
		repaint();
	}

	
	
	
	public void sortByAbsolutSelected() {
		Vector<Bar> balkenTemp = new Vector();
		for (int i = 0; i < bars.size(); i++) {
			Bar bar = bars.elementAt(i);
			int j = 0;
			while (j < balkenTemp.size()
					&& bar.selectedCount < balkenTemp.elementAt(j).selectedCount) {
				j++;
			}
			balkenTemp.insertElementAt(bar, j);

		}

		this.bars = balkenTemp;
		repaint();
	}
	
	
	

	public void sortByRelativeSelected() {
		Vector<Bar> balkenTemp = new Vector();
		for (int i = 0; i < bars.size(); i++) {
			Bar bar = bars.elementAt(i);
			int j = 0;
			while (j < balkenTemp.size() && 
					(double) bar.selectedCount / bar.barAbs < (double) balkenTemp.elementAt(j).selectedCount
					/ balkenTemp.elementAt(j).barAbs) {
				j++;
			}
			balkenTemp.insertElementAt(bar, j);

		}

		this.bars = balkenTemp;
		repaint();
	}

	
	
	
	
	public void sortByReverse() {
		Vector<Bar> balkenTemp = new Vector();
		for (int i = 0; i < bars.size(); i++) {
			Bar bar = bars.elementAt(i);
			balkenTemp.insertElementAt(bar, 0);

		}

		this.bars = balkenTemp;
		repaint();
	}

	
	
	
	public void sortByLexico() {
		
		

		Vector<Bar> balkenTemp = new Vector();
		for (int i = 0; i < bars.size(); i++) {
			Bar bar = bars.elementAt(i);
			int j = 0;
			while (j < balkenTemp.size()
					&& compareLexico(bar.name, balkenTemp.elementAt(j).name)) {
				j++;
			}
			balkenTemp.insertElementAt(bar, j);

		}

		this.bars = balkenTemp;
		repaint();
	}
	
	
	
	
	
	
    public void sortCHR() {
		

	
		for (int i = 0; i < bars.size(); i++) {
			for (int j = 0; j < bars.size(); j++) {
			
			Bar bar1 = bars.elementAt(i);
			Bar bar2 = bars.elementAt(j);
		
			if ( compareCHR(bar1.name, bar2.name)) {
				if (i<j) {
					bars.set(j,bar1);
					bars.set(i,bar2);
				}
			}
			}
		}

		

		
		
		
		repaint();
	}
	



    
    

public boolean compareCHR(String a, String b) {
	int i = 0;

	String tA = a.replace("\"","");
	String tB = b.replace("\"","");
	if (tA.equals("X") || tA.equals("x")) tA = "23";
	if (tB.equals("X") || tB.equals("x")) tB = "23";

	if (tA.equals("Y") || tA.equals("y")) tA = "24";
	if (tB.equals("Y") || tB.equals("y")) tB = "24";
	
	if (tA.equals("NA")) return true;
	if (tB.equals("NA")) return false;
	
	
	int aa = Integer.parseInt(tA);
	int bb = Integer.parseInt(tB);
	
	if (aa < bb) return false;
	return true;
}



	

	public boolean compareLexico(String a, String b) {
		int i = 0;

		while (a.length() > i && b.length() > i) {
			if (a.charAt(i) > b.charAt(i))
				return true;
			if (a.charAt(i) < b.charAt(i))
				return false;
			i++;

		}

		return false;
	}
	

	
	
	
	

	public void paint(Graphics g) {
        super.paint(g);
       // Graphics2D g = (Graphics2D) image.getGraphics();
	
		if (this.bars == null) {
			calculateBalken();
			calculateAbstandLinks();		
		}
		
		
		
		if (g == null) return;

		
		 g.setColor(Color.BLACK);
		 g.drawString(name, abstandLinks+x+2*border, y+yName);
		 g.setColor(Color.GRAY);
		 g.drawRect(abstandLinks+x,y, Tools.getStringSpace(name,g)+3*border,yName+2*border);
		 
		 
		
		for (int i = 0; i < bars.size(); i++) {

			g.setColor(Color.BLACK);
			
			Bar bar = bars.elementAt(i);

			if (bar.nameShort == null) bar.nameShort = bar.name;
			g.drawString(bar.nameShort,

			x+abstandLinks - Tools.getStringSpace(bar.nameShort,g)-5,
			y+abstandOben + i * (BarSpace+ BarHeigth) + BarHeigth * 1 / 2 + 5

			);

			g.setColor(bar.color);

			g.fillRect(x+abstandLinks, y+abstandOben + i *(BarSpace+ BarHeigth), bar.barWidth, BarHeigth);

			g.setColor(Color.RED);

			g.fillRect(x+abstandLinks,y+ abstandOben + i * (BarSpace+ BarHeigth), bar.barSelectedWidth, BarHeigth);

			g.setColor(Color.BLACK);

			g.drawRect( x+abstandLinks, y+abstandOben + i * (BarSpace+ BarHeigth), bar.barWidth, BarHeigth);

			if (bar.barSelectedWidth == 1) {
				g.setColor(Color.RED);
				g.fillRect(x+ abstandLinks,y+ abstandOben + i * (BarSpace+ BarHeigth), bar.barSelectedWidth, BarHeigth);
			}

		}

	

	}

	public Bar getBar(MouseEvent e) {
		int xx = e.getX()-x;
		int yy = e.getY()-y;

		int i = (yy - abstandOben) / (BarSpace + BarHeigth);

		if (xx >= abstandLinks-2 && xx <= width - abstandRechts+2 &&
			yy >= abstandOben-2 &&  yy <= abstandOben + BarHeigth  + i * (BarSpace+BarHeigth)  	
		)
		{
			if (i >= bars.size()) return null;
		    return bars.elementAt(i);
		}
		return null;

	}
	
	public boolean isLabelResizing(MouseEvent e) {
		if ((e.getX() == abstandLinks+x || e.getX() == abstandLinks+x-1|| e.getX() == abstandLinks+x+2) && 
			e.getY()>=abstandOben+y &&	(e.getY() - abstandOben-y)%(BarHeigth + BarSpace)<BarHeigth) {
			
			return true;
		}
		else return false;
	}
	
	
	
	
	
	public boolean isBarWidthResizing(MouseEvent e) {
		
		Bar bar = getBar(e);
		
		if (bar != null && 
			(abstandLinks+x + bar.barWidth == e.getX() || abstandLinks +x+ bar.barWidth == e.getX()-1||abstandLinks +x+ bar.barWidth == e.getX()-1)
				&& e.getY()>=abstandOben+y &&
				(e.getY() - abstandOben-y)%(BarHeigth + BarSpace)<BarHeigth) {
		
			return true;
		}
		else return false;
	}
	
	
    public boolean isHeightResizing(MouseEvent e) {
		
		Bar bar = getBar(e);
		
		if (bar != null && bars.indexOf(bar) == 0
				&& e.getY()>=abstandOben+y &&
				((e.getY() - abstandOben-y)%(BarHeigth + BarSpace)==BarHeigth || (e.getY() - abstandOben-y)%(BarHeigth + BarSpace)==BarHeigth-1 ||(e.getY() - abstandOben-y)%(BarHeigth + BarSpace)==BarHeigth+1)) {
			//System.out.println("Treffer");
			return true;
		}
		else return false;
	}
    
    
    public boolean isSpaceResizing(MouseEvent e) {
		
		Bar bar = getBar(e);
		
		if (bar != null
				&& e.getY()>=abstandOben+y &&
				(e.getY() - abstandOben-y)%(BarHeigth + BarSpace)==0 || (e.getY() - abstandOben-y)%(BarHeigth + BarSpace)==BarHeigth + BarSpace-1 ||(e.getY() - abstandOben-y)%(BarHeigth + BarSpace)==+1) {
			//System.out.println("Treffer");
			return true;
		}
		else return false;
	}

	
	
	
	
	public ISelectable getVariable(int i) {
		if (Cases == null)
			return null;
		return Cases.elementAt(i);
	}
	

	
	
	

	
	public void mouseMoved(MouseEvent arg0) {
		//System.out.println("Moved");
		super.mouseMoved(arg0);
		if (!isMouseIn(arg0)) return;

		// TODO Auto-generated method stub
		
        if (isLabelResizing(arg0) || isBarWidthResizing(arg0)) {
        	container.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR) );
        	return;
        }
  
        
        if (isHeightResizing(arg0) || isSpaceResizing(arg0)) {
        	container.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR) );
        	return;
        }
       
      //  container.setCursor(new Cursor(Cursor.DEFAULT_CURSOR ) );
             
        
	}

	
	
	class Bar {
		Barchart panel;
		String name;
		double koeff;
		int barWidth;
		double barRel;
		double selectedBalken;
		int barSelectedWidth;
		int barAbs;
		String nameShort;
		Color color;
		int selectedCount;
		Vector<ISelectable> cases = new Vector();

		public Bar(Barchart panel, String name) {
			this.panel = panel;
			this.name = name;
		}
		
		public boolean select() {
			
			boolean selected = false;
			for (int i = 0; i < cases.size(); i++) {
				cases.elementAt(i).select();
				selected = true;
			}
			return selected;
		}
		

	}

	

	public void removeColoring() {
		// TODO Auto-generated method stub
		
	}














	public void print() {
		// TODO Auto-generated method stub
		
	}

}
