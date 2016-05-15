package in.vvest.particles;

import java.awt.Color;
import java.awt.Graphics;

public class Particle {

    private Color color;
    private long birthTime;
    private int lifespan;
    private boolean dead;
    private float x, y, dX, dY;
	private int w, h;

    public Particle(int lifespan, Color c, float x, float y, float dX, float dY) {
		this.lifespan = lifespan;
		this.color = c;
		this.x = x;
		this.y = y;
		this.dX = dX;
		this.dY = dY;
	
		birthTime = System.currentTimeMillis();
	
		w = 1;
		h = 1;
    }

    public boolean isDead() {
    	return dead;
    }

    public void draw(Graphics g) {
    	g.setColor(color);
		g.fillRect((int) x, (int) y, w, h);
    }

    public void update() {
		if (!dead) {
		    x += dX;
		    y += dY;
	
		    if (System.currentTimeMillis() - lifespan > birthTime)
			dead = true;
		}
    }

    public String toString() {
    	return "particle of color: " + color + " at (" + x + ", " + y + ")";
    }
}
