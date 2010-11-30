package GUI;


import java.util.*;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

import Data.*;

class GlobalViewAbstract extends JFrame implements MatrixWindow, IPlot {
	int pixelSize = 2;

	Seurat seurat;

	GlobalViewAbstractPanel gPanel;

	// int[] orderZeilen;

	// int[] orderSpalten;

	JMenuItem item = new JMenuItem("Clustering");

	GlobalViewAbstract globalView = this;

	String methodColumns, methodRows;

	String distanceColumns, distanceRows;

	DataManager dataManager;

	Vector<ISelectable> Experiments;
	Vector<ISelectable> Genes;
	
	JLabel infoLabel;
	
	boolean resize = true;
	
	
	JPanel infoPanel;

	int oldPixelCount;

	public void setInfo(String info) {
		this.gPanel.setInfo(info);
	}
	
	
	
	
	public void applyNewPixelSize(int size) {
		this.pixelSize = size;
		this.gPanel.pixelSize = size;
		int col = Experiments.size();

		int row = Genes.size();
		
		
		int panelW = gPanel.abstandLinks + col* pixelSize;
		int panelH = gPanel.abstandOben+ row* pixelSize / gPanel.Width 
		+ this.dataManager.Experiments.elementAt(0).getBarchartToColors()
		.size() * (2 * this.pixelSize + 2);
		

		gPanel.setPreferredSize(new Dimension(panelW, panelH));
		
		
		
		infoLabel.setText("Aggregation: 1 : " + gPanel.Width);
	
	
		
		if (seurat.SYSTEM == seurat.WINDOWS) {

			this.setSize(
					panelW + 5+14,
					panelH+ 26+6
					+ infoPanel.getHeight()
					);

		} else
			this.setSize(
							panelW + 5,
							panelH+ 26
							+ infoPanel.getHeight()
						);
		

		updateSelection();
		
		
	}
	
	
	
	
	
	

	public GlobalViewAbstract(Seurat seurat, String name, Vector Experiments,
			Vector Genes, boolean clustering) {
		super(name);

		this.seurat = seurat;
		this.dataManager = seurat.dataManager;
		this.Experiments = Experiments;
		this.Genes = Genes;
		this.pixelSize = seurat.settings.PixelSize;
		this.getContentPane().setLayout(new BorderLayout());
		JPanel p = new JPanel();
		p.setBorder(BorderFactory.createEtchedBorder());
		p.setLayout(new BorderLayout());
			
		
		
		GlobalViewAbstractPanel panel = new GlobalViewAbstractPanel(seurat, this, pixelSize,
				Experiments, Genes);

		
		p.add(panel,BorderLayout.CENTER);
		
		
		panel.setBorder(BorderFactory.createEtchedBorder());
		infoPanel = new JPanel();
		if (Experiments.size() < 116) infoPanel.setPreferredSize(new Dimension(300,45));
		infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		infoPanel.setBorder(BorderFactory.createEtchedBorder());
		this.getContentPane().add(infoPanel,BorderLayout.SOUTH);
		
		
		
		this.addMouseListener(panel);

		int count = Genes.size();
		/*
		 * int height = Math.min(700, count);
		 * 
		 * panel.PixelCount = (height- panel.abstandOben) /
		 * globalView.dataManager.PixelSize;
		 * 
		 * 
		 * panel.Width = count / panel.PixelCount;
		 * 
		 * if ( (count - panel.PixelCount * panel.Width)>panel.Width) {
		 * panel.Width++; panel.PixelCount = count / panel.Width; }
		 * 
		 */
		int Width = 1;
		while ((count / Width) * this.pixelSize > 700) {
			Width++;
		}

		panel.PixelCount = count / Width;
		if (count % Width > 0)
			panel.PixelCount++;
		panel.Width = Width;

		int col = Experiments.size();

		int row = Genes.size();

		oldPixelCount = panel.PixelCount;

		panel.setPreferredSize(new Dimension(panel.abstandLinks + col
				* pixelSize, panel.abstandOben 
				+ row
				* pixelSize
				/ panel.Width
			));
		gPanel = panel;

		if (clustering) {
			gPanel.clustering = true;
			panel.abstandLinks = 150;
			panel.abstandOben = 150;
            panel.upShift = panel.abstandOben;
		}
		
		
this.getContentPane().add(p, BorderLayout.CENTER);
		
		
		
		
		
		infoLabel = new JLabel("Aggregation: 1 : " + gPanel.Width);
		Font myFont = new Font("SansSerif", 0, 10);

		
		JLabel label = new JLabel("Columns: " + Experiments.size());
		label.setFont(myFont);
		infoPanel.add(label);
		
		
		
		label = new JLabel(" Rows: "+Genes.size()+"  ");
		label.setFont(myFont);
		infoPanel.add(label);
		
		
		
		
		infoLabel.setFont(myFont);
		infoPanel.add(infoLabel);
		
		
		
		
		
		
		
/*
		if (seurat.SYSTEM == seurat.WINDOWS)
			this
					.setBounds(
							350,
							0,
							col * pixelSize + panel.abstandLinks + 19,
							panel.abstandOben+ 30
									+ row
									* pixelSize
									/ panel.Width
									+ 38
									);
		else
			this
					.setBounds(
							350,
							0,
							col * pixelSize + panel.abstandLinks + 5,
							panel.abstandOben+ 30
									+ row
									* pixelSize
									/ panel.Width
									+ 23
								);

		*/
		
		
		this.setLocation(750,0);
		
		this.setVisible(true);

		seurat.windows.add(this);

		seurat.windowMenu.add(item);

		panel.calculateMatrixValues();

		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				globalView.setVisible(true);
			}
		});

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				globalView.seurat.windowMenu.remove(item);
			}
		});

		this.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {

				
				//if (resize) {
				
				
					
				
				int count = globalView.Genes.size();
				
				
				int colors = 0;
				if (globalView.Experiments
						.elementAt(0) instanceof Variable) colors = ((Variable)globalView.Experiments
						.elementAt(0)).getBarchartToColors().size()
						* (2 * globalView.pixelSize + 2);
				

				gPanel.PixelCount = Math
				.min(
						(gPanel.getHeight() - globalView.gPanel.abstandOben - colors)
								/ globalView.seurat.settings.PixelSize,
						Math.min(700 / globalView.pixelSize, count));

		        gPanel.Width = count / gPanel.PixelCount;

		if ((count - gPanel.PixelCount * gPanel.Width) > gPanel.Width) {
			gPanel.Width++;
			gPanel.PixelCount = count / gPanel.Width;
		}
				
				
				

				gPanel.calculateMatrixValues();

				// Apply New PixelSize

				globalView.applyNewPixelSize(globalView.pixelSize);
				
				
				globalView.repaint();
				gPanel.repaint();
				
				

				//}
			//	else resize = true;
				
				
			}

		});

		
	
	}

	public void updateSelection() {
		// TODO Auto-generated method stub
		gPanel.updateSelection();
		
		
	}

	public void brush() {
		// TODO Auto-generated method stub

	}

	public void removeColoring() {
		// TODO Auto-generated method stub

	}

}

class GlobalViewAbstractPanel extends JPanel implements MouseListener, IPlot,
		MouseMotionListener {
	DataManager dataManager;

	Seurat seurat;

	int pixelSize = 1;

	int abstandLinks = 1;

	int abstandOben = 1;

	Vector<ISelectable> Rows;

	Vector<ISelectable> Columns;
	
	
	
	Vector<ISelectable> originalRows;

	Vector<ISelectable> originalColumns;
	
	
	

	int[] originalOrderSpalten;

	ClusterNode nodeZeilen;

	ClusterNode nodeSpalten;
	
	boolean updateTree = true;

	Point point1, point2;

	boolean clustering = true;
	
	boolean paintDendrCols = true;
	boolean paintDendrRows = true;

	String methodColumns, methodRows;

	String distanceColumns, distanceRows;

	String info, infoRows, infoColumns;

	int PixelCount = 10;

	int Width = 1;

	double[][] data;

	double[] Min, Max;

	GlobalViewAbstract globalView;

	Color SelectionColor = Color.black;

	Vector<CoordinateNode> nodesR;

	Vector<CoordinateNode> nodesC;

	Color[][] cellColor;

	int upShift;
	
	

	boolean[][] isCellSelected;


	public GlobalViewAbstractPanel(Seurat seurat, GlobalViewAbstract globalView, int pixelSize,
			Vector Experiments, Vector Genes) {

		this.seurat = seurat;
		this.dataManager = seurat.dataManager;
		this.globalView = globalView;
		this.pixelSize = pixelSize;

		clustering = false;

		this.Columns = Experiments;
		this.Rows = Genes;
		this.originalColumns = Experiments;
		this.originalRows = Genes;

		// this.originalOrderSpalten = orderSpalten;

		ToolTipManager.sharedInstance().registerComponent(this);
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		ToolTipManager.sharedInstance().setReshowDelay(0);

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		// createRowsAndColumns();
	}

	
	public void setInfo(String info) {
		this.info = info;
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		point1 = e.getPoint();
	}

	public void mouseExited(MouseEvent e) {
	}

	public void updateSelection() {

		cellColor = new Color[this.Columns.size()][this.data[0].length];

		boolean selection = false;

		for (int i = 0; i < this.Columns.size(); i++) {
			if (this.Columns.elementAt(i).isSelected())
				selection = true;
		}
		for (int i = 0; i < this.Rows.size(); i++) {
			if (this.Rows.elementAt(i).isSelected())
				selection = true;
		}

		if (seurat.settings.Model == 1) {

			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < PixelCount; j++) {

					if (data[i][j] > 0) {

						double koeff = data[i][j] / Max[i];

						Color c = Color.getHSBColor(0, (float) fPos(koeff), 1);

						if (PixelCount == Rows.size()
								&& Columns.elementAt(i).getRealValue(
										Rows.elementAt(j).getID()) == dataManager.NA)
							c = Color.WHITE;

						if (selection) {
							c = c.darker();
							c = c.darker();
						}

						boolean selected = false;

						for (int k = j * Width; k < (j + 1) * Width; k++) {
							if (k < Rows.size()) {
								if (Columns.elementAt(i).isSelected() && Rows.elementAt(k).isSelected()) {
									selected = true;
								}

							}
						}

						if (selected) {
							c = c.brighter();
							c = c.brighter();
						}

						cellColor[i][j] = c;

					} else {
						double koeff = data[i][j] / Min[i];
						
						if (Min [i] == 0) koeff = 0;

						Color c = (Color.getHSBColor((float) 0.33,
								(float) fNeg(koeff), 1));

						if (selection) {
							c = c.darker();
							c = c.darker();
						}

						boolean selected = false;

						for (int k = j * Width; k < (j + 1) * Width; k++) {
							if (k < Rows.size()) {
								if (Columns.elementAt(i).isSelected() && Rows.elementAt(k).isSelected()) {
									selected = true;
								}
							}
						}

						if (selected) {
							c = c.brighter();
							c = c.brighter();
						}

						// c = Color.WHITE;
						cellColor[i][j] = c;
					}

				}

			}

		}

		if (seurat.settings.Model == 2) {

			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < PixelCount; j++) {

					if (data[i][j] > 0) {
						/*
						 * float koeff = (float)Math.pow(
						 * Experiments.elementAt(i).doubleData[j] /
						 * Experiments.elementAt(i).max,
						 * 
						 * dataManager.colorParam);
						 */
						double koeff = data[i][j] / Max[i];

						Color c = new Color((float) fPos(koeff), 0, 0);

						if (PixelCount == Rows.size()
								&& Columns.elementAt(i).getRealValue(
										Rows.elementAt(j).getID()) == dataManager.NA)
							c = Color.WHITE;

						if (selection) {
							c = c.darker();
							c = c.darker();
							c = c.darker();
						}

						boolean selected = false;

						for (int k = j * Width; k < (j + 1) * Width; k++) {
							if (k < Rows.size()) {
								if (Columns.elementAt(i).isSelected() && Rows.elementAt(k).isSelected()) {
									selected = true;
								}

							}
						}

						if (selected) {
							c = c.brighter();
							c = c.brighter();
							c = c.brighter();
							c = c.brighter();

							// c = new Color((float)
							// (Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt((float)fPos(koeff)))))),
							// 0, 0);
							// c = new Color( 255,211,211);

							if (PixelCount == Rows.size()
									&& Columns.elementAt(i).getRealValue(
											Rows.elementAt(j).getID()) == dataManager.NA)
								c = Color.WHITE;

						}

						cellColor[i][j] = c;
					} else {
						/*
						 * double koeff = Math.pow(
						 * Experiments.elementAt(i).doubleData[j] /
						 * Experiments.elementAt(i).min,
						 * dataManager.colorParam);
						 */
						// Color c = new Color(0,(float)(0.5*koeff),0);
						double koeff = data[i][j] / Min[i];

						Color c = new Color(0, (float) fNeg(koeff), 0);

						if (selection) {
							c = c.darker();
							c = c.darker();
							c = c.darker();
						}

						boolean selected = false;

						for (int k = j * Width; k < (j + 1) * Width; k++) {
							if (k < Rows.size()) {
								if (Columns.elementAt(i).isSelected() && Rows.elementAt(k).isSelected()) {
									selected = true;
								}

							}
						}

						if (selected) {
							c = c.brighter();
							c = c.brighter();
							c = c.brighter();
							c = c.brighter();
							// c = new Color(0,(float)
							// Math.sqrt(Math.sqrt((float)fNeg(koeff))), 0);

						}
						cellColor[i][j] = c;
					}

				}
			}
		}

		if (clustering && nodesR != null) calculateTree(); 
		
		
		this.repaint();
	}
	
	
	
	
	

	public void mouseReleased(MouseEvent e) {

		point2 = e.getPoint();
		
		
		if (e.getButton() == MouseEvent.BUTTON3 || e.isControlDown()) {
		     return;
		}

		if (point1 != null && point2 != null) {
			
			seurat.dataManager.deleteSelection();

			if (e.isShiftDown()) {
				selectRectangle(0, point1.y, this.getWidth(), point2.y);
				Vector cases = new Vector();
				for (int i = 0; i < Rows.size(); i++) {

					if (Rows.elementAt(i).isSelected()) {
						cases.add(Rows.elementAt(i));
					}

				}

				// dataManager.clearSelection();

				new ZoomView(seurat, "ZoomView", Columns, cases);

				// view.gPanel.abstandLinks = 9;
				// view.gPanel.abstandOben = 9;

			} else {

				if (Math.max(point1.x, point2.x) < this.abstandLinks
						&& Math.max(point1.y, point2.y) < this.abstandOben) {
					dataManager.clearSelection();
					seurat.repaintWindows();
					point1 = null;
					point2 = null;
					return;
				} 
				
				if (Math.max(point1.x, point2.x) < this.abstandLinks
						|| Math.max(point1.y, point2.y) < this.abstandOben){
					
					
					
					
					if (point1.x == point2.x && point1.y == point2.y)	this.selectBranch(point1);
				    else {
					
					selectInTree(point1.x, point1.y, point2.x, point2.y); 
					point1 = null;
					point2 = null;
			        }
					
					
					seurat.repaintWindows();
					return;
			       
				}
				
				
				dataManager.deleteSelection();
				selectRectangle(point1.x, point1.y, point2.x, point2.y);
				if (clustering) selectTree(point1.x, point1.y, point2.x, point2.y);

				
				

			}

			point1 = null;
			point2 = null;
			seurat.repaintWindows();
		}

		this.repaint();
	}

	
	
	
	public void selectInTree(int xx1, int yy1, int xx2, int yy2) {
		dataManager.deleteSelection();

		
		for (int i = 0; i < this.nodesC.size(); i++) {
			CoordinateNode nd = nodesC.elementAt(i);
			for (int j = 0; j < nd.Lines.size(); j++) {
				Line line = nd.Lines.elementAt(j);
				if (containsLineInRect(line.x1,line.y1,line.x2,line.y2,xx1, yy1, xx2, yy2)) {
				
					nd.node.selectNode();}
			}
		}
		
		
		
		
		for (int i = 0; i < this.nodesR.size(); i++) {
			CoordinateNode nd = nodesR.elementAt(i);
			for (int j = 0; j < nd.Lines.size(); j++) {
				Line line = nd.Lines.elementAt(j);
				if (containsLineInRect(line.x1,line.y1,line.x2,line.y2,xx1, yy1, xx2, yy2)) {
				
					nd.node.selectNode();}
			}
		}
		
		
		
		
		
		this.updateSelection();
		repaint();
		
	}
	
	
	
	public void selectRectangle(int xx1, int yy1, int xx2, int yy2) {
		int x1 = Math.max(0, xx1 - abstandLinks) / this.pixelSize;
		int x2 = Math.max(0, xx2 - abstandLinks) / this.pixelSize;
		int y1 = Math.max(0, yy1 - upShift) * Width / this.pixelSize;
		int y2 = Math.max(0, yy2 - upShift) * Width / this.pixelSize;
		
		/*
		if (y1 == y2)
			y2 += Math.max(0,Width-1);
		if (x1 == x2)
			x2 += 1;*/

		dataManager.deleteSelection();
	

		for (int i = 0; i < Columns.size(); i++) {
			for (int j = 0; j < Rows.size(); j++) {
				if (i <= x2 && i >= x1 && j <= y2 && j >= y1) {

					this.Columns.elementAt(i).select(true);
				
			    	this.Rows.elementAt(j).select(true);

				}

			}
		}

		this.repaint();
		
		
		
	
		
		
		
		

	}
	
	
	public void calculateTree() {
		this.nodesC = new Vector();
		this.nodesR = new Vector();

		calculateClusteringRows(nodeZeilen);
		calculateClusteringColumns(nodeSpalten);
	}
	
	
	

	public void selectTree(int xx1, int yy1, int xx2, int yy2) {
		int x1 = Math.max(0, xx1 - abstandLinks) / this.pixelSize;
		int x2 = Math.max(0, xx2 - abstandLinks) / this.pixelSize;
		int y1 = Math.max(0, yy1 - upShift) * Width / this.pixelSize;
		int y2 = Math.max(0, yy2 - upShift) * Width / this.pixelSize;
		if (y1 == y2)
			y2 += Width;
		if (x1 == x2)
			x2 += 1;
		
		
		
		
		

		
		
		
		
		
		
		
		
		
		
		
		if (Math.max(yy1, yy2) < this.upShift
				|| Math.max(xx1, xx2) < this.abstandLinks) {

			for (int i = 0; i < Columns.size(); i++) {
				Columns.elementAt(i).unselect(true);

			}

			for (int i = 0; i < Rows.size(); i++) {
				Rows.elementAt(i).unselect(true);

			}

		}

		

		if (Math.max(yy1, yy2) < this.upShift) {
			for (int i = 0; i < Columns.size(); i++) {
					if (i < x2 && i >= x1) {

				this.Columns.elementAt(i).select(true);
			

					

				}
			}
		}

		if (Math.max(xx1, xx2) < this.abstandLinks) {
				for (int j = 0; j < Rows.size(); j++) {
					if (j < y2 && j >= y1) {

					this.Rows.elementAt(j).select(true);

					}

				
			}
		}

		this.repaint();

	}
	
	
	
	public boolean containsLineInRect(int lx1,int ly1,int lx2,int ly2,int rx1,int ry1,int rx2,int ry2) {
		
		//Vertikale Linie 
		if (lx1 == lx2) {
			if (lx1 < rx1) return false;
			if (lx1 > rx2) return false;
			if (ly1 > ry2) return false;
			if (ly2 < ry1) return false;
			return true;
		}
		
		
		if (ly1 == ly2) {
			if (lx2 < rx1) return false;
			if (lx1 > rx2) return false;
			if (ly1 > ry2) return false;
			if (ly2 < ry1) return false;
			return true;
		}
		
		
		
		
		return false;
	}
	
	
	

	public void selectBranch(Point p) {
		CoordinateNode node = null;
		int distance = 100000;
		dataManager.clearSelection();
		
/*
		if (p.y < this.abstandOben) {

			for (int i = 0; i < this.nodesC.size(); i++) {
				CoordinateNode nd = nodesC.elementAt(i);
				int dist = nd.getDistance(p.x, p.y);

				if (distance > dist) {
					node = nd;
					distance = dist;
				}
			}

		}

		if (p.x < this.abstandLinks) {

			for (int i = 0; i < this.nodesR.size(); i++) {
				CoordinateNode nd = nodesR.elementAt(i);
				int dist = nd.getDistance(p.x, p.y);

				if (distance > dist) {
					node = nd;
					distance = dist;
				}
			}

		}

		selectNode(node);*/

	}

	public void selectNode(CoordinateNode nodeC) {
		ClusterNode node = nodeC.node;
		nodeC.isSelected = true;
		node.selectNode();
		

		

			
		    
		
		
	}

	public void calculateMatrixValues() {

		data = new double[Columns.size()][this.PixelCount];

		for (int i = 0; i < Columns.size(); i++) {
			for (int j = 0; j < PixelCount; j++) {
				int count = 0;
				for (int k = j * Width; k < (j + 1) * Width; k++) {
					if (k < Rows.size()) {
						data[i][j] += Columns.elementAt(i).getValue(Rows.elementAt(k).getID());
						count++;
					}
				}
				data[i][j] = data[i][j] / count;
			}
		}

		Max = new double[this.data.length];
		for (int i = 0; i < this.data.length; i++) {
			for (int j = 0; j < this.PixelCount; j++) {
				if (dataManager.NA != data[i][j] && Max[i] < data[i][j])
					Max[i] = data[i][j];
			}

		}

		Min = new double[this.data.length];
		for (int i = 0; i < this.data.length; i++) {
			for (int j = 0; j < this.PixelCount; j++) {
				if (Min[i] > data[i][j])
					Min[i] = data[i][j];
			}
		}

	}

	public void mouseClicked(MouseEvent e) {

		point1 = e.getPoint();
		
	    if (e.getClickCount() == 2) {
	    	dataManager.clearSelection();
	        seurat.repaintWindows();
	    }
		
		
		if (e.getButton() == MouseEvent.BUTTON3 || e.isControlDown()) {

			JPopupMenu menu = new JPopupMenu();
			
			
			JMenuItem item = new JMenuItem("Open Selection");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// createCorrelationExperiments();

					Vector<ISelectable> subGenes = new Vector();
					Vector<ISelectable> subExps = new Vector();
					
					for (int i = 0; i < Rows.size(); i++) {
						if (Rows.elementAt(i).isSelected()) subGenes.add(Rows.elementAt(i));
					}
					
					
					for (int i = 0; i < Columns.size(); i++) {
						if (Columns.elementAt(i).isSelected()) subExps.add(Columns.elementAt(i));
					}
					
					
					
					
					GlobalViewAbstract globalView = new GlobalViewAbstract(seurat, "Global View", subExps, subGenes,  false);
                   globalView.applyNewPixelSize(globalView.pixelSize);
				}
			});
			menu.add(item);
			
			
			
			
			
			
			
			if (Rows.elementAt(0) instanceof Gene && dataManager.geneVariables != null) {
				
				JMenu m = new JMenu("Sort Genes by");
				
				item = new JMenuItem("Original Order");
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// createCorrelationGenes();

						Rows = originalRows;
						paintDendrRows = true;
						if (nodeZeilen != null) {
							abstandLinks = 150;
							globalView.setSize(globalView.getWidth() + 149, globalView.getHeight());
						}
						calculateMatrixValues();
						updateSelection();
					}
				});
				m.add(item);
				
				item = new JMenuItem("Chromosome Position");
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// createCorrelationGenes();

						sortGenesByChrPosition();
					}
				});
				m.add(item);
				
				menu.add(m);

			}
			
			
			
			/*
			
			
			if (dataManager.descriptionVariables != null) {
				
				
	            JMenu m = new JMenu("Sort Samples by");
				
				item = new JMenuItem("Original Order");
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// createCorrelationGenes();

						Columns = originalColumns;
						calculateMatrixValues();
						updateSelection();
					}
				});
				m.add(item);
				
				
				for (int i = 0; i < dataManager.descriptionVariables.size(); i++) {
				DescriptionVariable var = dataManager.descriptionVariables.elementAt(i);	
				
				item = new JMenuItem(""+var.getName());
				item.setActionCommand("i");
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// createCorrelationGenes();
                        int i = Integer.parseInt(e.getActionCommand());
                        DescriptionVariable var = dataManager.descriptionVariables.elementAt(i);	
            			
						//sortColumnsByVar(var);
					}
				});
				m.add(item);
				
				}
				
				menu.add(m);
				
			}
			*/
			
			
			
			
		
			
			
			
			
			
			
			
			
			item = new JMenuItem("Clustering");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// createCorrelationGenes();
					
					
					
					new KMeansDialog(seurat,Rows, Columns);
					
				}
			});
			menu.add(item);
			
			item = new JMenuItem("Seriation");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// createCorrelationGenes();
					
					
					
					new SeriationDialog(seurat,Rows, Columns);
					
				}
			});
			menu.add(item);
			

			item = new JMenuItem("Rows Correlation");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// createCorrelationGenes();

					new CorrelationFrame(seurat, Rows, Columns, Width,
							false, "Correlation Rows");
				}
			});
			menu.add(item);
			
					

			item = new JMenuItem("Columns Correlation");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// createCorrelationExperiments();

				new CorrelationFrame(seurat, Rows, Columns, 1, true,
							"Correlation Columns");

				}
			});
			menu.add(item);

			menu.show(this, e.getX(), e.getY());
		}

	}

	public void mouseMoved(MouseEvent e) {

	}

	public void mouseDragged(MouseEvent e) {
		point2 = e.getPoint();

		if (e.isShiftDown()) {
			this.SelectionColor = Color.YELLOW;

		} else
			this.SelectionColor = Color.BLACK;

		this.repaint();

	}

	
	
	
	public boolean isPointInRect(int x, int y, int Rx1, int Ry1, int Rx2,
			int Ry2) {
		if ((Rx1 <= x) && (Rx2 >= x) && (Ry1 <= y) && (Ry2 >= y))
			return true;
		else
			return false;
	}
	
	
	
	@Override
	public String getToolTipText(MouseEvent e) {
		
		int colorsHight = abstandOben;
		
		if (e.getY() < upShift) {
			for (int i = 0; i < Columns.size(); i++) {
				ISelectable var = Columns.elementAt(i);
	            if (var.getColors() == null) break;
				
				for (int j = var.getColors().size() - 1; j >= 0; j--) {

					colorsHight = var.getColors().size() * (2 * this.pixelSize + 1) + 4;

					//g.setColor(var.getColors().elementAt(j));

					if (isPointInRect(e.getX(), e.getY(),
							abstandLinks + i * this.pixelSize, 
							2 + abstandOben + j * (2 * this.pixelSize + 1), 
							abstandLinks + i * this.pixelSize+Math.max(pixelSize, 2),
							2 + abstandOben + j * (2 * this.pixelSize + 1)+2 * pixelSize + 1)
					){
						return var.getColorNames().elementAt(j);
					}
					
				
				}
			}
		}
		
		

		if (e.isControlDown()) {
			ISelectable exp = this.getExpAtPoint(e.getPoint());
			ISelectable gene = this.getGeneAtPoint(e.getPoint());

			if (exp != null && gene != null) {
			
				String s = "<HTML><BODY BGCOLOR = 'WHITE'><FONT FACE = 'Verdana'><STRONG>";
			
				s += "<FONT FACE = 'Arial'><TR><TD>" + exp.getName()
						+ "  </TD><TD> ";

				int x = Math.max(0, e.getPoint().x - abstandLinks)
						/ this.pixelSize;
				int y = Math.max(0, e.getPoint().y - upShift)
						/ (this.pixelSize);

				double valueD = 0;
				boolean isNA = true;

				for (int i = 0; i < Columns.size(); i++) {
					if (i == x) {

						for (int j = 0; j < Rows.size(); j++) {
							if (j == y) {

								if (Columns.elementAt(i).getRealValue(
										Rows.elementAt(j).getID()) != dataManager.NA) {
									valueD += Columns.elementAt(i)
											.getValue(Rows.elementAt(j).getID());
									isNA = false;
								}

							}
						}
					}
				}

				String value = "";

				if (isNA) {
					value = "NA";
				} else {

					value = valueD + "";
				}

				if (Width != 1) {

					value = this.data[x][y] + "";
				}

				s += "<FONT FACE = 'Arial'><TR><TD>" + value + "  </TD><TD> ";

				s += "</P></FONT></STRONG>";

				return s;
			}

			if (e.getX() >= this.abstandLinks
					&& e.getX() <= this.abstandLinks + this.Columns.size()
							* this.pixelSize && e.getY() <= this.upShift
					&& e.getY() >= this.abstandOben) {

				
				if (exp != null) {


					String s = "<HTML><BODY BGCOLOR = 'WHITE'><FONT FACE = 'Verdana'><STRONG>";

					String name = exp.getName();
		          
					
					s += "<FONT FACE = 'Arial'><TR><TD>"
							+ name + "<TR><TD>"
							+ "  </TD><TD> ";

					return s;

				}

			}

			return null;

		}
		return null;
	}

	public ISelectable getExpAtPoint(Point p) {
		int x = (int) Math.round(p.getX());
		int y = (int) Math.round(p.getY());

		if (x < abstandLinks)
			return null;
		if (y < upShift)
			return null;

		x = (x - abstandLinks) / this.pixelSize;
		y = (y - upShift) / this.pixelSize;


		return Columns.elementAt(x);
	}

	public int getColumnOrig(int col) {
		int exp = -1;
		return Columns.elementAt(col).getID();
	}

	public ISelectable getExperimentAtIndex(int index) {

		return Columns.elementAt(index);
	}

	
	public void sortGenesByChrPosition() {
	
		
		Vector<Vector<Gene>> Chromosomes = new Vector();
   
		
		
		
		for (int i = 0; i < Rows.size(); i++) {
			Gene gene = (Gene)Rows.elementAt(i);
			String chr = "NA";
			if (gene.chrName != null) chr = gene.chrName;
			          
			double pos = gene.nucleotidePosition;
			
			Vector<Gene> genes = findChrInList(Chromosomes,chr);
			
			if (genes == null) {
				genes = new Vector();
				genes.add(gene);
				Chromosomes.add(genes);
			}else {
				
				insertGene(gene,genes);
			}
			
		}
		
		
		Chromosomes = sortChromosomes(Chromosomes);
		
		
		Rows = new Vector();
		for (int i = 0; i < Chromosomes.size(); i++){
			for (int j = 0; j < Chromosomes.elementAt(i).size(); j++) {
				Rows.add(Chromosomes.elementAt(i).elementAt(j));
			//	System.out.println(Chromosomes.elementAt(i).elementAt(j).getName());
			}
		}
		
		//System.out.println("New Row Size " + Rows.size());
		
		
		paintDendrRows = false;
		if (nodeZeilen != null) {
			abstandLinks = 1;
			globalView.setSize(globalView.getWidth()-149,globalView.getHeight());
		}
		
		calculateMatrixValues();
		updateSelection();
		
	}
	
	
	
	
	
	public static Vector<Vector<Gene>> sortChromosomes(Vector<Vector<Gene>> stringBuffer) {

		Vector<Vector<Gene>> newBuffer = new Vector();
		for (int i = 0; i < stringBuffer.size(); i++) {
			String s = "NA";
			if (stringBuffer.elementAt(i).elementAt(0).chrName != null) s = stringBuffer.elementAt(i).elementAt(0).chrName;
			
			
			int j = 0;
			while (j < newBuffer.size()
					&& compareLexico(s, newBuffer.elementAt(j).elementAt(0).chrName)) {
				j++;
			}
			newBuffer.insertElementAt(stringBuffer.elementAt(i), j);

		}

		return newBuffer;
		
	}

	public static boolean compareLexico(String a, String b) {
		int i = 0;

		String tA = a.replace("\"","");
		String tB = b.replace("\"","");
		if (tA.equals("X") || tA.equals("x")) tA = "23";
		if (tB.equals("X") || tB.equals("x")) tB = "23";

		if (tA.equals("Y") || tA.equals("y")) tA = "24";
		if (tB.equals("Y") || tB.equals("y")) tB = "24";
		
		if (tA.equals("NA") || tB.equals("NA")) return true;
		
		
		int aa = Integer.parseInt(tA);
		int bb = Integer.parseInt(tB);
		
		if (aa < bb) return false;
		return true;
	}
	
	
	
	
	
	
	
	
	/*
	 *  setzt Gene in eine Liste, so dass die Gene der Liste nach Nucleotideposition sortiert sind
	 * */
	public void insertGene(Gene gene, Vector<Gene> genes) {
		boolean insert = true;
	     	for (int i = 0; i < genes.size(); i++) {
	     		if (gene.nucleotidePosition <= genes.elementAt(i).nucleotidePosition) {
	     			genes.insertElementAt(gene,i);
	     			insert = false;
	     			break;
	     		}
	     	}
	     	if (insert) genes.add(gene);
	}
	
	
	
	
	public Vector<Gene> findChrInList(Vector<Vector<Gene>> Chromosomes,String s) {
		for (int i = 0; i < Chromosomes.size(); i++) {
			String name = "NA";
		
			if (Chromosomes.elementAt(i).elementAt(0).chrName != null) name = Chromosomes.elementAt(i).elementAt(0).chrName;
			if (name.equals(s)) return Chromosomes.elementAt(i);
		}
		return null;
	}
	
	
	
	
	
	

	public ISelectable getGeneAtPoint(Point p) {
		int x = (int) Math.round(p.getX());
		int y = (int) Math.round(p.getY());

		if (x < abstandLinks)
			return null;
		if (y < upShift)
			return null;

		x = (x - abstandLinks) / this.pixelSize;
		y = (y - upShift) / this.pixelSize;

		int gene = 0;

		
		return Rows.elementAt(y);
	}

	public int getRowIndexOrigin(int row) {

		return Rows.elementAt(row).getID();
	}

	



	@Override
	public void paint(Graphics g) {
		
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		
		
		
		upShift = abstandOben;
		

		int colorsHight = 0;

		for (int i = 0; i < Columns.size(); i++) {
			ISelectable var = Columns.elementAt(i);
            if (var.getColors() == null) break;
			
			for (int j = var.getColors().size() - 1; j >= 0; j--) {

				colorsHight = var.getColors().size() * (2 * this.pixelSize + 1) + 4;

				g.setColor(var.getColors().elementAt(j));

				g.fillRect(abstandLinks + i * this.pixelSize, 2 + abstandOben
						+ j * (2 * this.pixelSize + 1), Math.max(pixelSize, 2),
						2 * pixelSize + 1);
			}
		}

		
		
		upShift = abstandOben + colorsHight;
		
		

		
		if (cellColor == null) this.updateSelection();
		
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < PixelCount; j++) {

					g.setColor(cellColor [i][j]);
					

					g.fillRect(abstandLinks + i * this.pixelSize, upShift + j
							* this.pixelSize, pixelSize, pixelSize);
				

			}
			}
		
			
			if (clustering && nodesR == null) {
				calculateTree();
			} 

			

			
			

		
		

			paintClustering(g);
			
		
		

		if (point1 != null && point2 != null) {
			g.setColor(SelectionColor);
			if (SelectionColor == Color.BLACK) {

				g.drawRect(Math.min(point1.x, point2.x), Math.min(point1.y,
						point2.y), Math.abs(point2.x - point1.x), Math
						.abs(point2.y - point1.y));
			} else {

				g.drawRect(this.abstandLinks - 1, Math.min(point1.y, point2.y),
						this.getWidth() - abstandLinks - 2, Math.abs(point2.y
								- point1.y));
				g.drawRect(this.abstandLinks - 1, 1 + Math.min(point1.y,
						point2.y), this.getWidth() - abstandLinks - 2, Math
						.abs(point2.y - point1.y));

			}
		}

	}

	
	
	/*
	public void paintClusteringRows(Graphics g, ClusterNode node, int Tiefe,
			int startAbstand) {

		if (node.isSelectedG())
			g.setColor(Color.RED);
		else
			g.setColor(Color.BLACK);

		if (node.nodeL != null && node.nodeR != null) {

			int maxTiefe = node.getTiefe();
			int pos = getYCoordinate(node.cases);
			int endPointX = startAbstand + this.abstandLinks
					/ (Tiefe + maxTiefe);

			CoordinateNode nodeC = new CoordinateNode(node, startAbstand, pos,
					endPointX, pos);
			nodesR.add(nodeC);

			g.drawLine(startAbstand, pos, endPointX, pos);
			if (node.nodeL != null) {

				if (node.nodeL.isSelectedG())
					g.setColor(Color.RED);
				else
					g.setColor(Color.BLACK);

				int pos1 = getYCoordinate(node.nodeL.cases);
				int pos2 = getYCoordinate(node.nodeR.cases);
				g.drawLine(endPointX, pos1, endPointX, pos);
				nodeC.Lines.add(new Line(endPointX, pos1, endPointX, pos));
				paintClusteringRows(g, node.nodeL, Tiefe + 1, endPointX);

			}

			if (node.nodeR != null) {

				if (node.nodeR.isSelectedG())
					g.setColor(Color.RED);
				else
					g.setColor(Color.BLACK);

				int pos1 = getYCoordinate(node.nodeL.cases);
				int pos2 = getYCoordinate(node.nodeR.cases);
				g.drawLine(endPointX, pos, endPointX, pos2);
				nodeC.Lines.add(new Line(endPointX, pos, endPointX, pos2));

				paintClusteringRows(g, node.nodeR, Tiefe + 1, endPointX);
			}

		} else {

			if (node.isSelectedG())
				g.setColor(Color.RED);
			else
				g.setColor(Color.BLACK);

			int pos = getYCoordinate(node.cases);
			// CoordinateNode nodeC = new CoordinateNode(node,startAbstand, pos,
			// this.abstandLinks, pos);
			// nodesR.add(nodeC);

			g.drawLine(startAbstand, pos, this.abstandLinks, pos);

		}

	}
	
	
	
	
	 */
	
	/*
	
	
	public void paintClusteringRows(Graphics g, ClusterNode node) {

		if (node.isSelectedG())
			g.setColor(Color.RED);
		else
			g.setColor(Color.BLACK);

		if (node.nodeL != null && node.nodeR != null) {

			int pos = getYCoordinate(node.cases);
			int x = (int)Math.round(this.abstandLinks - this.abstandLinks * node.currentHeight);

			
			
			
			
		
			
		    if (node.nodeL != null) {

				if (node.nodeL.isSelectedG())
					g.setColor(Color.RED);
				else
					g.setColor(Color.BLACK);

				int posL = getYCoordinate(node.nodeL.cases);
				int xL = (int)Math.round(this.abstandLinks - this.abstandLinks * node.nodeL.currentHeight);
				
				g.drawLine(x,pos,x,posL);
				g.drawLine(x, posL, xL, posL);
				
				
				CoordinateNode nodeC = new CoordinateNode(node.nodeL, x, posL, xL, posL);
				nodeC.Lines.add(new Line(x,pos,x,posL));
				this.nodesR.add(nodeC);
			
				
				paintClusteringRows(g, node.nodeL);

			}
		    

			if (node.nodeR != null) {

				if (node.nodeR.isSelectedG())
					g.setColor(Color.RED);
				else
					g.setColor(Color.BLACK);

				int posR = getYCoordinate(node.nodeR.cases);
				int xR = (int)Math.round(this.abstandLinks - this.abstandLinks * node.nodeR.currentHeight);
				
				g.drawLine(x,pos,x,posR);
				g.drawLine(x, posR, xR, posR);
				
				
				CoordinateNode nodeC = new CoordinateNode(node.nodeR, x, posR, xR, posR);
				nodeC.Lines.add(new Line(x,pos,x,posR));
				this.nodesR.add(nodeC);
				
				

				paintClusteringRows(g, node.nodeR);
			}

		} else {

			if (node.isSelectedG())
				g.setColor(Color.RED);
			else
				g.setColor(Color.BLACK);

			int pos = getYCoordinate(node.cases);
			// CoordinateNode nodeC = new CoordinateNode(node,startAbstand, pos,
			// this.abstandLinks, pos);
			// nodesR.add(nodeC);

			//g.drawLine((int)Math.round(this.abstandLinks - this.abstandLinks * node.currentHeight), pos, this.abstandLinks, pos);

		}

	}
	
	public void paintClusteringColumns(Graphics g, ClusterNode node) {

		if (node.isSelectedV())
			g.setColor(Color.RED);
		else
			
			g.setColor(Color.BLACK);

		if (node.nodeL != null && node.nodeR != null) {

			int pos = getXCoordinate(node.cases);
			int y = (int)Math.round(this.abstandOben - this.abstandOben * node.currentHeight);


			//CoordinateNode nodeC = new CoordinateNode(node, pos, startAbstand,
				//	pos, endPointY);
		//	nodesC.add(nodeC);

			if (node.nodeL != null) {
				if (node.nodeL.isSelectedV())
					g.setColor(Color.RED);
				else
					g.setColor(Color.BLACK);

				int posL = getXCoordinate(node.nodeL.cases);
				int yL = (int)Math.round(this.abstandOben - this.abstandOben * node.nodeL.currentHeight);
				
				g.drawLine(pos, y, posL, y);
				g.drawLine(posL, y, posL, yL);
				
				CoordinateNode nodeC = new CoordinateNode(node.nodeL, pos, y, posL, y);
				nodeC.Lines.add(new Line(pos, y, posL, y));
				this.nodesC.add(nodeC);
				
				
				
				paintClusteringColumns(g, node.nodeL);
			}

			if (node.nodeR != null) {

				if (node.nodeR.isSelectedV())
					g.setColor(Color.RED);
				else
					g.setColor(Color.BLACK);

				int posR = getXCoordinate(node.nodeR.cases);
				int yR = (int)Math.round(this.abstandOben - this.abstandOben * node.nodeR.currentHeight);
				
				
				CoordinateNode nodeC = new CoordinateNode(node.nodeR,pos, y, posR, y);
				nodeC.Lines.add(new Line(posR, y, posR, yR));
				this.nodesC.add(nodeC);
				
				
				g.drawLine(pos, y, posR, y);
				g.drawLine(posR, y, posR, yR);
				
				
				//nodeC.Lines.add(new Line(pos, endPointY, pos2, endPointY));

				paintClusteringColumns(g, node.nodeR);
			}

		} else {
			if (node.isSelectedV())
				g.setColor(Color.RED);
			else
				g.setColor(Color.BLACK);

			int pos = getXCoordinate(node.cases);
			//nodesC.add(new CoordinateNode(node, pos, startAbstand, pos,
				//	this.abstandOben));

			g.drawLine(pos, (int)Math.round(this.abstandOben - this.abstandOben * node.currentHeight), pos, this.abstandOben);

		}

	}

	
	*/
	
	
	
	
	

	
	public void paintClustering(Graphics g) {

		

		if (nodesR != null && paintDendrRows) {
			
			
			for (int i = 0; i < nodesR.size(); i++) {
				CoordinateNode node = nodesR.elementAt(i);
				
				if (node.isSelected)
					g.setColor(Color.RED);
				else
					g.setColor(Color.BLACK);
				
				for (int j = 0; j < node.Lines.size(); j++) {
					Line line = node.Lines.elementAt(j);
					g.drawLine(line.x1,line.y1,line.x2,line.y2);
				}
				
				
			}

			
		    
		}
		
		
if (nodesC != null && paintDendrCols) {
			
			
			for (int i = 0; i < nodesC.size(); i++) {
				CoordinateNode node = nodesC.elementAt(i);
				
				if (node.isSelected)
					g.setColor(Color.RED);
				else
					g.setColor(Color.BLACK);
				
				for (int j = 0; j < node.Lines.size(); j++) {
					Line line = node.Lines.elementAt(j);
					g.drawLine(line.x1,line.y1,line.x2,line.y2);
				}
				
				
			}

			
		    
		}




	}

	
	public void calculateClusteringRows(ClusterNode node) {

		

		if (node.nodeL != null && node.nodeR != null) {

			int pos = getYCoordinate(node.cases);
			int x = (int)Math.round(this.abstandLinks - this.abstandLinks * node.currentHeight);

			
		    if (node.nodeL != null) {

	
				int posL = getYCoordinate(node.nodeL.cases);
				int xL = (int)Math.round(this.abstandLinks - this.abstandLinks * node.nodeL.currentHeight);
						
				
				CoordinateNode nodeC = new CoordinateNode(node.nodeL, x, posL, xL, posL);
				nodeC.Lines.add(new Line(x,pos,x,posL));
				nodeC.Lines.add(new Line(x,posL,xL,posL));
				
				if (node.nodeL.isSelectedG()) nodeC.isSelected = true;
				
				this.nodesR.add(nodeC);
			
				
				calculateClusteringRows(node.nodeL);

			}
		    

			if (node.nodeR != null) {


				int posR = getYCoordinate(node.nodeR.cases);
				int xR = (int)Math.round(this.abstandLinks - this.abstandLinks * node.nodeR.currentHeight);
				
				
				CoordinateNode nodeC = new CoordinateNode(node.nodeR, x, posR, xR, posR);
				nodeC.Lines.add(new Line(x,pos,x,posR));
				nodeC.Lines.add(new Line(x,posR,xR,posR));
				this.nodesR.add(nodeC);
				
				if (node.nodeR.isSelectedG()) nodeC.isSelected = true;

				calculateClusteringRows(node.nodeR);
			}

		} 

		

	}
	
	
	
	public void sortExperimentsByVar(DescriptionVariable var) {
	}
	
	
	
	
	
	
	
	
	
	public void calculateClusteringColumns(ClusterNode node) {

	

		if (node.nodeL != null && node.nodeR != null) {

			int pos = getXCoordinate(node.cases);
			int y = (int)Math.round(this.abstandOben - this.abstandOben * node.currentHeight);


			if (node.nodeL != null) {
				

				int posL = getXCoordinate(node.nodeL.cases);
				int yL = (int)Math.round(this.abstandOben - this.abstandOben * node.nodeL.currentHeight);
				
			
				CoordinateNode nodeC = new CoordinateNode(node.nodeL, pos, y, posL, y);
				nodeC.Lines.add(new Line(pos, y, posL, y));
				nodeC.Lines.add(new Line(posL, y, posL, yL));
				this.nodesC.add(nodeC);
				
				if (node.nodeL.isSelectedV()) nodeC.isSelected = true;
				
				calculateClusteringColumns(node.nodeL);
			}

			if (node.nodeR != null) {


				int posR = getXCoordinate(node.nodeR.cases);
				int yR = (int)Math.round(this.abstandOben - this.abstandOben * node.nodeR.currentHeight);
				
				
			   CoordinateNode nodeC = new CoordinateNode(node.nodeR,pos, y, posR, y);
				nodeC.Lines.add(new Line(posR, y, posR, yR));
			//	nodeC.Lines.add(new Line(pos, y, posR, yR));
				this.nodesC.add(nodeC);
				
				
				if (node.nodeR.isSelectedV()) nodeC.isSelected = true;

				calculateClusteringColumns(node.nodeR);
			}

		} 
		

	}

	
	
	
	
	
	
	
	
	
	
	
	/*

	public void paintClusteringColumns(Graphics g, ClusterNode node, int Tiefe,
			int startAbstand) {

		if (node.isSelectedV())
			g.setColor(Color.RED);
		else
			g.setColor(Color.BLACK);

		if (node.nodeL != null && node.nodeR != null) {

			int maxTiefe = node.getTiefe();
			int pos = getXCoordinate(node.cases);
			int endPointY = startAbstand + this.abstandOben
					/ (Tiefe + maxTiefe);

			g.drawLine(pos, startAbstand, pos, endPointY);

			CoordinateNode nodeC = new CoordinateNode(node, pos, startAbstand,
					pos, endPointY);
			nodesC.add(nodeC);

			if (node.nodeL != null) {
				if (node.nodeL.isSelectedV())
					g.setColor(Color.RED);
				else
					g.setColor(Color.BLACK);

				int pos1 = getXCoordinate(node.nodeL.cases);
				g.drawLine(pos1, endPointY, pos, endPointY);
				nodeC.Lines.add(new Line(pos1, endPointY, pos, endPointY));
				paintClusteringColumns(g, node.nodeL, Tiefe + 1, endPointY);
			}

			if (node.nodeR != null) {

				if (node.nodeR.isSelectedV())
					g.setColor(Color.RED);
				else
					g.setColor(Color.BLACK);

				int pos2 = getXCoordinate(node.nodeR.cases);
				g.drawLine(pos, endPointY, pos2, endPointY);
				nodeC.Lines.add(new Line(pos, endPointY, pos2, endPointY));

				paintClusteringColumns(g, node.nodeR, Tiefe + 1, endPointY);
			}

		} else {
			if (node.isSelectedV())
				g.setColor(Color.RED);
			else
				g.setColor(Color.BLACK);

			int pos = getXCoordinate(node.cases);
			nodesC.add(new CoordinateNode(node, pos, startAbstand, pos,
					this.abstandOben));

			g.drawLine(pos, startAbstand, pos, this.abstandOben);

		}

	}*/

	public double fPos(double xx) {
		double a = seurat.settings.aPos;
		double b = seurat.settings.bPos;
		double value = 0;

		if (xx < seurat.settings.posMin)
			return 0;
		if (xx > seurat.settings.posMax)
			return 1;

		double x = (xx - seurat.settings.posMin)
				/ (seurat.settings.posMax - seurat.settings.posMin);

		if (x <= a) {
			value = a * Math.pow(x / a, b);
		} else {
			value = 1 - (1 - a) * Math.pow((1 - x) / (1 - a), b);
		}

		if (seurat.settings.invertShading)
			return 1 - value;
		else
			return value;

	}

	public double fNeg(double xx) {
		double a = seurat.settings.aNeg;
		double b = seurat.settings.bNeg;
		double value = 0;

		if (xx < seurat.settings.negMin)
			return 0;
		if (xx > seurat.settings.negMax)
			return 1;

		double x = (xx - seurat.settings.negMin)
				/ (seurat.settings.negMax - seurat.settings.negMin);

		if (x <= a) {
			value = a * Math.pow(x / a, b);
		} else {
			value = 1 - (1 - a) * Math.pow((1 - x) / (1 - a), b);
		}

		if (seurat.settings.invertShading)
			return 1 - value;
		else
			return value;

	}

	public int getYCoordinate(Vector<Integer> Cases) {
		int pos = 0;
		for (int i = 0; i < Cases.size(); i++) {
			pos += this.getIndexOfGeneInHeatMap(Cases.elementAt(i));
		}
		int max = 	upShift + (Rows.size() / this.Width) * this.pixelSize - this.pixelSize/2/this.Width;
		return Math.min((upShift + pos * this.pixelSize / (this.Width * Cases.size()) + this.pixelSize/2/this.Width),max);
	}

	public int getXCoordinate(Vector<Integer> Cases) {
		int pos = 0;
		for (int i = 0; i < Cases.size(); i++) {
			pos += getIndexOfExperimentInHeatMap(Cases.elementAt(i).intValue())
					* this.pixelSize;
		}

		return (this.abstandLinks + pos / Cases.size() + this.pixelSize/2);
	}

	public int getIndexOfExperimentInHeatMap(int ID) {
		for (int i = 0; i < Columns.size(); i++) {
			if (Columns.elementAt(i).getID() == ID)
				return i;
		}
		return -1;
	}

	public int getIndexOfGeneInHeatMap(int ID) {
		for (int i = 0; i < Rows.size(); i++) {
			if (Rows.elementAt(i).getID() == ID)
				return i;
		}
		return -1;
	}

	public float[][] calculateCorrs(double[][] data) {

		float[][] corr = new float[data.length][data.length];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data.length; j++) {

				float m1 = 0, m2 = 0, s1 = 0, s2 = 0;
				float p = 0;

				for (int k = 0; k < data[i].length; k++) {
					if (data[i][k] != dataManager.NA)
						m1 += data[i][k];

					if (data[j][k] != dataManager.NA)
						m2 += data[j][k];
				}
				m1 /= data[i].length;
				m2 /= data[j].length;

				for (int k = 0; k < data[i].length; k++) {
					if (data[i][k] != dataManager.NA)
						s1 += (data[i][k] - m1) * (data[i][k] - m1);
					else
						s1 += m1 * m1;
					if (data[j][k] != dataManager.NA)
						s2 += (data[j][k] - m2) * (data[j][k] - m2);
					else
						s2 += m2 * m2;
				}

				s1 = (float) Math.sqrt(s1);
				s2 = (float) Math.sqrt(s2);

				double a, b;
				for (int k = 0; k < data[i].length; k++) {
					a = data[i][k];
					b = data[j][k];
					if (a == dataManager.NA)
						a = 0;
					if (b == dataManager.NA)
						b = 0;

					p += (a - m1) * (b - m2);
				}

				corr[i][j] = p / s1 / s2;

			}
		}

		float[][] correlations = new float[this.PixelCount][this.PixelCount];
		for (int i = 0; i < correlations.length; i++) {
			for (int j = 0; j < correlations.length; j++) {
				int count = 0;
				for (int ii = i * this.Width; ii < Math.min((i + 1)
						* this.Width, Rows.size()); ii++) {
					for (int jj = j * this.Width; jj < Math.min((j + 1)
							* this.Width, Rows.size()); jj++) {
						correlations[i][j] += corr[ii][jj];
						count++;
					}
				}
				correlations[i][j] /= count;

			}

		}
		return correlations;
	}

	public float[][] calculateCorrelations(double[][] data) {
		float[][] corr = new float[data.length][data.length];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data.length; j++) {

				float m1 = 0, m2 = 0, s1 = 0, s2 = 0;
				float p = 0;

				for (int k = 0; k < data[i].length; k++) {
					if (data[i][k] != dataManager.NA)
						m1 += data[i][k];

					if (data[j][k] != dataManager.NA)
						m2 += data[j][k];
				}
				m1 /= data[i].length;
				m2 /= data[j].length;

				for (int k = 0; k < data[i].length; k++) {
					if (data[i][k] != dataManager.NA)
						s1 += (data[i][k] - m1) * (data[i][k] - m1);
					else
						s1 += m1 * m1;
					if (data[j][k] != dataManager.NA)
						s2 += (data[j][k] - m2) * (data[j][k] - m2);
					else
						s2 += m2 * m2;
				}

				s1 = (float) Math.sqrt(s1);
				s2 = (float) Math.sqrt(s2);

				double a, b;
				for (int k = 0; k < data[i].length; k++) {
					a = data[i][k];
					b = data[j][k];
					if (a == dataManager.NA)
						a = 0;
					if (b == dataManager.NA)
						b = 0;

					p += (a - m1) * (b - m2);
				}

				corr[i][j] = p / s1 / s2;

			}
		}
		return corr;
	}

	public void brush() {
		// TODO Auto-generated method stub

	}

	public void removeColoring() {
		// TODO Auto-generated method stub

	}

	

}