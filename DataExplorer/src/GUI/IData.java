package GUI;

import Data.*;
import Data.ISelectable;
import GUI.*;
import GUI.IconsManager.IconsManager;
import DataTree.*;
import GUI.Histogram.*;
import GUI.Barchart.*;
import GUI.Heatmap.*;


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

	Vector<JFrame> windows = new Vector();

	static IData iData;

	FileDialog fileDialog;


	JScrollPane objectsPane;
	JScrollPane datasetsPane;
	JPanel middlePanel = new JPanel();

	
	
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
	JMenuItem saveGeneExpressions;
	public JMenuItem closeDataset;
	JPanel west;
	
	
	
	
	Vector<ISelectable> Genes;
	Vector<Clustering> Clusterings;	
	Vector<ClusterNode> HClusterings;	
	
    
	public IData() {
		super("IData");
		this.setBounds(0, 0, 330, 400);
		
		RConnectionManager.tryToConnect(this);
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

		// this.setBounds(20,20,150,450);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(MainPanel, BorderLayout.CENTER);

		IconsManager.getIconsManager().loadLogos(this);

		MainPanel.setLayout(new BorderLayout());
		middlePanel.setBackground(Color.WHITE);
    	middlePanel.setBorder(BorderFactory.createEtchedBorder());

		west = new JPanel();
	
		west.setBackground(Color.WHITE);

		MainPanel.add(west, BorderLayout.CENTER);
		// MainPanel.add(datasetsPane, BorderLayout.EAST);
		// MainPanel.add(middlePanel, BorderLayout.CENTER);

		// infoPanel.setBackground(Color.WHITE);
		infoPanel.setPreferredSize(new Dimension(25, 25));

		infoPanel.setBorder(BorderFactory.createEtchedBorder());
		MainPanel.add(infoPanel, BorderLayout.SOUTH);

		infoLabel = new JLabel("");
		// infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		infoPanel.setLayout(new BorderLayout());
		infoPanel.add(infoLabel, BorderLayout.WEST);


	

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
				infoLabel.setText("Loading Geneexpression dataset...");
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
				
			
				
			}
		});
		fileMenu.add(openFileItem);

	
			
		
		

		fileMenu.addSeparator();
		
		saveGeneExpressions = new JMenuItem("Save File");
		saveGeneExpressions.setEnabled(false);
		
		saveGeneExpressions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//new SaveSelectionFrame(seurat);

			}
		});
		fileMenu.add(saveGeneExpressions);
		
		
		
		
		
		
		
		


		closeDataset = new JMenuItem("Close");
		closeDataset.setEnabled(false);
		closeDataset.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				if (DataManager.getDataManager().Variables != null && shouldBeClosed()) {
				closeDataSet();
				}
			}
		});
		fileMenu.add(closeDataset);

		
		fileMenu.addSeparator();
		
		
		JMenuItem exitItem = new JMenuItem("Quit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (shouldExit()) System.exit(0);
			}
		});
		fileMenu.add(exitItem);

		JMenuItem item = new JMenuItem("Pixel Settings");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//new ColorDialog(seurat,seurat,settings.PixelW,settings.PixelH);
			}
		});
		optionsMenu.add(item);
		
		
		
		
		item = new JMenuItem("Color Settings");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//	new ColorSettings(seurat);
			}
		});
		optionsMenu.add(item);
		
		item = new JMenuItem("Scaling");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//new ScaleFrame(seurat);
			}
		});
		optionsMenu.add(item);
		
		
		optionsMenu.addSeparator();
		

		

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
					  Barchart bar = new Barchart(cont, var.name, dm.Cases, var.getStringData());	
					  cont.add(bar,true);
					  cont.setVisible(true);
					}
				} 
				SelectionManager.getSelectionManager().repaintWindows();
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
		plotsMenu.addSeparator();
		
		menuBar.add(optionsMenu);
		
		windowMenu = new JMenu("Window");
		item = new JMenuItem("Close all");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SelectionManager s = SelectionManager.getSelectionManager();
				
				for (int i = 0; i < s.containers.size(); i++) {
					s.containers.elementAt(i).setVisible(false);
					windowMenu.remove(1);
				}
				windows = new Vector();
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

	
		for (int i = 0; i < this.windows.size(); i++) {
			(windows.elementAt(i)).setVisible(false);
		}


		SelectionManager.close();
		DataManager.close();
		MainPanel.removeAll();
		this.getContentPane().removeAll();
		menuBar.removeAll();
		infoPanel.removeAll();
		middlePanel.removeAll();

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
        	west.add(new JScrollPane(tree), BorderLayout.WEST);
        	
        	
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
		this.middlePanel.removeAll();

		infoLabel.setText("");

		MainPanel.remove(infoPanel);
		MainPanel.add(infoPanel, BorderLayout.SOUTH);
		//setVisible(true);
		update(getGraphics());

	}

	
	
	
	

}