package in.vvest.objects;

import java.awt.Color;
import java.awt.Graphics;

public class Laser {
	private static final double DEFAULT_LENGTH = 3;
	private static final double DEFAULT_SPEED = 7;
	private static final int DEFAULT_LIFESPAN = 55;

	private Vec2 pos;
	private Vec2 vel;
	private double length;
	private int lifespan;

	public Laser(Vec2 pos, double dir) {
		this(pos, new Vec2(dir).scale(DEFAULT_SPEED), DEFAULT_LENGTH, DEFAULT_LIFESPAN);
	}

	public Laser(Vec2 pos, Vec2 vel, double length, int lifespan) {
		this.pos = pos;
		this.vel = vel;
		this.length = length;
		this.lifespan = lifespan;
	}

	public void draw(Graphics g) {
		if (lifespan > 0) {
			Vec2 endPoint = pos.add(vel.normalize().scale(-length));
			g.setColor(Color.GREEN);
			g.drawLine((int) pos.x, (int) pos.y, (int) endPoint.x, (int) endPoint.y);
		}
	}

	public void update() {
		if (lifespan > 0) {
			lifespan--;
			pos = pos.add(vel);

			if (pos.x < 0)
				pos = new Vec2(400, pos.y);
			else if (pos.x > 400)
				pos = new Vec2(0, pos.y);

			if (pos.y < 0)
				pos = new Vec2(pos.x, 400);
			else if (pos.y > 400)
				pos = new Vec2(pos.x, 0);
		}
	}
	
	public void setDead(boolean dead) {
		if (dead)
			lifespan = 0;
	}

	public Vec2 getPos() {
		return pos;
	}
	
	public boolean isDead() {
		return lifespan <= 0;
	}
}
