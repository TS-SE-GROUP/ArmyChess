package armychess;

import java.awt.*;
import javax.swing.*;

public class BgPanel extends JPanel {

	private static final long serialVersionUID = 908692902865278163L;
	private ImageIcon imageIcon = null;

	public BgPanel(String imgPath) {
		super();
		imageIcon = new ImageIcon(imgPath);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (imageIcon != null) {
			float width = this.getWidth();
			float height = this.getHeight();
			int iconWidth = imageIcon.getIconWidth();
			int iconHeight = imageIcon.getIconHeight();
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.scale(width / iconWidth, height / iconHeight);
			g2d.drawImage(imageIcon.getImage(), 0, 0, null);
		}
	}
	
}
