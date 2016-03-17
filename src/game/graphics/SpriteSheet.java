package game.graphics;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	
	public final int width, height;
	public int[] pixels;
	
	public SpriteSheet(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
		for (int i = 0; i < pixels.length; i++) {
			int red = (pixels[i] >> 16) & 0xff;
			int green = (pixels[i] >> 8) & 0xff;
			int blue = pixels[i] & 0xff;
			
			pixels[i] = (red << 16) | (green << 8) | blue;
		}
	}
}
