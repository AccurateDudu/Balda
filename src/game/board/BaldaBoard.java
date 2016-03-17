package game.board;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import game.graphics.Screen;
import game.input.InputHandler;

public class BaldaBoard {
	
	public static final int BOARD_SIZE = 5;
	
	public static final int SELECTED_LETTER_COLOR = 0x0094FF;
	public static final int NEW_LETTER_COLOR = 0xFF0000;
	
	private Square[][] board;
	
	private int currentMousePressRow, currentMousePressCol;
	private int prevMousePressRow, prevMousePressCol;
	private Square currentSquarePressed, prevSquarePressed;
	private boolean put, selected;
	private boolean isLetterInWord;
	private String selectedWord;
	private char selectedChar;
	
	private List<String> dictionary = new ArrayList<String>();
	private List<String> possibleWords = new ArrayList<String>();
	private List<String> playedWords = new ArrayList<String>();
	private Map<Character, Point> selectedChars = new HashMap<Character, Point>();
	
	public BaldaBoard() {
		board = new Square[BOARD_SIZE][BOARD_SIZE];
		
		initSquares();
		initFirstWord();
	}
	
	private void initSquares() {
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				board[row][col] = new Square(row, col, null, null);
			}
		}
	}
	
	private void initFirstWord() {
		dictionary = new ArrayList<String>();
		String buffer = "";
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("res/dictionary.txt"));
			while ((buffer = reader.readLine()) != null) {
				dictionary.add(buffer);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String word = "";
		
		do {
			word = dictionary.get(new Random().nextInt(dictionary.size()));
		} while (word.length() != BOARD_SIZE);
		
		playedWords.add(word);
		
		for (int i = 0; i < board.length; i++) {
			board[2][i].setLetter(new Letter(word.charAt(i), 0));
		}
	}
	
	public void checkWords() {
		for (char letter = 'a'; letter <= 'z'; letter++) {
			for (int row = 0; row < board[0].length; row++) {
				for (int col = 0; col < board.length; col++) {
					String word = "";
					if (board[row][col].getLetter() == null) {
						selectedChar = letter;
						board[row][col].setLetter(new Letter(letter, 0));
						selectedChars.put(selectedChar, new Point(col, row));
						checkWords(word, selectedChar, row, col);
						board[row][col].setLetter(null);
					} else {
						selectedChar = board[row][col].getChar();
						selectedChars.put(selectedChar, new Point(col, row));
						checkWords(word, selectedChar, row, col);
					}
				}
			}
		}
		
		String bestWord = "";
		
		for (String word : possibleWords) {
			if (word.length() > bestWord.length()) bestWord = word;
		}
		
		System.out.println(bestWord);
	}
	
	public void checkWords(String word, char letter, int row, int col) {
		if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) return;
		if (board[row][col].getLetter() == null || board[row][col].getLetter().getColor() == SELECTED_LETTER_COLOR) return;
		
		word += board[row][col].getChar();
		board[row][col].getLetter().setColor(SELECTED_LETTER_COLOR);
		if (row == selectedChars.get(letter).y && col == selectedChars.get(letter).x) isLetterInWord = true;
		
		if ((dictionary.contains(word) && !possibleWords.contains(word) && !playedWords.contains(word)) && isLetterInWord) possibleWords.add(word);
		
		this.checkWords(word, letter, row - 1, col);
		this.checkWords(word, letter, row, col + 1);
		this.checkWords(word, letter, row + 1, col);
		this.checkWords(word, letter, row, col - 1);
		
		word = word.substring(0, word.length() - 1);
		board[row][col].getLetter().setColor(0);
		if (row == selectedChars.get(letter).y && col == selectedChars.get(letter).x) isLetterInWord = false;
	}
	
	public void update(InputHandler input) {
		if (input.mousePressed) {
			currentMousePressRow = input.mousePressedY;
			currentMousePressCol = input.mousePressedX;
			if (currentMousePressRow >= 0 && currentMousePressCol >= 0 && currentMousePressCol < BOARD_SIZE && currentMousePressRow < BOARD_SIZE) {
				currentSquarePressed = board[currentMousePressRow][currentMousePressCol];
			}
		}
		
		
		if (input.mouseReleased) {
			if (currentSquarePressed != null) {
				Letter currentSelectedLetter = currentSquarePressed.getLetter();
				if (selected) {
					if (currentSelectedLetter != null && currentSelectedLetter.getColor() != SELECTED_LETTER_COLOR) {
						int prevRow = prevSquarePressed.getRow();
						int prevCol = prevSquarePressed.getCol();
						int currentRow = currentSquarePressed.getRow();
						int currentCol = currentSquarePressed.getCol();
						if ((prevRow == currentRow - 1 && prevCol == currentCol) || (prevRow == currentRow + 1 && prevCol == currentCol) ||
								(prevRow == currentRow && prevCol == currentCol - 1) || (prevRow == currentRow && prevCol == currentCol + 1)) {
							int dirX = (currentMousePressCol - prevMousePressCol);
							int dirY = (currentMousePressRow - prevMousePressRow);
							int direction = 0;
							
							if (dirX < 0) direction = 2;
							if (dirX > 0) direction = 3;
							if (dirY < 0) direction = 1;
							if (dirY > 0) direction = 0;
							
							prevSquarePressed.setDirection(new Arrow(direction));
							currentSelectedLetter.setColor(SELECTED_LETTER_COLOR);
							selectedWord += currentSelectedLetter.getLetter();
							currentSquarePressed = null;
							
							prevMousePressRow = input.mousePressedY;
							prevMousePressCol = input.mousePressedX;
							prevSquarePressed = board[prevMousePressRow][prevMousePressCol];
						}
					}
				} else if (put) {
					if (currentSelectedLetter != null) {
						currentSelectedLetter.setColor(SELECTED_LETTER_COLOR);
						selectedWord += currentSelectedLetter.getLetter();
						currentSquarePressed = null;
						selected = true;
						put = false;
						
						prevMousePressRow = input.mousePressedY;
						prevMousePressCol = input.mousePressedX;
						prevSquarePressed = board[prevMousePressRow][prevMousePressCol];
					}
				} else {
					if (input.keyPressed && Character.isAlphabetic(input.key)) {
						if (currentSelectedLetter != null) {
							currentSquarePressed.setLetter(new Letter(input.key, 0));
						} else {
							if (checkAvailability()) {
								selectedChar = input.key;
								selectedWord = "";
								currentSquarePressed.setLetter(new Letter(selectedChar, NEW_LETTER_COLOR));
								selectedChars.put(selectedChar, new Point(currentMousePressCol, currentMousePressRow));
								currentSquarePressed = null;
								put = true;
							}
						}
					}
				}
			}
		}
	}
	
	public boolean checkAvailability() {
		Point[] directions = {
			new Point(-1, 0),
			new Point(0, 1),
			new Point(1, 0),
			new Point(0, -1)
		};
		
		for (int i = 0; i < directions.length; i++) {
			int neighborsRow = currentSquarePressed.getRow() + directions[i].y;
			int neighborsCol = currentSquarePressed.getCol() + directions[i].x;
			if (neighborsRow >= 0 && neighborsCol >= 0 && neighborsRow < BOARD_SIZE && neighborsCol < BOARD_SIZE) {
				if (board[neighborsRow][neighborsCol].getLetter() != null) return true;
			}
		}
		
		return false;
	}
	
	public void clear() {
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				if (board[row][col].getLetter() != null) board[row][col].getLetter().setColor(0);
				if (board[row][col].getDirection() != null) board[row][col].setDirection(null);
			}
		}
		
		put = false;
		selected = false;
	}
	
	public Square getBoard(int row, int col) {
		return board[row][col];
	}
	
	public char getSelectedChar() {
		return selectedChar;
	}
	
	public String getSelectedWord() {
		return selectedWord;
	}
	
	public Map<Character, Point> getCurrentChar() {
		return selectedChars;
	}
	
	public List<String> getDictionary() {
		return dictionary;
	}
	
	public List<String> getPlayedWords() {
		return playedWords;
	}
	
	public void render(Screen screen) {
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				board[row][col].render(screen);
			}
		}
	}
}
