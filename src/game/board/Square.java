package game.board;

import game.graphics.Screen;

public class Square {
	
	private int row, col;
	private Letter letter;
	private Arrow direction;
	
	public Square(int row, int col, Letter letter, Arrow direction) {
		this.row = row;
		this.col = col;
		this.letter = letter;
		this.direction = direction;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public Letter getLetter() {
		return letter;
	}
	
	public char getChar() {
		return letter.getLetter();
	}
	
	public void setLetter(Letter letter) {
		this.letter = letter;
	}
	
	public Arrow getDirection() {
		return direction;
	}
	
	public void setDirection(Arrow direction) {
		this.direction = direction;
	}
	
	public void render(Screen screen) {
		if (this.getLetter() == null) {
			screen.render(col << Screen.BINARY_64, row << Screen.BINARY_64, 0, 0, 0, 0);
		} else {
			letter.render(screen, col << Screen.BINARY_64, row << Screen.BINARY_64);
			if (direction != null) direction.render(screen, col << Screen.BINARY_64, row << Screen.BINARY_64);
		}
	}
}
