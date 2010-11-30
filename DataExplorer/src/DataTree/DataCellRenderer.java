package DataTree;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.sun.org.apache.xpath.internal.operations.Variable;


import Clustering.*;
import DataTree.*;
import Data.*;
import Data.ISelectable;
import GUI.IconsManager.*;


public class DataCellRenderer extends DefaultTreeCellRenderer{
	 Icon numIcon,discreteIcon,hclustIcon;



	    public DataCellRenderer() {

	        this.numIcon = (IconsManager.numImageIcon);
	        this.discreteIcon = IconsManager.contImageIcon;
            this.hclustIcon = IconsManager.hclustIcon;
	        
	    }



	    public Component getTreeCellRendererComponent(

	                        JTree tree,

	                        Object value,

	                        boolean sel,

	                        boolean expanded,

	                        boolean leaf,

	                        int row,

	                        boolean hasFocus) {



	        super.getTreeCellRendererComponent(

	                        tree, value, sel,

	                        expanded, leaf, row,

	                        hasFocus);
	        
	       
	        

	        if (leaf) {
	        	
	        	if (getType(value) == 1) {
	        		setIcon(numIcon);
	        		return this;
	        	}
	        	if (getType(value) == 2) {
	        		setIcon(discreteIcon);
	        		return this;
	        	}
	        	
	        	
	        setIcon(null);

	        
	        } else  setIcon(null);
	       
	        return this;

	    }



	    protected int getType(Object value) {

	    	if (value instanceof DataTreeNode) {
    	        DataTreeNode node = (DataTreeNode)value;

     	       return node.TYPE;
	    	}
	    	return -1;
     	       
	    }

}
