package GUI.IconsManager;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.*;

import javax.swing.JComponent;

public class PicCanvas extends JComponent {
	Image pic;

	JFrame frame;

	public PicCanvas(Image pic, JFrame frame) {
		this.pic = pic;
		this.frame = frame;
	}

	@Override
	public void paint(Graphics g) {

		g.drawImage(pic, 0, 0, this.getWidth(), this.getHeight(),frame);

	}
}