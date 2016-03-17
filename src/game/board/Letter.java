package game.board;

import game.graphics.Font;
import game.graphics.Screen;

public class Letter {
	
	private int color;
	private char letter;
	
	public Letter(char letter, int color) {
		this.letter = letter;
		this.color = color;
	}
	
	public char getLetter() {
		return letter;
	}
	
	public int getColor() {
		return color;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public void render(Screen screen, int x, int y) {
		Font.render(screen, x, y, letter, 0, color);
	}
}
