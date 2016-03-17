package game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import game.board.BaldaBoard;
import game.graphics.Screen;
import game.graphics.SpriteSheet;
import game.input.InputHandler;
import game.menu.Panel;

public class Display extends Canvas implements Runnable {
	
	public static final int WIDTH = BaldaBoard.BOARD_SIZE << Screen.BINARY_64;
	public static final int HEIGHT = (BaldaBoard.BOARD_SIZE << Screen.BINARY_64) + (Screen.SPRITE_SIZE * 2);
	public static final int SCALE = 1;
	public static final String TITLE = "BaldaDudu";
	
	BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	private JFrame frame;
	private Thread thread;
	private Screen screen;
	private InputHandler input;
	private BaldaBoard board;
	private Panel panel;
	
	private boolean running;
	
	public Display() {
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
		
		frame = new JFrame(TITLE);
		try {
			screen = new Screen(WIDTH, HEIGHT, new SpriteSheet(ImageIO.read(Display.class.getResourceAsStream("/sheet.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		input = new InputHandler(this);
		
		board = new BaldaBoard();
		panel = new Panel(board, input);
	}
	
	public synchronized void start() {
		if (running != true) running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while (running) {
			update();
			render();
		}
	}
	
	public void update() {
		board.update(input);
		panel.update();
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		board.render(screen);
		panel.render(screen);
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		bs.show();
		g.dispose();
	}
	
	public static void main(String[] args) {
		Display display = new Display();
		display.frame.add(display);
		display.frame.pack();
		display.frame.setLocationRelativeTo(null);
		display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.frame.setResizable(false);
		display.frame.setVisible(true);
		
		display.start();
	}

}
