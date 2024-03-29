package GUI;

import Data.*;
import Data.ISelectable;
import GUI.*;
import GUI.IconsManager.IconsManager;
import DataTree.*;
import GUI.Barchart.*;

import java.util.*;
import java.io.*;

import javax.swing.*;


import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;

import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.awt.datatransfer.*;
import javax.imageio.*;


import DataTree.DataTreeNode;
import StringTools.MyStringTokenizer;
import Data.Variable;

import java.util.zip.*;
import java.util.jar.*;

import RConnection.RConnectionManager;
import Settings.*;

import Data.*;
import Clustering.*;

import java.util.*;
import java.io.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;

import javax.swing.table.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.datatransfer.*;
import javax.imageio.*;


import StringTools.MyStringTokenizer;
import Data.Variable;

import java.util.zip.*;
import java.util.jar.*;

import RConnection.RConnectionManager;
import Settings.*;
import Tools.*;
import WindowManager.*;

import java.applet.Applet; 
import java.awt.Graphics;

public class IData extends JFrame {

	public JPanel MainPanel = new JPanel();

	public JFrame WorksPanel = new JFrame();

	public JMenuBar menuBar = new JMenuBar();

	public JMenu fileMenu;

	public JMenu optionsMenu;

	public JMenu windowMenu;

	public JMenu helpMenu;
	
	public JMenu plotsMenu;
	
	public JMenu statisticsMenu;
	
	public JMenu transformMenu;

	static IData iData;

	FileDialog fileDialog;


	JScrollPane objectsPane;
	JScrollPane datasetsPane;
	//JPanel middlePanel = new JPanel();

	
	
	public JProgressBar progressBar = new JProgressBar();
    public JLabel infoLabel;

	public JPanel infoPanel = new JPanel();

	DefaultMutableTreeNode topObj;
	public DefaultMutableTreeNode topData;
	public DefaultMutableTreeNode treeNode;
	DefaultMutableTreeNode topClustering;
	DefaultMutableTreeNode topSelection;
	DataTreeNode GenesNode; 

	public JTree tree;

	JMenuItem openGeneExpressionItem;
	JMenuItem openDescriptionItem;
	JMenuItem openGeneAnnotationsItem;
	JMenuItem saveItem;
	public JMenuItem closeDataset;
	//JPanel center;
	
	
	
	
	Vector<ISelectable> Genes;
	Vector<Clustering> Clusterings;	
	Vector<ClusterNode> HClusterings;	
	
    
	public IData() {
		super("IData");
		this.setBounds(0, 0, 500, 400);
		iData = this;
	//	RConnectionManager.tryToConnect(this);
		loadMainWindow();

		//new Container();
		
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
		addWindowListener(new WindowAdapter() 
		{
		public void windowClosing(WindowEvent we)
		{
			if (shouldExit()) {
				
				System.exit(0);
			}
		}
		});
	}

	public void loadMainWindow() {
		fileMenu = new JMenu("File");
		optionsMenu = new JMenu("Options");
		helpMenu = new JMenu("Help");
		transformMenu = new JMenu("Transform");
		statisticsMenu = new JMenu("Statistics");

		// this.setBounds(20,20,150,450);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(MainPanel, BorderLayout.CENTER);

		IconsManager.getIconsManager().loadLogos(this);

		MainPanel.setLayout(new BorderLayout());
		MainPanel.setBackground(Color.WHITE);
	

		//center = new JPanel();
	
	//	center.setBackground(Color.WHITE);

	//	MainPanel.add(center, BorderLayout.CENTER);
		// MainPanel.add(datasetsPane, BorderLayout.EAST);
		// MainPanel.add(middlePanel, BorderLayout.CENTER);

		// infoPanel.setBackground(Color.WHITE);
    	// infoPanel.setBackground(Color.WHITE);
		infoPanel.setPreferredSize(new Dimension(25, 25));

		infoPanel.setBorder(BorderFactory.createEtchedBorder());
	

		infoLabel = new JLabel("");
		// infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		infoPanel.setLayout(new BorderLayout());
		infoPanel.add(infoLabel, BorderLayout.WEST);
	
		this.getContentPane().add(infoPanel, BorderLayout.SOUTH);

		this.setJMenuBar(menuBar);

		/**
		 * ************************************************File
		 * Menu*************
		 * *****************************************************
		 */

		menuBar.add(fileMenu);

		JMenuItem openFileItem = new JMenuItem("Open File");
		openFileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (DataManager.getDataManager().Variables == null || shouldBeClosed()) {
				closeDataSet();
				
				infoPanel.removeAll();
				infoLabel.setText("Loading dataset...");
				MainPanel.remove(infoPanel);
				infoPanel.add(progressBar, BorderLayout.CENTER);
				MainPanel.add(infoPanel, BorderLayout.SOUTH);
				update(getGraphics());
				
				DataManager.getDataManager().openFile(null,iData);
				
				infoLabel.setText("");
				infoPanel.remove(progressBar);
				MainPanel.remove(infoPanel);
				MainPanel.add(infoPanel, BorderLayout.SOUTH);		
				closeDataset.setEnabled(true);
				
				createTree();	
				SelectionManager.getSelectionManager().repaintWindows();
				setVisible(true);
				return;
				}
				
			    setVisible(true);
				
			}
		});
		fileMenu.add(openFileItem);

		
		JMenuItem item = new JMenuItem("Open Database");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	
			
				
			}
		});
		fileMenu.add(item);
	
			
		
		
		
		item = new JMenuItem("Open Session");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (DataManager.getDataManager().Variables == null || shouldBeClosed()) {
					closeDataSet();
					
					infoPanel.removeAll();
					infoLabel.setText("Loading dataset...");
					MainPanel.remove(infoPanel);
					infoPanel.add(progressBar, BorderLayout.CENTER);
					MainPanel.add(infoPanel, BorderLayout.SOUTH);
					update(getGraphics());
					
					new SessionLoader().openSession(null,iData);
					
					infoLabel.setText("");
					infoPanel.remove(progressBar);
					MainPanel.remove(infoPanel);
					MainPanel.add(infoPanel, BorderLayout.SOUTH);		
					closeDataset.setEnabled(true);
					
					createTree();	
					SelectionManager.getSelectionManager().repaintWindows();
					setVisible(true);
					return;
					}
					
			
				
			}
		});
		fileMenu.add(item);

		

		fileMenu.addSeparator();
		
		item = new JMenuItem("Save File");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	
			
				
			}
		});
		fileMenu.add(item);
		
		
		item = new JMenuItem("Save Selection");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	
			
				
			}
		});
		fileMenu.add(item);
		
		saveItem = new JMenuItem("Save Session");
	//	saveItem.setEnabled(false);
		
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				new SessionLoader().saveData(getIData());

			}
		});
		fileMenu.add(saveItem);
		
		
		fileMenu.addSeparator();
		
		
		JMenu menu = new JMenu("Export as");
		fileMenu.add(menu);
		
		
		
		
		
		saveItem = new JMenuItem("Export as Applet");
		//	saveItem.setEnabled(false);
			
			saveItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					new SessionLoader().saveSessionApplet(getIData());

				}
			});
		menu.add(saveItem);
			
	
		
			saveItem = new JMenuItem("Export as JPG");
			
				
				saveItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						new SessionLoader().saveData(getIData());

					}
				});
		menu.add(saveItem);
				
			
				
				
		saveItem = new JMenuItem("Export as PDF");
			
		
		saveItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					new SessionLoader().saveData(getIData());

				}
		});
		menu.add(saveItem);
			
		
		


		
		fileMenu.addSeparator();
		
		
		closeDataset = new JMenuItem("Close");
		closeDataset.setEnabled(false);
		closeDataset.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				if (DataManager.getDataManager().Variables != null && shouldBeClosed()) {
				closeDataSet();
				}
                setVisible(true);
			}
		});
		fileMenu.add(closeDataset);

		
	
		
		
		JMenuItem exitItem = new JMenuItem("Quit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (shouldExit()) System.exit(0);
			}
		});
		fileMenu.add(exitItem);

	
		
		
		

		

		plotsMenu = new JMenu("Plots");
		menuBar.add(plotsMenu);
		
		
		item = new JMenuItem("Barchart");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Vector<Variable> vars = getSelectedVariables();
				Container cont = SelectionManager.getSelectionManager().getCurrentContainer();
				DataManager dm = DataManager.getDataManager();
				
				for (int i = 0; i < vars.size(); i++) {
					Variable var = vars.elementAt(i);
					
					if (var.isDiscret && var.type == Variable.String) {
					  Barchart bar = new Barchart(cont, var, dm.Cases);	
					  cont.add(bar,true);
					  cont.setVisible(true);
					}
				} 
				SelectionManager.getSelectionManager().repaintWindows();
			}
		});
		plotsMenu.add(item);
		
		
		
		item = new JMenuItem("Pie Chart");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	
			
				
			}
		});
		plotsMenu.add(item);
		
		
		item = new JMenuItem("Dotplot");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	
			
				
			}
		});
		plotsMenu.add(item);
		
		
		item = new JMenuItem("Boxplot");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	
			
				
			}
		});
		plotsMenu.add(item);
		
		
		
		item = new JMenuItem("Lineplot");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	
			
				
			}
		});
		plotsMenu.add(item);
		
		
		
		
		
		plotsMenu.addSeparator();
		
		
		item = new JMenuItem("Scatterplot");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	
			
				
			}
		});
		plotsMenu.add(item);
		
		item = new JMenuItem("SPLOM");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	
			
				
			}
		});
		plotsMenu.add(item);
		
		
		item = new JMenuItem("Parallel Coordinate Plot");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	
			
				
			}
		});
		plotsMenu.add(item);
		
		
		
		item = new JMenuItem("TS-Plot");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	
			
				
			}
		});
		plotsMenu.add(item);
		
		
		
		
		
		item = new JMenuItem("Mosaic Plot");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	
			
				
			}
		});
		plotsMenu.add(item);
		
		
		

		item = new JMenuItem("Heatmap");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// {"ARSA","BBURCG","BBWRCG","TSP","Chen","MDS","HC","GW","OLO"};

				DataManager dm = DataManager.getDataManager();
			
			
				int pixelW = Settings.PixelW;
				int pixelH = Settings.PixelH;
				
				if (dm.Cases.size() < 400) pixelH = 400 / dm.Cases.size();
				if (dm.Variables.size() < 560) pixelW = 560 / dm.Variables.size();
				
			}
		});
		plotsMenu.add(item);
		
		
		
		
		
		
		plotsMenu.addSeparator();	
		
		item = new JMenuItem("Grid");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	
			
				
			}
		});
		plotsMenu.add(item);
		
		
		item = new JMenuItem("Small Multiple");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	
			
				
			}
		});
		plotsMenu.add(item);
		
		
		item = new JMenuItem("Rosette");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

	
			
				
			}
		});
		plotsMenu.add(item);
		
		
		
		
		
		
		
	
		
		

		
		
		menuBar.add(transformMenu);
		
		item = new JMenuItem("Switch Variable Mode");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		transformMenu.add(item);
		
		
		item = new JMenuItem("Transform Variable");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		transformMenu.add(item);
		
		
		menu = new JMenu("Derive Variable");
		transformMenu.add(menu);
		
		
		item = new JMenuItem("from Selection");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		menu.add(item);
		
		
		item = new JMenuItem("from Colors");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		menu.add(item);
		
		
		
		
		
		
		
		
		
        menuBar.add(statisticsMenu);
		

		item = new JMenuItem("Clustering");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		statisticsMenu.add(item);
		
		
		item = new JMenuItem("MDS");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		statisticsMenu.add(item);
		
		

		item = new JMenuItem("PCA");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		statisticsMenu.add(item);
		
		
		statisticsMenu.addSeparator();

		item = new JMenuItem("Model Navigator");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		statisticsMenu.add(item);
		
		
		
		
		
		
		
		

		
		
		
		
		menuBar.add(optionsMenu);
		
		
		item = new JMenuItem("Toggle Selection");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		optionsMenu.add(item);
		
		
		item = new JMenuItem("Select all");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		optionsMenu.add(item);
		
		
		item = new JMenuItem("Alpha on Highlight");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		optionsMenu.add(item);
		
		
		
		item = new JMenuItem("Clear Colors");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		optionsMenu.add(item);
		
		
		item = new JMenuItem("Preferences");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		optionsMenu.add(item);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		windowMenu = new JMenu("Window");
		
		
		item = new JMenuItem("New Container");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		windowMenu.add(item);
		
		
		item = new JMenuItem("Container for Variable");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		windowMenu.add(item);
		
		
		item = new JMenuItem("Container for Selection");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		windowMenu.add(item);
		
		
		
		item = new JMenuItem("Copy");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		windowMenu.add(item);
		
		
		item = new JMenuItem("Toolbox");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				
			}
		});
		windowMenu.add(item);
		
		
		
		item = new JMenuItem("Close all");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SelectionManager s = SelectionManager.getSelectionManager();
				
				for (int i = 0; i < s.containers.size(); i++) {
					s.containers.elementAt(i).setVisible(false);
					windowMenu.remove(1);
				}
				
			}
		});
		windowMenu.add(item);
		windowMenu.addSeparator();

		menuBar.add(windowMenu);
		
		
		
		
		
		
		
		
		
		
		
		menuBar.add(helpMenu);
		
		item = new JMenuItem("Online Help");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				try {
					BrowserLauncher launcher = new BrowserLauncher();
					launcher.openURLinBrowser("http://seurat.r-forge.r-project.org/index.html");

				} catch (BrowserLaunchingInitializingException ee) {
					// TODO Auto-generated catch block
					ee.printStackTrace();
				} catch (UnsupportedOperatingSystemException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/

			}
		});
		helpMenu.add(item);
		
		

		

		IconsManager.getIconsManager().loadLogos(this);

	}

	
	
	public Vector<Variable> getSelectedVariables() {
	
		
		TreePath [] paths = tree.getSelectionPaths();
		
		Vector vars = new Vector();	
		 
		if (paths != null) { 
		for (int i = 0; i < paths.length; i++) {
			Object o = paths [i].getLastPathComponent();
			if (o != null && o instanceof DataTreeNode) {
			DataTreeNode node = (DataTreeNode)o;
			if (node.object instanceof Variable) vars.add(node.object);
		
		}
		}
		}
		
		return vars;
	
		
	}
	
	
	
	
	public static IData getIData() {
		if (iData == null) iData = new IData();
		return iData;		
	}
	
	
	
	
	
	

	public void closeDataSet() {

	    

		SelectionManager.close();
		DataManager.close();
		MainPanel.removeAll();
		this.getContentPane().removeAll();
		menuBar.removeAll();
		infoPanel.removeAll();
		

		loadMainWindow();
		this.repaint();
		
		
	}

	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
     //   System.out.println(new Double("2.3653757256299777766544"));
		IData iData = new IData();
		System.out.println("Software gestartet....");
		iData.setVisible(true);

	}

	
	
	
	
	
	public boolean shouldBeClosed()
	{    
		if (JOptionPane.showConfirmDialog(this,"Close all datasets?","Choose one of the following options",0) == 1)
		return false;
		else return true;
	}
	
	public boolean shouldExit()
	{    
		if (DataManager.getDataManager() != null) {
		
			if (JOptionPane.showConfirmDialog(this,"Close all datasets and quit Seurat?","Choose one of the following options",0) == 1)
		    return false;
		    else return true;
		}
		else {
			 if (JOptionPane.showConfirmDialog(this,"Exit Seurat?","Choose one of the following options",0) == 1)
			 return false;
		     else return true;
			
		}
		
	}
	
	
	
	
	
	public void createTree() {
		
		   
		    if (DataManager.getDataManager().Variables== null) return;     
		
		    treeNode = new DefaultMutableTreeNode("Data");
			
					
			for (int i = 0; i < DataManager.getDataManager().Variables.size(); i++) {
				Variable var = DataManager.getDataManager().Variables.elementAt(i);
				DataTreeNode Experiment = new DataTreeNode(var);
				treeNode.add(Experiment);
			}
			
			
			
			tree = new JTree(treeNode);
			BasicTreeUI ui = (BasicTreeUI) tree.getUI();
			ui.setCollapsedIcon(IconsManager.CollapsedIcon);
			ui.setExpandedIcon(IconsManager.ExpandedIcon);
			tree.putClientProperty("JTree.lineStyle","None");
			tree.setCellRenderer( new DataCellRenderer());
        MainPanel.add(new JScrollPane(tree), BorderLayout.CENTER);
        	
        	
        	/*
        	 * 
        	 
        	 MouseListener ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				
				
				
				if (e.getButton() == MouseEvent.BUTTON3 || e.isControlDown()) {

					int selRow = tree.getRowForLocation(e.getX(), e.getY());
					TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
					if (selRow != -1 && e.getClickCount() == 2) {

						Object obj = tree.getLastSelectedPathComponent();

						if (obj instanceof DataTreeNode) {

					if (obj instanceof DataTreeNode) {
						ISelectable object = ((DataTreeNode) obj).object;

						if ( ((DataTreeNode) obj).datasetVar &&
								 object.getType() == 1) {
							new Histogram(object);
						}

						if (((DataTreeNode) obj).datasetVar && object.getType() == 2) {
							System.out.println("Barchart");
							//new Barchart(object);
						}
						// Liste
					


						cleanMiddlePanel();
					
					}
				}
			}
		}}};

        	 
        	 
        	 * **/
			
	}
	
	
	
	

	public void cleanMiddlePanel() {
		
		infoLabel.setText("");

		MainPanel.remove(infoPanel);
		MainPanel.add(infoPanel, BorderLayout.SOUTH);
		//setVisible(true);
		update(getGraphics());

	}

	
	
	
	

}