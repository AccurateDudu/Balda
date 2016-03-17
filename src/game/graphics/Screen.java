package game.graphics;

public class Screen {
	
	public static final int SPRITE_SHEET_SIZE = 1024;
	public static final int SPRITE_SIZE = 64;
	public static final int BINARY_64 = 6;
	public static final int NUM_OF_SPRITES_IN_ROW = SPRITE_SHEET_SIZE / SPRITE_SIZE;
	public static final int BIT_MIRROR_X = 1;
	public static final int BIT_MIRROR_Y = 2;
	
	public final int width, height;
	public int[] pixels;
	
	private SpriteSheet sheet;
	
	public Screen(int width, int height, SpriteSheet sheet) {
		this.width = width;
		this.height = height;
		this.sheet = sheet;
		
		pixels = new int[width * height];
	}
	
	public void render(int xPosition, int yPosition, int tile, int direction, int srcColor, int destColor) {
		boolean mirrorX = (direction & BIT_MIRROR_X) > 0;
		boolean mirrorY = (direction & BIT_MIRROR_Y) > 0;
		
		int xTile = tile % NUM_OF_SPRITES_IN_ROW;
		int yTile = tile / NUM_OF_SPRITES_IN_ROW;
		int tileOffset = (xTile << BINARY_64) + (yTile << BINARY_64) * sheet.width;
		
		for (int y = 0; y < SPRITE_SIZE; y++) {
			int yCoordinateOfSpriteSheet = y;
			if (mirrorY) yCoordinateOfSpriteSheet = (SPRITE_SIZE - 1) - y;
			if (y + yPosition < 0 || y + yPosition >= height) continue;
			for (int x = 0; x < SPRITE_SIZE; x++) {
				int xCoordinateOfSpriteSheet = x;
				if (mirrorX) xCoordinateOfSpriteSheet = (SPRITE_SIZE - 1) - x;
				if (x + xPosition < 0 || x + xPosition >= width) continue;
				int color = sheet.pixels[xCoordinateOfSpriteSheet + yCoordinateOfSpriteSheet * sheet.width + tileOffset];
				if ((srcColor != 0 || destColor != 0) && color == srcColor) color = destColor;
				if (color != 0xD67FFF) pixels[(x + xPosition) + (y + yPosition) * width] = color;
			}
		}
	}
	
	public void renderPanel(int xp, int yp, int color) {
		for (int y = 0; y < (Screen.SPRITE_SIZE * 2); y++) {
			if (y + yp < 0 || y + yp >= height) continue;
			for (int x = 0; x < width; x++) {
				if (x + xp < 0 || x + xp >= width) continue;
				pixels[(x + xp) + (y + yp) * width] = color;
			}
		}
	}
}
