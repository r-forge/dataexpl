package GUI.IconsManager;

import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import Data.DataManager;
import GUI.*;

public class IconsManager {

	PicCanvas logoIcon;

	PicCanvas contIcon, numIcon;
	
	static IconsManager iconsManager;
	
	public static ImageIcon contImageIcon, numImageIcon, geneImageIcon,geneImageIconS, cloneImageIcon,cloneImageIconS,
	chrImageIcon, chrImageIconS,expImageIcon,hclustIcon, CollapsedIcon, ExpandedIcon;
	
	
	public static IconsManager getIconsManager() {
		if (iconsManager == null) iconsManager = new IconsManager();
		return iconsManager;
	}
	
	public static void close() {
		iconsManager = null;
	}
	
	
	public void loadLogos(JFrame frame) {

		numImageIcon = new ImageIcon(this.readGif("num.gif"));
		contImageIcon = new ImageIcon(this.readGif("alpha.gif"));

		/*
		geneImageIcon = new ImageIcon(this.readGif("Gene.gif"));
		System.out.println("Gene Logo loaded...");
		
		
		geneImageIconS = new ImageIcon(this.readGif("GeneS.gif"));
		System.out.println("GeneS Logo loaded...");
		
		
		
		expImageIcon = new ImageIcon(this.readGif("Exp.gif"));
		System.out.println("Exp Logo loaded...");
		cloneImageIcon = new ImageIcon(this.readGif("Clone.gif"));
		System.out.println("Clone Logo loaded...");
		
		cloneImageIconS = new ImageIcon(this.readGif("CloneS.gif"));
		System.out.println("Clone Logo S loaded...");
		
		
		chrImageIcon = new ImageIcon(this.readGif("Chromosom.gif"));
		System.out.println("Chromosome Logo loaded...");

		chrImageIconS = new ImageIcon(this.readGif("ChromosomS.gif"));
		System.out.println("ChromosomeS Logo loaded...");

		hclustIcon = new ImageIcon(this.readGif("hclust.gif"));
		System.out.println("HClustering Logo loaded...");
*/
		CollapsedIcon = new ImageIcon(this.readGif("Collapsed.GIF"));
		System.out.println("Collapsed Logo loaded...");

		ExpandedIcon = new ImageIcon(this.readGif("Expanded.GIF"));
		System.out.println("Expanded Logo loaded...");

		/*
		logoIcon = new PicCanvas(new ImageIcon(this.readGif("logo.gif"))
				.getImage(), frame);
*/
		contIcon = new PicCanvas(new ImageIcon(this.readGif("num.gif"))
				.getImage(), frame);
		numIcon = new PicCanvas(new ImageIcon(this.readGif("alpha.gif"))
				.getImage(), frame);

	}

	byte[] readGif(String name) {

		byte[] arrayLogo;
		try {
			JarFile MJF;
			try {
				MJF = new JarFile("DataExpl.jar");
			} catch (Exception e) {
				MJF = new JarFile(System.getProperty("java.class.path"));
			}

			ZipEntry LE = MJF.getEntry(name);
			InputStream inputLogo = MJF.getInputStream(LE);
			arrayLogo = new byte[(int) LE.getSize()];
			for (int i = 0; i < arrayLogo.length; i++) {
				arrayLogo[i] = (byte) inputLogo.read();
			}
		} catch (Exception e) {
			System.out.println("Logo Exception: " + e);
			arrayLogo = new byte[1];
		}
		return arrayLogo;
	}

	
		
	
}
