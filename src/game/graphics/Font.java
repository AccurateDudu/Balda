package game.graphics;

import game.board.BaldaBoard;

public class Font {
	
	private static String alphabet = "" + //
			"ABCDEFGHIJKLMNOP" + //
			"QRSTUVWXYZ      " + //
			"";

	public static final int START_ROW_OF_RED_ABC = 8;
	public static final int START_ROW_OF_BLUE_ABC = 10;
	public static final int START_ROW_OF_BLACK_ABC = 14;
	
	/*
	public static void render(Screen screen, int x, int y, String message, int srcColor, int destColor) {
		message = message.toUpperCase();
		for (int i = 0; i < message.length(); i++) {
			int c = chars.indexOf(message.charAt(i));
			if (c >= 0) screen.render(x + (i << 4), y, c + 14 * Screen.SPRITE_SIZE, 0, srcColor, destColor);
		}
	}
	*/
	
	public static void render(Screen screen, int x, int y, char letter, int srcColor, int destColor) {
		letter = Character.toUpperCase(letter);
		int c = alphabet.indexOf(letter);
		if (c >= 0) {
			if (destColor == BaldaBoard.NEW_LETTER_COLOR) {
				screen.render(x, y, c + START_ROW_OF_RED_ABC * Screen.NUM_OF_SPRITES_IN_ROW, 0, srcColor, destColor);
			} else if (destColor == BaldaBoard.SELECTED_LETTER_COLOR) {
				screen.render(x, y, c + START_ROW_OF_BLUE_ABC * Screen.NUM_OF_SPRITES_IN_ROW, 0, srcColor, destColor);
			} else {
				screen.render(x, y, c + START_ROW_OF_BLACK_ABC * Screen.NUM_OF_SPRITES_IN_ROW, 0, srcColor, destColor);
			}
		}
	}
			
}
