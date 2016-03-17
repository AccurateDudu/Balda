package game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.event.MouseInputListener;

import game.Display;
import game.graphics.Screen;

public class InputHandler implements KeyListener, MouseInputListener {
	
	public int mousePressedX, mousePressedY, mouseReleasedX, mouseReleasedY, mouseMovedX, mouseMovedY;
	public boolean mousePressed, mouseReleased;
	public boolean keyPressed, keyReleased;
	public char key;
	
	public InputHandler(Display display) {
		display.addKeyListener(this);
		display.addMouseListener(this);
		display.addMouseMotionListener(this);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyPressed = true;
		keyReleased = false;
		
		key = e.getKeyChar();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyReleased = true;
		keyPressed = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousePressed = true;
		mouseReleased = false;
		
		mousePressedX = (e.getX() >> Screen.BINARY_64) / Display.SCALE;
		mousePressedY = (e.getY() >> Screen.BINARY_64) / Display.SCALE;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseReleased = true;
		mousePressed = false;
		
		mouseReleasedX = (e.getX() >> Screen.BINARY_64) / Display.SCALE;
		mouseReleasedY = (e.getY() >> Screen.BINARY_64) / Display.SCALE;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseMovedX = (e.getX() >> Screen.BINARY_64) / Display.SCALE;
		mouseMovedY = (e.getY() >> Screen.BINARY_64) / Display.SCALE;
	}
}
