package game.board;

import game.graphics.Screen;

public class Arrow {
	
	private int direction;
	
	public Arrow(int direction) {
		this.direction = direction;
	}
	
	public void render(Screen screen, int x, int y) {
		int tile = (direction > 1) ? 2 : 1;
		int flip = 0;
		if (direction == 0) flip = 3;
		if (direction == 2) flip = 1;
		screen.render(x, y, tile, flip, 0, 0);
	}
}
