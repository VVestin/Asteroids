package in.vvest.gamestates;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class CountDownState extends GameState {

	private long startTime;
	private int countLength; 
	private GameState nextState;
	
	public CountDownState(int countStart, GameState nextState) {
		super();
		startTime = System.currentTimeMillis();
		countLength = countStart;
		this.nextState = nextState;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(g.getFont().deriveFont(100f));
		FontMetrics fm = g.getFontMetrics();
		String count = "" + (countLength - (System.currentTimeMillis() - startTime) / 1000);
		g.drawString(count, (400 - fm.stringWidth(count)) / 2, 200 + fm.getHeight() / 4);
	}
	
	public void update() {
		if (countLength - (System.currentTimeMillis() - startTime) / 1000 == 0) {
			setGameState(nextState);
		}
	}
	
}
