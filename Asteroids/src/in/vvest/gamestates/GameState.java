package in.vvest.gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public abstract class GameState {

	protected Map<String, Boolean> keyState;
	private GameState gameState;

	public GameState() {
		keyState = new HashMap<String, Boolean>();
		gameState = this;
	}

	public abstract void draw(Graphics g);
	
	public abstract void update();
	
	public void dispose() {};
	
	protected void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	public GameState getGameState() {
		return gameState;
	}

	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		char c = e.getKeyChar();
		if (k == KeyEvent.VK_UP) {
			keyState.put("up", true);
		} else if (k == KeyEvent.VK_DOWN) {
			keyState.put("down", true);
		} else if (k == KeyEvent.VK_LEFT) {
			keyState.put("left", true);
		} else if (k == KeyEvent.VK_RIGHT) {
			keyState.put("right", true);
		} else if (k == KeyEvent.VK_SPACE) {
			keyState.put("space", true);
		} else if (k == KeyEvent.VK_ENTER) {
			keyState.put("enter", true);
		} else if (k == KeyEvent.VK_TAB) {
			keyState.put("tab", true);
		} else if (k == KeyEvent.VK_SHIFT) {
			keyState.put("shift", true);
		} else if (k == KeyEvent.VK_ESCAPE) {
			keyState.put("escape", true);
		} else if (c != '?') {
			keyState.put(String.valueOf(c), true);
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();
		char c = e.getKeyChar();
		if (k == KeyEvent.VK_UP) {
			keyState.put("up", false);
		} else if (k == KeyEvent.VK_DOWN) {
			keyState.put("down", false);
		} else if (k == KeyEvent.VK_LEFT) {
			keyState.put("left", false);
		} else if (k == KeyEvent.VK_RIGHT) {
			keyState.put("right", false);
		} else if (k == KeyEvent.VK_SPACE) {
			keyState.put("space", false);
		} else if (k == KeyEvent.VK_ENTER) {
			keyState.put("enter", false);
		} else if (k == KeyEvent.VK_TAB) {
			keyState.put("tab", false);
		} else if (k == KeyEvent.VK_SHIFT) {
			keyState.put("shift", false);
		} else if (k == KeyEvent.VK_ESCAPE) {
			keyState.put("escape", false);
		} else if (c != '?') {
			keyState.put(String.valueOf(c), false);
		}
	}
	
	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}
	
	public void mouseReleased(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {}

}
