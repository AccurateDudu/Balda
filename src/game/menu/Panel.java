package game.menu;

import game.Display;
import game.board.BaldaBoard;
import game.board.Square;
import game.graphics.Screen;
import game.input.InputHandler;

public class Panel {
	
	public static final int BUTTONS_ROW = 16;
	
	private BaldaBoard board;
	private InputHandler input;
	
	private int mousePressRow, mousePressCol, mouseMoveRow, mouseMoveCol;
	private int confirmButton, calculateButton, cancelButton, turnButton;
	private boolean confirmPressed, calculatePressed, cancelPressed, turnPressed, turn;
	
	public Panel(BaldaBoard board, InputHandler input) {
		this.input = input;
		this.board = board;
	}
	
	public void update() {
		mouseMoveRow = input.mouseMovedY;
		mouseMoveCol = input.mouseMovedX;
		
		if (mouseMoveRow == 5 && (mouseMoveCol >= 0 && mouseMoveCol <= 1)) {
			confirmButton = Screen.NUM_OF_SPRITES_IN_ROW;
			cancelButton = 0;
			calculateButton = 0;
			turnButton = 0;
		} else if (mouseMoveRow == 5 && (mouseMoveCol >= 4 && mouseMoveCol <= 5)) {
			cancelButton = Screen.NUM_OF_SPRITES_IN_ROW;
			confirmButton = 0;
			calculateButton = 0;
			turnButton = 0;
		} else if (mouseMoveRow == 6 && (mouseMoveCol >= 0 && mouseMoveCol <= 1)) {
			calculateButton = Screen.NUM_OF_SPRITES_IN_ROW;
			confirmButton = 0;
			cancelButton = 0;
			turnButton = 0;
		} else if (mouseMoveRow == 6 && (mouseMoveCol >= 4 && mouseMoveCol <= 5)) {
			turnButton = Screen.NUM_OF_SPRITES_IN_ROW;
			confirmButton = 0;
			cancelButton = 0;
			calculateButton = 0;
		} else {
			confirmButton = 0;
			cancelButton = 0;
			calculateButton = 0;
			turnButton = 0;
		}
		
		if (input.mousePressed) {
			mousePressRow = input.mousePressedY;
			mousePressCol = input.mousePressedX;
			if (mousePressRow == 5 && (mousePressCol >= 0 && mousePressCol <= 1)) {
				confirmButton = Screen.NUM_OF_SPRITES_IN_ROW;
				confirmPressed = true;
			} else if (mousePressRow == 5 && (mousePressCol >= 4 && mousePressCol <= 5)) {
				cancelButton = Screen.NUM_OF_SPRITES_IN_ROW;
				cancelPressed = true;
			} else if (mousePressRow == 6 && (mousePressCol >= 0 && mousePressCol <= 1)) {
				calculateButton = Screen.NUM_OF_SPRITES_IN_ROW;
				calculatePressed = true;
			} else if (mousePressRow == 6 && (mousePressCol >= 4 && mousePressCol <= 5)) {
				turnButton = Screen.NUM_OF_SPRITES_IN_ROW;
				turnPressed = true;
			}
		}
		
		if (input.mouseReleased) {
			Square square = null;
			char selectedChar = board.getSelectedChar();
			String selectedWord = board.getSelectedWord();
			if (selectedChar != 0 && board.getCurrentChar() != null) {
				int row = board.getCurrentChar().get(selectedChar).y;
				int col = board.getCurrentChar().get(selectedChar).x;
				if (!board.getCurrentChar().isEmpty()) square = board.getBoard(row, col);
			}
			if (confirmPressed) {
				if ((square != null && square.getLetter() != null) && (square.getLetter().getColor() == BaldaBoard.SELECTED_LETTER_COLOR && board.getDictionary().contains(selectedWord))) {
					board.getPlayedWords().add(selectedWord);
					board.clear();
					turn = !turn;
					
				}
				confirmButton = 0;
				confirmPressed = false;
			} else if (cancelPressed) {
				if (square != null) board.getBoard(square.getRow(), square.getCol()).setLetter(null);
				board.clear();
				cancelButton = 0;
				cancelPressed = false;
			} else if (calculatePressed) {
				new Thread (new Runnable() {
					public void run() {
						board.checkWords();
					}
				}).start();
				calculateButton = 0;
				calculatePressed = false;
			} else if (turnPressed) {
				turnButton = 0;
				turn = !turn;
				turnPressed = false;
			}
		}
	}
	
	public void render(Screen screen) {
		screen.render(Screen.SPRITE_SIZE * 2, Display.HEIGHT - (Screen.SPRITE_SIZE * 2), BUTTONS_ROW + 10, 0, 0, 0);
		screen.render(Screen.SPRITE_SIZE * 2, Display.HEIGHT - Screen.SPRITE_SIZE, (BUTTONS_ROW * 2) + 10, 0, 0, 0);
		
		// Confirm
		screen.render(0, Display.HEIGHT - (Screen.SPRITE_SIZE * 2), BUTTONS_ROW + confirmButton, 0, 0, 0);
		screen.render(Screen.SPRITE_SIZE, Display.HEIGHT - (Screen.SPRITE_SIZE * 2), (BUTTONS_ROW + 1) + confirmButton, 0, 0, 0);
		
		// Calculate
		screen.render(0, Display.HEIGHT - Screen.SPRITE_SIZE, (BUTTONS_ROW + 2) + calculateButton, 0, 0, 0);
		screen.render(Screen.SPRITE_SIZE, Display.HEIGHT - Screen.SPRITE_SIZE, (BUTTONS_ROW + 3) + calculateButton, 0, 0, 0);
		
		// Cancel
		screen.render(Screen.SPRITE_SIZE * 3, Display.HEIGHT - (Screen.SPRITE_SIZE * 2), (BUTTONS_ROW + 4) + cancelButton, 0, 0, 0);
		screen.render(Screen.SPRITE_SIZE * 4, Display.HEIGHT - (Screen.SPRITE_SIZE * 2), (BUTTONS_ROW + 5) + cancelButton, 0, 0, 0);
		
		// Turn
		if (turn) {
			screen.render(Screen.SPRITE_SIZE * 3, Display.HEIGHT - Screen.SPRITE_SIZE, (BUTTONS_ROW + 8) + turnButton, 0, 0, 0);
			screen.render(Screen.SPRITE_SIZE * 4, Display.HEIGHT - Screen.SPRITE_SIZE, (BUTTONS_ROW + 9) + turnButton, 0, 0, 0);
		} else {
			screen.render(Screen.SPRITE_SIZE * 3, Display.HEIGHT - Screen.SPRITE_SIZE, (BUTTONS_ROW + 6) + turnButton, 0, 0, 0);
			screen.render(Screen.SPRITE_SIZE * 4, Display.HEIGHT - Screen.SPRITE_SIZE, (BUTTONS_ROW + 7) + turnButton, 0, 0, 0);
		}
	}
}
